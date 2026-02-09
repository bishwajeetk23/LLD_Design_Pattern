package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;


// Total 7 ways to create singleton object
// 1. Eager
// 2. StaticBlockEager
// 3. Lazy
// 4. Synchronized Lazy
// 5. Double locking Lazy (volatile keyword + synchronized block)
// 6. Bill pugh Lazy (Nested inner class)
// 7. Enum 


// 1. Eager

class Eager{
    private static final Eager instance = new Eager();
    private Eager(){System.out.println("constructor is created");};
    public static Eager getInstance(){
        return instance;
    }
}

// 2. Static Eager

class StaticEager{
    private static StaticEager instance;
    static{
        instance = new StaticEager();   
    }
    private StaticEager(){System.out.println("Constructor is called!!");}
    
    public static StaticEager getInstance(){
        return instance;
    }
}

// 3. Lazy

class Lazy{
    private static Lazy instance;
    private Lazy(){System.out.println("HI");}
    public static Lazy getInstance(){
        if(instance == null){
            instance = new Lazy();
        }
        return instance;
    }
}

// 4. Synchronized Lazy

class LazySynchronized{
    private static LazySynchronized instance;
    private LazySynchronized(){System.out.println("Hi");}
    public static synchronized  LazySynchronized getInstance(){
        if(instance==null){
            instance = new LazySynchronized();
        }
        return instance;
    }
}

// 5. Double locking Lazy

class DoubleLock{
    private static volatile DoubleLock instance;
    private DoubleLock(){System.out.println("Hi");}
    public static DoubleLock getInstance(){
        if(instance==null){
            // multiple threads can come
            synchronized (DoubleLock.class) {
                // critical section
                // it ensure one by one thread execution
                if(instance==null){
                    // and this check is for threads already cretaed object or not 
                    // therefore volatile keyword should be used to read from main memory
                    instance = new DoubleLock();
                }
            }
        }
        return instance;
    }
}

// 6. Bill pugh (nested inner class)

class SingletonNestedInner{
    private static class Holder{
        private static final SingletonNestedInner instance = new SingletonNestedInner();
    }
    private SingletonNestedInner(){System.out.println("Hi");}
    public static SingletonNestedInner getInstance(){
        return Holder.instance;
    }
}
// 7. Enum is define in seperate class
public class SingletonPattern {
    public static void main(String[] args) {
        
    }
}
