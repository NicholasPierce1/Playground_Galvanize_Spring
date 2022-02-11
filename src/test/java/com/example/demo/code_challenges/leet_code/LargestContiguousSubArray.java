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

        List<Integer> currentValues = new ArrayList<Integer>();

        for(final int currentNum : data) {

            if (currentValues.size() == 0)
                // first value
                currentValues.add(currentNum);
            else {

                final List<Integer> newValues = new ArrayList<Integer>();

                for(final Integer number: currentValues) {

                    final int localMax = Math.max(number + currentNum, currentNum);

                    newValues.add(localMax);

                    if (localMax > currentMax)
                        currentMax = localMax;

                }

                // reassign list reference to hold new prospective values/ sub-arrays
                currentValues = newValues;

            }
        }

        return currentMax;

    }

}
