package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Nested Inner class


class Singleton{

    private static class Holder{
        private static Singleton singleton = new Singleton();
    }

    
    private Singleton(){System.out.println("Constructor is created !!");}

    public static Singleton getInstance(){
        return Holder.singleton;
    }
}


public class LazyBillPug {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<?>> futureList = new ArrayList<>();
        for(int i=0;i<100;i++){
            futureList.add(executor.submit(()->{
                System.out.println(Singleton.getInstance()+"  <>  "+Thread.currentThread().getName());
            }));
        }
        futureList.forEach((f)->{
            try {
                f.get();
            } catch (Exception e) {
            }
        });
        System.out.println(Singleton.getInstance());
        executor.shutdown();
    }
}
