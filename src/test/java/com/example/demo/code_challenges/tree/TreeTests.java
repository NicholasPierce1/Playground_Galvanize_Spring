package com.example.demo.code_challenges.tree;

import com.example.demo.model.code_challenges.tree.MalformedTreeInputException;
import com.example.demo.model.code_challenges.tree.NodeNotFoundException;
import com.example.demo.model.code_challenges.tree.Tree;
import com.sun.istack.NotNull;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* TODO:
        1) refactor 'createTreesTest' to access equality via tree's equal implementation
        1.1.1) use clone method and test for equality of values (memory addresses are different + nodes' too)
        1.1) use reflections to auto create each respective tree
        1.2) use clone method from each incremental tree to add new nodes
        2) implement burn tree
 */
public class TreeTests {

    private final Integer[][] valid_trees = new Integer[][]{
            // tree one
            new Integer[]{5, 10},
            // tree two
            new Integer[]{5, 10, 0},
            // tree three
            new Integer[]{5, 10, 0, 7, 12},
            // tree four
            new Integer[]{5, 10, 3, 7, 12, 1, 4}

    };

    @Test
    public void createTreesTest(){


        for(int i = 0; i < this.valid_trees.length; i++) {

            System.out.printf(
                    "%s" + "creating tree: %d\n",
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
    public void getNodesTest(){

        // construct expected results
        final Integer[][] expected = new Integer[this.valid_trees.length][];

        for(int i = 0; i < this.valid_trees.length; i++) {
            final Integer[] copy = Arrays.copyOf(this.valid_trees[i], this.valid_trees[i].length);
            // Arrays.sort(copy, Comparator.reverseOrder());
            Arrays.sort(copy);
            expected[i] = copy;
        }

        final List<Tree<Integer>> trees = this.getTreeData();

        for(int i = 0; i < trees.size(); i++){

            final Tree<Integer> tree = trees.get(i);

            final Deque<Tree.Node<Integer>> nodes =  new ArrayDeque<Tree.Node<Integer>>();

            tree.getNodes(tree.getRoot(), nodes);

            System.out.printf("\nsize tree %d: expected(%d) -- actual(%d)\n", i + 1, expected[i].length, nodes.size());

            // size of queue & expected are same
            Assertions.assertEquals(expected[i].length, nodes.size());

            for(int j = 0; j < expected[i].length; j++) {
                final Integer actual = nodes.pop().getValue();
                System.out.printf("value: expected(%d) -- actual(%d)\n", expected[i][j], actual);
                Assertions.assertEquals(expected[i][j], actual);
            }
        }

    }

    @Test
    public void treeCloneTest(){

        this.getTreeData().stream().forEach(
                (tree) -> {

                    Assertions.assertEquals(tree, tree.clone());

                }
        );

    }

    // not imperative to test but good practice for using reflection
    @Test
    @SuppressWarnings("unchecked")
    public void createTreeIncrementsWithReflectionTest() {

        final Integer[][] incrementsToAppend = new Integer[this.valid_trees.length][];

        final Integer[] originalIncrement = this.valid_trees[0];

        final List<Tree<Integer>> trees = this.getTreeData();

        for(int i = 1; i < trees.size(); i++){

            try{

                // debug
                System.out.println( "\n" +
                        Arrays.asList(
                                Arrays.copyOfRange(
                                        this.valid_trees[i], // source
                                        this.valid_trees[i - 1].length, // whole - redundant = distinct
                                        this.valid_trees[i].length // end of whole
                                )
                        )
                );

                // creates expected & actual
                // clones previous tree to append new values to
                final Tree<Integer> clonedTree = (Tree<Integer>)trees.get(i - 1).clone();


                // transformation on tree to create expected
                clonedTree.addChildren(
                       clonedTree.getRoot(),
                       Arrays.copyOfRange(
                           this.valid_trees[i], // source
                           this.valid_trees[i - 1].length, // whole - redundant = distinct
                           this.valid_trees[i].length // end of whole
                       )
                );

                final Tree<Integer> actual =
                        this.<Integer>createTreeIncrementWithReflection(
                                new Pair<Tree<Integer>, Integer[]>(
                                    trees.get(i - 1), // clones previous tree to append new values to
                                    Arrays.copyOfRange(
                                            this.valid_trees[i], // source
                                            this.valid_trees[i - 1].length, // whole - redundant = distinct
                                            this.valid_trees[i].length // end of whole
                                    )
                                )
                            );


                    // create equals method via reflection
                    final Method equalsMethod =
                            ((Class<Tree<Integer>>)trees.get(i).getClass()).getMethod("equals", Object.class);

                    Assertions.assertTrue(
                            (boolean) equalsMethod.invoke(
                                    clonedTree,
                                    actual
                            )
                    );
            }
            catch(Exception ex){
                Assertions.fail(ex);
            }
        }

    }

    @Test
    public void findNodeTest(){

        this.getTreeData().stream().map(
                (tree) -> {

                    final Random random = new Random();

                    int valueThatExist;
                    int valuesThatDoesNotExist;

                    // not optimized but tests are conducted with small scales
                    final Deque<Tree.Node<Integer>> dequeTreeValues = new ArrayDeque<Tree.Node<Integer>>();
                    final Set<Integer> treeValues = new HashSet<Integer>();
                    tree.getNodes(tree.getRoot(), dequeTreeValues);

                    for(final Tree.Node<Integer> node : dequeTreeValues)
                        treeValues.add(node.getValue());

                    // value that exist
                    valueThatExist = new ArrayList<Tree.Node<Integer>>(dequeTreeValues)
                            .get(random.nextInt(treeValues.size()))
                            .getValue();

                    // value that doesn't exist
                    while(treeValues.contains(valuesThatDoesNotExist = random.nextInt(Integer.MAX_VALUE)));

                    return new Triplet<Tree<Integer>, Integer, Integer>(
                            tree,
                            valueThatExist,
                            valuesThatDoesNotExist
                    );
                }
        ).forEach(
                (triplet) -> {

                    final Tree<Integer> tree = triplet.getValue0();

                    // exists
                    Assertions.assertDoesNotThrow(
                            () -> {
                                final Tree.Node<Integer> node = tree.getNode(triplet.getValue1(), tree.getRoot());

                                Assertions.assertNotNull(node, "Null encountered. Was this value suppose to be null?");
                            }
                    );

                    // doesn't exist
                    Assertions.assertThrows(
                            NodeNotFoundException.class,
                            () ->{
                                final Tree.Node<Integer> node = tree.getNode(triplet.getValue2(), tree.getRoot());
                            }
                    );

                }
        );



    }

    @Test
    public void burningTreeImplTest(){

        final List<Triplet<Tree<Integer>, Integer, List<Set<Integer>>>> testData =
                this.getBurningTreeTestData();

        testData.stream().forEach(
                (triplet) -> {

                    System.out.println("\nnew tree");

                    // acquire actual (expected inside triplet-tuple)
                    final List<? extends Set<Integer>> actual =
                            this.<Integer>burnTree(
                                    triplet.getValue0(),
                                    triplet.getValue1()
                            );

                    // iterate through each level, print it out, and assert equality
                    Assertions.assertEquals(triplet.getValue2().size(), actual.size());

                    for(int i = 0; i < actual.size(); i++){

                        final Set<Integer> expectedBurntNodes = triplet.getValue2().get(i);

                        final Set<Integer> actualBurntNodes = actual.get(i);

                        Assertions.assertEquals(expectedBurntNodes.size(), actualBurntNodes.size());

                        System.out.printf(
                                "Burnt nodes for level (%d): expected(%s)  --- actual(%s)\n",
                                i + 1,
                                expectedBurntNodes.toString(),
                                actualBurntNodes.toString()
                        );

                        Assertions.assertEquals(expectedBurntNodes, actualBurntNodes);

                    }

                }
        );

    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> Tree<T> createTreeIncrementWithReflection(
            @NotNull final Pair<Tree<T>, T[]> pair) {

        /*
        uses reflection to acquire:
          1) tree's clone method
          2) tree's add method
          3) tree's getRoot method
         */

        try {

            final Tree<T> treeToCloneAndModify = pair.getValue0();

            // tree's type
            final Class<Tree<T>> treeClass = (Class<Tree<T>>) treeToCloneAndModify.getClass();

            // 1
            final Method cloneMethod = treeClass.getMethod("clone");
            final Tree<T> cloneTreeToAppend = (Tree<T>)cloneMethod.invoke(treeToCloneAndModify);

            // 2
            final Method addMethod = treeClass.getMethod("addChild", Tree.Node.class, Comparable.class);

            // 3
            final Method getRoot = treeClass.getMethod("getRoot");

            for(final T toAdd : pair.getValue1())
                addMethod.invoke(
                        cloneTreeToAppend, // calling object
                        (Tree.Node<T>)getRoot.invoke(cloneTreeToAppend), // 1st arg (Node<T>)
                        toAdd  // 2nd arg (T)
                );

            return cloneTreeToAppend;


        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    private List<Tree<Integer>> getTreeData(){


        final List<Tree<Integer>> trees = new ArrayList<Tree<Integer>>();

        for(int i = 0; i < this.valid_trees.length; i++) {

            System.out.printf(
                    "%s" + "creating tree: %d\n",
                    i == 0 ? "" : "\n",
                    i + 1
            );

            final Tree.TreeBuilder<Integer> treeBuilder = new Tree.TreeBuilder<Integer>();
            treeBuilder.setTreeInput(valid_trees[i]);

            trees.add(treeBuilder.construct());

        }

        return trees;

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
                            return new Pair<Tree<Integer>, Integer>(treeBuilder.construct(), data[data.length - 1]);
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

    private <T extends Comparable<T>> List<? extends Set<T>> burnTree(
            @NotNull final Tree<T> tree, @NotNull final T startingNodeValue){

        // acquire starting node
        final Tree.Node<T> startingNode = tree.getNode(startingNodeValue, tree.getRoot());

        final List<HashSet<T>> burntNodes = new ArrayList<HashSet<T>>();

        final Set<T> allBurntNodes = new HashSet<T>();

        // burn starting node
        burntNodes.add(new HashSet<T>(){{add(startingNode.getValue());}});
        allBurntNodes.add(startingNode.getValue());

        // enumerates elements to burn on next pass
        List<Tree.Node<T>> nodesToBurnNext = new ArrayList<Tree.Node<T>>();
        nodesToBurnNext.add(startingNode);


        while(!nodesToBurnNext.isEmpty()){

            List<Tree.Node<T>> nodesToVisit = new ArrayList<Tree.Node<T>>(nodesToBurnNext);
            nodesToBurnNext.clear();

            // acquires all non-null, contiguous nodes
            for(final Tree.Node<T> node : nodesToVisit){

                final List<Tree.Node<T>> contiguousNodes = Arrays.asList(
                        node.getParentNode(),
                        node.getLeftNode(),
                        node.getRightNode()
                );

                for(final Tree.Node<T> nextNode : contiguousNodes){

                    // not null and haven't seen it yet
                    if(nextNode != null && !allBurntNodes.contains(nextNode.getValue())){

                        // add to iterable and aggregate result
                        nodesToBurnNext.add(nextNode);
                        allBurntNodes.add(nextNode.getValue());

                    }

                }

            }

            // add nodes to burn next into return list -> convert into a set of node values
            if(!nodesToBurnNext.isEmpty())
                burntNodes.add(
                        nodesToBurnNext.stream()
                                .map(Tree.Node<T>::getValue)
                                .collect(Collectors.toCollection(HashSet<T>::new))
                );

        }

        return burntNodes;
    }

}
