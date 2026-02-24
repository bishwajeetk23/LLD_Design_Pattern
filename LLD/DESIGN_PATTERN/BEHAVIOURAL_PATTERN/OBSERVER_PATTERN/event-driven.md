Perfect 👍 — now we enter the **real senior-level discussion**.

After you propose Kafka / async observer, interviewer almost always asks:

> **“Kafka gives at-least-once delivery.
> So your consumer WILL receive duplicate events.
> How will you prevent duplicate notifications?”**

If you cannot answer this → instant reject ❌
Because notification/payment/email systems **must be idempotent**.

---

# 🧨 First Understand the Problem (Why duplicates happen)

Even if producer sends only once:

```
User uploads video
→ Kafka stores event
→ Consumer processes
→ Sends Email
→ CRASH before commit offset
→ Kafka retries
→ Email sent again
```

User receives:

> 📩 📩 "New video uploaded" (twice)

So problem is NOT Kafka
Problem is **side-effect executed twice**

---

# 🧠 Golden Rule

> We don’t make Kafka exactly-once
> We make the CONSUMER idempotent

---

# 🏗 Industry Solution: Idempotency Key

Every event must have a **unique deterministic ID**

```
notification_id = hash(channelId + videoId + userId + type)
```

Now system remembers:

> “Have I already sent this notification?”

---

# 📊 Where to Store Processed Events?

| Storage               | Use Case                  |
| --------------------- | ------------------------- |
| Redis                 | Fast dedupe (most common) |
| DB unique index       | Payment / critical        |
| Kafka compacted topic | stream systems            |
| Bloom filter          | extreme scale             |

For notifications → Redis best

---

# 🧩 Flow (Real Production)

```
Consumer receives event
    ↓
Check Redis: processed?
    ↓ YES → skip
    ↓ NO
Send notification
Store id in Redis
Commit offset
```

---

# ✨ Code (Staff Level Implementation)

We upgrade your worker.

---

## Step 1: Event with Idempotency Key

```java
class SendNotificationCommand {
    final String user;
    final String video;
    final String channel;

    SendNotificationCommand(String u, String v, String c) {
        user = u;
        video = v;
        channel = c;
    }

    public String idempotencyKey() {
        return channel + ":" + video + ":" + user;
    }
}
```

---

## Step 2: Idempotency Store (Simulating Redis)

```java
class IdempotencyStore {

    private static final Set<String> processed = ConcurrentHashMap.newKeySet();

    public static boolean alreadyProcessed(String key) {
        return processed.contains(key);
    }

    public static void markProcessed(String key) {
        processed.add(key);
    }
}
```

---

## Step 3: Idempotent Consumer

```java
class NotificationWorker {

    private final Notifier notifier = new EmailNotifier();

    public NotificationWorker() {
        EventBus.subscribe(SendNotificationCommand.class, this::process);
    }

    private void process(SendNotificationCommand cmd) {

        String key = cmd.idempotencyKey();

        // ⭐ DEDUPLICATION CHECK
        if (IdempotencyStore.alreadyProcessed(key)) {
            System.out.println("Duplicate skipped: " + key);
            return;
        }

        notifier.send(cmd.user, cmd.video);

        // mark AFTER success
        IdempotencyStore.markProcessed(key);
    }
}
```

---

# ⚠️ Important Interview Follow-up

Interviewer:

> What if service crashes AFTER sending email but BEFORE storing Redis key?

Then duplicate again 😄

---

# 🧠 Correct Production Answer

We must make:

> **Write + Side Effect atomic**

We use:

## Transactional Outbox Pattern

Instead of sending email directly:

```
DB transaction:
  save notification row (pending)
  commit

Worker reads DB
sends email
marks sent
```

Now retry safe.

---

# 🏆 Final Production Architecture

```
Service
  → writes event in DB (outbox)

Outbox Processor
  → publishes to Kafka

Consumer
  → checks Redis idempotency
  → sends notification
```

---

# 🔥 Interview Level Answer (Memorize This)

> We handle Kafka duplicate delivery using idempotent consumers backed by Redis.
> Each event carries deterministic idempotency key.
> Consumer checks and records processed events.
> For crash safety between DB and Kafka we implement transactional outbox pattern.

Say this → interviewer knows you worked on distributed systems.

---

# Next FAANG Follow-Up (Harder)

They now ask:

> “Good. But Redis memory will grow forever.
> How will you clean old idempotency keys without breaking correctness?”

Tell me your thought first — this is a real senior question 😄
