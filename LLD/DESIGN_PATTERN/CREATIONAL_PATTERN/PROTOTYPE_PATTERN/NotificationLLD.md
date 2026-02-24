Perfect 😄
Let’s do a **real Staff-level LLD question** — not textbook — something that actually appears in backend interviews (Flipkart / Uber / Amazon style).

---

# 🧠 LLD Question — Multi-Channel Notification Platform

Design a **Notification Service** used by a large product (think ride-booking / e-commerce).

The system must send notifications through:

* Email
* SMS
* Push Notification
* WhatsApp (future)

---

## Functional Requirements

1. A product event occurs

   * Order Placed
   * Payment Failed
   * OTP Login
   * Password Reset

2. For each event:

   * Different template per channel
   * Different content per user

3. Example

**Event: Order Placed**

| Channel  | Template      |
| -------- | ------------- |
| Email    | HTML invoice  |
| SMS      | Short text    |
| Push     | Short message |
| WhatsApp | Rich card     |

---

## Non-Functional Requirements (Important — senior level)

* 10K notifications/sec
* Template rendering must be fast
* Adding new channel must not change existing code
* Adding new notification type must not modify old code
* Different providers per channel (SES, Twilio, Firebase)

---

# 🔥 This Question Forces You To Use

| Problem              | Pattern Required      |
| -------------------- | --------------------- |
| Template duplication | Prototype             |
| Provider selection   | Strategy              |
| Channel creation     | Factory               |
| Event handling       | Command               |
| Extensibility        | Open-Closed Principle |

---

# Expected Discussion In Interview

You must design:

### 1️⃣ Notification Flow

```
Event → NotificationService → Channel → Provider → Send
```

### 2️⃣ Template Flow

```
TemplateRegistry → Clone Prototype → Fill Data → Send
```

---

# Your Task (You implement)

Design classes for:

1. NotificationService
2. Template system using Prototype
3. Channels (Email/SMS/Push)
4. Providers (SES/Twilio/Firebase)
5. Factory for channels
6. Ability to add WhatsApp later WITHOUT touching old code

---

## Input Example

```java
NotificationRequest request =
    new NotificationRequest(
        EventType.ORDER_PLACED,
        userId,
        Map.of("name","Bishwajeet","orderId","ORD123")
    );

notificationService.send(request);
```

---

## Expected Behavior

System decides automatically:

* Which channels
* Which template
* Which provider
* Personalizes message
* Sends notification

---

# Important Rule

You are NOT allowed to use:

* if-else chains for event type
* switch for channel
* switch for provider

👉 Because real scalable systems cannot rely on branching logic.

---

# Your Turn

Implement step-by-step:

Start with:

> **Step 1: Design Template + Prototype registry**

Send your code — I will review like interviewer and guide next step.
