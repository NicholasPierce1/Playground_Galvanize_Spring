package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration
public class OcpTest {

    @Test
    public void testStreamCompare(){
            Stream<Integer> intStreams = Stream.of(5, 1, 8);
            Comparator<Integer> comparator =
                    (a, b) -> a.compareTo(b);

            System.out.println(intStreams
                    .sorted(comparator)
                    .collect(Collectors.toList()));

            Stream<Integer> intStreams1 = Stream.of(5, 1, 8);
            Comparator<Integer> comparator1 =
                    (a, b) -> b.compareTo(a); // returns 1 but sorter sees that 'a' got the 1 so [1,2] --> [2,1]

        /*
        why:
        the caller of this method does not know if you compared a to b or b to a.
        it assumes that it is a to b!
        if b to a then
         */

            System.out.println(intStreams1
                    .sorted(comparator1)
                    .collect(Collectors.toList()));

    }

    @Test
    public void testStringAndWrapperNull(){
        String string; // no default
        //System.out.println(string == null);
        Integer x = 3;
        int x1 = x.intValue();
        int x2 = x;
        String a = "", b = "";
        String c, d = ""; // c is unassigned
        double double1 = 1_2.1_1_1;
    }

    @Test
    public void testListArrayPair(){
        final String[] array = {"hello", "world"};
        final List<String> list = Arrays.asList(array);
        //list.add("add me"); both break
        Double dob = 3e1D;
       // Long log = 3; bad (3l || 3L)
        // wrappers/auto-box cannot be casted or autoboxed from another
        long log = 3;
        Long log1 = 3l;
        Long log2 = 3L;
        Double xx = (double)log; // must cast
        System.out.println(list);
        final List<String> list1 = new ArrayList<>(){{
            add("hello");
            add("world");
        }};

        list1.add("!!");
        System.out.println(list1);

        final List<String> list2 = List.of(array);
        final List<String> list3 = Arrays.asList(array);
        list3.set(0, "my wish");
        // list3.add(" thank ya");
        //list3.remove(0);
        //list2.set(0, "also my wish");
        System.out.println(list3);
        //list2.add("!!!"); both break
        System.out.println(list2);
        /*
        list.of --> can't do anything but read (R)
        arrays.asList --> (RU) but NOT CD
         */
    }

    @Test
    public void testExpressionOne(){
        int flair = 15;
        if(flair >= 15 && flair < 37){
            System.out.println("A");
        }
        if(flair == 37){
            System.out.println("B");
        }
        else{
            System.out.println("C");
        }

    }

    @Test
    public void testStringPool(){
        String s = "hello";
        String t = "hello"; // via string pool their values are equal
        // string pools are only compatible with literal values
        String a = new String(s);
        System.out.println("hello" == s);
        System.out.println("hello".equals(s));
        System.out.println(s == t);
        System.out.println(s == a);
        String b = s.concat(" world");
        String c = "hello world";
        System.out.println(b == c);
        System.out.println(b.equals(c));
        System.out.println("hello world" == c);
        System.out.println("hello world" == b);
        StringBuilder builder = new StringBuilder("rumble");
        //System.out.println(builder == b); (==) only on same types
        //System.out.println("the value is: " + 1+2>=3); bad --> if no () on ternary then all to the left goes
        System.out.println("the value is: " + (1+2>3));

        // rumble4 --> rumle4 --> rum
        builder.append(4).deleteCharAt(3).delete(3, builder.length());
        System.out.println(builder);
        boolean bool1 = true;
        boolean bool2 = false;
        if(bool1 = bool2)
            System.out.println("A");
        else
            System.out.println("B");
        int x = 3;
    }

    @Test
    public void testSwitch(){
        int x = 3;
        switch(x){
            default:
                x++;
        }
        int[] arr = {15,18,20,22,26};
        System.out.println(
                Arrays.binarySearch(arr, 19) // -1 * (next_highest_num_i + 1) ==
        );

    }

    static interface MyInterface{
        default void doSomething(){};
        void doThis();
        public abstract void doThat();
    }

    static class A{
        public int x = 3;
        public static int y = 3;
        public static int x(){return 0;}
        public void print(double x){
            System.out.println("A");
        }
        public void exceptionTest() throws ArithmeticException{}
        public void exceptionCheckTest() throws IOException{}
    }

    static class B extends A{
        public int x = 3;
        public static int y = 3;
        public static int x(){return 0;}
        public void print(int x){
            System.out.println("B");
        }
        public void exceptionTest() throws RuntimeException{}
        //public void exceptionCheckTest() throws FileNotFoundException{} good
        public void exceptionCheckTest() throws IOException{} // good
    }

    static class C extends A{
        public void exceptionTest() throws NullPointerException, IllegalStateException{}
        //public void exceptionCheckTest() throws Error{} // good
        public void exceptionCheckTest() throws NullPointerException {
            try{
                File.createTempFile("","");
            }
            catch(RuntimeException e){}
            catch(Exception e){ // errors IF try body doesn't give something that throws it (checked exception)

            }
            catch(StackOverflowError e){

            }
        }
    }

    final static class E extends A{
//        @Override
//        public void exceptionTest() throws FileNotFoundException { ( no checks in runtime )
//            super.exceptionTest();
//        }
    }

    static abstract public class D{}

    static class ExceptionOne extends RuntimeException{}
    static class ExceptionTwo extends RuntimeException{}

    @Test
    public void testInheritance(){
        A b = new B();
        System.out.println(b.x);
        System.out.println(b.x());
        System.out.println(b.y);
        b.print(4);
        b.print(9.0);
        Predicate<String> blah = s -> true;
        try{
            throw new ExceptionOne();
        }
        catch(ExceptionOne | ExceptionTwo e){
            System.out.println("Caught");
        }
    }

    static int[] hello = {1}; // new int[]{1};
    @Test
    public void arrays(){
        int[] blah[] = new int[10][20];
       // System.out.println(blah.size());
        blah[0][0] = 'x';
        System.out.println(blah[0][0]);
        hello[0] = 3;
        System.out.println(hello); // obj address
        outer: while (true){
            inner: while (true){
                break outer;
            }
        }
        System.out.println("done");
        testChar('d');
        testInt((char)3); // explicit cast needed here
        int someInt = '3';
        char someChar = 3;
        testInt(someChar);
    }

    public void testChar(int x){}
    public void testInt(char x){}


    private int x = Integer.parseInt("3");
    private int y = Integer.parseInt(String.valueOf(x));
}
