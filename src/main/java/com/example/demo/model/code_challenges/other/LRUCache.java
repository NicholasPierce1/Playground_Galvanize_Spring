package com.example.demo.model.code_challenges.other;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public final class LRUCache<T> {

    private final HashMap<Integer, Node<T>> elements;

    private final TreeMap<Integer, TreeSet<Node<T>>> elementsOrder;

    private final int capacity;

    public LRUCache(int capacity) {

        // default policy: capacity * 2 to prevent resizes
        this.capacity = capacity;
        this.elements = new HashMap<Integer, Node<T>>(capacity * 2);
        this.elementsOrder = new TreeMap<Integer, TreeSet<Node<T>>>();

    }

    public T get(int key) {

        final Node<T> toReturn;

        if(this.elements.containsKey(key)){

            toReturn = this.elements.get(key);

            final TreeSet<Node<T>> toRemoveFrom = this.elementsOrder.get(toReturn.getTimesUsed());

            toRemoveFrom.remove(toReturn);

            if(toRemoveFrom.isEmpty())
                this.elementsOrder.remove(toReturn.getTimesUsed());

            toReturn.incrementUsage();

            this.addElementToOrderedStructure(toReturn);

        }
        else
            toReturn = null;

        return toReturn == null ? null : toReturn.getElement();
    }

    public void put(int key, T value) {

        final Node<T> toAdd = new Node<T>(value,key);

        if(this.capacity == this.elements.size()){

            // times used (int) are sorted in natural/ascending-order
            final int firstKey = this.elementsOrder.firstKey();

            final TreeSet<Node<T>> toRemoveFrom = this.elementsOrder.get(firstKey);

            // all elements in this set retain the same usage count
            // first element is the one with the latest date created
            final Node<T> toRemove = toRemoveFrom.pollFirst();

            if(toRemove == null)
                throw new RuntimeException("should never happen");

            // if last element with that usage count then remove
            if(toRemoveFrom.isEmpty())
                this.elementsOrder.remove(firstKey);

            this.elements.remove(toRemove.getKey());

        }

        this.elements.put(key,toAdd);

        this.addElementToOrderedStructure(toAdd);

    }

    private void addElementToOrderedStructure(@NotNull final Node<T> toAdd){

        if(elementsOrder.containsKey(toAdd.getTimesUsed()))
            elementsOrder.get(toAdd.getTimesUsed()).add(toAdd);
        else
            elementsOrder.put(toAdd.getTimesUsed(), new TreeSet<Node<T>>(){{add(toAdd);}});

    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString(){

        final JSONObject jsonObject = new JSONObject();

        try{

            final Class<LRUCache<T>> selfReflection = (Class<LRUCache<T>>)this.getClass();

            for(final Field field : selfReflection.getDeclaredFields())
                jsonObject.put(
                        field.getName(),
                        field.get(this)
                );

            jsonObject.put("size", this.elements.size());

            return jsonObject.toString(2);

        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }

    }

    // note: public for testing purposes
    public static final class Node<T> implements Comparable<Node<T>>{

        private final T element;

        private final int key;

        private int timesUsed = 0;

        private final LocalDateTime createdOn = LocalDateTime.now();

        Node(T element, int key){
            this.element = element;
            this.key = key;
        }

        void incrementUsage(){
            timesUsed++;
        }

        T getElement() {
            return element;
        }

        int getTimesUsed() {
            return timesUsed;
        }

        int getKey() {
            return key;
        }

        @Override
        public int compareTo(@NotNull LRUCache.Node<T> node) {

            final int compareResult = Integer.compare(this.timesUsed, node.timesUsed);

            // objects with the same usage count are separated on time they are created (can be multiple days)
            return compareResult != 0 ?
                    compareResult :
                    this.createdOn.compareTo(node.createdOn);

        }

        @Override
        public int hashCode(){

            return Arrays.hashCode(new Object[]{this.timesUsed, this.element, this.key, this.createdOn});

        }

        @Override
        public boolean equals(Object o){

            final Node<T> node;

            if(o == null)
                return false;
            else if (this == o)
                return true;
            else if (!(o instanceof Node<?>))
                return false;

            try {
                node = (Node<T>)o;
            }
            catch(Exception ex){
                return false;
            }

            return this.hashCode() == node.hashCode();

        }

        @Override
        @SuppressWarnings("unchecked")
        public String toString(){

            final JSONObject jsonObject = new JSONObject();

            try{

                final Class<Node<T>> selfReflection = (Class<Node<T>>)this.getClass();

                for(final Field field : selfReflection.getDeclaredFields())
                    jsonObject.put(
                            field.getName(),
                            field.get(this)
                    );

                return jsonObject.toString(2);

            }
            catch(Exception ex){
                throw new RuntimeException(ex);
            }

        }

    }
}
