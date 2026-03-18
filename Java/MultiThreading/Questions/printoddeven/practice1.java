package Java.MultiThreading.Questions.printoddeven;

class OddEvenTask{
    private volatile int number = 0;
    private int max = 10;
    public OddEvenTask(int num, int max){
        this.number = num;
        this.max = max;
    }

    public void printodd(){
            synchronized (this) {

        while(this.number<max){
            while(number%2==0){
                try {
                        wait();
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                } 
            }
            System.out.println("Printing odd number using thread -> "+ Thread.currentThread().getName()+" num: "+ this.number);
                this.number++;
                notify();
            }
        }
    }

    public void printeven(){
        synchronized (this) {
        while(this.number<max){
            while(number%2!=0){
                try {
                        wait();
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                } 
                }
            System.out.println("Printing even number using thread -> "+ Thread.currentThread().getName()+" num: "+ this.number);
                this.number++;
                notify();
            }
        }
    }
}
public class practice1 {
    public static void main(String[] args) {
        OddEvenTask task = new OddEvenTask(0, 10);
        Thread oddThread = new Thread(()->{
            task.printodd();
            System.out.println(" odd ");
        },"Odd_Thread");
        Thread evenThread = new Thread(()->{
            task.printeven();
            System.out.println("even");
        },"Even_Thread");

        oddThread.start();
        evenThread.start();

        try{
            oddThread.join();
            evenThread.join();
        }catch(InterruptedException e){

        }
        System.out.println(Thread.currentThread().getName());
    }
}
