package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class JudgeAnalytics{
    private static JudgeAnalytics judgeAnalytics;

    private JudgeAnalytics(){System.out.println("Constructor called from: " + this.getClass().getProtectionDomain().getCodeSource());}

    public static synchronized JudgeAnalytics getInstance(){
        if(judgeAnalytics == null){
            judgeAnalytics = new JudgeAnalytics();
        }
        return judgeAnalytics;
    }
}

public class LazySynchronized {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<?>> futureList = new ArrayList<>();
        for(int i=0;i<100;i++){
            futureList.add(executor.submit(()->{
                System.out.println(JudgeAnalytics.getInstance()+"  <>  "+Thread.currentThread().getName());
            }));
        }
        futureList.forEach((f)->{
            try {
                f.get();
            } catch (Exception e) {
            }
        });
        System.out.println(JudgeAnalytics.getInstance());
        executor.shutdown();
    }
}
