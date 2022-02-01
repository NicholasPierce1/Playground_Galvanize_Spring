package com.example.demo.model.code_challenges.tree;

import com.sun.istack.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class Tree<T extends Comparable<T>> {

    private Node<T> root;

    private Tree(){}
    
    private final class Node<V extends Comparable<V>>{
        
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

         Node<V> getLeftNode() {
            return leftNode;
        }

         Node<V> getRightNode() {
            return rightNode;
        }

         V getValue() {
            return value;
        }

        Node<V> getParentNode() {
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
            
            if(object == null)
                return false;
            
            else if(object == this)
                return true;
            
            else if(!(object instanceof Tree<?>.Node<?>))
                return false;
            
            Tree<?>.Node<?> node = (Tree<?>.Node<?>)object;
            
            return node.getValue().equals(this.getValue());
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
    }

     public Node<T> getRoot() {
         return root;
     }

     private void setRoot(Node<T> root) {
         this.root = root;
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
            this.setRoot(tree, treeInput[0]);

            Arrays.stream(treeInput).skip(1).forEach(
                    (T nodeValue) -> {
                        System.out.println(this.addChild(tree, tree.getRoot(), nodeValue));
                    }
            );

            return null;
         }
         
         private Tree<T>.Node<T> addChild(@NotNull final Tree<T> tree, @NotNull final Tree<T>.Node<T> currentNode, @NotNull final T value) throws MalformedTreeInputException{

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
                     return addChild(tree, currentNode.getRightNode(), value);
                 
                 // doesn't exist yet: set
                 currentNode.setRightNode(tree.new Node<T>(value, currentNode));
                 return currentNode.getRightNode();
             }
             
             else{

                 // less than

                 // traverse if not null
                 if(currentNode.getLeftNode() != null)
                     return addChild(tree, currentNode.getLeftNode(), value);

                 // doesn't exist yet: set
                 currentNode.setLeftNode(tree.new Node<T>(value, currentNode));
                 return currentNode.getLeftNode();
                 
             }
                 
             
         }
         
         private Tree<T>.Node<T> setRoot(@NotNull final Tree<T> tree, @NotNull final T value){
             tree.setRoot(tree.new Node<T>(value, null));

             System.out.println(tree.getRoot());

             return tree.getRoot();
         }
     }
 }
