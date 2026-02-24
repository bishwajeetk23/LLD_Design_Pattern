package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.Practice;

// 1. Singleton all types

import java.util.Map;
import java.util.function.Supplier;

class Eager{
    private static final Eager instance = new Eager();
    private Eager(){}
    public static Eager createInstance(){
        return instance;
    }
}
// 2. Eager Static
class EagerStatic{
    private static final EagerStatic instance;
    static{
        instance = new EagerStatic();
    }
    private EagerStatic(){}
    public static EagerStatic createInstance(){
        return instance;
    }
}
// 3. Lazy without thread safe
class Lazy{
    private static Lazy instance;
    private Lazy(){}
    public static Lazy createInstance(){
        if(instance==null){
            instance = new Lazy();
        }
        return instance;
    }
}

// 4. Synzhronized Thread safe
class LazySync{
    private static LazySync instance;
    private LazySync(){}
    public static synchronized LazySync createInstance(){
        if(instance==null){
            instance = new LazySync();
        }
        return instance;
    }
}

// 5. Double locking
class DoubleLock{
    private static volatile DoubleLock instance;
    private DoubleLock(){}
    public static DoubleLock createInstance(){
        if(instance==null){
            synchronized (DoubleLock.class) {
                if(instance==null){
                    instance=new DoubleLock();
                }
            }
        }
        return instance;
    }
}

// 6. BillPugh
class BillPugh{
    private static class Holder{
        private static final BillPugh instance = new BillPugh();
    }
    private BillPugh(){}
    public static BillPugh createInstance(){
        return Holder.instance;
    }
}
// 7. Enum
enum EnumSingleton{
    SINGLETON;
    private String id;
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
}

// Simple Factory

// Simple Factory centralizes creation using condition logic, while Factory Method delegates creation
//  responsibility to subclasses using polymorphism and supports open-closed principle.

enum GatewayType{
    RAZORPAY,
    PAYU
}

interface PaymentGateway{
    public void processPayment(double amount);
}

class Razorpay implements PaymentGateway{

    @Override
    public void processPayment(double amount) {
        System.out.println("Paying via Razorpay. Amount: "+amount);
    }
}

class PayU implements PaymentGateway{

    @Override
    public void processPayment(double amount) {
        System.out.println("Paying via PayU. Amount: "+amount);
    }
}

class PaymentGatewayFactory{
    private static final Map<GatewayType,Supplier<PaymentGateway>> factoryMap = Map.of(
        GatewayType.RAZORPAY,Razorpay::new,
        GatewayType.PAYU,PayU::new
    );
    public static PaymentGateway createGateway(GatewayType gatewayType){
        Supplier<PaymentGateway> supplier = factoryMap.get(gatewayType);
        if(supplier==null){
            throw new IllegalArgumentException("This gateway is not supported yet.");
        }
        return supplier.get();
    }
}

class PaymentService{
    public void checkout(GatewayType gatewayType, double amount){
        PaymentGateway gateway = PaymentGatewayFactory.createGateway(gatewayType);
        gateway.processPayment(amount);
    }
}


// Builder design pattern

class Employee{
    public static class Builder{
        private String name;
        private String employeeId;
        private String deptId;
        public Builder setname(String name){
            this.name = name;
            return this;
        }
        public Builder setempid(String empid){
            this.employeeId = empid;
            return this;
        }
        public Builder setdeptid(String deptid){
            this.deptId = deptid;
            return this;
        }
        public Employee build(){
            return new Employee(this);
        }
    }
    private String name;
    private String employeeId;
    private String deptId;
    private Employee(){}
    private Employee(Builder builder){
        this.deptId = builder.deptId;
        this.employeeId = builder.employeeId;
        this.name = builder.name;
    }
    public void printDescription(){
        System.out.println(this.name+ " is an empoyee of deptId: "+this.deptId+" whose empId: "+this.employeeId);
    }
}

public class p1 {
    public static void main(String[] args) {
        // Singleton Client
        // EnumSingleton instance1 = EnumSingleton.SINGLETON;
        // instance1.setId("Bishwajeet");
        // System.out.println(instance1.getId());
        // EnumSingleton instance2 = EnumSingleton.SINGLETON;
        // instance2.setId("V2");
        // System.out.println(instance1.getId());
        // System.out.println(instance2.getId());
        // System.out.println(instance2.hashCode()==instance1.hashCode());

        // Factory Client
        // PaymentService service = new PaymentService();
        // service.checkout(GatewayType.RAZORPAY, 1000);
        // service.checkout(GatewayType.PAYU, 1001);

        // Builder pattern client
        Employee e1 = new Employee.Builder()
        .setdeptid("deptid")
        .setempid("empid")
        .setname("name")
        .build();
        e1.printDescription();
    }
}