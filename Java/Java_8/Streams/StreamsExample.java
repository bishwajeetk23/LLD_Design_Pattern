package Java.Java_8.Streams;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamsExample {
    public static void main(String[] args) {
        // 1. Convert list in square
        List<Integer> list = Arrays.asList(1,2,3,5,6,7,8,9,10);
        // Mathod refrence
        List<Integer> squarList =  list.stream().map(Helper::listToSquareList).toList();
        List<Integer> collectsquarList =  list.stream().map(num->2*num).collect(Collectors.toList());
        System.out.println(squarList);
        System.out.println(collectsquarList);

        
    }
}
class Helper{
    public static int listToSquareList(int num){
        return num*num;
    }
}