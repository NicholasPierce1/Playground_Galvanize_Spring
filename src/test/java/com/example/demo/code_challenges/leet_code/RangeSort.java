package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RangeSort {

    @Test
    public void rangeSortTest(){

        Stream.<List<Integer>>of(
                Arrays.asList(5,4,3,2,1,0),
                Arrays.asList(3,2,1),
                Arrays.asList(3,3,3),
                Arrays.asList(3,1,1),
                Arrays.asList(2,3,1),
                Arrays.asList(3,3,2,2,5,5,0,0,1)
        ).map(
                (list) -> {
                    return new Pair<List<Integer>, List<Integer>>(
                            list,
                            list.stream().sorted().collect(Collectors.toList())
                    );
                }
        ).forEach(
                (listPair) -> {

                    Assertions.assertEquals(
                            listPair.getValue1(),
                            this.rangeSort(listPair.getValue0())
                    );

                }
        );

    }

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
