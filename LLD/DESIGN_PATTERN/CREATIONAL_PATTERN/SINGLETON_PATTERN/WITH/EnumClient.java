package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EnumClient {
    public static void main(String[] args) {
        
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<?>> futureList = new ArrayList<>();
        for(int i=0;i<100;i++){
            futureList.add(executor.submit(()->{
                EnumSingleton instance = EnumSingleton.INSTANCE;
                System.out.println(instance.hashCode()+"  <>  "+Thread.currentThread().getName());
            }));
        }
        futureList.forEach((f)->{
            try {
                f.get();
            } catch (Exception e) {
            }
        });
        
        EnumSingleton.getInstance(EnumSingleton.INSTANCE);
        executor.shutdown();
    }
}
