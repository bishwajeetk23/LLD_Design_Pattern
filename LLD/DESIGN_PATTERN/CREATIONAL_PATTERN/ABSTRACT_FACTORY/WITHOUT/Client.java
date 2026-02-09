package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.ABSTRACT_FACTORY.WITHOUT;

// problem statement -> Need to create a checkout service whose work is to 
// process payent vai some gateway and generate invoice. 
// Also Further it should be scalable and extensible.

interface PaymentGateway{
    public void processPayment();
}

class Razorpay implements  PaymentGateway{

    @Override
    public void processPayment() {
        System.out.println("Paying using Razorpay");
    }
    
}

class PayU implements  PaymentGateway{

    @Override
    public void processPayment() {
        System.out.println("Paying using Payu");
    }
}

interface Invoice{
    public void generateInvoice();
}

class GSTInvoice implements  Invoice{

    @Override
    public void generateInvoice() {
        System.out.println("Generating invoice using GST invoice...");
    }
}

class CheckoutService{
    private PaymentGateway paymentGateway;
    public void checkout(String gateway){
        if(gateway.equalsIgnoreCase("razorpay")){
            paymentGateway = new Razorpay();
        }else{
            paymentGateway = new PayU();
        }
        paymentGateway.processPayment();
        Invoice invoice = new GSTInvoice();
        invoice.generateInvoice();
    }
}

// Now first abstract creation of object and business logic seperate using Factory method.
/*
interface PaymentGateway{
    public void processPayment();
}

class Razorpay implements  PaymentGateway{

    @Override
    public void processPayment() {
        System.out.println("Paying using Razorpay");
    }
    
}

class PayU implements  PaymentGateway{

    @Override
    public void processPayment() {
        System.out.println("Paying using Payu");
    }
}

interface Invoice{
    public void generateInvoice();
}

class GSTInvoice implements  Invoice{

    @Override
    public void generateInvoice() {
        System.out.println("Generating invoice using GST invoice...");
    }
}

class PaymentGatewayFactory{
    private static PaymentGateway paymentGateway;
    public static PaymentGateway getGatewayInstance(String gateway){
        if(gateway.equalsIgnoreCase("razorpay")){
            paymentGateway = new Razorpay();
        }else{
            paymentGateway = new PayU();
        }
        return paymentGateway;
    }
}

class CheckoutService{
    public void checkout(String gateway){
        PaymentGateway paymentGateway = PaymentGatewayFactory.getGatewayInstance(gateway);
        paymentGateway.processPayment();
        Invoice invoice = new GSTInvoice();
        invoice.generateInvoice();
    }
}
*/

public class  Client {
    public static void main(String[] args) {
        CheckoutService service = new CheckoutService();
        service.checkout("payu");
    }
}

