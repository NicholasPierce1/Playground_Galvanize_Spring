package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class WiggleSort {

    @Test
    public void wiggleSortTest(){

        Stream.<List<Integer>>of(
            Arrays.asList(1,2,3),
            Arrays.asList(1,1,2,3,3,4,5),
            Arrays.asList(1,1,2,2,3,3,4,4,5,5),  // L:[1,1,2,2,3,3] R:[3,3,4,4,5,5]
            Arrays.asList(1,1,2,2,3,3,3,4,4,5,5)
        ).map(
                (unsortedList) -> {

                    final TreeMap<Integer,Integer> treeMap = this.rangeSort(unsortedList);

                    return new Triplet<Pair<List<Integer>, LinkedList<Integer>>, TreeMap<Integer,Integer>, Integer>(
                            this.partitionElementsForWiggleSort(
                                treeMap,
                                unsortedList.size()
                            ),
                            treeMap,
                            unsortedList.size()
                    );

                }
        ).forEach(
                (testData) -> {

                    final List<Integer> actual = this.wiggleSort(
                            testData.getValue0().getValue0(),
                            testData.getValue0().getValue1(),
                            testData.getValue1()
                    );

                    System.out.println(actual);
                    Assertions.assertEquals(
                            testData.getValue2(),
                            actual.size()
                    );

                    int previousElement = -1; // init -> will be re-assigned in first iteration of following loop

                    for(int i = 0; i < actual.size(); i++){

                        if(i != 0) {

                            System.out.printf("prev: %d -- actual: %d\n", previousElement, actual.get(i));

                            // (0+1) is odd --> less than, (1+1) is even --> greater than
                            if((i+1) % 2 == 0) // even
                                Assertions.assertTrue(previousElement < actual.get(i));
                            else
                                Assertions.assertTrue(previousElement > actual.get(i));

                        }

                        previousElement = actual.get(i);

                    }

                }
        );

    }

    @Test
    public void partitionElementsForWiggleSortTest(){

        final BiConsumer<? super List<Integer>, ? super List<Integer>> assertListEquality =
                (expected, actual) -> {

                    Assertions.assertEquals(
                            expected.size(),
                            actual.size()
                    );

                    for(int i = 0; i < expected.size(); i++)
                        Assertions.assertEquals(
                                expected.get(i),
                                actual.get(i)
                        );

                };

        Stream.<Triplet<List<Integer>, List<Integer>, List<Integer>>>of(
                new Triplet<List<Integer>, List<Integer>, List<Integer>>(
                        Arrays.<Integer>asList(1,2,3), // to encapsulate in tree map, then partition
                        Arrays.<Integer>asList(1,2), // left
                        Arrays.<Integer>asList(3)  // right
                ),
                new Triplet<List<Integer>, List<Integer>, List<Integer>>(
                        Arrays.<Integer>asList(1,2,2,4), // to encapsulate in tree map, then partition
                        Arrays.<Integer>asList(1,2), // left
                        Arrays.<Integer>asList(2,4)  // right
                ),
                new Triplet<List<Integer>, List<Integer>, List<Integer>>(
                        Arrays.<Integer>asList(1,1,1), // to encapsulate in tree map, then partition
                        Arrays.<Integer>asList(1), // left
                        Arrays.<Integer>asList(1)  // right
                ),
                new Triplet<List<Integer>, List<Integer>, List<Integer>>(
                        Arrays.<Integer>asList(1,5,1,1,6,4), // to encapsulate in tree map, then partition
                        Arrays.<Integer>asList(1), // left
                        Arrays.<Integer>asList(4,5,6)  // right
                ),
                new Triplet<List<Integer>, List<Integer>, List<Integer>>(
                        Arrays.<Integer>asList(1,1,2,3,3,4,5), // to encapsulate in tree map, then partition
                        Arrays.<Integer>asList(1,2,3), // left
                        Arrays.<Integer>asList(3,4,5)  // right
                ),
                new Triplet<List<Integer>, List<Integer>, List<Integer>>(
                        Arrays.<Integer>asList(1,1,2,2,3,3,4,4,5,5), // to encapsulate in tree map, then partition
                        Arrays.<Integer>asList(1,2,3), // left
                        Arrays.<Integer>asList(3,4,5)  // right
                ),
                new Triplet<List<Integer>, List<Integer>, List<Integer>>(
                        Arrays.<Integer>asList(1,1,2,2,3,3,3,4,4,5,5), // to encapsulate in tree map, then partition
                        Arrays.<Integer>asList(1,2,3), // left
                        Arrays.<Integer>asList(3,4,5)  // right
                )
        ).map(
                (testData) -> {

                    return new Triplet<TreeMap<Integer,Integer>, Integer, Pair<List<Integer>,List<Integer>>>(
                            this.rangeSort(testData.getValue0()),
                            testData.getValue0().size(),
                            new Pair<List<Integer>, List<Integer>>(
                                    testData.getValue1(),
                                    testData.getValue2()
                            )
                    );

                }
        ).forEach(
                (testData) -> {

                    final Pair<List<Integer>, LinkedList<Integer>> actual =
                            this.partitionElementsForWiggleSort(testData.getValue0(), testData.getValue1());

                    final Pair<List<Integer>, List<Integer>> expected = testData.getValue2();

                    assertListEquality.accept(
                            expected.getValue0(),
                            actual.getValue0()
                    );

                    assertListEquality.accept(
                            expected.getValue1(),
                            actual.getValue1()
                    );

                }
        );
    }

    /*
        [1,1,2,2,3] [3,4,4,5,5] --> { L: [1,2,3] R:[3,4,5,6,7] }
        --
        func(L,R,lessThan)
        each loop inside while has 2 operations
        if less than:
          left tail < right head* > left tail*
        else greater than:
          right head > left tail* < right head*

        * = hashset -> if element is already in set of 3 then
          if rightHead -> next rightHead
          else next leftTail


        1,1 2,2 3,3
        L:[1,1,2,2] R:[2,2,3,3]
        2<3>1|<2>1<3|
        ** = check if element's count is 0 first, if that's ok then check hashset before using
        this example above highlights why
        --
     */
    private <T> List<T> wiggleSort(
            @NotNull final List<T> left,
            @NotNull final LinkedList<T> right,
            @NotNull final TreeMap<T, Integer> treeMap){

        final List<T> wiggleElements = new ArrayList<T>();

        boolean lessThan = true;
        T element;

        while(!(left.isEmpty() && right.isEmpty())){

            final Set<T> present = new HashSet<T>();

                for(int i = 0; i < 3; i++){

                    element = this.getNextElement(
                            lessThan ? left : right,
                            present,
                            treeMap,
                            lessThan
                    );

                    if(element == null)
                        return wiggleElements;

                    wiggleElements.add(element);
                    present.add(element);
                    lessThan = !lessThan;

                }

            }

        return wiggleElements;
    }

    private <T> T getNextElement(
            @NotNull final List<T> elements,
            @NotNull final Set<T> present,
            @NotNull final TreeMap<T, Integer> treeMap,
            final boolean getTail){

        if(elements.size() == 0)
            return null;

        T element;

        if(getTail){

            element = elements.get(elements.size() - 1);

            if(treeMap.get(element) == 0) {

                elements.remove(elements.size() - 1);

                // last element already used
                return this.getNextElement(
                        elements,
                        present,
                        treeMap,
                        true
                );

            }
            else if(!present.contains(element)){

                treeMap.put(element, treeMap.get(element) - 1);

                return element;
            }
            else{ // elements already encountered: try to extract next element

                // dealing with last element in half-list OR empty
                if(!(elements.size() >= 2)) {

                    if (treeMap.get(element) > 0)
                        treeMap.put(element, treeMap.get(element) - 1);
                    else
                        element = null;

                    return element;
                }

                element = elements.get(elements.size() - 2);

                treeMap.put(element, treeMap.get(element) - 1);

                return element;
            }

        }
        else{

            element = elements.get(0);

            if(treeMap.get(element) == 0) {

                elements.remove(0);

                // last element already used
                return this.getNextElement(
                        elements,
                        present,
                        treeMap,
                        false
                );

            }
            else if(!present.contains(element)){

                treeMap.put(element, treeMap.get(element) - 1);

                return element;
            }
            else{ // elements already encountered: try to extract next element

                // dealing with last element in half-list OR empty
                if(!(elements.size() >= 2)) {

                    if (treeMap.get(element) > 0)
                        treeMap.put(element, treeMap.get(element) - 1);
                    else
                        element = null;

                    return element;
                }


                element = elements.get(1);

                treeMap.put(element, treeMap.get(element) - 1);

                return element;
            }

        }

    }

    private <T> Pair<List<T>, LinkedList<T>> partitionElementsForWiggleSort(
            @NotNull final TreeMap<T, Integer> treeMap, final int length){

        final List<T> left = new ArrayList<T>();
        final LinkedList<T> right = new LinkedList<T>();

        int leftTotal = 0;

        int halfWay = length % 2 == 0 ?
                length / 2 : // 4  -> 2
                length / 2 + 1; // 3 -> 2 (left size is half or 1 larger)

        for(final T key : treeMap.keySet()){

            if(leftTotal < halfWay){

                leftTotal += treeMap.get(key);
                left.add(key);

                // if added element just surpassed the threshold (done with duplicate ranges)
                // then append current element to right
                if(leftTotal > halfWay)
                    right.add(key);

            }
            else
                right.add(key);

        }

        return new Pair<List<T>, LinkedList<T>>(
                left,
                right
        );
    }

    // NOTE: pre-tested in RangeSort
    private <T extends Comparable<T>> TreeMap<T, Integer> rangeSort(@NotNull final List<T> toSort){

        final TreeMap<T, Integer> valuesAndCount = new TreeMap<T, Integer>();

        for(final T element : toSort){

            if(!valuesAndCount.containsKey(element))
                valuesAndCount.put(element, 1);
            else
                valuesAndCount.put(element, valuesAndCount.get(element) + 1);

        }

        return valuesAndCount;
    }

}
