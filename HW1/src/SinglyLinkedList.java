/**
 * Your implementation of a non-circular singly linked list with a tail pointer.
 *
 * @author Dasom Eom
 * @version 1.0
 */
public class SinglyLinkedList<T> {
    // Do not add new instance variables or modify existing ones.
    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;
    private int size;

    /**
     * Adds the element to the index specified.
     *
     * Adding to indices 0 and {@code size} should be O(1), all other cases are
     * O(n).
     *
     * @param index the requested index for the new element
     * @param data the data for the new element
     * @throws java.lang.IndexOutOfBoundsException if index is negative or
     * index > size
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative.");
        } else if (index > size) {
            throw new IndexOutOfBoundsException("Index "
                    + "cannot be bigger than size.");
        } else if (data == null) {
            throw new IllegalArgumentException("Null data cannot be added");
        }
        if (index == 0) {
            addToFront(data);
        } else if (index == size) {
            addToBack(data);
        } else {
            LinkedListNode<T> temp = new LinkedListNode<>(data);
            LinkedListNode<T> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.getNext();
            }
            temp.setNext(prev.getNext());
            prev.setNext(temp);
            size++;
        }



    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1) for all cases.
     *
     * @param data the data for the new element
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }
        head = new LinkedListNode<T>(data, head);
        if (isEmpty()) {
            tail = head;
        }
        size++;
    }
    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1) for all cases.
     *
     * @param data the data for the new element
     * @throws java.lang.IllegalArgumentException if data is null.
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }
        LinkedListNode<T> temp = new LinkedListNode<T>(data);
        if (isEmpty()) {
            head = temp;
            tail = head;
        } else {
            tail.setNext(temp);
            tail = tail.getNext();
        }
        size++;
    }

    /**
     * Removes and returns the element from the index specified.
     *
     * Removing from index 0 should be O(1), all other cases are O(n).
     *
     * @param index the requested index to be removed
     * @return the data formerly located at index
     * @throws java.lang.IndexOutOfBoundsException if index is negative or
     * index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index cannot be negative");
        } else if (index >= size) {
            throw new IndexOutOfBoundsException("index "
                    + "cannot be bigger than size");
        } else if (index == 0) {
            return removeFromFront();
        } else if (index == size - 1) {
            return removeFromBack();
        } else {
            LinkedListNode<T> temp = head;
            for (int i = 0; i < (index - 1); i++) {
                temp = temp.getNext();
            }
            T data = temp.getNext().getData();
            temp.setNext(temp.getNext().getNext());
            size--;
            return data;
        }

    }

    /**
     * Removes and returns the element at the front of the list. If the list is
     * empty, return {@code null}.
     *
     * Must be O(1) for all cases.
     *
     * @return the data formerly located at the front, null if empty list
     */
    public T removeFromFront() {
        if (isEmpty()) {
            return null;
        } else if (size == 1) {
            T data = head.getData();
            head = null;
            tail = head;
            size--;
            return data;
        } else {
            T data = head.getData();
            head = head.getNext();
            size--;
            return data;
        }
    }

    /**
     * Removes and returns the element at the back of the list. If the list is
     * empty, return {@code null}.
     *
     * Must be O(n) for all cases.
     *
     * @return the data formerly located at the back, null if empty list
     */
    public T removeFromBack() {
        if (isEmpty()) {
            return null;
        } else if (size == 1) {
            T data = tail.getData();
            tail = null;
            head = tail;
            size--;
            return data;
        } else {
            T data = tail.getData();
            LinkedListNode<T> temp = head;
            while (temp.getNext().getNext() != null) {
                temp = temp.getNext();
            }
            temp.setNext(null);
            tail = temp;
            size--;
            return data;

        }
    }

    /**
     * Returns the index of the first occurrence of the passed in data in the
     * list or -1 if it is not in the list.
     *
     * If data is in the head, should be O(1). In all other cases, O(n).
     *
     * @param data the data to search for
     * @throws java.lang.IllegalArgumentException if data is null
     * @return the index of the first occurrence or -1 if not in the list
     */
    public int indexOf(T data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        } else {
            LinkedListNode<T> temp = head;
            int index = 0;
            while (temp != null) {
                if (temp.getData().equals(data)) {
                    return index;
                }
                index++;
                temp = temp.getNext();
            }
            if (index == size) {
                return -1;
            } else {
                return index;
            }
        }
    }

    /**
     * Returns the element at the specified index.
     *
     * Getting the head and tail should be O(1), all other cases are O(n).
     *
     * @param index the index of the requested element
     * @return the object stored at index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or
     * index >= size
     */
    public T get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index cannot be negative");
        } else if (index >= size) {
            throw new IndexOutOfBoundsException("index"
                    + " cannot be bigger than size");
        } else if (index == 0) {
            return head.getData();
        } else if (index == size - 1) {
            return tail.getData();
        } else {
            LinkedListNode<T> temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.getNext();
            }
            return temp.getData();
        }

    }

    /**
     * Returns an array representation of the linked list.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length {@code size} holding all of the objects in
     * this list in the same order
     */
    public Object[] toArray() {
        LinkedListNode<T> temp = head;
        Object[] list = new Object[size];
        for (int i = 0; i < size; i++) {
            list[i] = temp.getData();
            temp = temp.getNext();
        }
        return list;

    }

    /**
     * Returns a boolean value indicating if the list is empty.
     *
     * Must be O(1) for all cases.
     *
     * @return true if empty; false otherwise
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Clears the list of all data and resets the size.
     *
     * Must be O(1) for all cases.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * Runs in O(1) for all cases.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY!
        return size;
    }

    /**
     * Returns the head node of the linked list.
     * Normally, you would not do this, but it's necessary for testing purposes.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return node at the head of the linked list
     */
    public LinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail node of the linked list.
     * Normally, you would not do this, but it's necessary for testing purposes.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return node at the tail of the linked list
     */
    public LinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }
}