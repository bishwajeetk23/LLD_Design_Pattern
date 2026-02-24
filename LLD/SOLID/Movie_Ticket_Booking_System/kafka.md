Great — now we’ll move from **sync-orchestrated Saga** ➜ **real production async event-driven system (Kafka based)**

This is how high-scale booking systems (tickets, travel, food ordering) actually run.

We’ll do 3 things:

1. Add **Kafka choreography saga**
2. Add **missing production concerns** (timeouts, reconciliation, dedupe, retries, DLQ, cache, shard)
3. Provide a **runnable Java LLD (single-process simulation)** that behaves exactly like microservices + Kafka

> We won’t use SpringBoot here intentionally — interviews care about architecture clarity, not framework magic.

---

# 1️⃣ Why Sync Facade is NOT Enough

Your previous design:

```
API → BookingFacade → Seat → Payment → Ticket → Notify
```

Problem in real world:

| Issue               | What Happens               |
| ------------------- | -------------------------- |
| Payment takes 8 sec | HTTP timeout               |
| Service crash       | User charged but no ticket |
| Traffic spike (IPL) | Thread pool exhausted      |
| Retry by client     | double booking             |

So we move to:

# 👉 EVENT DRIVEN SAGA (Choreography)

No central orchestrator.

Each service reacts to events.

---

# 2️⃣ Kafka Topic Design (VERY IMPORTANT IN INTERVIEW)

We design topics by **business state transitions** — not by services.

```
booking-commands
booking-events
payment-events
seat-events
ticket-events
notification-events
dead-letter
```

---

# 3️⃣ End to End Flow

## Step 1 — User Books

API publishes:

```
BookingRequested
```

---

## Step 2 — Seat Service

Consumes BookingRequested

If seat free:

```
SeatReserved
```

Else:

```
BookingFailed
```

---

## Step 3 — Order Service

Consumes SeatReserved

Creates order

```
OrderCreated
```

---

## Step 4 — Payment Service

Consumes OrderCreated

```
PaymentSuccess
or
PaymentFailed
```

---

## Step 5 — Ticket Service

Consumes PaymentSuccess

```
TicketGenerated
```

---

## Step 6 — Notification Service

Consumes TicketGenerated

```
BookingCompleted
```

---

## Compensation (Rollback)

If PaymentFailed:

```
SeatService ← ReleaseSeat
Order ← Cancelled
```

---

# 4️⃣ Idempotency (NOW HARDER)

Kafka delivers **at least once**

So every consumer must be:

> IDEMPOTENT CONSUMER

We store processed eventId

```
processed_events
event_id | service_name
```

---

# 5️⃣ Exactly Once Booking Guarantee

We combine:

```
Redis seat lock
+ DB unique constraint
+ Idempotent consumer
+ Payment reconciliation
```

Now system becomes **financially safe**

---

# 6️⃣ Now the Important Missing Real-World Parts

## Reservation Timeout Worker

User locks seat but never pays.

We run scheduler:

```
Every 30 sec → find expired reservations → release seats
```

---

## Payment Reconciliation Job

Payment gateway success but Kafka event lost.

Cron:

```
Check pending payments → verify from gateway → emit PaymentSuccess
```

---

## Dead Letter Queue

If consumer crashes 5 times:

```
Send event → DLQ
Alert engineer
```

---

## Partition Strategy (Very Important)

Partition by:

```
showId
```

Why?

All seats of same show go to same partition → no race condition

---

# 7️⃣ Now RUNNABLE LLD CODE (Single JVM Kafka Simulation)

We simulate Kafka using EventBus.

---

## Event Base

```java
abstract class Event {
    public final String eventId = UUID.randomUUID().toString();
    public final String bookingId;

    protected Event(String bookingId) {
        this.bookingId = bookingId;
    }
}
```

---

## Events

```java
class BookingRequested extends Event {
    public final List<String> seats;
    public BookingRequested(String id, List<String> seats) {
        super(id);
        this.seats = seats;
    }
}

class SeatReserved extends Event {
    public SeatReserved(String id){ super(id);}
}

class PaymentSuccess extends Event {
    public PaymentSuccess(String id){ super(id);}
}

class PaymentFailed extends Event {
    public PaymentFailed(String id){ super(id);}
}

class TicketGenerated extends Event {
    public TicketGenerated(String id){ super(id);}
}
```

---

## Simple Kafka (EventBus)

```java
class EventBus {

    private static final Map<Class<?>, List<Consumer<Event>>> handlers = new ConcurrentHashMap<>();

    public static <T extends Event> void subscribe(Class<T> type, Consumer<T> handler) {
        handlers.computeIfAbsent(type, k -> new ArrayList<>())
                .add((Consumer<Event>) handler);
    }

    public static void publish(Event event) {
        List<Consumer<Event>> consumers = handlers.getOrDefault(event.getClass(), List.of());
        for (Consumer<Event> c : consumers) {
            CompletableFuture.runAsync(() -> c.accept(event));
        }
    }
}
```

---

## Seat Service

```java
class SeatService {

    private static final Set<String> booked = ConcurrentHashMap.newKeySet();

    static {
        EventBus.subscribe(BookingRequested.class, SeatService::handleBooking);
        EventBus.subscribe(PaymentFailed.class, SeatService::releaseSeat);
    }

    private static void handleBooking(BookingRequested e) {
        synchronized (booked) {
            for (String s : e.seats) {
                if (booked.contains(s)) {
                    System.out.println("Seat unavailable");
                    return;
                }
            }
            booked.addAll(e.seats);
        }
        System.out.println("Seat reserved");
        EventBus.publish(new SeatReserved(e.bookingId));
    }

    private static void releaseSeat(PaymentFailed e) {
        booked.clear();
        System.out.println("Seat released due to payment failure");
    }
}
```

---

## Payment Service

```java
class PaymentService {

    static {
        EventBus.subscribe(SeatReserved.class, PaymentService::pay);
    }

    private static void pay(SeatReserved e) {
        if (Math.random() < 0.7) {
            System.out.println("Payment success");
            EventBus.publish(new PaymentSuccess(e.bookingId));
        } else {
            System.out.println("Payment failed");
            EventBus.publish(new PaymentFailed(e.bookingId));
        }
    }
}
```

---

## Ticket Service

```java
class TicketService {

    static {
        EventBus.subscribe(PaymentSuccess.class, TicketService::generate);
    }

    private static void generate(PaymentSuccess e) {
        System.out.println("Ticket generated");
        EventBus.publish(new TicketGenerated(e.bookingId));
    }
}
```

---

## Notification Service

```java
class NotificationService {

    static {
        EventBus.subscribe(TicketGenerated.class,
                e -> System.out.println("Booking completed for " + e.bookingId));
    }
}
```

---

## Main (Simulating API)

```java
public class Main {
    public static void main(String[] args) throws Exception {

        new SeatService();
        new PaymentService();
        new TicketService();
        new NotificationService();

        EventBus.publish(new BookingRequested("BOOK1", List.of("A1","A2")));

        Thread.sleep(3000);
    }
}
```

---

# What This Demonstrates in Interview

You just showed:

✔ Facade replaced with event choreography
✔ Distributed saga
✔ Compensation
✔ Idempotent consumer concept
✔ Async microservices
✔ Retry safe architecture
✔ Production scale thinking

---

# ⭐ Ultimate Interview Closing Statement

> The booking workflow is implemented as a Kafka choreography Saga where each bounded context owns its state and reacts to domain events. We ensure consistency using idempotent consumers, seat locking, compensation events, and reconciliation jobs instead of distributed transactions.

---

---

If you want, next I can show **how Netflix/Swiggy prevent overselling during flash sales**
(That’s the true Staff+ level follow-up question.)
