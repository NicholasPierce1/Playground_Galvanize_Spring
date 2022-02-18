package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

public class SmallestNumberAfterRemovingNDigits {

    @Test
    public void findSmallestNumberRemovingKDigitsTest(){

        Stream.<Triplet<String,Integer,String>>of(
                new Triplet<String,Integer,String>(
                        "1432219",
                        3,
                        "1219"
                ),
                new Triplet<String,Integer,String>(
                        "321",
                        1,
                        "21"
                ),
                new Triplet<String,Integer,String>(
                        "54645",
                        3,
                        "44"
                ),
                new Triplet<String,Integer,String>(
                        "123",
                        3,
                        ""
                ),
                new Triplet<String,Integer,String>(
                        "1",
                        0,
                        "1"
                ),
                new Triplet<String,Integer,String>(
                        "441",
                        1,
                        "41"
                ),
                new Triplet<String,Integer,String>(
                        "445",
                        1,
                        "44"
                ),
                new Triplet<String,Integer,String>(
                        "12210",
                        1,
                        "1210"
                ),
                new Triplet<String,Integer,String>(
                        "12230",
                        1,
                        "1220"
                ),
                new Triplet<String,Integer,String>(
                        "122387",
                        1,
                        "12237"
                ),
                new Triplet<String,Integer,String>(
                        "122317",
                        1,
                        "12217"
                ),
                new Triplet<String,Integer,String>(
                        "13345",
                        1,
                        "1334"
                )
        ).forEach(
                (testData) -> {

                    Assertions.assertEquals(
                            testData.getValue2(),
                            this.findSmallestNumberRemovingKDigits(
                                    testData.getValue0(),
                                    testData.getValue1()
                            )
                    );

                }
        );

    }

    private String findSmallestNumberRemovingKDigits(@NotNull final String number, final int toRemove){

        if(toRemove == 0)
            return number;
        else if(number.length() == 1)
            return findSmallestNumberRemovingKDigits(
                    "",
                    toRemove -1
            );

        int indexPlaceHolder = -1;
        int valueOfPlaceHolder = -1;

        for(int i = 0; i < number.length(); i++) {

            final int numAfter, numBefore, currentNum;

            currentNum = Integer.parseInt(String.valueOf(number.charAt(i)));

            if (i == 0) {

                numAfter = Integer.parseInt(String.valueOf(number.charAt(i + 1)));

                if (currentNum > numAfter)
                    return findSmallestNumberRemovingKDigits(
                            number.substring(i + 1),
                            toRemove - 1
                    );
                else if(currentNum == numAfter){

                    indexPlaceHolder = i;
                    valueOfPlaceHolder = currentNum;

                }


            } else if (i != number.length() - 1) {

                numBefore = Integer.parseInt(String.valueOf(number.charAt(i - 1)));
                numAfter = Integer.parseInt(String.valueOf(number.charAt(i + 1)));

                if (currentNum > numAfter && currentNum > numBefore)
                    return findSmallestNumberRemovingKDigits(
                            number.substring(0, i) + number.substring(i + 1),
                            toRemove - 1
                    );
                else if(indexPlaceHolder == -1) { // has not been set

                    if(currentNum == numBefore){

                        indexPlaceHolder = i;
                        valueOfPlaceHolder = currentNum;

                    }

                }
                else if(currentNum < valueOfPlaceHolder) // 122 1 9
                    return findSmallestNumberRemovingKDigits(
                            number.substring(0, indexPlaceHolder) + number.substring(indexPlaceHolder + 1),
                            toRemove -1
                    );

            }
            else {

                if(indexPlaceHolder == -1)
                    return findSmallestNumberRemovingKDigits(
                            number.substring(0, number.length() - 1),
                            toRemove - 1
                    );
                else if(currentNum > valueOfPlaceHolder) // remove current number
                    return findSmallestNumberRemovingKDigits(
                            number.substring(0,i), // at last number, just splice to it
                            toRemove - 1
                    );
                else // remove placeHolder
                    return findSmallestNumberRemovingKDigits(
                            number.substring(0, indexPlaceHolder) + number.substring(indexPlaceHolder + 1),
                            toRemove - 1
                    );

            }
        }

        throw new RuntimeException("should never happen");

    }

}
