package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

public class ListAllStringPermutations {

    @Test
    public void getAllStringPermutationsTest(){
        Stream.<Pair<String,? extends Set<String>>>of(
                new Pair<String, HashSet<String>>(
                        "ab",
                        new HashSet<String>(
                                Arrays.<String>asList("a", "b", "ab", "ba")
                        )
                ),
                new Pair<String, HashSet<String>>(
                        "abc",
                        new HashSet<String>(
                                Arrays.<String>asList(
                                        "a", "b", "c",
                                        "ab", "ac", "ba", "bc", "ca", "cb",
                                        "abc", "acb","bac", "bca", "cab", "cba"
                                )
                        )
                )
        ).map(
                (inputData) -> {

                        final int allPermutationsCount =
                                this.countSizePermutations(inputData.getValue0().length());

                        // sanity check on hardcoded input data & expected combination count
                        Assertions.assertEquals(
                                inputData.getValue1().size(),
                                allPermutationsCount
                        );

                        return new Triplet<String, Set<String>, Integer>(
                                inputData.getValue0(),
                                inputData.getValue1(),
                                allPermutationsCount
                        );

                }
        ).forEach(
                (inputData) -> {

                    final Set<String> actualAllPermutations =
                            this.getAllStringPermutations(inputData.getValue0());

                    Assertions.assertEquals(
                            inputData.getValue2(),
                            actualAllPermutations.size()
                    );

                    Assertions.assertTrue(inputData.getValue1().containsAll(actualAllPermutations));

                }
        );
    }

    private Set<String> getAllStringPermutations(@NotNull final String string){

        final HashMap<Integer, Character> characterAtIndex = new HashMap<Integer, Character>();
        final HashSet<String> allCombinations = new HashSet<String>();

        for(int i = 0; i < string.length(); i++)
            characterAtIndex.put(i, string.charAt(i));

        this.getAllStringPermutations(
                characterAtIndex,
                "",
                allCombinations
        );

        return allCombinations;
    }

    @SuppressWarnings("unchecked")
    private void getAllStringPermutations(
            @NotNull final HashMap<Integer, Character> characterAtIndex,
            @NotNull final String string,
            @NotNull final Set<String> currentCombinations){

            for (final Integer index : characterAtIndex.keySet()) {

                final String appendString = string + characterAtIndex.get(index);
                currentCombinations.add(appendString);
                final HashMap<Integer, Character> characterAtIndexClone = (HashMap<Integer, Character>) characterAtIndex.clone();
                characterAtIndexClone.remove(index);

                this.getAllStringPermutations(
                        characterAtIndexClone,
                        appendString,
                        currentCombinations
                );

            }

    }

    private int countSizePermutations(final int n){

        int totalProbabilities = 0;

        final int nFactorial = this.calculateFactorialMap(n);

        // n!/( (n-r)! )
        for(int r = 1; r <= n; r++)
            totalProbabilities += nFactorial / this.calculateFactorialMap(n-r);


        return totalProbabilities;
    }

    private final TreeMap<Integer, Integer> factorialMap = new TreeMap<Integer,Integer>();

    private int calculateFactorialMap(final int factorial){

        if(this.factorialMap.containsKey(factorial))
            return this.factorialMap.get(factorial);
        else if(this.factorialMap.isEmpty()) {
            this.factorialMap.put(0, 1); // 0! = 1
            this.factorialMap.put(1,1); // 1! = 1
        }

        final int lastKey = this.factorialMap.lastKey();
        int currentBiggestFactorial = this.factorialMap.get(lastKey);

        // ex: lastKey is 3, we want factorial of 5, iterate through 3 & 4
        for(int i = lastKey + 1; i <= factorial; i++) {

            final int nextBiggestFactorial = currentBiggestFactorial * i;

            this.factorialMap.put(
                    i, nextBiggestFactorial
            ); // 4 * 3! --> 4! * 5

            // store 4! then 5!
            currentBiggestFactorial = nextBiggestFactorial;
        }

        return currentBiggestFactorial;
    }
}
