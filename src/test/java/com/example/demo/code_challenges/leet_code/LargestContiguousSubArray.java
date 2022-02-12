package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

public class LargestContiguousSubArray {

    @Test
    public void findLargestSubArrayTest(){

        Stream.<Pair<int[],Integer>>of(
                new Pair<int[], Integer>(
                        new int[]{1,2,3},
                        6
                ),
                new Pair<int[], Integer>(
                        new int[]{10,-5,-5},
                        10
                ),
                new Pair<int[], Integer>(
                        new int[]{-5,1,10},
                        11
                ),
                new Pair<int[], Integer>(
                        new int[]{10,-5,10},
                        15
                ),
                new Pair<int[], Integer>(
                        new int[]{10,-5,10, 7, -10, 0, -5, 2, 1, 6, -2, 2, 9, -1, -1, -1, 3, 0},
                        25
                )
        ).forEach(
                (testData) -> {

                    Assertions.assertEquals(
                            testData.getValue1(),
                            this.findLargestSubArray(testData.getValue0())
                    );

                }
        );

    }


    private int findLargestSubArray(@NotNull final int[] data){

        if(data == null || data.length == 0)
            throw new RuntimeException("data must not be null and retain at least one value");

        /*
        new int[]{-5,2,1,10}
        new int[]{10,-5,10}
         */

        int currentMax = data[0];

        int currentValue = currentMax;

        for(int i = 1; i < data.length; i++) {

                final int currentNum = data[i];

                final int localMax = Math.max(currentValue + currentNum, currentNum);

                if (localMax > currentMax)
                    currentMax = localMax;

                // reassign int value to hold new prospective sub-array sum
                currentValue = localMax;

            }

        return currentMax;

    }

}
