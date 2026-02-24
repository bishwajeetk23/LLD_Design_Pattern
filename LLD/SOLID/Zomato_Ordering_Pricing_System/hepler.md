Perfect — now we’ll design this like a **real production food-ordering backend** 🍕

We will combine:

> **Builder → constructs the Pizza order (structure)**
> **Decorator → calculates pricing dynamically (behavior)**

And we will solve all 5 real problems:

1. Structured invoice (no string concat)
2. Remove topping
3. Coupon on specific topping
4. Tax only on cheese
5. Half-pizza toppings

---

# 🧠 Final Architecture (What big systems actually do)

Instead of wrapping objects endlessly (classic decorator),
we store **data as configuration** and run **pricing decorators as pipeline**

This is how scalable commerce engines work.

```
User builds pizza  → Builder
Pizza frozen order → Immutable Order
Pricing Engine     → Decorator chain
Invoice            → Structured components
```

So decorator operates on **Order Model**, not Pizza object.

---

# 1️⃣ Domain Model

## Topping Portion

```java
enum Portion {
    FULL,
    LEFT_HALF,
    RIGHT_HALF
}
```

---

## Topping

```java
class Topping {
    final String name;
    final double price;
    final Portion portion;

    Topping(String name, double price, Portion portion) {
        this.name = name;
        this.price = price;
        this.portion = portion;
    }

    double effectivePrice() {
        return portion == Portion.FULL ? price : price / 2;
    }
}
```

---

## Pizza Order (IMMUTABLE → Builder)

```java
import java.util.*;

class PizzaOrder {

    final String base;
    final double basePrice;
    final List<Topping> toppings;

    private PizzaOrder(Builder b) {
        this.base = b.base;
        this.basePrice = b.basePrice;
        this.toppings = List.copyOf(b.toppings);
    }

    static class Builder {
        private String base;
        private double basePrice;
        private List<Topping> toppings = new ArrayList<>();

        public Builder base(String base, double price) {
            this.base = base;
            this.basePrice = price;
            return this;
        }

        public Builder addTopping(String name, double price, Portion portion) {
            toppings.add(new Topping(name, price, portion));
            return this;
        }

        public Builder removeTopping(String name) {
            toppings.removeIf(t -> t.name.equalsIgnoreCase(name));
            return this;
        }

        public PizzaOrder build() {
            return new PizzaOrder(this);
        }
    }
}
```

✔ Supports removing toppings
✔ Supports half toppings
✔ Immutable after build

---

# 2️⃣ Pricing Engine → REAL Decorator

We now apply decorators on **Bill**

---

## Bill Line

```java
class BillItem {
    String name;
    double amount;

    BillItem(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }
}
```

---

## Bill

```java
import java.util.*;

class Bill {
    List<BillItem> items = new ArrayList<>();

    void add(String name, double amt) {
        items.add(new BillItem(name, amt));
    }

    double total() {
        return items.stream().mapToDouble(i -> i.amount).sum();
    }
}
```

---

# 3️⃣ Pricing Decorator Interface

```java
interface BillStep {
    void apply(PizzaOrder order, Bill bill);
}
```

This is the **real decorator**
Each rule independently modifies bill.

---

# 4️⃣ Base Price Step

```java
class BasePriceStep implements BillStep {
    public void apply(PizzaOrder order, Bill bill) {
        bill.add(order.base, order.basePrice);
    }
}
```

---

# 5️⃣ Topping Step

```java
class ToppingPriceStep implements BillStep {
    public void apply(PizzaOrder order, Bill bill) {
        for (Topping t : order.toppings) {
            bill.add(t.name + " ("+t.portion+")", t.effectivePrice());
        }
    }
}
```

---

# 6️⃣ Apply tax only on cheese

```java
class CheeseTaxStep implements BillStep {
    private static final double TAX = 0.1;

    public void apply(PizzaOrder order, Bill bill) {
        for (Topping t : order.toppings) {
            if (t.name.equalsIgnoreCase("cheese")) {
                bill.add("Cheese Tax", t.effectivePrice() * TAX);
            }
        }
    }
}
```

---

# 7️⃣ Coupon on specific topping

10% off olives

```java
class OliveDiscountStep implements BillStep {
    public void apply(PizzaOrder order, Bill bill) {
        for (Topping t : order.toppings) {
            if (t.name.equalsIgnoreCase("olive")) {
                bill.add("Olive Discount", -t.effectivePrice() * 0.10);
            }
        }
    }
}
```

---

# 8️⃣ Pricing Engine

```java
import java.util.List;

class PricingEngine {

    private final List<BillStep> steps;

    PricingEngine(List<BillStep> steps) {
        this.steps = steps;
    }

    Bill generate(PizzaOrder order) {
        Bill bill = new Bill();
        for (BillStep step : steps)
            step.apply(order, bill);
        return bill;
    }
}
```

This is **Decorator Chain (real production version)**

We can dynamically reorder / enable / disable rules.

---

# 9️⃣ Usage

```java
public class Main {

    public static void main(String[] args) {

        PizzaOrder order = new PizzaOrder.Builder()
                .base("Margherita",100)
                .addTopping("Cheese",60,Portion.FULL)
                .addTopping("Olive",40,Portion.LEFT_HALF)
                .removeTopping("Corn") // supported
                .build();

        PricingEngine engine = new PricingEngine(List.of(
                new BasePriceStep(),
                new ToppingPriceStep(),
                new CheeseTaxStep(),
                new OliveDiscountStep()
        ));

        Bill bill = engine.generate(order);

        bill.items.forEach(i ->
                System.out.println(i.name + " : " + i.amount));

        System.out.println("TOTAL = " + bill.total());
    }
}
```

---

# 🧠 What Patterns We Actually Used

| Pattern          | Where                              |
| ---------------- | ---------------------------------- |
| Builder          | Create order safely                |
| Decorator        | Pricing rules pipeline             |
| Strategy         | Each pricing rule independent      |
| Open Closed      | Add new rule without touching code |
| Immutable Object | Prevent post-checkout mutation     |

---

# Why This is Senior/Staff Level

Because real systems:

* never mutate domain objects
* separate order vs pricing
* treat pricing as rule engine
* allow runtime configuration

---

# Real Interview Follow-ups This Design Handles

| Question          | Works?            |
| ----------------- | ----------------- |
| Add GST later     | ✔ add new step    |
| Remove cheese tax | ✔ remove step     |
| Weekend discount  | ✔ new decorator   |
| Regional pricing  | ✔ choose rule set |
| A/B experiment    | ✔ toggle rules    |

---

# Key Takeaway

Classic decorator (wrapping objects)
👉 good for libraries

Pipeline decorator (rule engine)
👉 used in production commerce systems

---

If you want next —
I can turn this into a **Swiggy/Zomato HLD discussion answer** (how pricing microservice works).
