package com.example.demo.code_challenges.lru_cache;

import com.example.demo.model.code_challenges.other.LRUCache;
import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class LRUCacheTests {

    interface GetFieldName{
        
        public abstract String getFieldName();

    }

    private enum LRUCacheFields implements GetFieldName{
        elements("elements"),
        elementsOrder("elementsOrder"),
        capacity("capacity");

        private final String fieldName;

        LRUCacheFields(String fieldName){
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    private enum LRUCacheNodeFields implements GetFieldName{
        element("element"),
        key("key"),
        timesUsed("timesUsed"),
        createdOn("createdOn");

        private final String fieldName;

        LRUCacheNodeFields(String fieldName){
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    @SuppressWarnings("unchecked")
    private <T, R> R getFieldValue(@NotNull final LRUCache<T> lruCache, @NotNull final GetFieldName fieldToExtract)
    throws NoSuchFieldException, IllegalAccessException{

        final Class<LRUCache<T>> lruCacheReflection = (Class<LRUCache<T>>)lruCache.getClass();

        final Field lruCacheReflectionField = lruCacheReflection.getDeclaredField(fieldToExtract.getFieldName());

        lruCacheReflectionField.setAccessible(true);

        return (R)lruCacheReflectionField.get(lruCache);

    }

    @SuppressWarnings("unchecked")
    private <T, R> R getFieldCacheValue(@NotNull final LRUCache.Node<T> lruCacheNode, @NotNull final GetFieldName fieldToExtract)
            throws NoSuchFieldException, IllegalAccessException{

        final Class<LRUCache.Node<T>> lruCacheReflection = (Class<LRUCache.Node<T>>)lruCacheNode.getClass();

        final Field lruCacheReflectionField = lruCacheReflection.getDeclaredField(fieldToExtract.getFieldName());

        lruCacheReflectionField.setAccessible(true);

        return (R)lruCacheReflectionField.get(lruCacheNode);

    }

    private <T> HashMap<Integer, LRUCache.Node<T>> getElements(@NotNull final LRUCache<T> lruCache){
        try {
            return this.getFieldValue(lruCache, LRUCacheFields.elements);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private <T> TreeMap<Integer, TreeSet<LRUCache.Node<T>>> getElementsOrder(@NotNull final LRUCache<T> lruCache){
        try {
            return this.getFieldValue(lruCache, LRUCacheFields.elementsOrder);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private <T> Integer getCapacity(@NotNull final LRUCache<T> lruCache){
        try {
            return this.getFieldValue(lruCache, LRUCacheFields.capacity);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private <T> Integer getSize(@NotNull final LRUCache<T> lruCache){
        try {
            return this.getElements(lruCache).size();
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private <T> T getElement(@NotNull final LRUCache.Node<T> lruCacheNode){
        try {
            return this.getFieldCacheValue(lruCacheNode, LRUCacheNodeFields.element);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private <T> Integer getKey(@NotNull final LRUCache.Node<T> lruCacheNode){
        try {
            return this.getFieldCacheValue(lruCacheNode, LRUCacheNodeFields.key);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private <T> Integer getTimesUsed(@NotNull final LRUCache.Node<T> lruCacheNode){
        try {
            return this.getFieldCacheValue(lruCacheNode, LRUCacheNodeFields.timesUsed);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private <T> Integer getCreatedOn(@NotNull final LRUCache.Node<T> lruCacheNode){
        try {
            return this.getFieldCacheValue(lruCacheNode, LRUCacheNodeFields.createdOn);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void populateToCapacityLRUCacheTest(){

        Stream.<List<Pair<Integer,String>>>of(
                this.createInputDataRange(3),
                this.createInputDataRange(1),
                this.createInputDataRange(10)
        ).forEach(
                (testData) -> {

                    try {
                        final LRUCache<String> lruCache = new LRUCache<String>(testData.size());

                        Assertions.assertEquals(
                                testData.size(),
                                this.getCapacity(lruCache)
                        );

                        for (int i = 0; i < testData.size(); ) {

                            final Pair<Integer, String> toAdd = testData.get(i);

                            Thread.sleep(10);

                            lruCache.put(toAdd.getValue0(), toAdd.getValue1());

                            Assertions.assertEquals(
                                    ++i,
                                    this.getSize(lruCache)
                            );

                        }

                        System.out.println(lruCache + "\n");

                    }
                    catch(Exception ex){
                        Assertions.fail(ex);
                    }

            }
        );

    }

    @Test
    public void removeLeastRecentlyUsedNoUsageTest(){

        /*
        inputs: for every cache at capacity, wil required
        1) List of pair<string, string> to 'put' over capacity w/ expected key to be remove

        steps:
        1) take stream of Pair<List<Pair<Integer,String>>,List<Pair<integer, string>>> to create lru cache at capacity
        2) for each pair, against the mapped/created cache, 'put' element 1 and expect
          2.a) expected remove element is gone
          2.b) size of both elements and elementsOrder is equal & equal to capacity
         */

        Stream.<Pair<List<Pair<Integer,String>>,List<Pair<Integer, Integer>>>>of(
               new Pair<List<Pair<Integer,String>>,List<Pair<Integer, Integer>>>(
                       this.createInputDataRange(3),
                           Arrays.<Pair<Integer,Integer>>asList(
                                   new Pair<Integer,Integer>(4, 1),
                                   new Pair<Integer,Integer>(5, 2)
                           )
               ),
                new Pair<List<Pair<Integer,String>>,List<Pair<Integer, Integer>>>(
                        this.createInputDataRange(5),
                            Arrays.<Pair<Integer,Integer>>asList(
                                    new Pair<Integer,Integer>(6, 1),
                                    new Pair<Integer,Integer>(7, 2),
                                    new Pair<Integer,Integer>(8, 3),
                                    new Pair<Integer,Integer>(9, 4)
                            )
                )
        ).map(
                (inputData) -> {
                    try {
                        final LRUCache<String> lruCache = new LRUCache<String>(inputData.getValue0().size());

                        for (int i = 0; i < inputData.getValue0().size(); ) {

                            final Pair<Integer, String> toAdd = inputData.getValue0().get(i++);

                            Thread.sleep(10);

                            lruCache.put(toAdd.getValue0(), toAdd.getValue1());

                        }

                        return new Pair<LRUCache<String>,List<Pair<Integer, Integer>>>(
                                lruCache,
                                inputData.getValue1()
                        );

                    }
                    catch(Exception ex){
                        Assertions.fail(ex);
                        return null; // will never happen
                    }
                }
        ).forEach(
                (testData) -> {

                    final LRUCache<String> lruCache = testData.getValue0();

                    final HashMap<Integer, LRUCache.Node<String>> elements =
                            this.getElements(lruCache);

                    final TreeMap<Integer, TreeSet<LRUCache.Node<String>>> elementsOrdered =
                            this.getElementsOrder(lruCache);

                    Assertions.assertEquals(
                            this.getCapacity(lruCache),
                            elements.size()
                    );

                    Assertions.assertEquals(
                            elements.size(),
                            this.getTotalSizeOfElementsOrdered(elementsOrdered)
                    );

                    for(final Pair<Integer, Integer> testPair : testData.getValue1()){

                        try {
                            Thread.sleep(10);
                        }
                        catch(Exception ex){
                            Assertions.fail(ex);
                        }

                        final Pair<Integer,String> toAdd = this.createInputData(testPair.getValue0());

                        System.out.printf(
                                "Cache before (%s - %s) was added: %s\n",
                                toAdd.getValue0(),
                                toAdd.getValue1(),
                                lruCache
                        );

                        lruCache.put(toAdd.getValue0(), toAdd.getValue1());

                        System.out.printf(
                                "Cache after (%s - %s) was added: %s\n---------------\n",
                                toAdd.getValue0(),
                                toAdd.getValue1(),
                                lruCache
                        );

                        // assert that element to removed (2nd int in test pair) is gone
                        Assertions.assertNull(lruCache.get(testPair.getValue1()));

                        // assert that element is present in map
                        Assertions.assertTrue(elements.containsKey(toAdd.getValue0()));

                        // assert size is still same & at capacity
                        Assertions.assertEquals(
                                this.getCapacity(lruCache),
                                elements.size()
                        );

                        Assertions.assertEquals(
                                elements.size(),
                                this.getTotalSizeOfElementsOrdered(elementsOrdered)
                        );


                    }

                }
        );
        
    }

    /*
    Note: this method tests the 'get' as well
     */

    @Test
    public void removeLeastRecentlyUsedWithUsageTest(){

        /*
        note: comments of empty tree sets are removed in data but are kept here for simplicity
         */

        Stream.<Pair<List<Pair<Integer,String>>,List<Triplet<Integer, Integer, int[]>>>>of(
                new Pair<List<Pair<Integer,String>>,List<Triplet<Integer, Integer, int[]>>>(
                        this.createInputDataRange(3),
                        Arrays.<Triplet<Integer, Integer, int[]>>asList(
                                // 0 - [1,2,3]
                                // 0 - [3], 1 - [2], 2 - [1]
                                new Triplet<Integer, Integer, int[]>(4, 3, new int[]{1,1,2}),
                                // 0 - [4], 1 - [2], 2 - [1]
                                // 0 - [], 1 - [], 2 - [1,4], 3 - [2]
                                new Triplet<Integer, Integer, int[]>(5, 1, new int[]{4,4,2,2}),
                                // 0 - [5], 1 - [], 2 - [4], 3 - [2]
                                // 0 - [5], 1 - [], 2 - [], 3 - [2,4]
                                new Triplet<Integer, Integer, int[]>(6, 5, new int[]{4})
                        )
                ),
                new Pair<List<Pair<Integer,String>>,List<Triplet<Integer, Integer, int[]>>>(
                        this.createInputDataRange(5),
                        Arrays.<Triplet<Integer, Integer, int[]>>asList(
                                // 0 - [1,2,3,4,5]
                                // 0 - [4], 1 - [5], 2 - [1,2,3]
                                new Triplet<Integer, Integer, int[]>(6, 4, new int[]{1,1,2,2,3,3,5}),
                                // 0 - [6], 1 - [5], 2 - [1,2,3]
                                // 0 - [], 1 - [], 2 - [1,2,5,6], 3 - [3]
                                new Triplet<Integer, Integer, int[]>(7, 1, new int[]{6,6,5,3}),
                                // 0 - [7], 1 - [], 2 - [2,5,6], 3 - [3]
                                // 0 - [7], 1 - [], 2 - [], 3 - [2,3,5], 4 - [6]
                                new Triplet<Integer, Integer, int[]>(8, 7, new int[]{2,5,6,6}),
                                // 0 - [8], 1 - [], 2 - [], 3 - [2,3,5], 4 - [6]
                                // 0 - [], 1 - [], 2 - [], 3 - [3,5], 4 - [2,6], 5 - [8]
                                new Triplet<Integer, Integer, int[]>(9, 3, new int[]{8,8,8,8,8,2})
                        )
                )
        ).map(
                (inputData) -> {
                    try {
                        final LRUCache<String> lruCache = new LRUCache<String>(inputData.getValue0().size());

                        for (int i = 0; i < inputData.getValue0().size(); ) {

                            final Pair<Integer, String> toAdd = inputData.getValue0().get(i++);

                            Thread.sleep(10);

                            lruCache.put(toAdd.getValue0(), toAdd.getValue1());

                        }

                        return new Pair<LRUCache<String>,List<Triplet<Integer, Integer, int[]>>>(
                                lruCache,
                                inputData.getValue1()
                        );

                    }
                    catch(Exception ex){
                        Assertions.fail(ex);
                        return null; // will never happen
                    }
                }
        ).forEach(
                (testData) -> {

                    final LRUCache<String> lruCache = testData.getValue0();

                    final HashMap<Integer, LRUCache.Node<String>> elements =
                            this.getElements(lruCache);

                    final TreeMap<Integer, TreeSet<LRUCache.Node<String>>> elementsOrdered =
                            this.getElementsOrder(lruCache);

                    Assertions.assertEquals(
                            this.getCapacity(lruCache),
                            elements.size()
                    );

                    Assertions.assertEquals(
                            elements.size(),
                            this.getTotalSizeOfElementsOrdered(elementsOrdered)
                    );

                    for(final Triplet<Integer, Integer, int[]> testDataTriplet : testData.getValue1()){

                        try {
                            Thread.sleep(10);
                        }
                        catch(Exception ex){
                            Assertions.fail(ex);
                        }

                        final Pair<Integer,String> toAdd = this.createInputData(testDataTriplet.getValue0());

                        for(final int keyToGet : testDataTriplet.getValue2())
                            Assertions.assertEquals(
                                    this.createInputData(keyToGet).getValue1(),
                                    lruCache.get(keyToGet),
                                    String.format("key: %d", keyToGet)
                            );

                        System.out.printf(
                                "Cache before (%s - %s) was added: %s\n",
                                toAdd.getValue0(),
                                toAdd.getValue1(),
                                lruCache
                        );

                        lruCache.put(toAdd.getValue0(), toAdd.getValue1());

                        System.out.printf(
                                "Cache after (%s - %s) was added: %s\n---------------\n",
                                toAdd.getValue0(),
                                toAdd.getValue1(),
                                lruCache
                        );

                        // assert that element to removed (2nd int in test data) is gone
                        Assertions.assertNull(lruCache.get(testDataTriplet.getValue1()));

                        // assert that element is present in map
                        Assertions.assertTrue(elements.containsKey(toAdd.getValue0()));

                        // assert size is still same & at capacity
                        Assertions.assertEquals(
                                this.getCapacity(lruCache),
                                elements.size()
                        );

                        Assertions.assertEquals(
                                elements.size(),
                                this.getTotalSizeOfElementsOrdered(elementsOrdered)
                        );


                    }

                }
        );

    }

    private List<Pair<Integer,String>> createInputDataRange(final int toMake){

        List<Pair<Integer,String>> results = new ArrayList<Pair<Integer, String>>();

        for(int i = 0; i < toMake;)
            results.add(
                this.createInputData(++i)
            );

        return results;

    }

    private Pair<Integer,String> createInputData(final int num){
        return new Pair<Integer, String>(num, String.format("element%d", num));
    }

    private <T> int getTotalSizeOfElementsOrdered(@NotNull final TreeMap<Integer, TreeSet<LRUCache.Node<T>>> elementsOrdered){

        int total = 0;

        for(final Map.Entry<Integer,TreeSet<LRUCache.Node<T>>> treeSetEntry : elementsOrdered.entrySet())
            total += treeSetEntry.getValue().size();

        return total;

    }

    /*
    NOTE - disregard (future)
    future: test not only output, and what was removed, but rather the exhaustive, internal state
    of the functioning components of this LRU Cache implementation
     */
    private final Consumer<Triplet<LRUCache<String>,Pair<Integer,String>,Integer>> testRemoval =
            (testData) -> {

                final TreeMap<Integer, TreeSet<LRUCache.Node<String>>> elementsOrdered;
                final HashMap<Integer, LRUCache.Node<String>> elements = this.getElements(testData.getValue0());

                // affirms that object key (expected to be removed) is present
                Assertions.assertTrue(elements.containsKey(testData.getValue2()));

                elementsOrdered = this.getElementsOrder(testData.getValue0());

                // affirms cache is at capacity (element will be removed at next call to put)
                Assertions.assertEquals(this.getCapacity(testData.getValue0()), elements.size());
                Assertions.assertEquals(elements.size(), elementsOrdered.size());

                // gets expected value to be removed
                final int firstKey = elementsOrdered.firstKey();

                final TreeSet<LRUCache.Node<String>> leastTreeSet =
                        elementsOrdered.get(elementsOrdered.firstKey());

                final int sizeOfTreeSetToBeRemovedFrom = leastTreeSet.size();

                final LRUCache.Node<String> elementToBeRemoved =
                        leastTreeSet.pollLast();

                Assertions.assertNotNull(elementToBeRemoved);

                // affirms element to be removed is the same
                Assertions.assertEquals(
                        testData.getValue2(),
                        this.getKey(elementToBeRemoved)
                );

                // appends new element & affirms element to be remove was removed
                testData.getValue0().put(
                        testData.getValue1().getValue0(),
                        testData.getValue1().getValue1()
                );

                Assertions.assertFalse(elements.containsKey(testData.getValue2()));

                Assertions.assertNull(
                        testData.getValue0().get(testData.getValue2())
                );

                // affirms new element is present in element map
                Assertions.assertEquals(
                        testData.getValue1().getValue1(),
                        this.getElement(
                                elements.get(testData.getValue1().getValue0())
                        )
                );

                // affirms new element is present in element-usage map
                Assertions.assertTrue(
                        elementsOrdered.get(0).contains(
                                elements.get(testData.getValue1().getValue0())
                        )
                );

                    /*
                    affirms elements ordered is as expected in four cases:
                    1) if set to be removed from is empty w/ other elements in elements usage map
                       then the first key should be different

                     */
                Assertions.assertEquals(0, elementsOrdered.firstKey());
                if(leastTreeSet.isEmpty() && this.getTimesUsed(elementToBeRemoved) != 0) {
                    Assertions.assertNotEquals(firstKey, 0); // old first key is removed
                    Assertions.assertEquals(1, elementsOrdered.get(0).size()); // new first key is 0 & has 1 element
                }
                else if(leastTreeSet.isEmpty())
                    Assertions.assertEquals(1, elementsOrdered.get(0).size()); // new first key is 0 & has 1 element
                else if(this.getTimesUsed(elementToBeRemoved) == 0) {
                    Assertions.assertEquals(sizeOfTreeSetToBeRemovedFrom, leastTreeSet.size());
                    Assertions.assertFalse(leastTreeSet.contains(elementToBeRemoved));
                }
                else {
                    // tree set is not empty AND element that was removed was used more than once
                    // new element did (used 0 times) not added here
                    Assertions.assertEquals(sizeOfTreeSetToBeRemovedFrom - 1, leastTreeSet.size());
                    Assertions.assertFalse(leastTreeSet.contains(elements.get(testData.getValue1().getValue0())));
                }

                // affirms cache is STILL at capacity (expected element was removed w/ new element being appended)
                Assertions.assertEquals(this.getCapacity(testData.getValue0()), elements.size());
                Assertions.assertEquals(elements.size(), elementsOrdered.size());

            };

}
