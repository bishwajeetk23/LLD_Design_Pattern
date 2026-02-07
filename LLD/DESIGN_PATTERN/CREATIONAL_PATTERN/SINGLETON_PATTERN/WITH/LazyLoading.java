package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// As this is not thread safe because 
// concurrent thread can pass null check condition its not necessary that concurrent thread can create two different instance of the class. 
// Implemented how this is not thread safe
class JudgeAnalytics{

    private static JudgeAnalytics judgeAnalytics;
    private int count;
    private JudgeAnalytics(){
        System.out.println("Constructor is called !!!!");
    }

    public static JudgeAnalytics getInstance(){
        if(judgeAnalytics==null){
            try {
                Thread.sleep(0);
            } catch (Exception e) {
            }
            judgeAnalytics = new JudgeAnalytics();
        }
        return judgeAnalytics;
    }
    public void increment(){
        this.count = this.count + 1;
    }

    public int getCount() {
        return this.count;
    }
}

public class LazyLoading {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<?>> futureList = new ArrayList<>();
        for(int i=0;i<100;i++){
            futureList.add(executor.submit(()-> {
                JudgeAnalytics threadInstance = JudgeAnalytics.getInstance();
                threadInstance.increment();
                System.out.println(threadInstance + "  <>  " +Thread.currentThread().getName());
            }));
        }
        JudgeAnalytics instance = JudgeAnalytics.getInstance();
        futureList.forEach((f)->{
            try{
                f.get();
            }catch(ExecutionException e){
                System.out.println("Execution exception");
            }catch( InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        System.out.println(instance.getCount());
        executor.shutdown();
    }
}
