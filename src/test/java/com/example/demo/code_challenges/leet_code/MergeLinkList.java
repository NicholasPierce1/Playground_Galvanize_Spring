package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MergeLinkList {

    @Test
    public void mergeLinkedList(){

        // defines input stream
        final List<Pair<ArrayList<LinkedList<Integer>>, List<Integer>>> inputData = Stream.<Integer[][]>of(
                new Integer[][]{
                        new Integer[]{
                                1,4,7
                        },
                        new Integer[]{
                                2,5,8
                        },
                        new Integer[]{
                                3,6,9
                        }
                },
                new Integer[][]{}
        ).map(
                (intArrays) -> {
                    final ArrayList<LinkedList<Integer>> inputList = new ArrayList<>();

                    for(final Integer[] array : intArrays)
                        inputList.add(
                                new LinkedList<Integer>(Arrays.<Integer>asList(array))
                        );

                    return inputList;

                }
        )
        .map(
                (toSort) -> {
                    return new Pair<ArrayList<LinkedList<Integer>>, List<Integer>>(
                           toSort,
                           toSort.stream()
                           .map(
                                   (linkedList) -> {
                                       return Stream.<Integer>of(linkedList.<Integer>toArray(new Integer[0]));
                                   }
                           ).flatMap(
                                   (integer) -> integer
                           ).sorted()
                           .collect(Collectors.toList())
                    );
                }
        ).collect(Collectors.toList()); // covert to link list,  capture as list, convert to array for final input

        inputData.forEach(
                (inputPair) -> {

                    final List<Integer> actual = this.<Integer>mergeLinkedListSort(inputPair.getValue0());

                    // Assertions.assertEquals(inputPair.getValue1(), actual);

                    System.out.printf(
                            "\nExpected size: %d -- Actual size: %d\n",
                            inputPair.getValue1().size(),
                            actual.size()
                    );

                    Assertions.assertEquals(inputPair.getValue1().size(), actual.size());

                    for(int i = 0; i < actual.size(); i++){
                        System.out.printf(
                                "Expected value: %d -- Actual value: %d\n",
                                inputPair.getValue1().get(i),
                                actual.get(i)
                            );

                        Assertions.assertEquals(inputPair.getValue1().get(i), actual.get(i));
                    }

                }
        );

    }

    private <T extends Comparable<T>> List<T> mergeLinkedListSort(@NotNull final List<LinkedList<T>> toSort){

        final List<T> resultList = new ArrayList<T>();

        boolean allListsEmpty;

        do{

            allListsEmpty = true;

            final HashMap<T, LinkedList<T>> map = new HashMap<>();

            T minimumValue = null;

            for(final LinkedList<T> linkedList : toSort){

                if(linkedList.size() != 0) {

                    allListsEmpty = false;

                    map.put(linkedList.getFirst(), linkedList);

                    if(minimumValue == null || linkedList.getFirst().compareTo(minimumValue) < 0)
                        minimumValue = linkedList.getFirst();

                }

            }

            // append minimum value to result and remove top-most element
            if(!allListsEmpty) {
                resultList.add(minimumValue);
                map.get(minimumValue).removeFirst();
            }

        }while(!allListsEmpty);

        return resultList;

    }

}
