package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.ABSTRACT_FACTORY.WITH;

// problem statement -> Need to create a checkout service whose work is to 

import java.util.Map;
import java.util.function.Supplier;

// process payent vai some gateway and generate invoice. 
// Also Further it should be scalable and extensible.


/*  Without any design pattern.
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
    */


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
// Now we have used Factory and our service looks clean but Now there is region India and USA. 
// India have Razorpay and PayU as payment gateway and GSTInvoice as India way of generating invoice and 
// USA have Stripe and Paypal as payment gateway and USAInvoice as USA way of generating invoice.

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

class Stripe implements  PaymentGateway{

    @Override
    public void processPayment() {
        System.out.println("Paying using Stripe .....");
    }
}

class Paypal implements  PaymentGateway{

    @Override
    public void processPayment() {
        System.out.println("Paying using Paypal .....");
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

class USAInvoice implements  Invoice{

    @Override
    public void generateInvoice() {
        System.out.println("Generating invoice using USA invoice...");
    }
}


class IndiaFactory{
    public static PaymentGateway getGatewayInstance(String gateway){
        return switch (gateway.toUpperCase()) {
            case "RAZORPAY" -> new Razorpay();
            case "PAYU" -> new PayU();
            default -> throw new IllegalArgumentException("Unsupported gateway for India");
        };
    }
    public static Invoice createInvoice(){
        return new GSTInvoice();
    }
}

class USAFactory{
    public static PaymentGateway getGatewayInstance(String gateway){
        return switch (gateway.toUpperCase()) {
            case "STRIPE" -> new Razorpay();
            case "PAYPAL" -> new PayU();
            default -> throw new IllegalArgumentException("Unsupported gateway for USA");
        };
    }
    public static Invoice createInvoice(){
        return new USAInvoice();
    }
}

class CheckoutService{
    public void checkout(String gateway,String region){
        if(region.equalsIgnoreCase("INDIA")){
            PaymentGateway paymentGateway = IndiaFactory.getGatewayInstance(gateway);
            paymentGateway.processPayment();
            Invoice invoice = IndiaFactory.createInvoice();
            invoice.generateInvoice();
        }else{
            PaymentGateway paymentGateway = USAFactory.getGatewayInstance(gateway);
            paymentGateway.processPayment();
            Invoice invoice = USAFactory.createInvoice();
            invoice.generateInvoice();
        }
    }
}

*/

// When we add extra region then it is not much scalable. For this case only we use Abstract factory,
//  i.e using and interface for creating families of related or dependent objects without specifying their concrete classes.


enum GatewayType{
    RAZORPAY,
    PAYU,
    PAYPAL,
    STRIPE
}

enum InvoiceType{
    GSTINVOICE,
    USAINVOICE
}

interface PaymentGateway{
    public void processPayment(double amount);
}

class Razorpay implements  PaymentGateway{

    @Override
    public void processPayment(double amount) {
        System.out.println("Paying using Razorpay: " + amount);
    }
    
}

class PayU implements  PaymentGateway{

    @Override
    public void processPayment(double amount) {
        System.out.println("Paying using Payu: "+ amount);
    }
}

class Stripe implements  PaymentGateway{

    @Override
    public void processPayment(double amount) {
        System.out.println("Paying using Stripe: "+ amount);
    }
}

class Paypal implements  PaymentGateway{

    @Override
    public void processPayment(double amount) {
        System.out.println("Paying using Paypal: "+amount);
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

class USAInvoice implements  Invoice{

    @Override
    public void generateInvoice() {
        System.out.println("Generating invoice using USA invoice...");
    }
}

interface RegionFactory{
    public PaymentGateway getGatewayInstance(GatewayType gateway);
    public Invoice createInvoice();
}

class IndiaFactory implements  RegionFactory{
    private final Map<GatewayType,Supplier<PaymentGateway>> gateways = Map.of(
        GatewayType.RAZORPAY, Razorpay::new,
        GatewayType.PAYU, PayU::new
    );

    private final Map<InvoiceType,Supplier<Invoice>> invoice = Map.of(
        InvoiceType.GSTINVOICE, GSTInvoice::new
    );

    @Override
    public PaymentGateway getGatewayInstance(GatewayType gateway){
        Supplier<PaymentGateway> supplier = gateways.get(gateway);
        if(supplier==null){
            throw new IllegalArgumentException("Unsupported gateway for India");
        }
        return supplier.get();
    }

    @Override
    public Invoice createInvoice(){
        Supplier<Invoice> supplier = invoice.get(InvoiceType.GSTINVOICE);
        if(supplier==null){
            throw new IllegalArgumentException("Unsupported invoice method for India");
        }
        return supplier.get();
    }
}

class USAFactory implements  RegionFactory{
    private final Map<GatewayType,Supplier<PaymentGateway>> gateways = Map.of(
        GatewayType.STRIPE, Stripe::new,
        GatewayType.PAYPAL, Paypal::new
    );

    private final Map<InvoiceType,Supplier<Invoice>> invoice = Map.of(
        InvoiceType.USAINVOICE, USAInvoice::new
    );

    @Override
    public PaymentGateway getGatewayInstance(GatewayType gateway){
        Supplier<PaymentGateway> supplier = gateways.get(gateway);
        if(supplier==null){
            throw new IllegalArgumentException("Unsupported gateway method for USA");
        }
        return supplier.get();
    }
    @Override
    public Invoice createInvoice(){
        return new USAInvoice();
    }
}

class CheckoutService{
    private final RegionFactory regionFactory;
    public CheckoutService(RegionFactory regionFactory){
        this.regionFactory = regionFactory;
    }
    public void checkout(double amount, GatewayType gatewayType){
        PaymentGateway paymentGateway = regionFactory.getGatewayInstance(gatewayType);
        paymentGateway.processPayment(amount);
        Invoice invoice = regionFactory.createInvoice();
        invoice.generateInvoice();
    }
}

public class  Main {
    public static void main(String[] args) {
        CheckoutService indiaService = new CheckoutService(new IndiaFactory());
        CheckoutService usaService = new CheckoutService(new USAFactory());
        indiaService.checkout(1000,GatewayType.RAZORPAY);
        usaService.checkout(101,GatewayType.STRIPE);
        usaService.checkout(1005,GatewayType.PAYPAL);
    }
}


