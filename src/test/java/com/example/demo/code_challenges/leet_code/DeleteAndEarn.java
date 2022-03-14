package com.example.demo.code_challenges.leet_code;

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
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DeleteAndEarn {

    @Test
    public void createDataTest(){

        Stream.<Pair<List<Integer>,ConcurrentSkipListMap<Integer,ValueBucket>>>of(
                new Pair<List<Integer>,ConcurrentSkipListMap<Integer,ValueBucket>>(
                        Arrays.asList(1,1,2,2,3,3),
                        new ConcurrentSkipListMap<Integer, ValueBucket>(){{
                            put(1,new ValueBucket(2,4));
                            put(2,new ValueBucket(4,8));
                            put(3,new ValueBucket(6,4));
                        }}
                ),
                new Pair<List<Integer>,ConcurrentSkipListMap<Integer,ValueBucket>>(
                        Arrays.asList(1,2,3,1,2,3),
                        new ConcurrentSkipListMap<Integer, ValueBucket>(){{
                            put(1,new ValueBucket(2,4));
                            put(2,new ValueBucket(4,8));
                            put(3,new ValueBucket(6,4));
                        }}
                ),
                new Pair<List<Integer>,ConcurrentSkipListMap<Integer,ValueBucket>>(
                        Arrays.asList(1,3,2,2,3,1),
                        new ConcurrentSkipListMap<Integer, ValueBucket>(){{
                            put(1,new ValueBucket(2,4));
                            put(2,new ValueBucket(4,8));
                            put(3,new ValueBucket(6,4));
                        }}
                )
        ).forEach(
                (testData) -> {

                    final ConcurrentSkipListMap<Integer,ValueBucket> actual =
                            this.createData(testData.getValue0());

                   Assertions.assertEquals(testData.getValue1().size(), actual.size());

                   testData.getValue1().forEach(
                           (key,value) -> {

                               Assertions.assertTrue(actual.containsKey(key),String.format("does not have: %d", key));

                               Assertions.assertEquals(value, actual.get(key));

                           }
                   );

                }
        );

    }

    @Test
    public void createAndAddRemovalsForContiguousListTest(){

        Stream.<Pair<List<Integer>,Set<Integer>>>of(
                new Pair<List<Integer>,Set<Integer>>(
                        Arrays.asList(1,2,3),
                        new HashSet<Integer>(Arrays.asList(1,3))
                ),
                new Pair<List<Integer>,Set<Integer>>(
                        Arrays.asList(1,2,3,4),
                        new HashSet<Integer>(Arrays.asList(4,2))
                ),
                new Pair<List<Integer>,Set<Integer>>(
                        Arrays.asList(1,2,3,4,5),
                        new HashSet<Integer>(Arrays.asList(5,3,1))
                )
        ).forEach(
                (testData) -> {

                    Assertions.assertEquals(
                            testData.getValue1(),
                            this.createAndAddRemovalsForContiguousList(testData.getValue0())
                    );

                }
        );

    }

    @Test
    public void deleteAndEarnTest(){

        Stream.<Triplet<List<Integer>,Integer,Set<Integer>>>of(
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(5),
                        5,
                        new HashSet<Integer>(Arrays.asList(5))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(1,2),
                        2,
                        new HashSet<Integer>(Arrays.asList(2))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(1,2,3),
                        4,
                        new HashSet<Integer>(Arrays.asList(1,3))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(1,1,2,5),
                        7,
                        new HashSet<Integer>(Arrays.asList(2,5))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(1,2,3,5),
                        9,
                        new HashSet<Integer>(Arrays.asList(1,3,5))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(1,3,5),
                        9,
                        new HashSet<Integer>(Arrays.asList(1,3,5))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(1,2,3,5,7,9,11,12,14,15,16),
                        67,
                        new HashSet<Integer>(Arrays.asList(1,3,5,7,9,12,14,16))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(2,2,3,3,3,4),
                        9,
                        new HashSet<Integer>(Arrays.asList(3))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(4,2,3),
                        6,
                        new HashSet<Integer>(Arrays.asList(2,4))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(16,11,7,5,2,1,3,15,12,9,14),
                        67,
                        new HashSet<Integer>(Arrays.asList(1,3,5,7,9,12,14,16))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(1,2,2,3),
                        4,
                        new HashSet<Integer>(Arrays.asList(1,3))
                ),
                new Triplet<List<Integer>,Integer,Set<Integer>>(
                        Arrays.asList(1,2,2,2,3),
                        6,
                        new HashSet<Integer>(Arrays.asList(2))
                )
        ).forEach(
                (testData) -> {

                    final Pair<Integer, Set<Integer>> actual =
                            this.deleteAndEarn(testData.getValue0());

                    Assertions.assertEquals(
                            testData.getValue1(),
                            actual.getValue0(),
                            String.format("expected: (%s)\nactual: (%s)\n",testData.getValue2(),actual.getValue1())
                    );

                    Assertions.assertEquals(testData.getValue2(),actual.getValue1());

                }
        );

    }


    private Pair<Integer, Set<Integer>> deleteAndEarn(@NotNull final  List<Integer> numbers){

        final ConcurrentSkipListMap<Integer, ValueBucket> data = this.createData(numbers);

        final Pair<? extends Set<Integer>,? extends List<List<Integer>>> toRemove =
                new Pair<HashSet<Integer>,ArrayList<List<Integer>>>(
                        new HashSet<Integer>(),
                        new ArrayList<List<Integer>>()
                );

        final Set<Integer> allValuesRemoved = new HashSet<Integer>();

        int sum = 0;

        /*
        case 1) 1,2,3
        1.5) 1,2,4
        case 2) 2,3,3,4
        2.5) 3,3,4,5,6
        case 3) 2,5,7
        case 4) 1,2
         */

        List<Integer> negatives = new ArrayList<Integer>();

        for(final Integer key : data.keySet()){

            if(!data.containsKey(key))
                continue;

            // evaluate case 3
            if(!data.containsKey(key - 1) && !data.containsKey(key + 1)) // no neighbors add to removal
                toRemove.getValue0().add(key);

            // if bucket value is greater than the other value buckets then case 3 & remove
            final int valueBucketDifference =
                    data.get(key).getValue() -
                            (
                                    (data.containsKey(key - 1) ? data.get(key - 1).getValue() : 0) +
                                    (data.containsKey(key + 1) ? data.get(key + 1).getValue() : 0)
                            );

            // positive
            if(valueBucketDifference > 0){

                // disjoint
                /*
                [1,3] -> not possible, will be handle separately,
                [1,1,2,4,4,5],
                [5]
                 */
                if(!data.containsKey(key - 1)){

                    if(negatives.size() >= 2)
                        toRemove.getValue1().add(negatives);
                    else if(negatives.size() == 1)
                        throw new IllegalArgumentException("negative size is 1 on a disjoint");

                    data.remove(key + 1);

                }
                // conjoint
                /*
                [1,2]
                [1,2,3]
                [1,2,2,2,3]
                [1,2,3,3,3,4]
                 */
                else{

                    if(negatives.size() == 1) {
                        data.remove(key - 1);
                        data.remove(key + 1);
                    }
                    else if(negatives.size() == 0)
                        throw new IllegalArgumentException("negative size is 0 on a disjoint");
                    else{

                        negatives.add(key);
                        toRemove.getValue1().add(negatives);

                        data.remove(key + 1);

                        System.out.printf(
                                "conjoint - positive(%d)\nindividual(%s)\ncontiguous(%s)\n--------------\n",
                                key,
                                toRemove.getValue0(),
                                toRemove.getValue1()
                        );

                        negatives = new ArrayList<Integer>();

                        continue;

                    }

                }

                // regardless, list will be cleared & item is added
                negatives = new ArrayList<Integer>();
                toRemove.getValue0().add(key);

                System.out.printf(
                        "positive(%d)\nindividual(%s)\ncontiguous(%s)\n-------------\n",
                        key,
                        toRemove.getValue0(),
                        toRemove.getValue1()
                );

            }
            // negative
            else
                negatives.add(key);

        }

        if(!negatives.isEmpty())
            toRemove.getValue1().add(negatives);

        // iterate through all individual values, add to sum and result set
        for(final Integer valueRemoved : toRemove.getValue0()){

            if(!data.containsKey(valueRemoved))
                throw new RuntimeException("should never happen - " + valueRemoved);

            sum += data.get(valueRemoved).getValue();
            allValuesRemoved.add(valueRemoved);
        }

        // iterate through all contiguous values, add to sum and result set
        for(final List<Integer> contiguousValues : toRemove.getValue1()){

            final Set<Integer> contiguousResult =
                    this.createAndAddRemovalsForContiguousList(contiguousValues);

            for(final Integer valueRemoved : contiguousResult) {

                if(!data.containsKey(valueRemoved))
                    throw new RuntimeException("should never happen - " + valueRemoved);

                sum += data.get(valueRemoved).getValue();

                allValuesRemoved.add(valueRemoved);
            }
        }

        return new Pair<Integer, Set<Integer>>(sum, allValuesRemoved);

    }

    private Set<Integer> createAndAddRemovalsForContiguousList(@NotNull final List<Integer> list){

        final Set<Integer> valuesToAdd = new HashSet<Integer>();

        for(int i = list.size() - 1; i >= 0; i -= 2)
            valuesToAdd.add(list.get(i));

        return valuesToAdd;

    }

    private ConcurrentSkipListMap<Integer, ValueBucket> createData(@NotNull final List<Integer> numbers){

        final ConcurrentSkipListMap<Integer, ValueBucket> data =
                new ConcurrentSkipListMap<Integer,ValueBucket>();

        final List<Integer> sorted = this.<Integer>rangeSort(numbers);

        for(final int num : sorted){

            if(data.containsKey(num))
                data.get(num).addValue(num);
            else
                data.put(
                        num,
                        new ValueBucket(
                                num,
                                data.containsKey(num - 1) ? data.get(num - 1).getValue() : 0
                        )
                );

            // left
            if(data.containsKey(num - 1))
                data.get(num - 1).addPointsOnTable(num);

            // right
            if(data.containsKey(num + 1))
                data.get(num + 1).addPointsOnTable(num);

        }

        return data;
    }

    private final static class ValueBucket{

        int value, pointsOnTable;

        ValueBucket(final int value, final int pointsOnTable){
            this.value = value;
            this.pointsOnTable = pointsOnTable;
        }

        int getPointsOnTable() {
            return pointsOnTable;
        }

        int getValue() {
            return value;
        }

        void setValue(int value) {
            this.value = value;
        }

        void setPointsOnTable(int pointsOnTable) {
            this.pointsOnTable = pointsOnTable;
        }

        void addPointsOnTable(final int pointsOnTable){
            this.pointsOnTable += pointsOnTable;
        }

        void addValue(final int value){
            this.value += value;
        }

        @Override
        public boolean equals(Object object){

            if(object == null)
                return false;
            else if(object == this)
                return true;
            else if(!(object instanceof ValueBucket))
                return false;

            final ValueBucket valueBucket = (ValueBucket)object;

            return this.value == valueBucket.getValue() && this.pointsOnTable == valueBucket.pointsOnTable;

        }

        @Override
        public String toString(){
            return String.format(
                    "(value: %d -- points on table: %d)",
                    this.value,
                    this.pointsOnTable
            );
        }
    }

    // note: pre-tested
    private <T extends Comparable<T>> List<T> rangeSort(@NotNull final List<T> toSort){

        final List<T> toReturn = new ArrayList<T>();

        final TreeMap<T, Integer> valuesAndCount = new TreeMap<T, Integer>();

        for(final T element : toSort){

            if(!valuesAndCount.containsKey(element))
                valuesAndCount.put(element, 1);
            else
                valuesAndCount.put(element, valuesAndCount.get(element) + 1);

        }

        // keySet is in natural order (ascending)
        for(final T element: valuesAndCount.keySet())
            for(int i = 0; i < valuesAndCount.get(element); i++)
                toReturn.add(element);

        return toReturn;
    }

}
