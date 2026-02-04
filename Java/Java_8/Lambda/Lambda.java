package Java.Java_8.Lambda;

public class Lambda {
    public static void main(String[] args) {
        Thread thread1 = new Thread(()->{
            for(int i=0;i<10;i++)System.out.println(i + Thread.currentThread().getName());
            
        },"Thread 1");
        thread1.start();
    }
}
