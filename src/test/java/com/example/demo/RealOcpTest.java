package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;
import scala.collection.mutable.StringBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RealOcpTest {

    @Test
    public void testInnerClass(){
        final ParentWithInner parentWithInner = new ParentWithInner();
        //System.out.println(new parentWithInner.Inner());
        final ParentWithInner.Inner inner = parentWithInner.new Inner();
        System.out.println(inner.x);
        final ParentWithInner.Inner inner1 = new ParentWithInner().new Inner();
    }

    class ParentWithInner{

        private int y = 5;

        private int x = 10;

        private String name = "inner";

        void printInnerX(){
            System.out.println(new Inner().x);
        }

        static void tryItAgain(){
           // System.out.println(new Inner().x);
        }

        class Inner{
            public static int z = 3;

            public int x = 100;

            int getSum(){
                return x + y; // if Inner is not static
            }

        }

        static class StaticInner {

            public String name = "inner";

            void printX(){
                StaticInner inner = new StaticInner();
                //ParentWithInner parentWithInner = new StaticInner();
                System.out.println(name);
            }

        }
    }

    @Test
    public void testEnum(){

    }

    enum Animals{
        MAMMAL(true){
            @Override
            public void printType() {
                System.out.println("I'm a mammal!");
            }
        },
        FISH{
            @Override
            public void printType() {
                System.out.println("I'm a fish!");
            }
        };

        public boolean isCool;

        Animals(){
            this.isCool = false;
        }

        Animals(boolean isCool){
            this.isCool = isCool;
        }

        public void makeCool(){
            this.isCool = true;
        }

        public abstract void printType();
    }

    @Test
    public void testFunctionalInterface(){
        final DoSomething doSomething =
                () -> {
                    System.out.println("helloooooo");
                };

        doSomething.doSomething();
    }

    @FunctionalInterface // not needed
    interface DoSomething{
        public abstract void doSomething();
    }

    static class A_ThreadSafe{

        volatile int x = -1; // one x variable (no copies -- note: the class should be singleton)
        // if an instance is shared amongst threads then that instance should volatile

        double balance = 100;

        synchronized int doSomething(){ // thread safe

            if(x < 0)
                x = 0; // thread safe here but not optimized

            if(++x >= 10)
                x = 0;

            return x;
        }

        int doSomethingBetter(){
            synchronized (A_ThreadSafe.class){ // not really better
                if(x < 0)
                    x = 0; // thread safe here but not optimized

                if(++x >= 10)
                    x = 0;

                return x;
            }
        }

        private static volatile A_ThreadSafe instance;
        // stored in main memory not a thread's local cache
        // changes to this reference variable will be visible to all threads

        public static A_ThreadSafe getInstance() {
            if(instance == null) {
                synchronized(A_ThreadSafe.class) { // may wait as other threads do stuff with that class
                    // careful tho: it will sleep all threads attempting to do something with this class (even instances)
                    if(instance == null) { // once you have the lock check again
                        instance = new A_ThreadSafe();
                    }
                }
            }
            return instance;
        }

        public void addToBalance(double addTo){
            if(addTo > 0)
                synchronized (this) {
                // only one thread per instance can access this section
                // multiple threads can execute the method but if 2+ are at this section then only one goes
                    this.balance += addTo;
                }
        }

        /*
        volatile vs synchronized:
        -- you only should use one (99% of the time) --
        synchronized: mutual exclusion AND visibility
          Use when you need other threads in the same critical section to sleep if already accessed.
          ANY changes made by it (except for local variables) will be written in main memory.

        volatile: visibility
        Edge case where synchronization/mutual-exclusion is not needed.
        Rarely, a program is ok with threads executing same sections concurrently.
        The data that is being accessed between both is only needed to be reflected in the other thread.

        Why both above?
        JVM has an edge case with its optimization that it will access an object before it's fully constructed.
        Putting both here bars that.

         */

    }

    @Test
    public void testGenerics(){
        RealOcpTest.<LinkedList<String>>genericMethod(new LinkedList<>());

        List<Integer> intList = new ArrayList<>();
        addToList(intList);
        try {
            final Integer num = intList.get(0); // breaks here instead
        }
        catch(Throwable t){
            System.out.println("oops");
        }
    }

    public static <T extends List<?>> void genericMethod(T myList){
        System.out.println(myList.size());
    }

    public static void addToList(List list){
        list.add("oops"); // should break here but it doesn't (legacy support??)
    }

    @Test
    public void testSearch(){
        List<Integer> integerList = new ArrayList<>(){{
            add(1);
            add(5);
            add(10);
            add(15);
        }};

        System.out.println(Collections.binarySearch(integerList, 15));

        integerList.sort(Comparator.reverseOrder());

        System.out.println("sorted list is + " + integerList);

        System.out.println(Collections.binarySearch(integerList, 15));

    }

    @Test
    public void testMergeMap(){
        final Map<String, String> map = new HashMap<>(){{
            put("A", "A");
            put("B", null);
            put("C", "C");
        }};

        BiFunction<String,String,String> biFunction = (s1, s2) -> s1.length() >= s2.length() ? s1 : s2;

        map.merge("A", "", biFunction);
        map.merge("B", "B", biFunction);
        map.merge("C", "C", biFunction);
        map.merge("D", "D", biFunction);

        System.out.println(map);

        map.computeIfPresent(
                "A",
                (k, v) -> v.concat("A")
        );
        System.out.println(map);

        List list = new ArrayList();
        h(list);

        int[][] blah = new int[3][3];
        blah[0] = new int[3];
        blah[1] = new int[5];
        blah[1][4] = 3;
        System.out.println(blah[1][4]);

        Function<List<? extends Number>, ?> myFunction = List::size;
        System.out.println(myFunction.apply(new ArrayList<Integer>()));
    }

    public void h(List<?> list){}

    @Test
    public void testFilter(){
        Stream<Integer> intStream =
                Stream.
                        <Integer>iterate(1, i -> ++i) // infinite (1,2,3, ...)
                        .skip(3) // infinite (4,5,6, ...)
                        .limit(3); // finite (4,5,6)

        System.out.print("stream is: ");
        // final List<Integer> intList = intStream.collect(Collectors.toList()); (terminal)
        intStream.forEach(i -> System.out.print(i + ", ")); // terminal
        System.out.println();

        // -------------------------------------

        Stream<Integer> intStreamFilter =
                Stream.
                        <Integer>iterate(1, i -> ++i) // infinite (1,2,3, ...)
                        .skip(3) // infinite (4,5,6, ...)
                        .limit(3); // finite (4,5,6)

        System.out.print("after filter is: ");
        intStreamFilter.filter(i -> i % 2 == 0 ) // keep evens (4,6)
            .peek(i -> System.out.print("(" + i +"), ")) // (4)
                // peek does an operation on the value BEFORE the next function goes
                .<Integer>map(i -> ++i)
        .forEach(i -> System.out.print(i + ", ")); // (4), 5, (6), 7

        // -----------------------------------

        System.out.println();
        Stream<List<?>> listStream = Stream.of(
                new ArrayList<Integer>(){{add(1);}},
                new ArrayList<Character>(){{add('a');}}
        );

        listStream
                .peek(System.out::println) // ONLY fires if there is new operation (doesn't close explicitly)
        .forEach(System.out::println); //close() no works

        // ---------------------------
        System.out.println();
        intStream = Stream.of(1,2,3,4,5);
        intStream.filter(i -> i % 2 == 0) //evens (2,4)
        .peek(System.out::println)
                .filter(i -> !i.equals(2)) // prints reject because filter still accepts the stream's input
                .forEach(System.out::println);

        System.out.println();
        intStream = Stream.of(1,2,3,4,5);
        intStream.filter(i -> i % 2 == 0) //evens (2,4)
                .peek(System.out::println)
                .limit(1) // no print for 4 (reject) cuz it denies the rest of the stream's input (partial close)
                .forEach(System.out::println);

        // --------------------
        StringBuilder builder =
                Stream.<String>of("hello", " ", "world", "!")
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                );

        LinkedList<?> linkedList =
                Stream.<Integer>of(1,2,3,4)
                .collect(
                        Collectors.
                                <Integer, LinkedList<Integer>>toCollection(LinkedList<Integer>::new)
                );

       Optional<? extends Collection<? extends Integer>> linkedListOptional = this.
               <Integer, LinkedList<Integer>>createCollectionOfGenericStream(LinkedList<Integer>::new, 1, 2, 3);

       // ---------------------------------------

        TreeMap<Integer, ArrayList<String>> treeMapList =
                Stream.<String>of("hello", "world", "!!!")
                .collect(
                        Collectors.groupingBy(
                                String::length,
                                TreeMap::new,
                                Collectors.toCollection(
                                        ArrayList<String>::new
                                )
                        )
                );

        System.out.println(treeMapList);

        //--------------------------------
        System.out.println();

        treeMapList = this.createGenericCollectionGrouping(
                TreeMap<Integer, ArrayList<String>>::new,
                ArrayList<String>::new,
                String::length,
                "Hello", "world", "!!!"
        );

        System.out.println(treeMapList);
    }

    public <E, T extends Collection<E>> Optional<T> createCollectionOfGenericStream(Supplier<T> collectionSupplier, E... args){
        return
                Optional.<T>of(
                        Stream.<E>of(args)
                            .collect(
                                    Collectors.<E, T>toCollection(collectionSupplier)
                            )
                );
    }

    public <V, K, L extends Collection<V>, T extends Map<K,L>> T createGenericCollectionGrouping(
            Supplier<T> collectionSupplier,
            Supplier<L> valueCollectionSupplier,
            Function<V,K> keyProducer,
            V... args){
        return
                Stream.<V>of(args)
                .collect(
                        Collectors.groupingBy(
                                keyProducer,
                                collectionSupplier,
                                Collectors.<V, L>toCollection(
                                        valueCollectionSupplier
                                )
                        )
                );
    }

}
