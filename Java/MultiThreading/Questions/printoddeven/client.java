package Java.MultiThreading.Questions.printoddeven;


class MyTask{
    private int i = 0;
    private final int max = 10;
    public synchronized void printodd(){
        while(i<max){
            while(i%2==0){
            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(i+" "+Thread.currentThread().getName());
        if(i<=max)i++;
        notify();
        }
    }
    public synchronized void printeven(){
        while(i<max){
            while(i%2!=0){
            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(i+" "+Thread.currentThread().getName());
        if(i<=max)i++;
        notify();
        }
    }
}

public class client {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread oddThread = new Thread(()->{
            task.printodd();
        },"Odd Thread");
        Thread evenThread = new Thread(()->{
            task.printeven();
        },"Even Thread");

        oddThread.start();
        evenThread.start();
        try {
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
