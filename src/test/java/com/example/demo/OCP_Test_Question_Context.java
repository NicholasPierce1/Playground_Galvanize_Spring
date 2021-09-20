package com.example.demo;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import scala.Int;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.*;

public class OCP_Test_Question_Context {

    private enum TestEnum{
        A{
            @Override
            void doSomething() {

            }
        },
        B{
            @Override
            void doSomething() {

            }
        };

        abstract void doSomething();
    }

    private int x = 3;

    @Test
    public void testClassType(){
        final int y = 10;
        int z = y;
        //z++;
        abstract class local{ // or final (local)
            int x = OCP_Test_Question_Context.this.x + y;
            int a = z;
            static int r = 5;
            static void testStatic(){}
        }
    }

    public class Matrix {
        private int level = 1;
        class Deep {
            private int level = 2;
            class Deeper {
                private int level = 5;
                public void printReality() {
                    System.out.print(level);
                    System.out.print(" "+Matrix.this.level);
                    System.out.print(" "+Deep.this.level);
                }
            }
        }
    }

    interface Edible{
        void eat();
    }

    @Test
    public void testMemberClassOverloads(){
        Matrix.Deep.Deeper simulation = new Matrix().new Deep().new Deeper();
        simulation.printReality();
    }

//    public class Woods {
//        static class Tree {}
//        public static void main(String[] leaves) {
//            int water = 10+5;
//            final class Oak extends Tree { // p1
//                public int getWater() {
//                    return water; // p2
//                }
//            }
//            System.out.print(new Oak().getWater());
//        }
//    }
    /*
    usage:
    //        new Edible(){
//
//            @Override
//            public void eat() {
//
//            }
//        };
     */

//    interface Alex {
//        default void write() {}
//        static void publish() {}
//        void think();
//    }interface Michael {
//        public default void write() {}
//        public static void publish() {}
//        public void think();
//    }
//    public static class Twins implements Alex, Michael {
//        @Override public void write() {}
//        //@Override public static void publish() {}
//        @Override public void think() {
//            System.out.print("Thinking...");
//        }
//    }

    abstract interface IsFunctional{
        void a();
        default void b(){}
        static void c(){}
    }

    @Test
    public void testRandom(){
        new ArrayList<String>(){
            public int hello(){return 3;}

            @Override
            public boolean add(String s) {
                return super.add(s);
            }
        };

        IsFunctional temp = ()->{};

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.offer(10);
        deque.push(1);
        System.out.println(deque.poll());

        BiFunction<String, Integer, Character> biFunction =  String::charAt;

        Pattern pattern = Pattern.compile("^[0-9]{2,}:\s.*$");
        System.out.println(pattern.matcher("10: ").matches());


        Consumer<String> print = System.out::println;
        Stream.of("hi").peek(print)
                .peek(print)
                .peek(print)
                .forEach(print);

        Stream<String> s = Stream.of("over the river",
                "through the woods",
                "to grandmother's house we go");
        s.filter(n -> n.startsWith("t"))
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .ifPresent(System.out::println);

        IntStream ints = IntStream.empty();
        IntStream moreInts = IntStream.of(66, 77, 88);
        Stream.of(ints, moreInts).flatMapToInt(x -> x).forEach(System.out::print);
        //IntSummaryStatistics intSummaryStatistics = ints.mapToObj(a->a).collect(Collectors.summarizingInt(a->a));
        Stream.of(new ArrayList<>()).flatMap(Collection::stream).forEach(System.out::println);
        System.out.println("\na");

        System.out.println(Stream.of(1,2,3).collect(Collectors.summarizingInt(a -> a)).getSum());
    }


    interface Toy { String play(); }

    public class Gift {
        public static void main(String[] matrix) {
            abstract class Robot {}
            class Transformer extends Robot implements Toy {
                public String name = "GiantRobot";
                public String play() {return "DinosaurRobot";}
            }
            Transformer prime = new Transformer () {
                public String play() {return name;} // y1
            };
            System.out.print(prime.play()+" "+prime.name);
        }
    }

    public class Catch {
        public static void notMain(String[] args) {
            Optional opt = Optional.empty();
            List list = new ArrayList<String>(); // can be empty
            List<String> list1 = (List<String>) list;
            try {
                apply(opt);
                applyList(list);
                applyListOk(list1);
            } catch (IllegalArgumentException e) {
                System.out.println("Caught it");
            }
        }
        private static void apply(Optional<Exception> opt) {
            opt.orElseThrow(IllegalArgumentException::new);
        }
        private static void applyList(List<Exception> opt) {
           // opt.orElseThrow(IllegalArgumentException::new);
        }
        private static void applyListOk(List opt) {
            // opt.orElseThrow(IllegalArgumentException::new);
        }
    }

    @Test
    public void testRandomTwo() throws IOException{
//        try(AutoCloseable autoCloseable = new AutoCloseable() {
//            @Override
//            public void close() {
//
//            }
//        }){}
       // catch(Exception e){}
        new Exception((String)null);
        LocalDateTime pi = LocalDateTime.of(2017, 10, 14, 1, 59);

        System.out.println(DateTimeFormatter.ISO_DATE_TIME.format(pi));
        System.out.println(DateTimeFormatter.ofPattern("yyyy....$%^MMMM").format(pi));
        System.out.println(DateTimeFormatter.ofPattern("M.ddhhmm").format(pi));

        try(A a = null){

        }
        catch(Exception e){}
        catch (Error r){}
        catch(Throwable t){}

        System.out.println(LocalDate.of(1874, Month.MARCH, 24).isBefore(LocalDate.now()));
        System.out.println(LocalDate.now().until(LocalDate.now().plusDays(1), ChronoUnit.DAYS));

        int x = (int)30L;
        System.out.println(
                ChronoUnit.MONTHS.between(
                        LocalDate.of(1,1,1),
                        LocalDate.of(1,2,1)
                ));

        System.out.println(
                Paths.get("/a").relativize(Paths.get("/../../b"))
        );

        System.out.println(Instant.now());

        assert true : false ? "a" : 1;

        System.out.println(new ArrayList[2] instanceof Collection[]);

        BiFunction<String, Integer, Character> characterBiFunction = String::charAt;

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.execute(OCP_Test_Question_Context::getInt); // void
        
        System.out.println(
                Paths.get("..").toAbsolutePath()
        );

        System.out.println(
                Paths.get("").relativize(Paths.get("a/b"))
        );

        System.out.println(Stream.of(1,2).limit(3).max(Comparator.<Integer>naturalOrder()).orElse(-1));

        System.out.println(
                Paths.get("/").getParent()
        );
   }

   static int getInt(){return 3;}

    static class A implements AutoCloseable{
        public void close() throws RuntimeException{}
        int x;
        A(){
            A.this.x = 3;
        }
    }

    @Test
    public void enumerateUniquePrimitiveFunctions(){
        IntConsumer intConsumer = System.out::println;
        IntBinaryOperator intBinaryOperator = Integer::sum;
        IntToDoubleFunction intToDoubleFunction = (int a) -> a;
        ToIntFunction<? extends Number> toIntFunction = Number::intValue;
        IntFunction<String> intFunction = String::valueOf;
        ObjIntConsumer<Integer> objIntConsumer = (Integer a, int b) -> {};
        Consumer<String> stringConsumer = String::toString;
        ToIntBiFunction<String, String> toIntBiFunction = (s1, s2) -> Integer.sum(s1.length(), s2.length());
        DoubleStream.empty().mapToObj(d -> d);
    } // 413, 555

    public class InnerMemberClass{}

    public void testInnerMemberClassLegality(){
        OCP_Test_Question_Context test_question_context = new OCP_Test_Question_Context();
        class LocalInnerClass extends OCP_Test_Question_Context.InnerMemberClass{

        }

        test_question_context.new InnerMemberClass();
    }
}
