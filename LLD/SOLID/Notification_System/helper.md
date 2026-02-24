Perfect — now we’ll turn your academic prototype into a **real production-grade Notification System**
(what companies like Uber / Amazon / Swiggy actually build)

We’ll evolve it step-by-step like a Staff engineer designing an LLD in interview.

---

# 🎯 Goal

We want a system that can:

* Reuse heavy templates efficiently (Prototype Pattern)
* Support multiple channels (Email / SMS / Push later)
* Allow runtime data injection (name, otp, orderId…)
* Avoid rebuilding templates every request
* Be extensible (new templates without code change)

---

# Final Architecture (What we’re building)

```
                   ┌──────────────────────┐
                   │  Template Registry   │
                   │ (stores prototypes)  │
                   └──────────┬───────────┘
                              │ clone()
                              ▼
                   ┌──────────────────────┐
                   │ Template Instance     │  ← mutable per request
                   │ (inject variables)    │
                   └──────────┬───────────┘
                              │ render()
                              ▼
                   ┌──────────────────────┐
                   │  RenderedMessage      │
                   │  (pure data object)   │
                   └──────────┬───────────┘
                              │ send()
                              ▼
                   ┌──────────────────────┐
                   │ Channel (Email/SMS)  │
                   └──────────────────────┘
```

👉 Prototype exists ONLY at template level
👉 Sending is separate (Single Responsibility)

---

# Step 1 — Rendered Message (Final Output)

This is immutable data after rendering.

```java
class RenderedMessage {
    private final String subject;
    private final String body;
    private final String recipient;

    public RenderedMessage(String subject, String body, String recipient) {
        this.subject = subject;
        this.body = body;
        this.recipient = recipient;
    }

    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public String getRecipient() { return recipient; }
}
```

---

# Step 2 — Template Prototype

Now template DOES NOT SEND
It only creates instances and renders.

```java
import java.util.Map;

interface NotificationTemplate {
    NotificationTemplateInstance copy();
}
```

---

# Step 3 — Mutable Template Instance (Per Request Object)

This is what actually gets modified using variables.

```java
interface NotificationTemplateInstance {
    void setVariable(String key, String value);
    RenderedMessage render(String to);
}
```

---

# Step 4 — Concrete Email Template (Prototype)

Heavy object → compiled once → cloned many times

```java
import java.util.HashMap;
import java.util.Map;

class WelcomeEmailTemplate implements NotificationTemplate {

    private final String subjectTemplate;
    private final String bodyTemplate;

    public WelcomeEmailTemplate() {
        // Imagine HTML parsed + compiled here (heavy work)
        this.subjectTemplate = "Welcome {{name}}!";
        this.bodyTemplate = "Hi {{name}}, thanks for joining {{company}}.";
    }

    @Override
    public NotificationTemplateInstance copy() {
        return new WelcomeEmailInstance(this);
    }

    public String getSubjectTemplate() { return subjectTemplate; }
    public String getBodyTemplate() { return bodyTemplate; }
}
```

---

# Step 5 — Instance Implementation (Mutable + Render)

```java
class WelcomeEmailInstance implements NotificationTemplateInstance {

    private final WelcomeEmailTemplate template;
    private final Map<String, String> variables = new HashMap<>();

    public WelcomeEmailInstance(WelcomeEmailTemplate template) {
        this.template = template;
    }

    @Override
    public void setVariable(String key, String value) {
        variables.put(key, value);
    }

    @Override
    public RenderedMessage render(String to) {
        String subject = template.getSubjectTemplate();
        String body = template.getBodyTemplate();

        for (Map.Entry<String,String> e : variables.entrySet()) {
            subject = subject.replace("{{" + e.getKey() + "}}", e.getValue());
            body = body.replace("{{" + e.getKey() + "}}", e.getValue());
        }

        return new RenderedMessage(subject, body, to);
    }
}
```

---

# Step 6 — Template Registry (Prototype Store)

Now this becomes a real Prototype Registry

```java
import java.util.Map;

enum TemplateType {
    WELCOME_EMAIL
}

class TemplateRegistry {

    private static final Map<TemplateType, NotificationTemplate> templates = Map.of(
            TemplateType.WELCOME_EMAIL, new WelcomeEmailTemplate()
    );

    public static NotificationTemplateInstance getTemplate(TemplateType type) {
        NotificationTemplate template = templates.get(type);
        if (template == null)
            throw new IllegalArgumentException("Template not found");

        return template.copy(); // 🔥 CLONING PROTOTYPE
    }
}
```

---

# Step 7 — Channel (Separate Responsibility)

```java
interface NotificationChannel {
    void send(RenderedMessage message);
}
```

---

### Email Sender

```java
class EmailChannel implements NotificationChannel {
    @Override
    public void send(RenderedMessage message) {
        System.out.println("Sending EMAIL to " + message.getRecipient());
        System.out.println("Subject: " + message.getSubject());
        System.out.println("Body: " + message.getBody());
    }
}
```

---

# Step 8 — Notification Service (Real Entry Point)

```java
class NotificationService {

    private final NotificationChannel emailChannel = new EmailChannel();

    public void sendWelcome(String email, String name, String company) {

        NotificationTemplateInstance template =
                TemplateRegistry.getTemplate(TemplateType.WELCOME_EMAIL);

        template.setVariable("name", name);
        template.setVariable("company", company);

        RenderedMessage message = template.render(email);

        emailChannel.send(message);
    }
}
```

---

# Step 9 — Client

```java
public class Main {
    public static void main(String[] args) {

        NotificationService service = new NotificationService();

        service.sendWelcome("bish@example.com", "Bishwajeet", "Amazon");
        service.sendWelcome("rahul@example.com", "Rahul", "Uber");
    }
}
```

---

# Why This Is Now a TRUE Production Prototype

| Feature                | Supported |
| ---------------------- | --------- |
| Heavy template reused  | ✔         |
| Per request mutation   | ✔         |
| Thread safe            | ✔         |
| No shared state        | ✔         |
| Multi channel ready    | ✔         |
| Extendable templates   | ✔         |
| Separation of concerns | ✔         |

---

# What You Just Built (Interview Answer)

> We use Prototype Pattern to avoid re-parsing heavy templates per request.
> A compiled template is stored in registry and cloned for each notification.
> Each clone receives runtime variables and produces a rendered immutable message which is sent via channel abstraction.

This is a **very strong SDE-2 / Senior answer**

---

If you want next level —
we can now add **SMS + Push + Retry + Kafka queue**
→ turns this into a real distributed notification system (very commonly asked).
