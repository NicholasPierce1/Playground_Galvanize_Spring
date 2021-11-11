package com.example.demo;

import org.junit.jupiter.api.Test;
import scala.Int;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OCP_Concurrency {

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println("runnable done");
            } catch (InterruptedException e) {
                System.out.println("oops");
            }
        }
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("thread done");
        }
    }

    @Test
    public void testConcurrencyOne() throws InterruptedException {
        System.out.println("start");
        // thread 2 (thread override)
        final Thread threadOne = new MyThread();
        threadOne.start();
        // thread 3 (thread with runnable)
        final Thread threadTwo = new Thread(new MyRunnable());
        threadTwo.start();

        // waits the threads
        threadOne.join();
        threadTwo.join();

        System.out.println("end");

        System.out.println(threadTwo.getPriority());
    }

    @Test
    public void testSingleThreadExecutor() throws InterruptedException, ExecutionException {
        ExecutorService service = null;
        try {
            service = Executors.newSingleThreadExecutor();
            System.out.println("begin");
            service.execute(() -> System.out.println("Printing zoo inventory"));
//            service.execute(() -> {
//                        for (int i = 0; i < 3; i++) {
//                            System.out.println("Printing record: " + i);
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//            ); // service, even single thread, cannot await the single thread
            service.execute(() -> System.out.println("Printing zoo inventory"));

            // future
            Future<?> myFuture = service.submit(() -> {
                        for (int i = 0; i < 3; i++) {
                            System.out.println("Printing record: " + i);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
            System.out.println("before: " + myFuture.isCancelled() + " " + myFuture.isDone());
            myFuture.get();
            System.out.println("after: " + myFuture.isCancelled() + " " + myFuture.isDone());

            // Future with data
            final Future<? extends Integer> myDataFuture = service.<Integer>submit(() -> 10);
            System.out.println("data: " + myDataFuture.get());
            final Future<?> badFuture; // can't get thread running it :(
            try {
                badFuture = service.<Number>submit(() -> {
                    throw new Exception("badd");
                });
                System.out.println("starting the bad boy");
                badFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("bad boy caught " + e.getMessage());
                System.out.println(e instanceof ExecutionException);
            }

            System.out.println("end");
        } finally {
            if (service != null) service.shutdown();
        }
    }

    @Test
    public void crazyLongThreadTest() {
        final ExecutorService singleThreadService = Executors.newSingleThreadExecutor();

        try {
            //singleThreadService = Executors.newSingleThreadExecutor();
            final Future<?> allDayAllNight = singleThreadService.submit(
                    () -> {
                        for (int i = 0; i < 100000; i++) ;
                    }
            );
            //singleThreadService.submit((Callable<?>)()->null);

            allDayAllNight.get(1, TimeUnit.DAYS);
        } catch (TimeoutException e) {

        } catch (InterruptedException | ExecutionException e) {

        } finally {
            if (singleThreadService != null)
                singleThreadService.shutdown();
        }
    }

    @Test
    public void testScheduling() {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            final ScheduledFuture<?> future =
                    executorService.schedule(() -> System.out.println("finished"), 2, TimeUnit.SECONDS);
            future.get(); //(will wait)

            executorService.schedule(
                    () -> {
                        throw new Exception();
                    },
                    1,
                    TimeUnit.MICROSECONDS
            );

            System.out.println(Runtime.getRuntime().availableProcessors());

            /*
            note: only one thread fires -- even is multiple scheduled (happens here in schedule but not single thread executor)
            No concurrency in schedule cuz it shares with other threads (including main as always)

            scheduleAtFixedRate: if thread takes longer than rate allotted then multiple threads scheduled to do same thing
            scheduleAtFixedDelay: when one ends then the delay starts before the next (can't tell when it goes however)
             */
        } catch (InterruptedException | ExecutionException e) {

        } catch (Exception e) {

        } finally {
            if (executorService != null)
                executorService.shutdown();
        }
    }

    // ------------------

    static final class Counter {
        private Integer x = 0;

        private static final Integer y = 0;

        static void waitHere() throws InterruptedException {
            synchronized (Counter.class) {
                System.out.println("S1 Start");
                Thread.sleep(2000);
                System.out.println("S1 End");
            }
        }

        synchronized static void waitHere1() throws InterruptedException {
            System.out.println("S2 Start");
            Thread.sleep(1000);
            System.out.println("S2 End");
        }

        synchronized static void waitHere2() throws InterruptedException {
            System.out.println("S3 Start-End");
        }

        synchronized static void waitHere3() throws InterruptedException {
            System.out.println("S4 Start");
            Thread.sleep(1000);
            System.out.println("S4 End");
        }

        int incrementAndReturn() {
            synchronized (this.x) {
                return ++this.x;
            }
        }

        void testInstanceLock1() {
            try {
                synchronized (this.x) {
                    System.out.println("I2 Field Lock Obtained");
                    Thread.sleep(3000);
                    System.out.println("I2 Field Lock Released");
                }
//                Thread.sleep(0);
//                System.out.println("I2 Start-End");
            } catch (InterruptedException e) {
                System.out.println("oops");
            }
        }

        void testInstanceLock2() {
            try {
                synchronized (x) {
                    System.out.println("I3 Field Lock Obtained");
                    Thread.sleep(1000);
                    System.out.println("I3 Field Lock Released");
                }
            } catch (InterruptedException e) {
                System.out.println("oops");
            }
        }

        synchronized void saySomething() {
            System.out.println("I1 Start");
            try {
                Thread.sleep(2500);
//                synchronized (Counter.y){
//                    System.out.println("I1 Static Field Lock Obtained & Released");
//                }
                //Thread.sleep(1000);
                //this.waitHere1();
            } catch (InterruptedException e) {
                System.out.println("oops");
            }
            System.out.println("I1 End");
        }

        synchronized int doItAgain() {
            return ++this.x;
        }
    }

    static final class AtomicCounter {
        private AtomicInteger x = new AtomicInteger(0);

        int incrementAndReturn() {
            return this.x.incrementAndGet();
        }

        void printSomething() {
            class Inner {
                final int x = AtomicCounter.this.x.get();

                void test() {
                    System.out.println(this.x);
                }
            }

            final Inner inner = new Inner();
            inner.test();
        }
    }

    @Test
    public void testConcurrencyWithSynchronization() {
        final Counter counter = new Counter();
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            Collection<Callable<Integer>> incrementCallables = new ArrayList<Callable<Integer>>();

            for (int i = 0; i < 10; i++)
                incrementCallables.add(counter::incrementAndReturn);

            final List<Future<Integer>> futureList = executorService.<Integer>invokeAll(incrementCallables);

            for (final Future<? extends Integer> future : futureList)
                System.out.println("value is: " + future.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("oops");
        } finally {
            if (executorService != null)
                executorService.shutdown();
        }
    }

    @Test
    public void testConcurrencyWithAtomic() {
        final AtomicCounter atomicCounter = new AtomicCounter();
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {

            final Collection<Callable<Integer>> callables = new ArrayDeque<Callable<Integer>>();

            for (int i = 0; i < 10; i++)
                callables.add(atomicCounter::incrementAndReturn);

            final List<? extends Future<Integer>> futures = executorService.invokeAll(callables);

            for (final Future<? extends Integer> future : futures)
                System.out.println("value is: " + future.get());

        } catch (InterruptedException | ExecutionException | CancellationException e) {
            System.out.println("oops");
        } finally {
            if (executorService != null)
                executorService.shutdown();
        }
    }

    @Test
    public void testStaticConcurrency() {
        final Counter counter = new Counter();
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {

            final Runnable runnable = counter::saySomething;

            final Runnable instanceRunnable = counter::testInstanceLock1;

            final Runnable instanceRunnableTwo = counter::testInstanceLock2;


            final Runnable runnable1 = () -> {
                try {
                    Counter.waitHere1();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            };

            final Runnable runnable2 = () -> {
                try {
                    Counter.waitHere2();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            };

            final Runnable runnable3 = () -> {
                try {
                    Counter.waitHere3();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            };

            // static start
            executorService.submit(() -> {
                try {
                    Counter.waitHere();
                } catch (InterruptedException i) {
                    System.out.println(i.getMessage());
                }
            });

            // submit instance
            executorService.submit(runnable1);

            executorService.submit(runnable);

            executorService.submit(instanceRunnable);

            executorService.submit(instanceRunnableTwo);

            executorService.submit(runnable2);

            executorService.submit(runnable3);

            Thread.sleep(6000);
        } catch (InterruptedException | RuntimeException e) {
            System.out.println("oops");
        } finally {
            if (executorService != null)
                executorService.shutdown();
        }
    }

    @Test
    public void testConcurrencyCollectionQueue() {
        try {
            BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
            blockingQueue.offer(39);
            blockingQueue.offer(3, 4, TimeUnit.SECONDS);
            System.out.println(blockingQueue.poll());
            System.out.println(blockingQueue.poll(10, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            // Handle interruption
        }

        System.out.println("---------------");

        try {
            BlockingDeque<Integer> blockingDeque = new LinkedBlockingDeque<>();
            blockingDeque.offer(91);
            blockingDeque.offerFirst(5, 2, TimeUnit.MINUTES);
            blockingDeque.offerLast(47, 100, TimeUnit.MICROSECONDS);
            blockingDeque.offer(3, 4, TimeUnit.SECONDS);
            System.out.println(blockingDeque.poll());
            System.out.println(blockingDeque.poll(950, TimeUnit.MILLISECONDS));
            System.out.println(blockingDeque.pollFirst(200, TimeUnit.NANOSECONDS));
            System.out.println(blockingDeque.pollLast(1, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            // Handle interruption
        }
    }

    @Test
    public void testConcurrencySetAndMap() {
        // tree map
        NavigableMap<String, Integer> skipListMap = new ConcurrentSkipListMap<>();

        skipListMap.put("hello", 10);
        skipListMap.put("Hello", 100);
        System.out.println(skipListMap);
        skipListMap.merge("hello", 10, (o, n) -> (int) Math.pow(o, n));
        System.out.println(skipListMap);

        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("hello", 10);
        map.put("Hello", 100);
        map.put("1", 2);
        System.out.println(map);
        map.merge("hello", 10, (o, n) -> o * n);
        System.out.println(map);

        // tree set
        System.out.println("--------------");
        NavigableSet<String> stringNavigableSet = new ConcurrentSkipListSet<>();

        stringNavigableSet.add("hello");
        stringNavigableSet.add("Hello");
        stringNavigableSet.add("1");
        if (!stringNavigableSet.remove("idk"))
            System.out.println(stringNavigableSet.contains("1"));
        System.out.println(stringNavigableSet.ceiling("Helloo"));
        System.out.println(stringNavigableSet);

        // list (copy on write)
        List<Integer> list = new CopyOnWriteArrayList<>(Arrays.asList(4, 3, 52));
        for (Integer item : list) {
            System.out.print(item + " ");
            list.add(9);
        }
        System.out.println();
        System.out.println("Size: " + list.size());
        System.out.println(list);
//        Stream.of(1,2,3).collect(
//                ArrayList::new,
//                ArrayList::add,
//                ArrayList::addAll
//                );
    }

    @Test
    public void testConcurrentCollectionsWithParallelStreams() {
        Stream<String> ohMy = Stream.of("lions", "tigers", "bears").unordered().parallel();
        Stream<String> ohMy1 = Stream.of("lions", "tigers", "bears").parallel();
        Stream<String> ohMy2 = Stream.of("lions", "tigers", "bears").parallel();

        ConcurrentMap<Integer, String> map = ohMy
                .collect(Collectors.toConcurrentMap(
                        String::length,
                        k -> k,
                        (s1, s2) -> s1 + "," + s2)
                );
        System.out.println(map); // {5=lions,bears, 6=tigers}
        System.out.println(map.getClass()); // java.util.concurrent.ConcurrentHashMap

        ConcurrentMap<Integer, LinkedList<String>> map1 =
                ohMy1.collect(
                        Collectors.groupingByConcurrent(
                                String::length, ConcurrentHashMap::new, // optional
                                Collectors.toCollection( // already given that it's a concurrent map
                                        // could be, on circumstance, Collectors.toList()
                                        LinkedList::new
                                )
                        )
                );

//        System.out.println(
//                ohMy2.collect(
//                        Collectors.groupingBy(
//                                String::length,
//                                HashMap::new,
//                                Collectors.toCollection(
//                                        ArrayList::new
//                                )
//                        )
//                ).toString()
//        ); (same as groupingByConcurrent but no concurrent map is given) + slower on JVM

//        TreeMap<Integer, LinkedList<String>> map2 =
//                ohMy.collect(
//                        Collectors.groupingBy(
//                                String::length,
//                                TreeMap::new, (give me the map type -- supplier)
//                                Collectors.toCollection(
//                                        LinkedList::new
//                                )
//                        )
//                );
    }

    public class LionPenManager {
        private void removeAnimals() {
            System.out.println("Removing animals");
        }

        private void cleanPen() {
            System.out.println("Cleaning the pen");
        }

        private void addAnimals() {
            System.out.println("Adding animals");
        }

        //        public void performTask() {
//            synchronized (this) {
//                removeAnimals();
//                cleanPen();
//                addAnimals();
//            }
//        }
        public void performTask(final CyclicBarrier removeAnimals, final CyclicBarrier cleanPen) {
            try {
                removeAnimals();
                removeAnimals.await();
                cleanPen();
                cleanPen.await();
                addAnimals();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @Test
    public void testCyclicManager() {
        ExecutorService service = null;
        try {
            CyclicBarrier removeAnimals = new CyclicBarrier(2);
            CyclicBarrier cleanThePens = new CyclicBarrier(2, () -> System.out.println("pens are cleaned"));

            service = Executors.newFixedThreadPool(4);
            LionPenManager manager = new LionPenManager();
            for (int i = 0; i < 2; i++)
                service.submit(() -> manager.performTask(removeAnimals, cleanThePens));
        } finally {
            if (service != null) service.shutdown();
        }
    }

    @Test
    public void testRandom() {
        Arrays.asList(1, 2, 3, 4).stream()
                .forEach(System.out::println);
        Arrays.asList(1, 2, 3, 4).parallelStream() // not parallel
                .forEachOrdered(System.out::println);

        Callable c = new Callable() {
            public Object call() throws Exception {
                return 10;
            }
        };

        //Executors.newCachedThreadPool().invokeAny(null);
        Future<? super Integer> future = Executors.newSingleThreadExecutor().<Integer>submit(() -> 3);
        // must shutdown first
        //Executors.newSingleThreadExecutor().awaitTermination(1, TimeUnit.DAYS);
    }

    public static String concat1(List<String> values) {
        return values.parallelStream().unordered()
                .reduce("a",
                        (x, y) -> x + y,
                        String::concat);
    }

    public static String concat2(List<String> values) {
        //values.parallelStream().reduce("a", String::concat);
        return values.parallelStream()
                .reduce((w, z) -> z + w).orElseThrow();
    }

    @Test
    public void testReducers() {
        List<String> list = Arrays.asList("Big", "Hat", "Cat");
        //list.sort(Comparator.reverseOrder());
        String x = concat1(list);
        String y = concat2(list);
        System.out.println(x + ":" + y);
        //Future<? extends Number> future = Executors.newSingleThreadExecutor().submit(() -> (Integer)1);
        //Executors.newSingleThreadExecutor().execute(System.out::println);
    }
}


