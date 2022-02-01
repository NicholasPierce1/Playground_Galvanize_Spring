package com.example.demo.code_challenges.tree;

import com.example.demo.model.code_challenges.tree.MalformedTreeInputException;
import com.example.demo.model.code_challenges.tree.Tree;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scala.Int;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeTests {

    @Test
    public void createTreesTest(){

        final Integer[][] valid_trees = new Integer[][]{
                // tree one
                new Integer[]{0,10},
                // tree two
                new Integer[]{5, 10, 0},
                // tree three
                new Integer[]{5, 10, 0, 7, 12},
                // tree four
                new Integer[]{5, 10, 3, 7, 12, 1, 4}

        };

        for(int i = 0; i < valid_trees.length; i++) {

            System.out.printf(
                    "%s" + "creating tree: %d%n",
                    i == 0 ? "" : "\n",
                    i + 1
            );

            final int index = i;

            Assertions.assertDoesNotThrow(
                    () -> {
                        final Tree.TreeBuilder<Integer> treeBuilder = new Tree.TreeBuilder<Integer>();
                        treeBuilder.setTreeInput(valid_trees[index]);
                        final Tree<Integer> tree = treeBuilder.construct();
                    }
            );
        }
    }

    @Test
    public void redundantBadTreeTest(){

        Assertions.assertThrows(MalformedTreeInputException.class,
                () -> {
                    final Tree.TreeBuilder<Integer> treeBuilder = new Tree.TreeBuilder<Integer>();
                    treeBuilder.setTreeInput(1,2,3,4,5,1);
                    final Tree<Integer> tree = treeBuilder.construct();
            }
        );

    }

    @Test
    public void nullBadTreeTest(){

        Assertions.assertThrows(NullPointerException.class,
                () -> {
                    final Tree.TreeBuilder<Integer> treeBuilder = new Tree.TreeBuilder<Integer>();
                    treeBuilder.setTreeInput(1,2,3,4,5,null);
                    final Tree<Integer> tree = treeBuilder.construct();
                }
        );

    }

    @Test
    public void burningTreeImplTest(){

        final List<Triplet<Tree<Integer>, Integer, List<Set<Integer>>>> testData =
                this.getBurningTreeTestData();

    }

    private List<Triplet<Tree<Integer>, Integer, List<Set<Integer>>>> getBurningTreeTestData(){

        final Stream<Integer[]> tree_data = Stream.<Integer[]>of(
                // tree one
                new Integer[]{0,10},
                // tree two
                new Integer[]{5, 10, 0},
                // tree three
                new Integer[]{5, 10, 0, 7, 12},
                // tree four
                new Integer[]{5, 10, 3, 7, 12, 1, 4}

        );

        final Stream<Pair<Tree<Integer>, Integer>> trees =
                tree_data.map(
                        (data) -> {
                            System.out.println("\ncreating tree: ");
                            final Tree.TreeBuilder<Integer> treeBuilder = new Tree.TreeBuilder<Integer>();
                            treeBuilder.setTreeInput(data);
                            return new Pair<Tree<Integer>, Integer>(treeBuilder.construct(), data[data.length -1]);
                        }
                );

        final Queue<List<Set<Integer>>> tree_results =
                Stream.<Integer[][]>of(
                        // tree one (new Integer[]{0,10}) -- burning node 10
                        new Integer[][]{
                                // level 1
                                new Integer[]{10},
                                // level 2
                                new Integer[]{0}
                        },
                        // tree two (new Integer[]{5,10,0}) -- burning node 0
                        new Integer[][]{
                                // level 1
                                new Integer[]{0},
                                // level 2
                                new Integer[]{5},
                                // level 3
                                new Integer[]{10}
                        },
                        // tree three (new Integer[]{5, 10, 0, 7, 12}) -- burning node 12
                        new Integer[][]{
                                // level 1
                                new Integer[]{12},
                                // level 2
                                new Integer[]{10},
                                // level 3
                                new Integer[]{7,5},
                                // level 4
                                new Integer[]{0}
                        },
                        // tree four (new Integer[]{5, 10, 3, 7, 12, 1, 4}) -- burning node 4
                        new Integer[][]{
                                // level 1
                                new Integer[]{4},
                                // level 2
                                new Integer[]{3},
                                // level 3
                                new Integer[]{1,5},
                                // level 4
                                new Integer[]{10},
                                // level 5
                                new Integer[]{7,12}
                        }
                ).map(
                        (treeResultData) -> {

                            final List<Set<Integer>> treeResult = new ArrayList<Set<Integer>>();

                            for(final Integer[] burnRowResult : treeResultData)
                                treeResult.add(new HashSet<Integer>(Arrays.asList(burnRowResult)));

                            return treeResult;
                        }
                ).collect(
                        Collectors.toCollection(ArrayDeque<List<Set<Integer>>>::new)
                );

        final List<Triplet<Tree<Integer>, Integer, List<Set<Integer>>>> testData =
                trees.map(
                        (tree) -> new Triplet<Tree<Integer>, Integer, List<Set<Integer>>>(
                                tree.getValue0(),
                                tree.getValue1(),
                                tree_results.poll()
                        )
                ).collect(Collectors.toList());


        System.out.println("\n");
        testData.stream().forEach((data) -> System.out.println(data.getValue2()));

        return testData;
    }

}
