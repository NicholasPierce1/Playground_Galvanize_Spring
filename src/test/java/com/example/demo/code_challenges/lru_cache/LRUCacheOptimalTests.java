package com.example.demo.code_challenges.lru_cache;

import com.example.demo.model.code_challenges.other.LRUCache;
import com.example.demo.model.code_challenges.other.LRUCache_Optimal;
import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/*
NOTE: these tests feature exclusive, manual validation and a simple design as oppose to it's predecessor
consequently that they perform relatively the same functionality
 */
public class LRUCacheOptimalTests {

    @Test
    public void createLRUCacheAtCapacity(){

        Stream.<List<Pair<Integer,String>>>of(
                this.createInputDataRange(3),
                this.createInputDataRange(5)
        ).forEach(

                (testData) -> {

                    final LRUCache_Optimal<String> lruCache_optimal = new LRUCache_Optimal<String>(testData.size());

                    for(final Pair<Integer, String> toAdd : testData)
                        lruCache_optimal.put(toAdd.getValue0(), toAdd.getValue1());

                    System.out.println("created cache:\n" + lruCache_optimal);

                }

        );

    }

    @Test
    public void putOverCapacityAndDuplicateKeyLRUCacheTests(){

        List<Pair<Integer,String>> testData = this.createInputDataRange(3);

        final LRUCache_Optimal<String> lruCache_optimal = new LRUCache_Optimal<String>(testData.size());

        for(final Pair<Integer, String> toAdd : testData)
            lruCache_optimal.put(toAdd.getValue0(), toAdd.getValue1());

        for(int i = 0; i < testData.size();){

            final Pair<Integer,String> toAdd =
                    this.createInputData(i + testData.size() + 1);

            lruCache_optimal.put(toAdd.getValue0(), toAdd.getValue1());

            Assertions.assertNull(lruCache_optimal.get(testData.get(i++).getValue0()));

        }

        for(int i = 0; i < testData.size();){

            final int key = ++i + testData.size();

            final String element = lruCache_optimal.get(key).concat("" + key); // element44

            lruCache_optimal.put(key,element);

            Assertions.assertEquals(
                    element,
                    lruCache_optimal.get(key)
            );

        }

        System.out.println(lruCache_optimal);
    }

    @Test
    public void putOverCapacityWithUsagesTest(){

        List<Pair<Integer,String>> testData = this.createInputDataRange(3);

        final LRUCache_Optimal<String> lruCache_optimal = new LRUCache_Optimal<String>(testData.size());

        for(final Pair<Integer, String> toAdd : testData)
            lruCache_optimal.put(toAdd.getValue0(), toAdd.getValue1());

        for(int i = 0; i < testData.size();){

            final Pair<Integer,String> toAdd =
                    this.createInputData(++i + testData.size());


            /*
             [3,2,1] -> [1,3,2]
             [1,3,4] -> [4,1,3]
             [4,1,5] -> [5,4,1] --> [5,4,6]
             */

            final Pair<Integer, Integer> toGetAndExpectedRemove = switch (i) {
                case 2 -> new Pair<Integer, Integer>(4, 3);
                case 3 -> new Pair<Integer, Integer>(5, 1);
                default -> new Pair<Integer, Integer>(1, 2);
            };

            lruCache_optimal.get(toGetAndExpectedRemove.getValue0());

            lruCache_optimal.put(toAdd.getValue0(), toAdd.getValue1());

            Assertions.assertNull(lruCache_optimal.get(toGetAndExpectedRemove.getValue1()));

        }

        System.out.println(lruCache_optimal);


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

    @Test
    public void randomTest(){

        //String toRemove = "{\"blah\": 123, event-id"

        //Pattern pattern = Pattern.compile()

        System.out.println("{\"blah\": 123, \"event-id\" : \"abc-123\", \"myVal\" : true}"
                .replaceAll("^.*\"event-id\" : ", "")
                .replaceAll(",.*","")
        );

    }

}
