package com.example.demo.code_challenges.leet_code;

import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FastestSnake2DPath {

    @Test
    public void findFastestSnakePathTest(){

        Stream.<Quartet<int[][],Integer,Point,ArrayList<Integer>>>of(
                new Quartet<int[][],Integer,Point,ArrayList<Integer>>(
                        new int[][]{
                                new int[]{1,100},
                                new int[]{10,2}
                        },
                        13,
                        new Point(1,1), // last element
                        Stream.<Integer>of(1,10,2).collect(
                                Collectors.toCollection(ArrayList<Integer>::new)
                        )
                ),
                    new Quartet<int[][],Integer,Point,ArrayList<Integer>>(
                            new int[][]{
                                    new int[]{1,10},
                                    new int[]{0,0},
                                    new int[]{1,5}
                            },
                            6,
                            new Point(2,1), // last element
                            Stream.<Integer>of(1,0,0,5).collect(
                                    Collectors.toCollection(ArrayList<Integer>::new)
                            )
                    )
                ).forEach(
                    (testData) -> {

                        final Pair<Integer, List<Integer>> expected =
                                this.findFastestSnakePath(
                                        testData.getValue0(),
                                        testData.getValue2()
                                );

                        // size of path (including target)
                        Assertions.assertEquals(
                                testData.getValue1(),
                                expected.getValue0()
                        );

                        // all elements within the path
                        Assertions.assertEquals(
                                testData.getValue3(),
                                expected.getValue1()
                        );

                    }
        );

    }

    private Pair<Integer, List<Integer>> findFastestSnakePath(@NotNull final int[][] data, @NotNull final Point target){

        final HashMap<Point, Pair<Integer, List<Integer>>> dataMap = new HashMap<Point, Pair<Integer, List<Integer>>>();

        for(int x = 0; x < data.length; x++){

            for(int y = 0; y < data[x].length; y++){

                final Point currentPoint = new Point(x,y);
                final int currentElement = data[x][y];

                if(x == 0 && y == 0)
                    dataMap.put(
                            currentPoint,
                            new Pair<Integer, List<Integer>>(
                                    currentElement,
                                    new ArrayList<Integer>(){{add(currentElement);}}
                            )
                    );
                // first row (only access left path)
                else if(x == 0){

                    final Pair<Integer, List<Integer>> leftParent =
                            dataMap.get(new Point(x, y - 1));

                    dataMap.put(
                            currentPoint,
                            new Pair<Integer, List<Integer>>(
                                    currentElement + leftParent.getValue0(),
                                    new ArrayList<Integer>(leftParent.getValue1()){{add(currentElement);}}
                            )
                    );

                }
                // first element in nth-row (only access top path)
                else if(y == 0){

                    final Pair<Integer, List<Integer>> topParent =
                            dataMap.get(new Point(x - 1, y));

                    dataMap.put(
                            currentPoint,
                            new Pair<Integer, List<Integer>>(
                                    currentElement + topParent.getValue0(),
                                    new ArrayList<Integer>(topParent.getValue1()){{add(currentElement);}}
                            )
                    );

                }
                /*
                access left & top parent
                whichever has the shortest path is the one this position will extend on
                 */
                else{

                    final Pair<Integer, List<Integer>> topParent =
                            dataMap.get(new Point(x - 1, y));

                    final Pair<Integer, List<Integer>> leftParent =
                            dataMap.get(new Point(x, y - 1));

                    final Pair<Integer, List<Integer>> bestPath =
                            topParent.getValue0().compareTo(leftParent.getValue0()) > 0 ?
                                    // topParent is larger (leftParent is shortest path)
                                    leftParent :
                                    // leftParent is larger (topParent is shortestPath)
                                    // or both are equal (left will be used by default)
                                    topParent;

                    dataMap.put(
                            currentPoint,
                            new Pair<Integer, List<Integer>>(
                                    currentElement + bestPath.getValue0(),
                                    new ArrayList<Integer>(bestPath.getValue1()){{add(currentElement);}}
                            )
                    );

                }

                // if current position is target point then return the map's value at said position
                if(currentPoint.equals(target))
                    return dataMap.get(target);

            }

        }

        // should never happen
        throw new RuntimeException(
                String.format(
                        "Target Point(%s) was never reached",
                        target
                )
        );

    }

    private static record Point(int x, int y) {

        // no need for constructor unless inter calculations are occurring
        // no need for toString, hashCode, or equals
        // can have custom methods, of course

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public int hashCode(){
            return Arrays.hashCode(new int[]{this.x, this.y});
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
                    this.getX() == point.getX()
                    &&
                    this.getY() == point.getY();

        }

        @Override
        public String toString(){
            return String.format("[x: %d, y: %d]", this.x, this.y);
        }
    }

}
