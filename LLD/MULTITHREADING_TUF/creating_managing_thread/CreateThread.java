package LLD.MULTITHREADING_TUF.creating_managing_thread;

/*
Problem Statement
Imagine you’ve placed an order, and the next steps involve sending three notifications:

Send an SMS: This takes 2 seconds.
Send an Email: This takes 3 seconds.
Send ETA (Estimated Time of Arrival): This takes 5 seconds.

To simulate these delays in Java, we will use the Thread.sleep() from the java.lang.

Let's look at the code that tries to solve this problem performing the tasks in a sequential manner (without using Multithreading).
*/
interface Notification{
    public void send(String content);
}

class EmailNotify implements Notification{

    @Override
    public void send(String content) {
        System.out.println(Thread.currentThread().getName()+" is working on email send");
        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.out.println("Email sending: "+ content);
    }
    
}

class SmsNotify implements Notification{

    @Override
    public void send(String content) {
        System.out.println(Thread.currentThread().getName()+" is working on sms send");
        try{
            Thread.sleep(3000);
        }catch(InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.out.println("Sms sending: "+ content);
    }
    
}

class ETANotify implements Notification{

    @Override
    public void send(String content) {
        System.out.println(Thread.currentThread().getName()+" is working on eta calculation and send");
        System.out.println("Started calculating ETA");
        try{
            Thread.sleep(5000);
        }catch(InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.out.println("ETA sending: "+ content);
    }
    
}

public class CreateThread {
    public static void main(String[] args) {
        // Blocking main thread
        // Notification mail = new EmailNotify();
        // Notification sms = new SmsNotify();
        // Notification eta = new ETANotify();

        // mail.send("content");
        // sms.send("content");
        // eta.send("content");

        // using Threads to work concurrently non blocking main thread...
        Notification mail = new EmailNotify();
        Notification sms = new SmsNotify();
        Notification eta = new ETANotify();

        Thread t1 = new Thread(()->mail.send("content"));
        Thread t2 = new Thread(()->sms.send("content"));
        Thread t3 = new Thread(()->eta.send("content"));
        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();        
            t2.join();        
            t3.join();  
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }    
    }
}
