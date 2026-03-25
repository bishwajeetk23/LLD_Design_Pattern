package LLD.DESIGN_PATTERN.BEHAVIOURAL_PATTERN.STRATAGY_PATTERN.WITH;

import java.util.Map;
import java.util.function.Supplier;

enum PaymentStratagy{
    RAZORPAY,
    PAYU
}

// using factory we can make this more flexible

interface PaymentGateway{
    public void makePayment();
}

class Razorpay implements PaymentGateway{

    @Override
    public void makePayment() {
        System.out.println("This payment is razorpay gateway for making payment.");
    }
}

class PayU implements PaymentGateway{

    @Override
    public void makePayment() {
        System.out.println("This payment is payu gateway for making payment.");
    }
    
}

class PaymentGatewayFactory{
    private static Map<PaymentStratagy, Supplier<PaymentGateway>> gatewayMap = Map.of(
        PaymentStratagy.RAZORPAY, Razorpay::new,
        PaymentStratagy.PAYU, PayU::new
    );
    public static PaymentGateway createGateway(PaymentStratagy strategy) {
        Supplier<PaymentGateway> gateway = gatewayMap.get(strategy);

        if(gateway == null)throw new IllegalArgumentException();

        return gateway.get();
    }
}

class PaymentService{
    public PaymentService(){
        System.out.println("Payment service initialized.");
    }
    public void processPayment(PaymentStratagy stratagy){
        System.out.println("Started processing of payment using gateway: " + stratagy.name());
        PaymentGatewayFactory.createGateway(stratagy).makePayment();
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting the application.");
        PaymentService service = new PaymentService();
        service.processPayment(PaymentStratagy.RAZORPAY);
    }
}
