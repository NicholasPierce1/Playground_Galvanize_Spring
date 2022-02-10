package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MedianSearchNonSort {

    @Test
    public void test(){
        this.getTestData().forEach(
                (testData) -> System.out.println(testData.getValue2())
        );
    }

    @Test
    public void bubbleSearchUpTest(){

        Stream.<Triplet<Integer, Integer, Integer>>of(
                new Triplet<Integer, Integer, Integer>(-1, 10, 5),
                new Triplet<Integer, Integer, Integer>(-1,1,0),
                new Triplet<Integer, Integer, Integer>(0,1,1),
                new Triplet<Integer, Integer, Integer>(-1,0,0),
                new Triplet<Integer, Integer, Integer>(1,1,-1),
                new Triplet<Integer, Integer, Integer>(0,2,1)
        ).forEach(
                (testData) -> {
                    Assertions.assertEquals(
                            testData.getValue2(),
                            this.bubbleSearchUp(testData.getValue0(), testData.getValue1())
                    );
                }
        );

    }

    @Test
    public void bubbleSearchDownTest(){

        Stream.<Triplet<Integer, Integer, Integer>>of(
                new Triplet<Integer, Integer, Integer>(0, 10, 5),
                new Triplet<Integer, Integer, Integer>(0,1,0),
                new Triplet<Integer, Integer, Integer>(0,0,-1),
                new Triplet<Integer, Integer, Integer>(0,2,1)
        ).forEach(
                (testData) -> {
                    Assertions.assertEquals(
                            testData.getValue2(),
                            this.bubbleSearchDown(testData.getValue0(), testData.getValue1())
                    );
                }
        );

    }

    @Test
    public void getRelativePositionForElementTest(){

        Stream.<Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>>of(
                // one jump
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1,3,5},
                        4,
                        new Triplet<Integer, Integer, Boolean>(2, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1,3,5},
                        2,
                        new Triplet<Integer, Integer, Boolean>(1, null, false)
                ),
                // two or more jumps
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1, 3, 5, 10, 15, 20, 25},
                        4,
                        new Triplet<Integer, Integer, Boolean>(2, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1, 3, 5, 10, 15, 20, 25},
                        21,
                        new Triplet<Integer, Integer, Boolean>(6, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1, 3, 5, 10, 15, 20, 25},
                        16,
                        new Triplet<Integer, Integer, Boolean>(5, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1, 3, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50},
                        42,
                        new Triplet<Integer, Integer, Boolean>(10, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1, 3, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50},
                        2,
                        new Triplet<Integer, Integer, Boolean>(1, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1, 3, 5, 10},
                        12,
                        new Triplet<Integer, Integer, Boolean>(4, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{1, 3, 5, 10},
                        0,
                        new Triplet<Integer, Integer, Boolean>(0, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{},
                        0,
                        new Triplet<Integer, Integer, Boolean>(0, null, false)
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{-5,-5,-4,0,1,2,2,2,3},
                        -5,
                        new Triplet<Integer, Integer, Boolean>(2, 0, true) // 2 - 0 = 2
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{-10,-10,-5,-5,-4,0,1,2,2,2,3},
                        -5,
                        new Triplet<Integer, Integer, Boolean>(2, 2, true) // 4 - 2 = 2
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{-5,-5,-5,-5,-4,0,1,2,2,2,3},
                        -5,
                        new Triplet<Integer, Integer, Boolean>(4, 0, true) // 4 - 0 = 5
                ),
                new Triplet<Integer[], Integer, Triplet<Integer, Integer, Boolean>>(
                        new Integer[]{-10,-10,-10,-10,-5,-5,-5,-4,0,1,2,2,2,3},
                        -5,
                        new Triplet<Integer, Integer, Boolean>(3, 4, true) // 7 - 4 = 3
                )
        ).forEach(
                (testData) -> {

                    final Triplet<Integer, Integer, Boolean> results = this.<Integer>getRelativePositionForElement(
                            testData.getValue1(),
                            testData.getValue0()
                    );

                    // boolean
                    Assertions.assertEquals(
                           testData.getValue2().getValue2(),
                           results.getValue2()
                    );

                    // index or range
                    Assertions.assertEquals(
                            testData.getValue2().getValue0(),
                            results.getValue0()
                    );

                    // lowerBound
                    Assertions.assertEquals(
                            testData.getValue2().getValue1(),
                            results.getValue1()
                    );

                }
        );

    }

    @Test
    public void getNextGreatestValueIndexTest(){

        Stream.<Triplet<Integer[], Integer, Integer>>of(
                new Triplet<Integer[], Integer, Integer>(
                    new Integer[]{1,1,2,2,2,3,3},
                    2,
                    5
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{1,1,2,2,2,3,3,4,4},
                        2,
                        5
                ),
                new Triplet<Integer[], Integer, Integer>(
                    new Integer[]{1,1,1},
                    1,
                    3
                ),
                new Triplet<Integer[], Integer, Integer>(
                    new Integer[]{0,1,1},
                    1,
                    3
                ),
                new Triplet<Integer[], Integer, Integer>(
                    new Integer[]{-5,-4,0,1,2,2,2,3},
                    7,
                    8
                ),
                new Triplet<Integer[], Integer, Integer>(
                    new Integer[]{-5,-4,0,1,2,2,2,3},
                    -5,
                    1
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{-5,-5,-4,0,1,2,2,2,3},
                        -5,
                        2
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{-10,-10,-5,-5,-4,0,1,2,2,2,3},
                        -5,
                        4
                ),
                new Triplet<Integer[], Integer, Integer>(
                    new Integer[]{-5,-4,0,1,2,2,2,3},
                    -7,
                    0
                )
        ).forEach(
                (testData) -> {

                    Assertions.assertEquals(
                            testData.getValue2(),
                            this.<Integer>getNextGreatestValueIndex(
                                    testData.getValue1(),
                                    testData.getValue0()
                            )
                    );

                }
        );

    }

    @Test
    public void getNextLowestValueIndexTest(){

        Stream.<Triplet<Integer[], Integer, Integer>>of(
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{1,1,2,2,2,3,3},
                        2,
                        2
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{1,1,2,2,2,3,3,4,4},
                        2,
                        2
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{1,1,1},
                        1,
                        0
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{0,1,1},
                        1,
                        1
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{-5,-4,0,1,2,2,2,3},
                        7,
                        8
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{-5,-4,0,1,2,2,2,3},
                        -5,
                        0
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{-5,-5,-4,0,1,2,2,2,3},
                        -5,
                        0
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{-10,-10,-5,-5,-4,0,1,2,2,2,3},
                        -5,
                        2
                ),
                new Triplet<Integer[], Integer, Integer>(
                        new Integer[]{-5,-4,0,1,2,2,2,3},
                        -7,
                        0
                )
        ).forEach(
                (testData) -> {

                    Assertions.assertEquals(
                            testData.getValue2(),
                            this.<Integer>getNextLowestValueIndex(
                                    testData.getValue1(),
                                    testData.getValue0()
                            )
                    );

                }
        );

    }

    @Test
    public void findMedianForArrayTest(){

        this.getArraySearchStateTestData().forEach(
                (testData) -> {

                    Integer lowerBoundOrMedian;
                    Integer upperBoundOrNull = null;

                    final int lengthSum = testData.getValue0().getPerspective().length
                            + testData.getValue0().getToSearch().length;

                    if(lengthSum % 2 == 0){ // even
                        lowerBoundOrMedian = lengthSum / 2 - 1;
                        upperBoundOrNull = lengthSum / 2;
                    }
                    else // odd
                        lowerBoundOrMedian = lengthSum / 2;

                    System.out.printf(
                            "target indexes:\n\tsum:%d\n\tlowerBoundOrMedian:%d\n\tupperBoundOrNull:%s\n",
                            lengthSum,
                            lowerBoundOrMedian,
                            upperBoundOrNull
                    );

                    while(testData.getValue0().isStillSearching()){

                        final Double iterationResult =
                                this.findMedianForArray(
                                        testData.getValue0(),
                                        lowerBoundOrMedian,
                                        upperBoundOrNull,
                                        false
                                );

                        if(iterationResult == null)
                            Assertions.assertTrue(
                                    testData.getValue0().isStillSearching()
                                    ||
                                    testData.getValue1() == null // isStillSearching will be false on last run
                            );
                        else
                            Assertions.assertEquals(testData.getValue1(),iterationResult);

                    }

                }
        );

    }

    @Test
    public void findMedianForTwoSortedArraysTest(){

        this.getArraySearchStateTestData().map(
                (testDataPair) -> {

                    // sort list
                    final List<Integer> sortedList = new ArrayList<Integer>();
                    sortedList.addAll(Arrays.asList(testDataPair.getValue0().getPerspective()));
                    sortedList.addAll(Arrays.asList(testDataPair.getValue0().getToSearch()));
                    sortedList.sort(Comparator.<Integer>naturalOrder());

                    final int size = sortedList.size();

                    // extract median
                    Double median = sortedList.size() % 2 == 0 ?
                            // even (size = 4 --> median is ((size/2)+(size/2 - 1))/2
                            (sortedList.get(size/2) + sortedList.get(size/2 - 1)) / 2.0
                            :
                            // odd (size = 3 --> median is (size/2)
                            (sortedList.get(size/2));

                    return new Pair<ArraySearchState, Double>(
                            testDataPair.getValue0(),
                            median
                    );

                }
        ).forEach(
                (testData) -> {

                    System.out.println("median: " + testData.getValue1());

                    Assertions.assertEquals(
                            testData.getValue1().doubleValue(),
                            this.findMedianFromTwoSortedArrays(
                                    testData.getValue0().getPerspective(),
                                    testData.getValue0().getToSearch()
                            )
                    );

                }
        );

    }

    private Stream<Pair<ArraySearchState, Double>> getArraySearchStateTestData(){
        return
                Stream.<Pair<ArraySearchState, Double>>of(
                        // odd - contains median
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10}, // perspective
                                        new Integer[]{2,8} // toSearch
                                ),
                                5d
                        ),
                        // odd - contains median after 1 jump up
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10,15,20}, // perspective
                                        new Integer[]{21,22} // toSearch
                                ),
                                15d
                        ),
                        // odd - contains median after 1 jump down
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10,15,20}, // perspective
                                        new Integer[]{2,3} // toSearch
                                ),
                                5d
                        ),
                        // odd - contains median after 2 jumps up (edge)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10,15,20}, // perspective
                                        new Integer[]{21,22,23,24} // toSearch
                                ),
                                20d
                        ),
                        // odd - contains median after 2 jumps down (edge)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10,15,20}, // perspective
                                        new Integer[]{-5,-4,-3,-2} // toSearch
                                ),
                                1d
                        ),
                        // odd - no median after 2 jumps up
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10,15,20}, // perspective
                                        new Integer[]{19,22,23,24} // toSearch
                                ),
                                null
                        ),
                        // odd - no median after 2 jumps down (edge)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10,15}, // perspective
                                        new Integer[]{-4,-3,-2,-1,0} // toSearch
                                ),
                                null
                        ),
                        // odd - no median after 2 jumps up (edge)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{5,10,15,20}, // perspective
                                        new Integer[]{21,22,23,24,25} // toSearch
                                ),
                                null
                        ),
                        // odd - duplicates: median after no jumps
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10}, // perspective
                                        new Integer[]{1,5,5,5} // toSearch
                                ),
                                5d
                        ),
                        // even - no duplicated: median after no jumps & 2nd median found
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10}, // perspective
                                        new Integer[]{11} // toSearch
                                ),
                                7.5
                        ),
                        // even - no duplicated: median after no jumps & 2nd median not found (lowerBound)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10}, // perspective
                                        new Integer[]{9} // toSearch
                                ),
                                5d
                        ),
                        // even - no duplicated: median after no jumps & 2nd median not found (upperBound)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10}, // perspective
                                        new Integer[]{3} // toSearch
                                ),
                                5d
                        ),
                        // even - no duplicated: median after no jumps & 2nd median not found (upper edge)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10}, // perspective
                                        new Integer[]{11,12,13} // toSearch
                                ),
                                10d
                        ),
                        // even - no duplicated: median after no jumps & 2nd median not found (lower edge)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10}, // perspective
                                        new Integer[]{-2,-1,0} // toSearch
                                ),
                                1d
                        ),
                        // even - no duplicated: median 1 found & 2nd median not found (already visited - upperBound)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10,15,20,25,30,35,40}, // perspective (20-30-25 -> 20)
                                        new Integer[]{26,41,42,43,44} // toSearch
                                ),
                                30d
                        ),
                        // even - no duplicated: median 1 found & 2nd median not found (already visited - lowerBound)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10,15,20,25,30,35,40}, // perspective (20-10-15 -> 20)
                                        new Integer[]{2,3,16} // toSearch
                                ),
                                15d
                        ),
                        // odd - duplicated: median after no jumps
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,5,10}, // perspective
                                        new Integer[]{1,5,5,5,10,11} // toSearch
                                ),
                                5d
                        ),
                        // odd - duplicated: median
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{1,2,5}, // perspective
                                        new Integer[]{-1,0,1,5,5,5,10,11} // toSearch
                                ),
                                5d
                        ),
                        // odd - duplicated: median after no jumps
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{5,6,7}, // perspective
                                        new Integer[]{1,5,5,5,10,11} // toSearch
                                ),
                                5d
                        ),
                        // odd - duplicate: median after jump/s
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{5,6}, // perspective
                                        new Integer[]{5,10,11} // toSearch
                                ),
                                6d
                        ),
                        // odd - duplicate: median after jump/s
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{4,5,6}, // perspective
                                        new Integer[]{1,2,3,4,5,10} // toSearch
                                ),
                                4d
                        ),
                        // odd - duplicated: median after no jumps
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{5}, // perspective
                                        new Integer[]{1,5,10,11,12,13} // toSearch
                                ),
                                null
                        ),
                        // odd - duplicated: median after no jumps
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{11}, // perspective
                                        new Integer[]{1,5,10,10,10,11,12,13} // toSearch
                                ),
                                null
                        ),
                        // even - duplicated: median 1 and 2 found (swallow)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{5}, // perspective
                                        new Integer[]{1,5,10} // toSearch
                                ),
                                5d
                        ),
                        // even - duplicated: median 1 and 2 found (lowerBound)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{5,6}, // perspective
                                        new Integer[]{1,5,10,11} // toSearch
                                ),
                                5.5
                        ),
                        // even - duplicated: median 1 and 2 found (upperBound)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{4,5,6}, // perspective
                                        new Integer[]{0,1,2,5,10} // toSearch (0,1,2,4,5,5,6,10)
                                ),
                                4.5
                        ),
                        // even - duplicated: median 1 but not 2 (lower)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{5}, // perspective
                                        new Integer[]{0,5,10,11,12} // toSearch
                                ),
                                5d
                        ),
                        // even - duplicated: median 1 but not 2 (upper)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{5}, // perspective
                                        new Integer[]{0,1,5} // toSearch
                                ),
                                5d
                        ),
                        // even - duplicated: median 1 but not 2 (upper)
                        new Pair<ArraySearchState, Double>(
                                new ArraySearchState(
                                        new Integer[]{2,2,3}, // perspective
                                        new Integer[]{0,1,2} // toSearch
                                ),
                                2d
                        )
                );
    }

    // start of worker methods

    // upperBound is inclusive
    private int bubbleSearchUp(final int lowerBound, final int upperBound){

        if(lowerBound == upperBound)
            return -1;

        final int firstAvailableBound = lowerBound + 1;

        return firstAvailableBound + (upperBound - firstAvailableBound) / 2;

    }

    // lowerBound is inclusive
    private int bubbleSearchDown(final int lowerBound, final int upperBound){

        if(lowerBound == upperBound)
            return -1;

        final int firstAvailableBound = upperBound - 1;

        return firstAvailableBound - (firstAvailableBound - lowerBound) / 2;

    }


    private List<Triplet<Integer[], Integer[], Double>> getTestData(){

        return Stream.<Pair<Integer[], Integer[]>>of(
                new Pair<Integer[], Integer[]>(
                        new Integer[]{0,5,10},
                        new Integer[]{2,7,12}
                ),
                new Pair<Integer[], Integer[]>(
                        new Integer[]{0,10},
                        new Integer[]{1,2,3}
                ),
                new Pair<Integer[], Integer[]>(
                        new Integer[]{0,5,10,15,20,25,30,35,40,45},
                        new Integer[]{6,12,18}
                ),
                new Pair<Integer[], Integer[]>(
                        new Integer[]{0,1,1},
                        new Integer[]{2,2,4}
                ),
                new Pair<Integer[], Integer[]>(
                        new Integer[]{25,30,31,32},
                        new Integer[]{0,18,23,25}
                )
        ).map(
                (pair) -> {

                    // sort list
                    final List<Integer> sortedList = new ArrayList<Integer>();
                    sortedList.addAll(Arrays.asList(pair.getValue0()));
                    sortedList.addAll(Arrays.asList(pair.getValue1()));
                    sortedList.sort(Comparator.<Integer>naturalOrder());

                    final int size = sortedList.size();

                    // extract median
                    Double median = sortedList.size() % 2 == 0 ?
                            // even (size = 4 --> median is ((size/2)+(size/2 - 1))/2
                            (sortedList.get(size/2) + sortedList.get(size/2 - 1)) / 2.0
                            :
                            // odd (size = 3 --> median is (size/2)
                            (sortedList.get(size/2));

                    return new Triplet<Integer[], Integer[], Double>(
                            pair.getValue0(),
                            pair.getValue1(),
                            median
                    );

                }
        ).collect(Collectors.toList());

    }

    private <T extends Comparable<T>> Triplet<Integer, Integer, Boolean> getRelativePositionForElement(@NotNull final T value, @NotNull final T[] data){

        // edge case: data is empty
        if(data.length == 0)
            return new Triplet<Integer, Integer, Boolean>(
                    0,
                    null,
                    false
            );

        final HashSet<Integer> visited = new HashSet<Integer>();

        int upperBound = data.length - 1;
        int lowerBound = -1; // initialized

        int currentSearchIndex = this.bubbleSearchUp(lowerBound++, upperBound);
        int compareResult;


        do{

            // add new element to visited
            visited.add(currentSearchIndex);

            // compare new element to visited
            compareResult = value.compareTo(data[currentSearchIndex]);

            if(compareResult > 0) // value is greater -> bubble search up
                currentSearchIndex = this.bubbleSearchUp(
                        (lowerBound = currentSearchIndex),
                        upperBound
                );

            else if(compareResult < 0) // value is less than -> bubble search down
                currentSearchIndex = this.bubbleSearchDown(
                        lowerBound,
                        (upperBound = currentSearchIndex)
                );

            else {
                // duplicate encountered
                // return the difference/range between the next greatest and lowest elements

                lowerBound = this.getNextLowestValueIndex(data[currentSearchIndex], data);

                return new Triplet<Integer, Integer, Boolean>(
                        this.getNextGreatestValueIndex(data[currentSearchIndex], data) - lowerBound,
                        lowerBound,
                        true
                );
            }

            // guard clause against edge cases of when bounds are equal
            if(currentSearchIndex == -1) // last index either searching up or down
                return compareResult > 0 ?
                        /*

                        upperBound + 1 --> greater than all values in data, positioned after last index
                        upperBound --> less than all values in data, positioned at first index

                        can be lower bound too (interchangeable)

                         */
                        new Triplet<Integer, Integer, Boolean>(
                                upperBound + 1,
                                null,
                                false
                        ) :
                        new Triplet<Integer, Integer, Boolean>(
                                upperBound,
                                null,
                                false
                        );

        }while(!visited.contains(currentSearchIndex));

        // currentSearchIndex points at visited element

//        return compareResult < 0 ?
//                // lowerBound was last index that pointed to an element that was greater than value
//                lowerBound + 1
//                :
//                // upperBound was last index that pointed to an element that was lower than value
//                upperBound - 1;

        return new Triplet<Integer, Integer, Boolean>(
                lowerBound + 1,
                null,
                false
        );

    }

    private <T extends Comparable<T>> int getNextGreatestValueIndex(@NotNull final T value, @NotNull final T[] data){

        if(data.length == 0)
            throw new RuntimeException("the data array cannot be empty");

        final HashSet<Integer> visited = new HashSet<Integer>();

        int upperBound = data.length - 1;
        int lowerBound = -1; // initialized

        int currentSearchIndex = this.bubbleSearchUp(lowerBound++, upperBound);
        int compareResult;

        do{

            // add new element to visited
            visited.add(currentSearchIndex);

            // compare new element to visited
            compareResult = value.compareTo(data[currentSearchIndex]);

            if(compareResult >= 0) // target element is greater than or equal to current element -> bubble search up
                currentSearchIndex = this.bubbleSearchUp(
                        (lowerBound = currentSearchIndex),
                        upperBound
                );

            else // target element is less than current element -> bubble search down
                currentSearchIndex = this.bubbleSearchDown(
                        lowerBound,
                        (upperBound = currentSearchIndex)
                );

            // guard clause against edge cases of when bounds are equal
            if(currentSearchIndex == -1) // last index either searching down
                // [2,2,2], find 2 --> 3
                return compareResult >= 0 ?
                        /*

                        upperBound + 1 --> greater than or equal to all values in data, positioned after last index
                        upperBound --> less than all values in data, positioned at first index

                        can be lower bound too (interchangeable)

                         */
                       upperBound + 1 : upperBound;

//            System.out.printf("currentSearchIndex: %d -- ub: %d -- lb: %d\n",
//                    currentSearchIndex,
//                    upperBound,
//                    lowerBound);
        }while(!visited.contains(currentSearchIndex));

        // currentSearchIndex points at visited element
//        System.out.println("out here");
        return lowerBound + 1;
    }

    private <T extends Comparable<T>> int getNextLowestValueIndex(@NotNull final T value, @NotNull final T[] data){

        if(data.length == 0)
            throw new RuntimeException("the data array cannot be empty");

        final HashSet<Integer> visited = new HashSet<Integer>();

        int upperBound = data.length - 1;
        int lowerBound = -1; // initialized

        int currentSearchIndex = this.bubbleSearchUp(lowerBound++, upperBound);
        int compareResult;

        do{

            // add new element to visited
            visited.add(currentSearchIndex);

            // compare new element to visited
            compareResult = value.compareTo(data[currentSearchIndex]);

            if(compareResult <= 0) // target element is less than or equal to current element -> bubble search down
                currentSearchIndex = this.bubbleSearchDown(
                        lowerBound,
                        (upperBound = currentSearchIndex)
                );

            else // target element is greater than current element -> bubble search up
                currentSearchIndex = this.bubbleSearchUp(
                        (lowerBound = currentSearchIndex),
                        upperBound
                );

            // guard clause against edge cases of when bounds are equal
            if(currentSearchIndex == -1) // last index either searching down
                // [2,2,2], find 2 --> 3
                return compareResult <= 0 ?
                        /*

                        upperBound + 1 --> greater than or equal to all values in data, positioned after last index
                        upperBound --> less than all values in data, positioned at first index

                        can be lower bound too (interchangeable)

                         */
                        upperBound : upperBound + 1;

//            System.out.printf("currentSearchIndex: %d -- ub: %d -- lb: %d\n",
//                    currentSearchIndex,
//                    upperBound,
//                    lowerBound);
        }while(!visited.contains(currentSearchIndex));

        // currentSearchIndex points at visited element
//        System.out.println("out here");
        return lowerBound + 1;
    }

    private Double findMedianForArray(
            @NotNull final ArraySearchState arraySearchState,
            @NotNull final Integer lowerBoundOrMedianIndex,
            @Nullable final Integer upperBoundIndex,
            final boolean isRecurse){

        if(arraySearchState.getCurrentSearchIndex() == -1) { // first pass

            // set current index and lower bound
            arraySearchState.setCurrentSearchIndex(
                    this.bubbleSearchUp(
                            arraySearchState.getLowerBound(),
                            arraySearchState.getUpperBound()
                    )
            );

            arraySearchState.setLowerBound(0);

            // append current index to visited
            arraySearchState.getVisited().add(arraySearchState.getCurrentSearchIndex());

        }

        // extract current element from perspective array
        final Integer element = arraySearchState.getValueAtCurrentIndex();

        Integer resultLowerBoundIndex = null;
        Integer resultUpperBoundIndex = null;

        // acquire relative positioning in search array with element
        /*

        ex:
        new Integer[]{-5,-5,-5,-5,-4,0,1,2,2,2,3},
        -5,
        new Triplet<Integer, Integer, Boolean>(4, 0, true) // 4 - 0 = 5

        new Integer[]{-10,-10,-5,-5,-4,0,1,2,2,2,3},
        -5,
        new Triplet<Integer, Integer, Boolean>(2, 2, true) // 4 - 2 = 2

        new Integer[]{1,3,5},
        2,
        new Triplet<Integer, Integer, Boolean>(1, null, false)

        value0 -> range || relative value
        value1 -> lower bound || null
        value2 -> if duplicates exist

        ex: [-5] && new Integer[]{-5,-5,-5,-5,-4,0,1,2,2,2,3}
        lb_index = currentIndex + lower bound --> 0 + 0 = 0
        ub_index = currentIndex + lower bound + range --> 0 + 0 + 4 = 4

        ex: [-5,-5*] && new Integer[]{-10,-10,-5,-5,-4,0,1,2,2,2,3}
        lb_index = 1 + 2 = 3
        up_index = 1 + 2 + 2 = 5

         */
        Triplet<Integer, Integer, Boolean> result =
                this.getRelativePositionForElement(element, arraySearchState.getToSearch());

        if(result.getValue2()){
            // duplicates
            // calculate range
            resultLowerBoundIndex = arraySearchState.getCurrentSearchIndex() + result.getValue1();
            resultUpperBoundIndex = resultLowerBoundIndex + result.getValue0();
        }
        else{
            // first value is not range but relative position in other array
            resultLowerBoundIndex = arraySearchState.getCurrentSearchIndex() + result.getValue0();
        }

        // validation
        if(result.getValue2()){
            // duplicates

            // 6 | ? -> may be null
            // 3-9, 3-6, 6-7
            boolean lowerBoundFound =
                    lowerBoundOrMedianIndex >= resultLowerBoundIndex && lowerBoundOrMedianIndex <= resultUpperBoundIndex;

            if(upperBoundIndex != null) {

                // 6 | 7
                // 3-9, 6-7, 7-8
                boolean upperBoundFound =
                        upperBoundIndex >= resultLowerBoundIndex && upperBoundIndex <= resultUpperBoundIndex;

                if (lowerBoundFound && upperBoundFound) {

                    arraySearchState.setStillSearching(false);
                    arraySearchState.setAllValuesFound(true);

                    return element.doubleValue();

                } else if (lowerBoundFound || upperBoundFound) {
                    // lower/upper bound found:

                    if (isRecurse)
                        return element.doubleValue();
                    else
                        return this.searchMedianRecurseHelper(
                                arraySearchState,
                                lowerBoundOrMedianIndex,
                                upperBoundIndex,
                                element.doubleValue(),
                                lowerBoundFound
                        );

                }

                // no bound found || only one bound exists
                if (!isRecurse) {
                    // no bound found but not recursive - determine if bubble up or down
                    updateArrayStateForBubbleUpOrDown(
                            arraySearchState,
                            resultLowerBoundIndex,
                            lowerBoundOrMedianIndex
                    );
                }

                return null;
            }
            else{ // only one bound target (lowerBoundOrMedian)

                if(lowerBoundFound){
                    arraySearchState.setStillSearching(false);
                    arraySearchState.setAllValuesFound(true);
                    return element.doubleValue();
                }
                else{

                    // adjust search state for subsequent search
                    this.updateArrayStateForBubbleUpOrDown(
                            arraySearchState,
                            resultLowerBoundIndex,
                            lowerBoundOrMedianIndex
                    );

                    return null;

                }

            }
        }
        else{ // no duplicates -- just relative value

            if(upperBoundIndex == null) { // only 1 target (lowerBoundOrMedianIndex)
                if (resultLowerBoundIndex.equals(lowerBoundOrMedianIndex)) {

                    arraySearchState.setStillSearching(false);
                    arraySearchState.setAllValuesFound(true);

                    return element.doubleValue();

                }

                // adjust search state for subsequent search
                this.updateArrayStateForBubbleUpOrDown(
                        arraySearchState,
                        resultLowerBoundIndex,
                        lowerBoundOrMedianIndex
                );

                return null;
            }
            else{ // lower and upper bound targets

                // lower or upper found
                if(resultLowerBoundIndex.equals(lowerBoundOrMedianIndex) || resultLowerBoundIndex.equals(upperBoundIndex)) {

                    if (isRecurse)
                        return element.doubleValue();

                    return this.searchMedianRecurseHelper(
                            arraySearchState,
                            lowerBoundOrMedianIndex,
                            upperBoundIndex,
                            element.doubleValue(),
                            resultLowerBoundIndex.equals(lowerBoundOrMedianIndex)
                    );
                }
                else {

                    if(!isRecurse)
                    // no bound found
                    // adjust search state for subsequent search
                    this.updateArrayStateForBubbleUpOrDown(
                            arraySearchState,
                            resultLowerBoundIndex,
                            lowerBoundOrMedianIndex
                    );

                    return null;

                }

            }

        }

    }

    private Double searchMedianRecurseHelper(
            @NotNull final ArraySearchState arraySearchState,
            @NotNull final Integer lowerBound,
            @NotNull final Integer upperBound,
            @NotNull final Double elementFound,
            final boolean lowerBoundFound) {

        // searching will be done after this recursive step
        arraySearchState.setStillSearching(false);

        // lower bound & able to do search on next highest index (exist and not visited before)
        if(
                lowerBoundFound &&
                arraySearchState.getPerspective().length > arraySearchState.getCurrentSearchIndex() + 1 &&
                !arraySearchState.getVisited().contains(arraySearchState.getCurrentSearchIndex() + 1)
        )
            arraySearchState.setCurrentSearchIndex(arraySearchState.getCurrentSearchIndex() + 1);
        // upper bound & able to do search on next lowest index (exist and not visited before)
        else if(
                (arraySearchState.getCurrentSearchIndex() - 1) >= 0 &&
                !arraySearchState.getVisited().contains(arraySearchState.getCurrentSearchIndex() - 1)
        )
            arraySearchState.setCurrentSearchIndex(arraySearchState.getCurrentSearchIndex() - 1);
        else
            return elementFound;


        final Double recurse =
                this.findMedianForArray(
                        arraySearchState,
                        lowerBound,
                        upperBound,
                        true
                );

        if(recurse != null) {
            arraySearchState.setAllValuesFound(true);
            return (recurse + elementFound) / 2;
        }
        else
            return elementFound;

    }

    private void updateArrayStateForBubbleUpOrDown(
            @NotNull final ArraySearchState arraySearchState,
            @NotNull final Integer resultLowerBound,
            @NotNull final Integer lowerBound){

        if(lowerBound < resultLowerBound){ // index is too high:
            // bubble down

            arraySearchState.setUpperBound(arraySearchState.getCurrentSearchIndex());

            arraySearchState.setCurrentSearchIndex(
                    this.bubbleSearchDown(
                            arraySearchState.getLowerBound(),
                            arraySearchState.getUpperBound()
                    )
            );

        }
        else { // index is too low
            // bubble up

            arraySearchState.setLowerBound(arraySearchState.getCurrentSearchIndex());

            arraySearchState.setCurrentSearchIndex(
                    this.bubbleSearchUp(
                            arraySearchState.getLowerBound(),
                            arraySearchState.getUpperBound()
                    )
            );


        }

        if(
                arraySearchState.getCurrentSearchIndex() == -1
                        ||
                arraySearchState.getVisited().contains(arraySearchState.getCurrentSearchIndex())
        )
            arraySearchState.setStillSearching(false);

        // new index to search -> add to visited
        arraySearchState.getVisited().add(arraySearchState.getCurrentSearchIndex());

    }

    private double findMedianFromTwoSortedArrays(
            @NotNull final Integer[] array1,
            @NotNull final Integer[] array2) {

        /*
        it is possible that both arrays can return the same median (duplicate ranges)
        the median will either:
        1) be the result of two distinct values
        2) be the same value (which ArraySearchState "allValuesFound"
           would be true & order of operations would prevent this)

        [0,1,2]
        [2,2,3]
         */
        final Deque<Double> medians = new ArrayDeque<Double>();

        final Integer lowerBoundOrMedian;
        final Integer upperBoundOrNull;

        final int lengthSum = array1.length + array2.length;

        final boolean isEven = lengthSum % 2 == 0;

        if(isEven){ // even
            lowerBoundOrMedian = lengthSum / 2 - 1;
            upperBoundOrNull = lengthSum / 2;
        }
        else { // odd
            lowerBoundOrMedian = lengthSum / 2;
            upperBoundOrNull = null;
        }

        boolean continueArray1 = array1.length != 0;
        boolean continueArray2 = array2.length != 0;

        Double result = null;

        final ArraySearchState arraySearchState1 =
                new ArraySearchState(
                        array1,
                        array2
                );

        final ArraySearchState arraySearchState2 =
                new ArraySearchState(
                        array2,
                        array1
                );

        while(continueArray1 || continueArray2){

            if(continueArray1){

                result =
                        this.findMedianForArray(
                                arraySearchState1,
                                lowerBoundOrMedian,
                                upperBoundOrNull,
                                false
                        );

                /*
                three cases:
                1) no value found in current increment [result is null & still searching]
                2) all values found in current increment [result is not null, all values found, and not searching]
                3) 1 value found total [result is not null, not searching, and not all values found]
                4) no values found [result is null and not still searching]
                 */

                // can be (1) || (4)
                if(result == null)
                    continueArray1 = arraySearchState1.isStillSearching();
                else if(arraySearchState1.areAllValuesFound()) { // (2)

                    if(arraySearchState1.isStillSearching())
                        throw new RuntimeException("(2): this should never happen");

                    return result;
                }
                else { // (3)

                    if(arraySearchState1.isStillSearching())
                        throw new RuntimeException("(3): this should never happen");

                    medians.push(result);

                    continueArray1 = false;

                }

            }

            if(continueArray2){

                result =
                        this.findMedianForArray(
                                arraySearchState2,
                                lowerBoundOrMedian,
                                upperBoundOrNull,
                                false
                        );

                /*
                three cases:
                1) no value found in current increment [result is null & still searching]
                2) all values found in current increment [result is not null, all values found, and not searching]
                3) 1 value found total [result is not null, not searching, and not all values found]
                4) no values found [result is null and not still searching]
                 */

                // can be (1) || (4)
                if(result == null)
                    continueArray2 = arraySearchState2.isStillSearching();
                else if(arraySearchState2.areAllValuesFound()) { // (2)

                    if(arraySearchState2.isStillSearching())
                        throw new RuntimeException("(2): this should never happen");

                    return result;
                }
                else { // (3)

                    if(arraySearchState2.isStillSearching())
                        throw new RuntimeException("(3): this should never happen");

                    medians.push(result);

                    continueArray2 = false;

                }

            }

        }

        // both search arrays had a case 3
        // case 1: still be searching
        // case 2: returned already
        // case 4: only 1 would have a case 4 --> the other would have a case 2

        if(medians.size() != 2)
            throw new RuntimeException(
                    String.format("Two medians were expected - size(%d)", medians.size())
            );

        return (medians.pop() + medians.pop()) / 2;
    }

    private static final class ArraySearchState{

        Integer upperBound, lowerBound, currentSearchIndex = -1;

        final Integer[] perspective, toSearch;

        final HashSet<Integer> visited = new HashSet<Integer>();

        boolean stillSearching = true, allValuesFound = false;

        ArraySearchState(Integer[] perspective, Integer[] toSearch){

            this.perspective = perspective;
            this.toSearch = toSearch;
            this.upperBound = this.perspective.length - 1; // inclusive
            this.lowerBound = -1; // initialized

        }

        public HashSet<Integer> getVisited() {
            return visited;
        }

        public Integer getLowerBound() {
            return lowerBound;
        }

        public Integer getUpperBound() {
            return upperBound;
        }

        public Integer[] getPerspective() {
            return perspective;
        }

        public Integer[] getToSearch() {
            return toSearch;
        }

        public Integer getCurrentSearchIndex() {
            return currentSearchIndex;
        }

        public boolean areAllValuesFound() {
            return allValuesFound;
        }

        public boolean isStillSearching() {
            return stillSearching;
        }

        public void setLowerBound(Integer lowerBound) {
            this.lowerBound = lowerBound;
        }

        public void setUpperBound(Integer upperBound) {
            this.upperBound = upperBound;
        }

        public void setCurrentSearchIndex(Integer currentSearchIndex) {
            this.currentSearchIndex = currentSearchIndex;
        }

        public void setStillSearching(boolean stillSearching) {
            this.stillSearching = stillSearching;
        }

        public void setAllValuesFound(boolean allValuesFound) {
            this.allValuesFound = allValuesFound;
        }

        public Integer getValueAtCurrentIndex(){
            return this.perspective[this.currentSearchIndex];
        }
    }
}
