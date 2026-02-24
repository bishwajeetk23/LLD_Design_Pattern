package Java.Java_8.Streams;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

// 1️⃣ Filter + Map

// You are given a list of integers.
// Return squares of only even numbers.

// Example:
// [1,2,3,4,5] → [4,16]

public class Questions {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        Predicate<Integer> evenpredicate = a -> a%2 == 0;
        List<Integer> ans = list.stream().filter((a)->evenpredicate.test(a)).map(convertSquareHelper::convert).toList();
        System.out.println(ans);
    }
}

class convertSquareHelper{
    public static int convert(int a){
        return a*a;
    }
}
