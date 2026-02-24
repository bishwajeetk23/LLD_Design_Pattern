Perfect — now we are entering **real backend LLD territory (SDE-2 / SDE-3 / Staff)**
We’ll design a **BookMyShow-like booking platform** — not a demo, but a **production-style architecture**.

You asked to include:

✔ Seat concurrency safety
✔ Distributed transaction (no double charge / no ghost seat)
✔ Idempotency (retry safe)
✔ Extensible → movie today, concert tomorrow
✔ End-to-end flow

We will design using:

> **DDD + Facade + Strategy + Saga + Locking + Idempotency Keys**

---

# 🧠 1. System Mindset (How seniors think)

A booking system is NOT a single transaction.

It is a **multi-service distributed workflow**:

```
Reserve Seat → Charge Money → Confirm Booking → Generate Ticket → Notify
```

If ANY step fails → rollback previous steps

This is called a **Saga Transaction** (not DB transaction).

---

# 🧱 2. High Level Architecture

```
                API Gateway
                     |
             Booking Application Service (Facade)
                     |
     ------------------------------------------------
     |        |         |        |        |         |
SeatSvc   PaymentSvc  OrderSvc TicketSvc UserSvc NotificationSvc
     |         |
 Redis Lock   Payment Gateway
```

---

# 🧩 3. Extensibility First (Movie vs Concert)

We never create `MovieBookingService` ❌

We create a **Bookable Event abstraction**

## BookableEvent (Core Domain)

```java
interface BookableEvent {
    String getEventId();
    EventType getType();
    SeatLayout getSeatLayout();
}
```

### Future ready

```
Movie implements BookableEvent
Concert implements BookableEvent
SportsMatch implements BookableEvent
StandupComedy implements BookableEvent
```

➡ Now platform never changes when new event added

---

# 🧱 4. Seat Concurrency Problem

### Real Problem

2 users click seat A1 at same time

Without lock:

```
User A reads FREE
User B reads FREE
Both pay
Both booked ❌
```

---

## Correct Solution → Distributed Lock + Temporary Reservation

We do NOT directly book seat.

We **reserve seat for 5 minutes**.

---

## Seat States

```
AVAILABLE
LOCKED (5 min hold)
BOOKED
```

---

## Seat Table

```
seat_id | show_id | status | locked_by | lock_expiry
```

---

## Redis Locking

We use:

```
SETNX seat:A1:show123 user123 EX 300
```

If success → you got the seat
If fail → someone else has it

---

# 🧱 5. Idempotency (CRITICAL)

Mobile apps retry requests automatically.

Without idempotency:

User double clicks PAY → charged twice 💀

---

## Solution

Client sends:

```
Idempotency-Key: 8f1a-223b-xyz
```

Server stores result:

```
idempotency_key | response | status
```

If same key comes again → return old response

---

# 🧱 6. Distributed Transaction (Saga Pattern)

We DO NOT use 2PC ❌
We use **Compensating Actions**

---

## Booking Saga Flow

```
1 Reserve Seat
2 Create Pending Order
3 Charge Payment
4 Confirm Seat
5 Generate Ticket
6 Notify User
```

If payment fails:

```
Release Seat
Cancel Order
```

If ticket fails:

```
Refund Payment
Release Seat
Cancel Order
```

---

# 🧱 7. Core Booking Facade (Application Service)

This is NOT business logic — this is workflow orchestration.

```java
class BookingFacade {

    private final SeatService seatService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final TicketService ticketService;
    private final IdempotencyService idempotencyService;

    public BookingResponse book(BookingRequest req) {

        // 1️⃣ Idempotency check
        BookingResponse cached = idempotencyService.get(req.idempotencyKey);
        if (cached != null) return cached;

        String reservationId = seatService.reserveSeats(req);

        try {

            String orderId = orderService.createPendingOrder(req);

            paymentService.charge(orderId, req.amount);

            seatService.confirmReservation(reservationId);

            String ticketId = ticketService.generate(orderId);

            BookingResponse response = BookingResponse.success(ticketId);

            idempotencyService.save(req.idempotencyKey, response);

            return response;

        } catch (Exception e) {

            // SAGA COMPENSATION
            seatService.releaseReservation(reservationId);
            paymentService.refund(req.paymentRef);

            return BookingResponse.failed();
        }
    }
}
```

---

# 🧱 8. Seat Service (Concurrency Safe)

```java
class SeatService {

    private RedisLockManager lockManager;

    public String reserveSeats(BookingRequest req) {

        for (String seat : req.seats) {

            boolean locked = lockManager.tryLock(
                    "seat:" + seat + ":" + req.showId,
                    req.userId,
                    300
            );

            if (!locked)
                throw new SeatUnavailableException(seat);
        }

        return UUID.randomUUID().toString();
    }

    public void confirmReservation(String reservationId) {
        // mark BOOKED in DB
    }

    public void releaseReservation(String reservationId) {
        // unlock + mark AVAILABLE
    }
}
```

---

# 🧱 9. Payment Service (Retry Safe)

```java
class PaymentService {

    public void charge(String orderId, double amount) {

        Payment existing = paymentRepo.findByOrder(orderId);
        if (existing != null && existing.isSuccess())
            return; // idempotent payment

        paymentGateway.charge(orderId, amount);
    }

    public void refund(String paymentRef) {
        paymentGateway.refund(paymentRef);
    }
}
```

---

# 🧱 10. Why This Design is Senior Level

| Problem            | Solved By                 |
| ------------------ | ------------------------- |
| Double booking     | Redis distributed lock    |
| Double payment     | Idempotency key           |
| Partial failure    | Saga rollback             |
| Retry safety       | Stored responses          |
| Future event types | BookableEvent abstraction |
| Scalability        | Stateless facade          |
| Crash recovery     | Reservation expiry        |

---

# 🧠 Interview Explanation (Golden Line)

> We cannot use ACID DB transaction because booking spans multiple microservices.
> So we implemented a Saga-based workflow with compensating rollback and Redis distributed locking to guarantee seat consistency and payment correctness.

---

# ⭐ What Interviewer Usually Asks Next

1. What if Redis crashes?
   → DB unique constraint fallback

2. What if payment succeeds but server crashes?
   → Payment reconciliation job

3. How prevent seat hoarding?
   → Limit active reservations per user

4. How scale to IPL tickets?
   → Partition by showId + virtual queue

---

---

If you want, next I can show:

👉 Kafka event-driven version (real companies use this, not synchronous saga)
This is usually the “hire/no-hire” differentiator in senior interviews.
