Great — now we’ll extend the architecture to a **real fintech-style payment orchestration**:

> Try primary gateway → if failed → fallback to next → configurable order

This is called **Payment Routing / Failover Strategy**
(Used in UPI, cards, wallets, etc.)

---

# Design We Are Building

```
Controller
   ↓
PaymentService
   ↓
RoutingStrategy (decides order)
   ↓
Factory (finds implementations)
   ↓
Gateway Strategies (Razorpay / PayU / Stripe...)
```

Patterns used:

| Pattern                 | Role                      |
| ----------------------- | ------------------------- |
| Strategy                | Each gateway              |
| Factory                 | Resolve implementation    |
| Dependency Injection    | Auto wiring               |
| Chain of Responsibility | Failover routing          |
| Configuration Driven    | Change order without code |

---

# FULL IMPLEMENTATION (Spring Boot Style)

---

## 1️⃣ Models

### PaymentRequest

```java
public record PaymentRequest(
        String orderId,
        double amount,
        String currency
) {}
```

### PaymentStatus

```java
public enum PaymentStatus {
    SUCCESS,
    FAILED,
    RETRYABLE_FAILURE
}
```

### PaymentResult

```java
public class PaymentResult {
    private final PaymentStatus status;
    private final String gateway;
    private final String message;

    public PaymentResult(PaymentStatus status, String gateway, String message) {
        this.status = status;
        this.gateway = gateway;
        this.message = message;
    }

    public boolean isSuccess() {
        return status == PaymentStatus.SUCCESS;
    }

    public boolean shouldRetry() {
        return status == PaymentStatus.RETRYABLE_FAILURE;
    }

    public static PaymentResult success(String gateway){
        return new PaymentResult(PaymentStatus.SUCCESS, gateway, "Payment success");
    }

    public static PaymentResult failed(String gateway){
        return new PaymentResult(PaymentStatus.FAILED, gateway, "Hard failure");
    }

    public static PaymentResult retryable(String gateway){
        return new PaymentResult(PaymentStatus.RETRYABLE_FAILURE, gateway, "Temporary issue");
    }
}
```

---

## 2️⃣ Strategy Interface (Gateway Contract)

```java
public interface PaymentGateway {
    String name();
    PaymentResult pay(PaymentRequest request);
}
```

---

## 3️⃣ Gateway Implementations

### Razorpay

```java
@Component
public class RazorpayGateway implements PaymentGateway {

    @Override
    public String name() {
        return "razorpay";
    }

    @Override
    public PaymentResult pay(PaymentRequest request) {
        System.out.println("Trying Razorpay...");

        // simulate temporary failure
        return PaymentResult.retryable(name());
    }
}
```

---

### PayU

```java
@Component
public class PayUGateway implements PaymentGateway {

    @Override
    public String name() {
        return "payu";
    }

    @Override
    public PaymentResult pay(PaymentRequest request) {
        System.out.println("Trying PayU...");
        return PaymentResult.success(name());
    }
}
```

---

## 4️⃣ Factory (Auto-Registry Factory)

```java
@Component
public class PaymentGatewayFactory {

    private final Map<String, PaymentGateway> gatewayMap;

    public PaymentGatewayFactory(List<PaymentGateway> gateways) {
        this.gatewayMap = gateways.stream()
                .collect(Collectors.toMap(PaymentGateway::name, g -> g));
    }

    public PaymentGateway get(String name) {
        PaymentGateway gateway = gatewayMap.get(name);
        if (gateway == null)
            throw new IllegalArgumentException("Unsupported gateway: " + name);
        return gateway;
    }
}
```

---

## 5️⃣ Routing Strategy (Failover Logic)

### Interface

```java
public interface PaymentRoutingStrategy {
    List<String> getGatewayOrder(PaymentRequest request);
}
```

---

### Config Driven Routing

```java
@Component
public class ConfigBasedRoutingStrategy implements PaymentRoutingStrategy {

    // Imagine this coming from DB / config server
    @Override
    public List<String> getGatewayOrder(PaymentRequest request) {
        return List.of("razorpay", "payu"); // priority order
    }
}
```

---

## 6️⃣ Payment Orchestrator (THE IMPORTANT CLASS)

```java
@Service
public class PaymentService {

    private final PaymentGatewayFactory factory;
    private final PaymentRoutingStrategy routingStrategy;

    public PaymentService(PaymentGatewayFactory factory,
                          PaymentRoutingStrategy routingStrategy) {
        this.factory = factory;
        this.routingStrategy = routingStrategy;
    }

    public PaymentResult checkout(PaymentRequest request) {

        List<String> order = routingStrategy.getGatewayOrder(request);

        for (String gatewayName : order) {

            PaymentGateway gateway = factory.get(gatewayName);
            PaymentResult result = gateway.pay(request);

            if (result.isSuccess()) {
                System.out.println("Paid via " + gatewayName);
                return result;
            }

            if (!result.shouldRetry()) {
                return result; // hard failure → stop
            }

            System.out.println("Failover from " + gatewayName);
        }

        return new PaymentResult(
                PaymentStatus.FAILED,
                "NONE",
                "All gateways failed"
        );
    }
}
```

---

## 7️⃣ Controller (Entry Point)

```java
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public PaymentResult pay(@RequestBody PaymentRequest request){
        return service.checkout(request);
    }
}
```

---

# What Happens at Runtime

```
User pays ₹1000
      ↓
Try Razorpay
      ↓
Temporary failure
      ↓
Failover to PayU
      ↓
Success
```

Output:

```
Trying Razorpay...
Failover from razorpay
Trying PayU...
Paid via payu
```

---

# Why This Design Is Senior-Level

| Feature                    | Supported |
| -------------------------- | --------- |
| Dynamic gateway onboarding | ✔         |
| Priority routing           | ✔         |
| Failover                   | ✔         |
| Config driven              | ✔         |
| Independent deployments    | ✔         |
| Testable                   | ✔         |
| No switch-case             | ✔         |

---

# Interview Answer (Very Important)

If interviewer asks:

**“Design payment gateway integration”**

You say:

> I would build a pluggable strategy-based provider system and add a routing orchestration layer that supports failover and priority selection. The routing policy should be configuration driven so new providers can be onboarded without code changes.

---

If you want next — I can extend this to **load balancing routing (30% Razorpay / 70% PayU)** which is a very common fintech follow-up.




-----Some more questions----

payment providers

notification channels

authentication providers

storage engines

pricing engines