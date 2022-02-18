package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

public class InverseSearch {

    @Test
    public void findValuesThatEqualSumTest(){

        /*
        1) can I solve it? (mentally)
        2) what logic did I use?
        3) how can I translate that logic into code? (data structures, control flow, recursion, etc.)
        4) <at end of implementation (3)> is this the best or good enough solution?
         */
        Stream.<Triplet<int[], Integer, ? extends Set<Integer>>>of(
                new Triplet<int[], Integer, Set<Integer>>(
                        new int[]{1,2,3,4,5,6,7,8,9,10},
                        12,
                        new HashSet<Integer>(){{add(5); add(7);}}
                ),
                new Triplet<int[], Integer, Set<Integer>>(
                        new int[]{4, 3, -1, 0, 9},
                        -1,
                        new HashSet<Integer>(){{add(-1); add(0);}}
                ),
                new Triplet<int[], Integer, Set<Integer>>(
                        new int[]{0, 5, 10, 15, -5, -10},
                        25,
                        new HashSet<Integer>(){{add(10); add(15);}}
                ),
                new Triplet<int[], Integer, Set<Integer>>(
                        new int[]{1,3,4,7},
                        5,
                        new HashSet<Integer>(){{add(1); add(4);}}
                )
        ).forEach(
                (testData) -> {

                    final Pair<Integer, Integer> actual =
                            this.findValuesThatEqualSum(testData.getValue0(), testData.getValue1());

                    Assertions.assertTrue(testData.getValue2().contains(actual.getValue0()));
                    Assertions.assertTrue(testData.getValue2().contains(actual.getValue1()));

                    Assertions.assertEquals(
                            testData.getValue1(),
                            actual.getValue1() + actual.getValue0()
                    );

                }
        );

    }

    private Pair<Integer, Integer> findValuesThatEqualSum(@NotNull final int[] data, final int target){

      /*
        given an int array, find a pair of two values who's sum will return target value
        O(n^2) 10 -> 100 | 100 -> 10000
        O(n)
     */

        // 12 | 5 | 12 - 5 = 7 |
        Set<Integer> visited = new HashSet<Integer>();

        for(final int num : data){ // n

            final int inverse = target - num; // 1

            if(visited.contains(inverse)) // 1
                return new Pair<Integer, Integer>(inverse, num); // 1

            visited.add(num); // 1

        }

      throw new RuntimeException("target value never found");

    }

}
