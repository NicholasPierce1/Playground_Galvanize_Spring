package com.example.demo;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;
import scala.collection.mutable.StringBuilder;

import javax.swing.text.NumberFormatter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class RealOcpTest {

    interface  B{
        void doSomething();
    }
    enum EnumA implements B{
        a{
            @Override
            public void doSomething() {
                System.out.println("A");
            }
        },b;

        @Override
        public void doSomething() {
            System.out.println("B");
        }
    }

    @Test
    public void testEnumInheritance(){
        EnumA.a.doSomething();
        EnumA.b.doSomething();
    }

    @Test
    public void testInnerClass() {
        final ParentWithInner parentWithInner = new ParentWithInner();
        //System.out.println(new parentWithInner.Inner());
        final ParentWithInner.Inner inner = parentWithInner.new Inner();
        System.out.println(inner.x);
        final ParentWithInner.Inner inner1 = new ParentWithInner().new Inner();
    }

    class ParentWithInner {

        private int y = 5;

        private int x = 10;

        private String name = "inner";

        void printInnerX() {
            System.out.println(new Inner().x);
        }

        static void tryItAgain() {
            // System.out.println(new Inner().x);
        }

        class Inner {
            public static int z = 3;

            public int x = 100;

            int getSum() {
                return x + y; // if Inner is not static
            }

        }

        static class StaticInner {

            public String name = "inner";

            void printX() {
                StaticInner inner = new StaticInner();
                //ParentWithInner parentWithInner = new StaticInner();
                System.out.println(name);
            }

        }
    }

    @Test
    public void testEnum() {

    }

    enum Animals {
        MAMMAL(true) {
            @Override
            public void printType() {
                System.out.println("I'm a mammal!");
            }
        },
        FISH {
            @Override
            public void printType() {
                System.out.println("I'm a fish!");
            }
        };

        public boolean isCool;

        Animals() {
            this.isCool = false;
        }

        Animals(boolean isCool) {
            this.isCool = isCool;
        }

        public void makeCool() {
            this.isCool = true;
        }

        public abstract void printType();
    }

    @Test
    public void testFunctionalInterface() {
        final DoSomething doSomething =
                () -> {
                    System.out.println("helloooooo");
                };

        doSomething.doSomething();
    }

    @FunctionalInterface // not needed
    interface DoSomething {
        public abstract void doSomething();
    }

    static class A_ThreadSafe {

        volatile int x = -1; // one x variable (no copies -- note: the class should be singleton)
        // if an instance is shared amongst threads then that instance should volatile

        double balance = 100;

        synchronized int doSomething() { // thread safe

            if (x < 0)
                x = 0; // thread safe here but not optimized

            if (++x >= 10)
                x = 0;

            return x;
        }

        int doSomethingBetter() {
            synchronized (A_ThreadSafe.class) { // not really better
                if (x < 0)
                    x = 0; // thread safe here but not optimized

                if (++x >= 10)
                    x = 0;

                return x;
            }
        }

        private static volatile A_ThreadSafe instance;
        // stored in main memory not a thread's local cache
        // changes to this reference variable will be visible to all threads

        public static A_ThreadSafe getInstance() {
            if (instance == null) {
                synchronized (A_ThreadSafe.class) { // may wait as other threads do stuff with that class
                    // careful tho: it will sleep all threads attempting to do something with this class (even instances)
                    if (instance == null) { // once you have the lock check again
                        instance = new A_ThreadSafe();
                    }
                }
            }
            return instance;
        }

        public void addToBalance(double addTo) {
            if (addTo > 0)
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
    public void testGenerics() {
        RealOcpTest.<LinkedList<String>>genericMethod(new LinkedList<>());

        List<Integer> intList = new ArrayList<>();
        addToList(intList);
        try {
            final Integer num = intList.get(0); // breaks here instead
        } catch (Throwable t) {
            System.out.println("oops");
        }
    }

    public static <T extends List<?>> void genericMethod(T myList) {
        System.out.println(myList.size());
    }

    public static void addToList(List list) {
        list.add("oops"); // should break here but it doesn't (legacy support??)
    }

    @Test
    public void testSearch() {
        List<Integer> integerList = new ArrayList<>() {{
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
    public void testMergeMap() {
        final Map<String, String> map = new HashMap<>() {{
            put("A", "A");
            put("B", null);
            put("C", "C");
        }};

        BiFunction<String, String, String> biFunction = (s1, s2) -> s1.length() >= s2.length() ? s1 : s2;

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

    public void h(List<?> list) {
    }

    @Test
    public void testFilter() {
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
        intStreamFilter.filter(i -> i % 2 == 0) // keep evens (4,6)
                .peek(i -> System.out.print("(" + i + "), ")) // (4)
                // peek does an operation on the value BEFORE the next function goes
                .<Integer>map(i -> ++i)
                .forEach(i -> System.out.print(i + ", ")); // (4), 5, (6), 7

        // -----------------------------------

        System.out.println();
        Stream<List<?>> listStream = Stream.of(
                new ArrayList<Integer>() {{
                    add(1);
                }},
                new ArrayList<Character>() {{
                    add('a');
                }}
        );

        listStream
                .peek(System.out::println) // ONLY fires if there is new operation (doesn't close explicitly)
                .forEach(System.out::println); //close() no works

        // ---------------------------
        System.out.println();
        intStream = Stream.of(1, 2, 3, 4, 5);
        intStream.filter(i -> i % 2 == 0) //evens (2,4)
                .peek(System.out::println)
                .filter(i -> !i.equals(2)) // prints reject because filter still accepts the stream's input
                .forEach(System.out::println);

        System.out.println();
        intStream = Stream.of(1, 2, 3, 4, 5);
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
                Stream.<Integer>of(1, 2, 3, 4)
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

        Map<Boolean, List<String>> g = Stream.<String>empty().collect(
                Collectors.groupingBy(b -> b.startsWith("c")));
        //DoubleStream.of(1,2.4).peek(System.out::println).filter(d -> d > 2).count();
        //System.out.println(Stream.<String>empty().collect(Collectors.mapping(i -> i.startsWith("c"), toList())));
        //System.out.println(Stream.iterate(1, x -> ++x).limit(5).map(x -> ""+x).collect(Collectors.joining()));
        /*
        know da difference between type and value
        array list is a value so it must be applicable to whatever '? super List<...>' may be!
        Serializable is a type and must be applicable to the constraint appended
         */

        final Vector<String> vector = Stream.<String>of("hello", "world").map(t -> t.concat("!!")).collect(Collectors.<String, Vector<String>>toCollection(Vector<String>::new));
        System.out.println(vector.size());

    }

    public <E, T extends Collection<E>> Optional<T> createCollectionOfGenericStream(Supplier<T> collectionSupplier, E... args) {
        return
                Optional.<T>of(
                        Stream.<E>of(args)
                                .collect(
                                        Collectors.<E, T>toCollection(collectionSupplier)
                                )
                );
    }

    public <V, K, L extends Collection<V>, T extends Map<K, L>> T createGenericCollectionGrouping(
            Supplier<T> collectionSupplier,
            Supplier<L> valueCollectionSupplier,
            Function<V, K> keyProducer,
            V... args) {
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

    @Test
    public void testGenericConstraints() {
        Predicate<? super Number> predicate = (Object s) -> s.hashCode() == 100;
        // object type: predicate of something that is a Number or super
        // ref type: predicate of an Object
        // must: pass in something that is for sure an Number
        predicate.test(1);
        ((Predicate<Object>) predicate).test(new Object());

        Predicate<? super List<? super Number>> predicate2 = (Collection<? super Number> c) -> c.isEmpty();
        predicate2.test(new ArrayList<Serializable>());
        Function<? super List<? extends Number>, ? extends Collection<? extends Number>> blah =
                (Collection<? extends Number> numCollection) -> new LinkedList<Integer>() {{
                    add(numCollection.size());
                }};
        Collection<? extends Number> col = blah.apply(new ArrayList<Double>());
        Supplier<? extends Queue<? extends Number>> supplier = ArrayDeque<Double>::new;
        Queue<Number> queueNumber = (Queue<Number>) supplier.get();

        BiFunction<? super List<? extends Number>, ? super List<? super Number>, ? extends List<? extends Number>> myConsumer =
        (Collection<? extends Number> p1, List<? super Number> p2) -> new ArrayList<Double>();
        final List<? extends Number> myList = myConsumer.apply(
                new Vector<Integer>(),
                new Vector<Serializable>());

        Function<? super Set<? extends Set<? super Number>>, ? extends Set<? super Number>> func =
                (Collection<? extends Set<? super Number>> p1) -> new TreeSet<Serializable>((a,b) -> Integer.compare(a.hashCode(), b.hashCode()));

        final Set<? super Number> set = func.apply(new HashSet<TreeSet<Serializable>>());
    }

    @Test
    public void testTimeZones() {
        ZoneId.getAvailableZoneIds()
                .stream()
                .filter(zone -> zone.startsWith("US") || zone.startsWith("America"))
                .sorted((a, b) -> b.compareTo(a)) // reverse
                .limit(5) // comment out when ya want long list
                .forEach(System.out::println);

        ZonedDateTime zonedDateTime = ZonedDateTime.of(
                2021,
                8,
                18,
                12,
                7,
                0,
                0,
                ZoneId.of("US/Central")
        );
        System.out.println(zonedDateTime);

        Period threeWeeks = Period.ofWeeks(3); // Period.ofDays(21) --> (P21D)
        Period fiveWeeks = Period.of(0, 0, 35);
        System.out.println(threeWeeks);
        System.out.println(fiveWeeks);
        System.out.println(fiveWeeks);

        Duration twoHours = Duration.ofHours(2); // (P2H)
        Duration twoHoursAgain = Duration.of(2, ChronoUnit.HOURS); // (PT2H)
        Duration day = Duration.of(1, ChronoUnit.DAYS);
        System.out.println(day);
        System.out.println(twoHours);
        long timeBetween = ChronoUnit.HOURS.between(
                LocalTime.of(1, 0),
                LocalTime.of(3, 10)
        );
        System.out.println(timeBetween);

        System.out.println();

        Assertions.assertThrows(DateTimeException.class, () -> Duration.of(1, ChronoUnit.WEEKS));
        Assertions.assertDoesNotThrow(() -> Duration.of(600, ChronoUnit.DAYS));
    }

    @Test
    public void testSpringForwardAndFallBack() {
        /* --spring forward--
        LocalDate date = LocalDate.of(2016, Month.MARCH, 13);
        LocalTime time = LocalTime.of(1, 30);
        ZoneId zone = ZoneId.of("US/Eastern");
        ZonedDateTime dateTime = ZonedDateTime.of(date, time, zone);
        System.out.println(dateTime); // 2016–03–13T01:30–05:00[US/Eastern]
        dateTime = dateTime.plusHours(1);
        System.out.println(dateTime); // 2016–03–13T03:30–04:00[US/Eastern] (went 2 hours AND timezone went back one)
         */

        /* --fall back--
        LocalDate date = LocalDate.of(2016, Month.NOVEMBER, 6);
        LocalTime time = LocalTime.of(1, 30);
        ZoneId zone = ZoneId.of("US/Eastern");
        ZonedDateTime dateTime = ZonedDateTime.of(date, time, zone);
        System.out.println(dateTime); // 2016–11–06T01:30–04:00[US/Eastern]
        dateTime = dateTime.plusHours(1);
        System.out.println(dateTime); // 2016–11–06T01:30–05:00[US/Eastern] (went back to 1:30 again AND timezone went forward one)
        dateTime = dateTime.plusHours(1);
        System.out.println(dateTime); // 2016–11–06T02:30–05:00[US/Eastern] (now we go forward one)
         */
    }

    @Test
    public void testLocaleAndInternalization(){
        final Locale locale = Locale.getDefault();
        System.out.println(locale);
        System.out.println(Locale.ENGLISH + " : " + Locale.US);

        Locale locale1 = new Locale("en", "US");
        locale1 = new Locale("hi", "US");
        System.out.println(locale1);

        // warning -- legal but unconventional
        final Locale localeBuilder = new Locale.Builder()
                .setLanguage("EN")
                .setRegion("us")
                .build();
        System.out.println(localeBuilder);
//        Locale.setDefault(new Locale("fr"));
//        System.out.println("hello?");

        System.out.println();
        final Locale englishLocale = new Locale("en", "US");
        final Locale frenchLocale = new Locale.Builder()
                .setLanguage("fr")
                .setRegion("FR")
                .build();

        final ResourceBundle englishResourceBundle = ResourceBundle.getBundle("greetings", englishLocale);
        System.out.println(
                "greetings:" + englishResourceBundle.getString("greeting") +
                "\tsalutation: " + englishResourceBundle.getString("salutation")
        );

        final ResourceBundle frenchResourceBundle = ResourceBundle.getBundle("greetings", frenchLocale);
        System.out.println(
                "greetings:" + frenchResourceBundle.getString("greeting") +
                "\tsalutation: " + frenchResourceBundle.getString("salutation")
        );

        englishResourceBundle.keySet().stream()
                .limit(10)
                .forEach(System.out::println);

        // ----------------
        System.out.println();
        final Properties properties = new Properties();
        List<List<ResourceBundle>> resourceBundleList = new ArrayList<>(){{
            add(new ArrayList<>(){{add(englishResourceBundle);}});
            add(new ArrayList<>(){{add(frenchResourceBundle);}});
        }};
        resourceBundleList.stream()
                .<ResourceBundle>flatMap(List<ResourceBundle>::stream)
                .forEach(
                        resourceBundle ->
                                resourceBundle.keySet().stream()
                        .limit(10)
                        .forEach(key -> {
                            System.out.println(resourceBundle.getString(key));
                            if(resourceBundle == englishResourceBundle) {
                                properties.put(key, resourceBundle.getString(key));
                                //System.out.println(resourceBundle.getLocale().getCountry() + " : " + englishResourceBundle.getLocale().getCountry());
                            }
                        })
                );

        properties.forEach(
                (k, v) -> System.out.println("key: " + k + "\tvalue: " + v)
        );

        System.out.println("prop: " + properties.getProperty("greeting", "default"));
        System.out.println("prop: " + properties.getProperty("greetings", "default"));


        /*
            2: import java.util.*;
            3: public class Tax_en_US extends ListResourceBundle {
            4: protected Object[][] getContents() {
            5: return new Object[][] { { "tax", new UsTaxCode() } };
            6: }
            7: public static void main(String[] args) {
            8: ResourceBundle rb = ResourceBundle.getBundle(
            9: "resourcebundles.Tax", Locale.US);
            10: System.out.println(rb.getObject("tax")); // object address
            11: }}
         */

        System.out.println();
        Locale.setDefault(new Locale("en"));
        ResourceBundle whichOne = ResourceBundle.getBundle("greetings", new Locale("fr"));
        System.out.println(whichOne.getLocale().getLanguage() + " : " + whichOne.getLocale().getCountry());
        whichOne.keySet().stream().forEach(k -> System.out.println(whichOne.getString(k)));
        Locale.setDefault(new Locale("en_US"));

        /* order of getting bundles:
        NOTE: java locales extend ListResourceBundle
        1) requested java locale
        2) requested resource locale
        3) <optional> java locale of just language
        4) <optional> resource of just language
        5) java locale of default
        6) resource locale of default
        7) java base name of default
        8) properties base name of default
         */
    }

    @Test
    public void testNumberAndCurrentFormatter() throws Exception{

        final NumberFormat usNumberFormat = NumberFormat.getNumberInstance(new Locale("en", "US"));
        final NumberFormat gNumberFormat = NumberFormat.getNumberInstance(
                new Locale(Locale.GERMAN.getLanguage(), Locale.GERMANY.getCountry())
        );
        final NumberFormat cNumberFormat = NumberFormat.getNumberInstance(
                new Locale.Builder()
                .setLanguage(Locale.CANADA_FRENCH.getLanguage())
                .setRegion(Locale.CANADA_FRENCH.getCountry())
                .build()
        );

        System.out.println(usNumberFormat.format(100_000.59));
        System.out.println(gNumberFormat.format(100_000.59));
        System.out.println(cNumberFormat.format(1000_00.59));

        System.out.println();

        final NumberFormat usNumberFormatCurrency = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        final NumberFormat gNumberFormatCurrency = NumberFormat.getCurrencyInstance(
                new Locale(Locale.GERMAN.getLanguage(), Locale.GERMANY.getCountry())
        );
        final NumberFormat cNumberFormatCurrency = NumberFormat.getCurrencyInstance(
                new Locale.Builder()
                        .setLanguage(Locale.CANADA_FRENCH.getLanguage())
                        .setRegion(Locale.CANADA_FRENCH.getCountry())
                        .build()
        );

        System.out.println(usNumberFormatCurrency.format(100_000.59));
        System.out.println(gNumberFormatCurrency.format(100_000.59));
        System.out.println(cNumberFormatCurrency.format(100_000.59));

        System.out.println(usNumberFormatCurrency.parse("$123.567"));
        System.out.println(usNumberFormat.parse("123.456"));

        //System.out.println(gNumberFormatCurrency.parse("123"));
        System.out.println(gNumberFormat.parse("123.456,99"));

        LocalDate localDate = LocalDate.of(2021, 10, 19);
        System.out.println(localDate.format(DateTimeFormatter.ofPattern("yyyy-M-dd")));
        ZonedDateTime zonedDateTime = ZonedDateTime.of(
                LocalDate.of(2021, 10, 19),
                LocalTime.of(10, 1, 0),
                ZoneId.of("US/Central")
        );
        System.out.println(zonedDateTime.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm z XXX")));
        System.out.println(ChronoUnit.HOURS.between(LocalTime.of(10,1), LocalTime.of(12,1)));
    }

    @Test
    public void testDatesAgain(){
        LocalDate date = LocalDate.of(2016, Month.MARCH, 13);
        LocalTime time = LocalTime.of(1, 30);
        ZoneId zone = ZoneId.of("US/Eastern");
        ZonedDateTime dateTime1 = ZonedDateTime.of(date, time, zone);
        ZonedDateTime dateTime2 = dateTime1.plus(1, ChronoUnit.HOURS);
        System.out.println(dateTime1);
        System.out.println(dateTime2);
        long hours = ChronoUnit.HOURS.between(dateTime1, dateTime2);
        System.out.println(dateTime1.getHour());
        System.out.println(dateTime2.getHour());
        System.out.println(hours);

        System.out.println();
        LocalDateTime localDateTime1 = LocalDateTime.of(2020,3, 13, 1, 30);
        LocalDateTime localDateTime2 = localDateTime1.plusHours(1);
        System.out.println(localDateTime1);
        System.out.println(localDateTime2); // spring forward and fall back only apply to zone date times
        hours = ChronoUnit.HOURS.between(localDateTime1, localDateTime2);
        System.out.println(hours);

        System.out.println(
                ChronoUnit.HOURS.between(
                ZonedDateTime.of(
                        localDateTime1,
                        ZoneId.of("US/Central")),
                        ZonedDateTime.of(
                                localDateTime1,
                                ZoneId.of("US/Eastern"))
                        )
        );

        System.out.println(
                ChronoUnit.HOURS.between(
                        ZonedDateTime.of(
                                localDateTime1,
                                ZoneId.of("US/Eastern")),
                        ZonedDateTime.of(
                                localDateTime1,
                                ZoneId.of("US/Central"))
                )
        );
    }

    @Test
    public void testException() throws IOException{
        try{
            throw new IOException();
        }
        catch(IOException | RuntimeException e){
           // e = null; // illegal

        }
       // catch(SQLException blah){} (never thrown)
        catch(Exception e){
            e = null; // legal
        }
//
//        try(BufferedReader bufferedReader = Files.newBufferedReader(Path.of("flightTicket.json"))){
//
//        } // catch and finally can be gone (bufferedReader dead here)
//        catch(IOException e){
//
//        }
//        finally{
//
//        }
    }
    
    @Test
    public void testExceptionClosing() throws IOException, SQLException{
        try (JammedTurkeyCage t = new JammedTurkeyCage();
        JammedTurkeyCage iCloseFirst = new JammedTurkeyCage()) {
            throw new IllegalStateException("turkeys ran off");
            } catch (IllegalStateException e) {
            System.out.println("caught: " + e.getMessage());
            for (Throwable t: e.getSuppressed())
                System.out.println(t.getMessage());
            }
        catch(Exception e){
            System.out.println("caught here: " + e.getMessage());
            for (Throwable t: e.getSuppressed())
                System.out.println(t.getMessage());
        }

        try{
            throwMeAGoodOne();
        }
        catch(Exception e){
            System.out.println(e);
            throw e;
        }

    }

    public static class JammedTurkeyCage implements AutoCloseable{

        @Override
        public void close() throws IllegalStateException {
            throw new IllegalStateException("suppressed");
        }
    }

    public void throwMeAGoodOne() throws IOException, SQLException, DateTimeParseException, NullPointerException {}


    int testAssertionsBlah =3;
    @Test
    public void testAssertions(){
        assert 1!=0 : "oops"; // bad asserts (when enabled) throw Errors
        /*
        java -ea:com.wiley.demos... -da:com.wiley.demos.TestColors my.programs.Main

        explanation:
        enable assertions on all java classes in all subpackages for com.wiley.demos
        disabled assertions for com.wiley.demo.TestColors
         */
        class Inner{
            public final int outer = RealOcpTest.this.testAssertionsBlah;
        }

        final Inner inner = new Inner();
        System.out.println(inner.outer);

    }

    @Test
    public void testInnerClasses(){
        A a1 = new A();
        A a2 = new A().new B();
        A a3 = a1.new B();

        A.C c1 = new A.C();
        //A.C c2 = new A().new C(); illegal
    }

    class A{
        int x =3;
        static int y = 5;

        // final B myB = new B();

        class B extends A{
            int test = this.z +1;
            int z = A.this.x;
            int z2 = x;
            int testSuper = super.y;

            // no static (jdk 8 only)
            static int badIn8 = y;
        }

        static class C {
            static class D{static int r = 4;}
        } // can extend anything + implement (just not here -- jdk 16)

    }

    // enum section (no tests but valid code)
    static enum blah{
        A,B{public void a(){}};

        public int x; // default to 0

        blah(){}
    }

    interface Default{
        public default void doSomething(){}
    }

//    interface D {
//        public default void doSomething(){}
//    }
//
//    class BLH implements  Default, D{
//        @Override
//        public void doSomething() {
//            Default.super.doSomething();
//        }
//    }

    @Test
    public void testEquals(){
        Integer x = 3;
        System.out.println(x.equals(null));
        System.out.println(x.equals(4D));

        // Default d = () -> {};
        String[] strings = {"1", "a", "B"};
        // natural: 1, B, a
        // reverse: a, B, 1
        Arrays.sort(strings);
        System.out.println("N: " + Arrays.stream(strings).reduce("", String::concat));
        Arrays.sort(strings, Comparator.reverseOrder());
        System.out.println("R: " + Arrays.stream(strings).reduce("", String::concat));

    }

    public static void bla() throws SQLException{
        try{
            //throw new Exception();
            bal1();
        }
        catch(Exception e) {
            //throw new Exception();
            throw e;
        }
    }
    public static void bal1() throws SQLException{}
}