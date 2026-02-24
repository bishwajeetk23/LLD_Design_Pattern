Nice — now we enter **real senior-level Builder pattern** territory 🚀

What we wrote earlier works only for **flat objects**.

But the moment inheritance appears:

```
Employee
   ↑
Manager
   ↑
Director
```

Your previous Builder **breaks badly**.

This is exactly the problem described in *Effective Java — Joshua Bloch*:

> “Builder does not compose well with inheritance unless we use self-types (recursive generics).”

This is called the **Hierarchical Builder Pattern**.

---

# ❌ Why Normal Builder Fails with Inheritance

Suppose:

```java
class Manager extends Employee {
    private int teamSize;
}
```

Now try:

```
new Manager.Builder()
    .name("A")
    .departmentId("D")
    .teamSize(5)
    .build();
```

Problem:

Parent builder returns `Employee.Builder`
So chaining breaks.

You lose subtype methods.

---

# Root Cause

Your builder returns:

```java
public Builder departmentId(String id)
```

But when subclass extends it, the return type is still **parent Builder**.

So Java thinks:

```
Employee.Builder → no teamSize()
```

This is called the **covariant return type problem in fluent APIs**

---

# Solution — SELF TYPE GENERICS (Recursive Generics)

We make builder aware of its child type.

This pattern looks scary but is actually simple:

```
abstract static class Builder<T extends Builder<T>>
```

The builder returns `T` instead of `Builder`.

This enables polymorphic chaining.

---

# FULL HIERARCHICAL BUILDER IMPLEMENTATION

---

## Step 1 — Base Class (Employee)

```java
abstract class Employee {

    final String name;
    final String employeeId;
    final String departmentId;

    abstract static class Builder<T extends Builder<T>> {

        private String name;
        private String employeeId;
        private String departmentId;

        public T name(String name){
            this.name = name;
            return self();
        }

        public T employeeId(String id){
            this.employeeId = id;
            return self();
        }

        public T departmentId(String id){
            this.departmentId = id;
            return self();
        }

        // IMPORTANT: subclass returns itself
        protected abstract T self();

        abstract Employee build();
    }

    protected Employee(Builder<?> builder){
        this.name = builder.name;
        this.employeeId = builder.employeeId;
        this.departmentId = builder.departmentId;
    }
}
```

---

## Step 2 — Subclass (Manager)

```java
class Manager extends Employee {

    private final int teamSize;

    public static class Builder extends Employee.Builder<Builder> {

        private int teamSize;

        public Builder teamSize(int size){
            this.teamSize = size;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Manager build() {
            return new Manager(this);
        }
    }

    private Manager(Builder builder){
        super(builder);
        this.teamSize = builder.teamSize;
    }

    @Override
    public String toString(){
        return name + " manages team of " + teamSize;
    }
}
```

---

## Step 3 — Another Subclass (Director)

```java
class Director extends Manager {

    private final double budget;

    public static class Builder extends Manager.Builder {

        private double budget;

        public Builder budget(double budget){
            this.budget = budget;
            return this;
        }

        @Override
        public Director build() {
            return new Director(this);
        }
    }

    private Director(Builder builder){
        super(builder);
        this.budget = builder.budget;
    }

    @Override
    public String toString(){
        return name + " manages budget " + budget;
    }
}
```

---

# Usage

```java
public class Main {
    public static void main(String[] args) {

        Manager m = new Manager.Builder()
                .name("Alice")
                .employeeId("E101")
                .departmentId("Tech")
                .teamSize(8)
                .build();

        Director d = new Director.Builder()
                .name("Bob")
                .employeeId("E201")
                .departmentId("Finance")
                .teamSize(20)
                .budget(5_000_000)
                .build();

        System.out.println(m);
        System.out.println(d);
    }
}
```

---

# Why This Works

Because:

```
Builder<T extends Builder<T>>
```

means:

> Every subclass builder returns its OWN TYPE

So chaining preserves subtype methods.

---

# What Pattern This Demonstrates

| Concept                  | Used |
| ------------------------ | ---- |
| Recursive Generics       | ✔    |
| Covariant return typing  | ✔    |
| Fluent interface         | ✔    |
| Inheritance-safe builder | ✔    |

---

# Interview Explanation (Golden Line)

> Hierarchical builders use recursive generics to preserve fluent chaining across inheritance hierarchies by returning the self type instead of the base builder type.

---

# When You Actually Need This

Rare — but critical in:

* Query builders (Hibernate / JPA criteria)
* HTTP request builders
* AST / compiler nodes
* Complex domain hierarchies
* SDK design

---

If you want, next I can show the **step-builder pattern** (compile-time mandatory order enforcement) — that one impresses interviewers a lot 😄
