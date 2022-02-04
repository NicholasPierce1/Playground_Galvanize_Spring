package com.example.demo.model.code_challenges.tree;

import com.sun.istack.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class Tree<T extends Comparable<T>> implements Cloneable {

    private Node<T> root;

    private Tree(){}
    
    public static final class Node<V> implements Cloneable{
        
        private Node<V> leftNode = null;
        
        private Node<V> rightNode = null;

        private Node<V> parentNode = null;
        
        private V value;

        Node(V value, @Nullable Node<V> parentNode){

            this(value, parentNode, null, null);

        }
        
        Node(V value, @Nullable Node<V> parentNode, @Nullable Node<V> leftNode, @Nullable Node<V> rightNode){
            
            this.value = value;
            this.parentNode = parentNode;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
            
        }

         public Node<V> getLeftNode() {
            return leftNode;
        }

         public Node<V> getRightNode() {
            return rightNode;
        }

         public V getValue() {
            return value;
        }

        public Node<V> getParentNode() {
            return parentNode;
        }

        void setParentNode(Node<V> parentNode) {
            this.parentNode = parentNode;
        }

        void setLeftNode(Node<V> leftNode) {
            this.leftNode = leftNode;
        }

         void setRightNode(Node<V> rightNode) {
            this.rightNode = rightNode;
        }

         void setValue(V value) {
            this.value = value;
        }
        
        @Override
        public boolean equals(Object object){
            
            boolean leftRecurse = false;
            boolean rightRecurse = false;
            
            if(object == null)
                return false;
            
            else if(object == this)
                return true;
            
            else if(!(object instanceof Node<?>))
                return false;
            
            Node<?> node = (Node<?>)object;
            
            // value -> early exit (performance boost)
            if(!this.getValue().equals(node.getValue()))
                return false;
            
            // recursively check left
            if(this.getLeftNode() == node.getLeftNode()) // memory addresses are same
                leftRecurse = true;
            else if(this.getLeftNode() != null && node.getLeftNode() != null){  // both not null, recursively evaluate
                leftRecurse = this.getLeftNode().equals(node.getLeftNode());
            }
            
            // left -> early exit (performance boost)
            if(!leftRecurse)
                return false;
            
            // recursively check right
            if(this.getRightNode() == node.getRightNode()) // memory addresses are same
                rightRecurse = true;
            else if(this.getRightNode() != null && node.getRightNode() != null){  // both not null, recursively evaluate
                rightRecurse = this.getRightNode().equals(node.getRightNode());
            }
            
            return rightRecurse;
        }
        
        @Override
        public String toString(){
            return String.format(
                    "Node(value: %s - parent: %s - L||R: %s)",
                    this.getValue(),
                    this.getParentNode() != null ? this.getParentNode().getValue() : "",
                    this.determineIfLeftOrRightToParent()
                    );
        }
        
        private String determineIfLeftOrRightToParent(){
            
            if(this.getParentNode() == null)
                return "";
            
            return this.equals(this.getParentNode().getLeftNode()) ? "L" : "R";
        }

        @Override
        public Object clone() {
            // create current node as root
            final Node<V> rootNode = new Node<V>(this.getValue(), null);

            // set left child
            rootNode.setLeftNode(
                    this.createChildNode(
                            rootNode,
                            this.getLeftNode()
                    )
            );

            // set right child
            rootNode.setRightNode(
                    this.createChildNode(
                            rootNode,
                            this.getRightNode()
                    )
            );

            return rootNode;

        }

        private Node<V> createChildNode(
                @NotNull final Node<V> parentNode,
                @NotNull final Node<V> currentNodeToClone){

            // for root caller's children
            if(currentNodeToClone == null)
                return null;

            Node<V> currentNodeClone =
                    new Node<V>(
                            currentNodeToClone.getValue(),
                            parentNode
                    );

            Node<V> childNode = null;

            // add left child
            if(currentNodeToClone.getLeftNode() != null)
                childNode = this.createChildNode(
                        currentNodeClone,
                        currentNodeToClone.getLeftNode()
                );

            currentNodeClone.setLeftNode(childNode);

            // reset pointer (if right node is null then left node is set twice)
            childNode = null;

            // add right child
            if(currentNodeToClone.getRightNode() != null)
                childNode = this.createChildNode(
                        currentNodeClone,
                        currentNodeToClone.getRightNode()
                );

            currentNodeClone.setRightNode(childNode);

            return currentNodeClone;

        }

    }

     public Node<T> getRoot() {
         return root;
     }

     private void setRoot(@NotNull final Node<T> root) {
         this.root = root;
     }

    public Node<T> getNode(@NotNull final T value, @NotNull final Node<T> subTreeToSearch) throws NodeNotFoundException{

        final T currentValue = subTreeToSearch.getValue();

        if(currentValue.equals(value))
            return subTreeToSearch;

        else if(currentValue.compareTo(value) > 0 && subTreeToSearch.getLeftNode() != null)
            // value to search is smaller && left node exists -> step left
            return this.getNode(value, subTreeToSearch.getLeftNode());

        else if(subTreeToSearch.getRightNode() != null)
            // value to search is greater && right node exists -> step right
            return this.getNode(value, subTreeToSearch.getRightNode());

        else
            throw new NodeNotFoundException(value);
    }

    @SafeVarargs
    public final List<Node<T>> addChildren(@NotNull final Node<T> currentNode, @NotNull final T... values) throws MalformedTreeInputException {

        final List<Node<T>> nodes = new ArrayList<Node<T>>();

        for(final T toAdd : values)
            nodes.add(this.addChild(currentNode, toAdd));

        return nodes;

    }

    public Node<T> addChild(@NotNull final Node<T> currentNode, @NotNull final T value) throws MalformedTreeInputException{

        // determined L || R
        // if not null, recurse
        // set & return child

        if(value == null)
            throw new NullPointerException("Value of node cannot be null");

        final int compareValue = currentNode.getValue().compareTo(value);

        if(compareValue == 0)
            throw new MalformedTreeInputException("Node values cannot be identical");

        else if(compareValue < 0) {
            // greater than

            // traverse if not null
            if(currentNode.getRightNode() != null)
                return addChild(currentNode.getRightNode(), value);

            // doesn't exist yet: set
            currentNode.setRightNode(new Node<T>(value, currentNode));
            return currentNode.getRightNode();
        }

        else{

            // less than

            // traverse if not null
            if(currentNode.getLeftNode() != null)
                return addChild(currentNode.getLeftNode(), value);

            // doesn't exist yet: set
            currentNode.setLeftNode(new Node<T>(value, currentNode));
            return currentNode.getLeftNode();

        }


    }


    public void getNodes(@NotNull final Node<T> currentNode, @NotNull final Deque<Node<T>> nodes){

        // ascending pushes (descending order -> [1,2,3] tree -> push 3, push 2, push 1)
        // check right
         if(currentNode.getRightNode() != null)
             getNodes(currentNode.getRightNode(), nodes);

         // right most node (biggest value) - add current node
         nodes.push(currentNode);

         // check left
         if(currentNode.getLeftNode() != null)
             getNodes(currentNode.getLeftNode(), nodes);

     }

     @Override
     public boolean equals(Object object){

        if(object == null)
            return false;

        else if(this == object)
            return true;

        else if(!(object instanceof Tree<?>))
            return false;

        final Tree<?> tree = (Tree<?>)object;

        // invokes recursive evaluation of the two trees' nodes
        // NOTE: doesn't matter it tree's type parameter is different
        // REASON: Node's equality implementation checks first if the values of the two nodes are the same
        // two objects of differing types will always evaluate to false
        return this.getRoot().equals(tree.getRoot());

     }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Object clone() {
        final Tree<T> tree = new Tree<T>();
        tree.setRoot((Node<T>)this.getRoot().clone());
        return tree;
    }

    public static final class TreeBuilder<T extends Comparable<T>>{

        private T[] treeInput;

        public TreeBuilder(){

        }

        @SafeVarargs
        public final void setTreeInput(@NotNull final T... treeInput) {

            if(treeInput == null)
                throw new NullPointerException("Tree input cannot be null");

             this.treeInput = treeInput;
         }

         public Tree<T> construct() throws MalformedTreeInputException {

            final Tree<T> tree = new Tree<T>();

            if(this.treeInput.length == 0)
                return tree;
            
            // set first node
            System.out.println(this.setRoot(tree, treeInput[0]));

            Arrays.stream(treeInput).skip(1).forEach(
                    (T nodeValue) -> {
                        System.out.println(tree.addChild(tree.getRoot(), nodeValue));
                    }
            );

            return tree;
         }
         
         private Node<T> setRoot(@NotNull final Tree<T> tree, @NotNull final T value){
             tree.setRoot(new Node<T>(value, null));

             return tree.getRoot();
         }
     }
 }
