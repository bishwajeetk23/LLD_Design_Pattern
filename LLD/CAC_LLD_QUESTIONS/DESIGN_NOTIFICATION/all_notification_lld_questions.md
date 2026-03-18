# Notification System LLD Question Bank

This document is a complete question bank for Low Level Design interview variants around a Notification System.
Use it as a checklist while preparing or while driving requirement clarifications in interviews.

## 1) Problem Framing and Clarification
1. Design a Notification System that can send notifications from any business module.
2. What types of notifications should be supported: transactional, promotional, system alerts?
3. Are we designing for one product or a multi-tenant platform?
4. Is the system synchronous, asynchronous, or hybrid?
5. What is the expected QPS (peak and average)?
6. What is the acceptable end-to-end latency per notification type?
7. Is at-least-once delivery acceptable, or do we need exactly-once semantics?
8. Should duplicate notifications be prevented? If yes, dedupe window?
9. Are we designing only LLD in-memory, or with persistence and queue boundaries?
10. Should notification history be queryable by users and support teams?
11. Is scheduling required for future delivery?
12. Is cancellation required before scheduled send time?
13. Should users be able to snooze notifications?
14. Do we need per-user timezone aware scheduling?
15. Should preferences be channel-level only, or event-level also?
16. Is a global fallback channel required (for example, email if push fails)?
17. Should we support localization and per-locale templates?
18. Is template versioning required?
19. Are there legal constraints: opt-out, DND, consent, GDPR-like delete requests?
20. What observability is expected: logs, metrics, tracing, dashboards, alerts?

## 2) Functional Requirement Variants
1. Send OTP notifications with very low latency.
2. Send order status updates with strict ordering.
3. Send marketing campaigns with rate limits and user consent checks.
4. Send security alerts with high priority and forced channel rules.
5. Support channels: Email, SMS, Push, WhatsApp, In-app.
6. Support per-user preferences: enable/disable channel and category.
7. Support fallback chain: Push -> SMS -> Email.
8. Support retries with configurable backoff.
9. Support template rendering with runtime placeholders.
10. Support bulk notifications to millions of users.
11. Support event-triggered notifications from multiple producer services.
12. Support scheduled and recurring notifications.
13. Support notification read/unread for in-app channel.
14. Support campaign pause/resume/cancel.
15. Support idempotent send requests from upstream clients.

## 3) Non-Functional Requirement Variants
1. Extensibility for adding new channels without changing core logic.
2. High availability of dispatch pipeline.
3. Horizontal scalability under burst traffic.
4. Reliability with durable queue and retry isolation.
5. Thread safety for concurrent dispatch.
6. Fault tolerance when third-party channel provider is down.
7. Low coupling between template engine, dispatcher, and channel adapters.
8. Maintainability and testability of core orchestration.
9. Cost efficiency under large campaign traffic.
10. Backpressure handling during spikes.

## 4) Core Class Design Questions
1. What should be the core entities: Notification, UserPreference, Template, Channel, DeliveryAttempt?
2. What fields belong in Notification object?
3. What fields belong in DeliveryAttempt object?
4. How will you model priority, expiry, and dedupe keys?
5. How will you model channel status and failure reason enums?
6. Should Notification be immutable after creation?
7. Which class acts as entry point: NotificationService or NotificationManager?
8. What responsibilities belong to Dispatcher vs Channel classes?
9. How do you separate orchestration from transport-specific logic?
10. How do you design a NotificationChannel interface?
11. Do you use a factory or strategy registry for channel resolution?
12. Where do you place validation logic?
13. How do you model retry policy as an object?
14. How do you model routing rules and fallback rules?
15. How do you prevent God Object anti-pattern in NotificationService?

## 5) API and Contract Questions
1. Design `sendNotification()` request/response contract.
2. Design a bulk send API.
3. Design API for scheduling notifications.
4. Design API for cancellation of scheduled notifications.
5. Design API to update user preferences.
6. Design API to fetch delivery status timeline.
7. What idempotency key format will you use?
8. What error contract will clients receive on partial failures?
9. How do you expose async acknowledgement vs final delivery status?
10. How do you version API contracts safely?

## 6) Data Modeling and Persistence Questions
1. Which entities need persistent storage and why?
2. How do you model notification state transitions in DB?
3. How do you store per-channel attempts?
4. How do you index by userId, status, and createdAt for support queries?
5. How do you store dedupe keys and expiry?
6. How do you store template versions and locale variants?
7. Which data belongs in cache vs database?
8. How long do you retain delivery history?
9. How do you archive old records?
10. How do you ensure schema evolution without breaking readers?

## 7) Queue and Asynchronous Flow Questions
1. Where do you place queues in the architecture?
2. One queue per channel or one common queue plus routing?
3. How do you ensure producer idempotency?
4. How do consumers handle duplicate messages?
5. How do you implement dead-letter queue handling?
6. How do you implement retry topics with exponential backoff?
7. How do you preserve ordering where required?
8. How do you partition by user or notification type?
9. How do you handle poison messages?
10. How do you re-drive failed messages safely?

## 8) Channel Adapter Questions
1. How do you abstract Email provider integrations?
2. How do you abstract SMS provider integrations?
3. How do you abstract Push provider integrations?
4. How do you normalize provider-specific error codes?
5. How do you support provider failover for same channel?
6. How do you support per-channel rate limits?
7. How do you support channel capability checks (attachment, rich content, length)?
8. How do you validate payload constraints per channel?
9. How do you handle webhook callbacks from providers?
10. How do you map callbacks to internal delivery state?

## 9) Template and Personalization Questions
1. Where should template rendering happen: producer side or dispatcher side?
2. How do you design placeholder validation before send?
3. How do you handle missing template variables?
4. How do you support template preview and test send?
5. How do you support localization fallback chain (user locale -> default locale)?
6. How do you version templates without breaking in-flight notifications?
7. Should templates be cached in memory? How to invalidate cache?
8. How do you prevent template injection vulnerabilities?
9. How do you support channel-specific template formats?
10. How do you support A/B template experiments?

## 10) Preference, Consent, and Policy Questions
1. How do you model opt-in/opt-out at category level?
2. How do you enforce quiet hours and DND windows?
3. How do you handle country-level regulatory rules?
4. How do you enforce mandatory notifications that bypass opt-out?
5. How do you resolve conflicts between global and channel preferences?
6. How do you model tenant-specific policy rules?
7. How do you ensure preference reads are low latency?
8. How do you audit preference changes?
9. How do you keep preference cache consistent?
10. How do you apply policy checks in pipeline without high coupling?

## 11) Reliability and Failure Handling Questions
1. How do you design retry policies per channel and error type?
2. Which errors are retryable vs non-retryable?
3. How do you avoid retry storms during provider outages?
4. How do you implement circuit breaker for external providers?
5. How do you implement timeout and fallback logic?
6. How do you guarantee eventual delivery where required?
7. How do you avoid duplicate sends during retries?
8. How do you handle partial success in multi-channel sends?
9. How do you expose final status for support and analytics?
10. How do you recover from dispatcher crash mid-processing?

## 12) Concurrency and Thread-Safety Questions
1. Is NotificationService stateless and thread-safe?
2. What shared mutable state exists and how is it protected?
3. Do you need locks, concurrent collections, or actor-style isolation?
4. How do worker threads pick tasks safely?
5. How do you prevent race conditions in status transitions?
6. How do you implement optimistic locking for delivery updates?
7. How do you ensure idempotent updates in concurrent retries?
8. How do you handle duplicate callbacks arriving in parallel?
9. How do you size thread pools for each channel worker?
10. How do you prevent thread starvation and queue buildup?

## 13) Design Pattern and SOLID Questions
1. Which pattern fits channel selection: Strategy?
2. Which pattern fits channel object creation: Factory?
3. Which pattern fits event listeners: Observer?
4. Which pattern fits policy composition: Chain of Responsibility?
5. Which pattern fits retry decorators around channels?
6. How do you apply Open-Closed Principle for new channels?
7. How do you avoid Interface Segregation violations in channel contracts?
8. How do you apply Dependency Inversion for provider SDKs?
9. Should dispatcher orchestrate with Command objects?
10. Where to use Builder for request creation?

## 14) Scalability and Performance Questions
1. How do you scale from 1K to 1M notifications per minute?
2. How do you shard workload across consumers?
3. How do you avoid hot partitions for high-activity users?
4. What batching opportunities exist per channel?
5. How do you tune retry delays to balance latency vs load?
6. How do you optimize template rendering throughput?
7. How do you minimize DB writes for delivery updates?
8. How do you design cache strategy for templates and preferences?
9. How do you design backpressure controls?
10. Which metrics indicate throughput saturation?

## 15) Observability and Operations Questions
1. Which metrics are mandatory: sent, delivered, failed, retry count, latency?
2. How do you tag metrics by channel, tenant, template, and provider?
3. What structured logs are needed for debugging one notification flow?
4. How do you propagate correlation IDs end-to-end?
5. What alerts indicate provider degradation?
6. How do you build SLOs for delivery latency and success rate?
7. How do you create dashboards for operations and product teams?
8. How do you support replay tools for failed notifications?
9. How do you support operational kill-switch per channel?
10. How do you run incident mitigation for queue backlog?

## 16) Security and Privacy Questions
1. How do you protect PII in notification payloads?
2. What data should be encrypted at rest?
3. How do you manage provider credentials securely?
4. How do you avoid sensitive data exposure in logs?
5. How do you enforce RBAC for template/policy management APIs?
6. How do you support audit trails for admin actions?
7. How do you implement right-to-erasure on notification history?
8. How do you validate webhook authenticity from providers?
9. How do you prevent abuse/spam from internal clients?
10. How do you rate limit by tenant/client API key?

## 17) Testing and Interview Deep-Dive Questions
1. Which unit tests validate dispatcher behavior?
2. Which tests validate channel fallback logic?
3. How do you test idempotency and duplicate suppression?
4. How do you test retry backoff correctness?
5. How do you test concurrent updates and race scenarios?
6. How do you test provider outage and recovery paths?
7. How do you test template rendering edge cases?
8. How do you run load tests for peak traffic assumptions?
9. Which contract tests are needed for provider adapters?
10. How do you design fault-injection tests for pipeline resilience?

## 18) Common Interview Follow-Ups
1. Add WhatsApp channel tomorrow without changing existing channel classes.
2. Add per-tenant custom retry policy.
3. Add campaign scheduling with pause/resume.
4. Add digest notifications (batch multiple events into one message).
5. Add user-level throttling (max N notifications per hour).
6. Add priority queues (critical first).
7. Add dedupe for same event within 5 minutes.
8. Add multi-region failover design.
9. Add in-app inbox with read/unread and pagination.
10. Add analytics events for open/click conversion.

## 19) Machine Coding Variants Based on Same Problem
1. Implement basic NotificationService with Email, SMS, Push strategies.
2. Add user preference filter in dispatch pipeline.
3. Add retry with exponential backoff and max attempts.
4. Add asynchronous queue-based dispatcher using worker threads.
5. Add fallback routing when primary channel fails.
6. Add template engine with placeholder substitution.
7. Add idempotency handling for repeated requests.
8. Add status repository and API to fetch notification timeline.
9. Add per-channel rate limiter.
10. Add observability hooks (metrics + correlation id logging).

## 20) Quick Evaluation Rubric (For Self-Review)
1. Requirement clarity: Did I separate must-have vs good-to-have?
2. Design quality: Are responsibilities cleanly separated?
3. Extensibility: Can I add a new channel with minimal changes?
4. Reliability: Are retries, idempotency, DLQ handled?
5. Concurrency: Is the design thread-safe and race-free?
6. Scalability: Is queueing and partitioning strategy clear?
7. Operability: Are metrics, logs, and alerts defined?
8. Security: Are PII and credentials protected?
9. Testing: Are critical failure paths test-covered?
10. Trade-offs: Did I justify major design choices?

---

Use this bank to practice in three rounds:
1. Round 1: Pick any 20 questions and answer verbally.
2. Round 2: Draw class diagram and sequence for one variant.
3. Round 3: Implement machine coding subset (service + strategies + retry + async).
