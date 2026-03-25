package Java.Java_8.Streams;

// Stream API Full concept and practice.

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamConcept {
    public static void main(String[] args) {
        // 1. How manay salary is greater than 3000, solve using list and array both.
        Predicate<Integer> predicate = (num) -> num>3000;
        System.out.println(" Solving using array");
        int arr[] = {200,4100,9000,1000,3500};
        System.out.println(Arrays.stream(arr).filter(predicate::test).count());

        System.out.println("Solving using List");
        List<Integer> list = Arrays.asList(200,4100,9000,1000,3500);
        System.out.println(list.stream().filter(predicate::test).count());

        // There are five ways by which Streams can be created.

        // 1. Using Lists
        List<Integer> l = Arrays.asList(100,200,300,400); 
        Stream<Integer> streamFromList = l.stream();

        // 2. Using Arrays
        int[] array = {100,110,203,443};
        Stream<Integer> streamfromarray = Arrays.stream(array).boxed();

        // 3. using Stream class
        Stream<Integer> streamfromclass = Stream.of(100,10001,234);

        // 4. using stream builder
        // Stream<Integer> streamfrombuilder = Stream.builder().add(677).add(988).build();
        Stream.Builder<Integer> streambuilder = Stream.builder();
        Stream<Integer> streamusingbuilder = streambuilder.add(199).add(87).build();

        // 5. using iterator creating 
        Stream<Integer> stream = Stream.iterate(100, (Integer num)-> num+1000).limit(5);


        // Intermediate Operations whose input is stream and output is also stream.

        // 1. filter<Predicate<T>predicate>

        // 2. map<Function<T,R> mapper>

        // 3. flatMap(T,Stream<R> mapper)

        // 4. sorted()

        // 5. distinct()

        // 6. peek()

        // 7. mapToInt()

        // 8. 

    }   
}
