package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class AIAnalytics{  
    private static volatile AIAnalytics aiAnalytics;

    private AIAnalytics(){System.out.println("Constructor is called !!");}

    public static AIAnalytics getInstance(){
        if(aiAnalytics==null){
            synchronized (AIAnalytics.class) {
                if(aiAnalytics==null){
                    aiAnalytics = new AIAnalytics();
                }
            }
        }
        return aiAnalytics;
    }
}

public class LazyDoubleLocking {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<?>> futureList = new ArrayList<>();
        for(int i=0;i<100;i++){
            futureList.add(executor.submit(()->{
                System.out.println(AIAnalytics.getInstance()+"  <>  "+Thread.currentThread().getName());
            }));
        }
        futureList.forEach((f)->{
            try {
                f.get();
            } catch (Exception e) {
            }
        });
        System.out.println(AIAnalytics.getInstance());
        executor.shutdown();
    }
}
