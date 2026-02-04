package Java.Java_8.Functional_Interface;

interface FunctionalInterface{
    String getName();
}

public class MyClass{
   public static void main(String[] args) {
    FunctionalInterface demo = () -> "hello";
    demo.getName();
   }
}


