package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class StaticBlockSingleton{
    private static StaticBlockSingleton staticBlockSingleton;
    static{
        staticBlockSingleton = new StaticBlockSingleton();
    }
    private StaticBlockSingleton(){System.out.println("Constructor is created !!!");}
    public static StaticBlockSingleton getInstance(){
        return staticBlockSingleton;
    }
}

public class StaticBlockLoading {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<?>> futureList = new ArrayList<>();
        for(int i=0;i<100;i++){
            futureList.add(executor.submit(()->{
                System.out.println(StaticBlockSingleton.getInstance()+"  <>  "+Thread.currentThread().getName());
            }));
        }
        futureList.forEach((f)->{
            try {
                f.get();
            } catch (Exception e) {
            }
        });
        System.out.println(StaticBlockSingleton.getInstance());
        executor.shutdown();
    }
}
