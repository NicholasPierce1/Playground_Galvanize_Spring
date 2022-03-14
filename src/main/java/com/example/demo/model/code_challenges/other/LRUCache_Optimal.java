package com.example.demo.model.code_challenges.other;

import com.sun.istack.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

public final class LRUCache_Optimal<T> {

    private final HashMap<Integer, Node<T>> elements;

    private final CachedLinkedList<T> elementsOrdered;

    private final int capacity;

    public LRUCache_Optimal(final int capacity) {

        this.capacity = capacity;

        this.elements = new HashMap<Integer, Node<T>>(capacity * 2);
        this.elementsOrdered = new CachedLinkedList<>();

    }

    public T get(@NotNull final int key) {

        if (this.elements.containsKey(key)) {

            final Node<T> element = this.elements.get(key);

            this.elements.replace(key, this.elementsOrdered.addFirst(element.getValue(), key));

            this.elementsOrdered.remove(element);

            return element.getValue();

        } else
            return null;

    }

    public void put(final int key, @NotNull final T value) {

        if (this.elements.containsKey(key)) {

            final Node<T> element = this.elements.get(key);

            this.elements.replace(key, this.elementsOrdered.addFirst(value, key));

            this.elementsOrdered.remove(element);

        } else if (this.capacity == this.elements.size()) {

            this.elements.remove(this.elementsOrdered.getTail().getKey());

            this.elementsOrdered.removeLast();

            this.elements.put(
                    key,
                    this.elementsOrdered.addFirst(value, key)
            );

        } else
            this.elements.put(
                    key,
                    this.elementsOrdered.addFirst(value, key)
            );


    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {

        final JSONObject jsonObject = new JSONObject();

        try {

            final Class<LRUCache_Optimal<T>> selfReflection = (Class<LRUCache_Optimal<T>>) this.getClass();

            for (final Field field : selfReflection.getDeclaredFields())
                jsonObject.put(
                        field.getName(),
                        field.get(this)
                );

            jsonObject.put("size", this.elements.size());

            return jsonObject.toString(2);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }


    private static final class CachedLinkedList<T> {

        Node<T> head = null, tail = null;

        int size = 0;

        CachedLinkedList() {
        }

        Node<T> addFirst(@NotNull final T toAdd, final int key) {

            final Node<T> newNode = new Node<T>(toAdd, key);

            if (this.head == null) {
                this.head = newNode;
                this.tail = head;
            } else {
                newNode.next = this.head;
                this.head.prev = newNode;
                this.head = newNode;
            }

            size++;

            return newNode;

        }

        Node<T> addLast(@NotNull final T toAdd, final int key) {

            final Node<T> newNode = new Node<T>(toAdd, key);

            if (this.head == null)
                return this.addFirst(toAdd, key);
            else if (this.tail == this.head) {
                this.tail = newNode;
                this.tail.prev = this.head;
            } else {
                newNode.prev = this.tail;
                this.tail = newNode;
            }

            size++;

            return newNode;

        }

        void removeFirst() {

            if (size != 0) {
                this.head = this.head.next;
                this.head.prev = null;
            }

        }

        void removeLast() {

            if (size != 0) {
                this.tail = this.tail.prev;
                this.tail.next = null;
            }

        }

        void remove(@NotNull final Node<T> toRemove) {

            if (toRemove.equals(this.tail))
                this.removeLast();
            else if (toRemove.equals(this.head))
                this.removeFirst();
            else {
                /*
                    {1}
                    0 <-> 1 <-> 2
                   --------------
                    0 <- 1 -> 2
                    0 -> 2
                    1 <- 2
                    -------------
                    0 <- 1 -> 2
                    0 <-> 2
                    ---------
                    null <- 1 -> null (for garbage collection)
                 */
                toRemove.prev.next = toRemove.next;
                toRemove.next.prev = toRemove.prev;

                toRemove.next = null;
                toRemove.prev = null;

            }

            if (this.size != 0)
                size--;

        }

        int getSize() {
            return size;
        }

        Node<T> getHead() {
            return head;
        }

        Node<T> getTail() {
            return tail;
        }

        @Override
        public String toString(){

            final JSONArray jsonArray = new JSONArray();

            try{

                for(Node<T> current = head; current != null; current = current.next)
                    jsonArray.put(current.toJSONObject());

                return jsonArray.toString();

            }
            catch(Exception ex){
                throw new RuntimeException(ex);
            }

        }
    }

    private static final class Node<T> {

        Node<T> prev = null, next = null;

        T value;

        final int key;

        Node(@NotNull final T value, final int key) {
            this.value = value;
            this.key = key;
        }

        T getValue() {
            return value;
        }

        int getKey() {
            return key;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {

            if (o == null)
                return false;
            else if (this == o)
                return true;
            else if (!(o instanceof Node))
                return false;

            final Node<T> node;

            try {
                node = (Node<T>) o;
            } catch (Exception ex) {
                return false;
            }

            return
                    this.value == node.value &&
                            this.next == node.next &&
                            this.prev == node.prev;

        }

        public JSONObject toJSONObject(){

            final JSONObject jsonObject = new JSONObject();

            try {
//                jsonObject.put("prev", this.prev == null ? null : this.prev.getValue());
//                jsonObject.put("next", this.next == null ? null : this.next.getValue());
                jsonObject.put("value", this.value);

                return jsonObject;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

        @Override
        public String toString() {

            final JSONObject jsonObject = new JSONObject();

            try {
//                jsonObject.put("prev", this.prev == null ? null : this.prev.getValue());
//                jsonObject.put("next", this.next == null ? null : this.next.getValue());
                jsonObject.put("value", this.value);

                return jsonObject.toString(2);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

    }
}
