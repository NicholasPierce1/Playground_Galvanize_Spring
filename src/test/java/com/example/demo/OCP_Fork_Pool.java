package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OCP_Fork_Pool {

    // class one (Recursive Action)
    public static final class WeighAnimalAction extends RecursiveAction {
        private final int start;
        private final int end;
        private final Double[] weights;
        public WeighAnimalAction(Double[] weights, int start, int end) {
            this.start = start;
            this.end = end;
            this.weights = weights;
        }

        @Override
        protected void compute() {
            if(end-start <= 3)
                for(int i=start; i<end; i++) {
                    weights[i] = (double)new Random().nextInt(100);
                    System.out.println("Animal Weighed: "+i);
                    System.out.flush();
                }
            else { // recursively splits into 2 halves
                final int middle = start+(int)((end-start)/2);
                System.out.println("[start="+start+",middle="+middle+",end="+end+"]");

                // enjoins ForkJoinTask to instigate execution of the RecursiveActions
                invokeAll(new WeighAnimalAction(weights,start,middle),
                        new WeighAnimalAction(weights,middle,end));
            }
        }
    }

    @Test
    public void testForkAction(){
        Double[] weights = new Double[10];

        // creates initial fork (Recursive action and upcast)
        ForkJoinTask<Void> task = new WeighAnimalAction(weights,0,weights.length);

        // creates fork join pool to encapsulate the executor's creation, scheduling, and thread management
        ForkJoinPool pool = new ForkJoinPool();

        Void voided = pool.<Void>invoke(task); // returns the Void
        pool.shutdown();
        // Print results
        System.out.println();
        System.out.print("Weights: ");
        Arrays.stream(weights).forEach(
                d -> System.out.print(d.intValue()+" "));
    }

    // creates class for RecursiveTask<?>
    public static final class WeighAnimalTask extends RecursiveTask<Double> {
        private final int start;
        private final int end;
        private final Double[] weights;
        public WeighAnimalTask(Double[] weights, int start, int end) {
            this.start = start;
            this.end = end;
            this.weights = weights;
        }

        @Override
        protected Double compute() {
            if(end-start <= 3) {
                Double sum = 0D;
                for (int i = start; i < end; i++) {
                    sum += this.weights[i];
                }
                return sum;
            }
            else { // recursively splits into 2 halves
                final int middle = start+(int)((end-start)/2);
                System.out.println("[start="+start+",middle="+middle+",end="+end+"]");

                final WeighAnimalTask weighAnimalTask = new WeighAnimalTask(weights, start, middle);
                weighAnimalTask.fork();

                return new WeighAnimalTask(weights, middle, end).compute() + weighAnimalTask.join();
            }
        }
    }

    @Test
    public void testForkJoinTask(){
        final Double[] weights = new Double[10];
        for(int i = 0; i < 10; i++)
            weights[i] = (double) Math.round(Math.random() * (Math.random() * 100));

        final ForkJoinTask<Double> weighAnimalTask = new WeighAnimalTask(weights, 0, 10);

        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        final Double result = forkJoinPool.invoke(weighAnimalTask);

        System.out.println("result is: " + result);

        Double expected = 0d;
        for(final Double weight: weights)
            expected += weight;

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void temp() {
        System.out.println(List.of(1, 2, 3,4,5,6,7,8,9,10, 11, 12).parallelStream().sorted().findAny().get());
        System.out.println(List.of("a", "bc").parallelStream().parallel()
                .reduce(
                        0,
                        (Integer a, String b) -> a + b.length(),
                         Integer::sum)
        );

        System.out.println(Stream.of(Stream.of("a","b"), Stream.of("cc", "dd")).flatMap(a -> a)
                .collect(
                        Collectors.groupingByConcurrent(
                                (a) -> a.length() != 2
                                //,Collectors.toList()
                        )
                ));

        System.out.println("-----------------------");
        ExecutorService service = Executors.newScheduledThreadPool(10);
        DoubleStream.of(3.14159,2.71828) // b1
                .forEach(c -> service.submit( // b2
                        () -> System.out.println(10*c))); // b3
        service.execute(() -> System.out.println("Printed")); // b4

        //Consumer<Integer> blah = (i) -> Integer.sum(i, 3);
        service.shutdown();
    }


    static int counter =0;

    @Test
    public void testNullSubmission() throws InterruptedException, ExecutionException{
        ExecutorService service = Executors.newSingleThreadExecutor();
        List<Future<?>> results = new ArrayList<>();
        IntStream.iterate(0, i -> i+1).limit(5).forEach(
                i -> results.add(service.submit(() -> counter++)) // n1
        );
        for(Future<?> result : results) {
            System.out.print(result.get()+" "); // n2
        }
        service.shutdown();

        System.out.println(
                Stream.of("ab", "cd").parallel().reduce(
                        0,
                        (n1, s1) -> n1 + s1.length(),
                        Integer::sum
                        // Stream<Integer> needs a function to combine all the integer together
                        // given accumulator is BiFunction<Integer,String, Integer> not BinaryOperator<Integer>
                )
        );

        System.out.println(
                Stream.of("ab", "cd").parallel().reduce(
                        "",
                        String::concat)
                // why 2 -- cuz Stream<String> will be applied to this accumulator in any order to get
                // result
        );
    }


}
