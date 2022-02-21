package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

public class LargestPalindrome {

    @Test
    public void appendToFrontAndBackTest(){

        Stream.<Triplet<char[], Character, char[]>>of(
                new Triplet<char[], Character, char[]>(
                        new char[]{'a','a'},
                        'b',
                        new char[]{'b','a','a','b'}
                ),
                new Triplet<char[], Character, char[]>(
                        new char[]{'b','a','a','b'},
                        'c',
                        new char[]{'c','b','a','a','b','c'}
                )
        ).forEach(
                (testData) -> {

                    Assertions.assertEquals(
                            Arrays.hashCode(testData.getValue2()),
                            Arrays.hashCode(
                                    this.appendToFrontAndBack(
                                        testData.getValue0(),
                                        testData.getValue1()
                                )
                            )
                    );

                }
        );

    }

    @Test
    public void determineIfPalindromeTest(){

        Stream.<Pair<Triplet<String,Integer,Integer>,char[]>>of(
                new Pair<Triplet<String,Integer,Integer>,char[]>(
                        new Triplet<String,Integer,Integer>(
                                "12baab45",
                                3,
                                4
                        ),
                        new char[]{'a','a'}
                ),
                new Pair<Triplet<String,Integer,Integer>,char[]>(
                        new Triplet<String,Integer,Integer>(
                                "aa",
                                0,
                                1
                        ),
                        new char[]{'a','a'}
                ),
                new Pair<Triplet<String,Integer,Integer>,char[]>(
                        new Triplet<String,Integer,Integer>(
                                "bab",
                                0,
                                2
                        ),
                        new char[]{'b','a','b'}
                ),
                new Pair<Triplet<String,Integer,Integer>,char[]>(
                        new Triplet<String,Integer,Integer>(
                                "12bab34",
                                2,
                                4
                        ),
                        new char[]{'b','a','b'}
                ),
                new Pair<Triplet<String,Integer,Integer>,char[]>(
                        new Triplet<String,Integer,Integer>(
                                "12baab45",
                                2,
                                5
                        ),
                        new char[]{'b','a','a','b'}
                ),
                new Pair<Triplet<String,Integer,Integer>,char[]>(
                        new Triplet<String,Integer,Integer>(
                                "12baab45",
                                0,
                                7
                        ),
                        null
                ),
                new Pair<Triplet<String,Integer,Integer>,char[]>(
                        new Triplet<String,Integer,Integer>(
                                "12baab45",
                                0,
                                1
                        ),
                        null
                )
        ).forEach(
                (testData) -> {

                    final char[] actual =
                            this.determineIfPalindrome(
                                    testData.getValue0().getValue0(),
                                    testData.getValue0().getValue1(),
                                    testData.getValue0().getValue2()
                            );

                    if(testData.getValue1() == null)
                        Assertions.assertNull(actual);
                    else{

                        Assertions.assertNotNull(actual);

                        Assertions.assertEquals(
                                testData.getValue1().length,
                                actual.length
                        );

                        System.out.printf(
                                "actual(%s) -- expected(%s)\n",
                                Arrays.toString(actual),
                                Arrays.toString(testData.getValue1())
                        );
                        for(int i = 0; i < actual.length; i++)
                            Assertions.assertEquals(
                                    testData.getValue1()[i],
                                    actual[i]
                            );

                    }

                }
        );

    }

    @Test
    public void findLargestPalindromeTest(){

        Stream.<Pair<String, String>>of(
                new Pair<String,String>(
                        "aa",
                        "aa"
                ),
                new Pair<String,String>(
                        "baab",
                        "baab"
                ),
                new Pair<String,String>(
                        "bacab",
                        "bacab"
                ),
                new Pair<String,String>(
                        "1aa2",
                        "aa"
                ),
                new Pair<String,String>(
                        "12baab34",
                        "baab"
                ),
                new Pair<String,String>(
                        "12bacab34",
                        "bacab"
                ),
                new Pair<String,String>(
                        "%^bacdeedcab!#",
                        "bacdeedcab"
                ),
                new Pair<String,String>(
                        "%^bacdeedcab!#asdfjklfasdjklafdljkfsadkjl",
                        "bacdeedcab"
                ),
                new Pair<String,String>(
                        "1",
                        null
                ),
                new Pair<String,String>(
                        "abc",
                        null
                )
        ).forEach(
                (testData) -> {

                    final String actual =
                            this.findLargestPalindrome(testData.getValue0());

                    if(testData.getValue1() == null)
                        Assertions.assertNull(actual);
                    else
                        Assertions.assertEquals(
                                testData.getValue1(),
                                actual
                        );

                }
        );

    }


    /*
    note: String's charAt(int index) is a O(1)
    String uses internal array char[].

    '1baab', 1, 4
    'bab', 0, 2
     */
    private char[] determineIfPalindrome(@NotNull final String text, int start, int end){

        int frontPosition = 0, backPosition = end - start;
        final char[] result = new char[backPosition + 1]; // allocates one more than the upperbound index

        while(start < end){

            if(text.charAt(start) != text.charAt(end))
                return null; // no match

            result[frontPosition++] = text.charAt(start++);
            result[backPosition--] = text.charAt(end--);

        }

        if(start == end) // b, a*, b
            result[frontPosition] = text.charAt(start);

        return result;

    }

    // 'baab' --> [a,a], b == [b,a,a,b]
    private char[] appendToFrontAndBack(@NotNull final char[] text, final char toAdd){

        final char[] toReturn = new char[text.length + 2];
        toReturn[0] = toAdd;

        System.arraycopy(text, 0, toReturn, 1, text.length);

        toReturn[toReturn.length - 1] = toAdd;

        return toReturn;

    }

    private String findLargestPalindrome(@NotNull final String string){

        char[] currentLargestPalindrome = null;

        HashMap<Character, List<Integer>> positionsOfCharacter =
                new HashMap<Character, List<Integer>>();

        HashMap<Point, char[]> allKnownPalindromes = new HashMap<Point, char[]>();

        for(int i = 0; i < string.length(); i++){

            final int index = i;

            final char currentCharacter = string.charAt(i);

            if(positionsOfCharacter.containsKey(currentCharacter)){

                for(final Integer previousIndexOfChar : positionsOfCharacter.get(currentCharacter)){

                    char[] foundPalindrome = null;

                    if(i - previousIndexOfChar == 1)  // 'abbc' --> 'bb'
                        foundPalindrome = new char[]{currentCharacter, currentCharacter};

                    else { // baab -> 'aa' will need to be eval but 'baab' will not

                        final Point innerPoint = new Point(previousIndexOfChar + 1, i - 1);

                        if(allKnownPalindromes.containsKey(innerPoint)) // 'baab'
                            foundPalindrome = this.appendToFrontAndBack(
                                    allKnownPalindromes.get(innerPoint),
                                    currentCharacter
                            );
                        else // 'ba-cbc-ab' (cbc would be here as 'a' & 'b' would be in case above) || 'bab'
                            foundPalindrome = this.determineIfPalindrome(string, previousIndexOfChar, i);

                    }

                    if(foundPalindrome != null){

                        if(currentLargestPalindrome == null || foundPalindrome.length > currentLargestPalindrome.length)
                            currentLargestPalindrome = foundPalindrome;

                        allKnownPalindromes.put(new Point(previousIndexOfChar, i), foundPalindrome);

                    }

                }

                positionsOfCharacter.get(string.charAt(i)).add(i);

            }
            else
                positionsOfCharacter.put(string.charAt(i), new ArrayList<Integer>(){{add(index);}});

        }

        return currentLargestPalindrome == null ?
                null : new String(currentLargestPalindrome);

    }

    private static record Point(int startIndex, int endIndex) {

        // no need for constructor unless inter calculations are occurring
        // no need for toString, hashCode, or equals
        // can have custom methods, of course

        public int getStartIndex() {
            return this.startIndex;
        }

        public int getEndIndex() {
            return this.endIndex;
        }

        @Override
        public int hashCode(){
            return Arrays.hashCode(new int[]{this.startIndex, this.endIndex});
        }

        @Override
        public boolean equals(Object object){

            if(object == null)
                return false;
            else if(this == object)
                return true;
            else if(!(object instanceof Point))
                return false;

            final Point point = (Point)object;

            return
                    this.startIndex() == point.startIndex()
                            &&
                            this.endIndex() == point.endIndex();

        }

        @Override
        public String toString(){
            return String.format("[x: %d, y: %d]", this.startIndex, this.endIndex);
        }
    }

}