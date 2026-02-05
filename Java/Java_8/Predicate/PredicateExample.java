package Java.Java_8.Predicate;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class PredicateExample {
    public static void main(String[] args) {
        Predicate<Integer> isEven = (a) -> a%2==0;
        System.out.println(isEven.test(5));
        UnaryOperator<Integer> sq = x -> x*x;
        System.out.println(sq.apply(100));;
    }
}
