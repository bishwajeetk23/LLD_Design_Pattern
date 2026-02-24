package WITH;
interface PaymentGateway{
    public void processPayment(double amount);
}
class PayU implements PaymentGateway{
    @Override
    public void processPayment(double amount) {
        System.out.println("Paying " + amount +" using PayU..");
    }
} 
class Razorpay{
    public void sendMoney(double amount){
        System.out.println("Paying " + amount +" using Razorpay..");
    }
}
class RazorpayAdapter implements PaymentGateway{
    @Override
    public void processPayment(double amount) {
        Razorpay razorpay = new Razorpay();
        razorpay.sendMoney(amount);
    }
}
class CheckoutService{
    public void pay(PaymentGateway paymentGateway, double amount){
        paymentGateway.processPayment(amount);
    }
}
public class Main {
    public static void main(String[] args) {
        CheckoutService service = new CheckoutService();
        service.pay(new PayU(), 100);
        service.pay(new RazorpayAdapter(), 1001);
    }
}
