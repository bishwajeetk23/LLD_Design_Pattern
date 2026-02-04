package Java.Java_8.DefaultMethod;

interface A{
    default void hello(){
        System.out.println("Hello from A");
    }
}

interface B{
    default void hello(){
        System.out.println("Hello from B");
    }
}

public class DefaultMethod implements A,B{

    @Override
    public void hello() {
        A.super.hello();
        B.super.hello();
        System.out.println("My own implementation");
    }


    public static void main(String[] args) {
        DefaultMethod myclass = new DefaultMethod();
        myclass.hello();
    }
}
