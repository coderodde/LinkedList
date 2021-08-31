package com.github.coderodde.util;

/**
 * This class implements a simple hash set for non-negative {@code int} values.
 * It is used in the {@link com.github.coderodde.util.LinkedList} in order to 
 * keep track of nodes that are being pointed to by fingers.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 29, 2021)
 * @since 1.6 (Aug 29, 2021)
 */
class IntHashSet {

    private static final int INITIAL_CAPACITY = 8;
    private static final float MAXIMUM_LOAD_FACTOR = 0.75f;

    static final class IntHashTableCollisionChainNode {
        IntHashTableCollisionChainNode next;
        int integer;

        IntHashTableCollisionChainNode(
                int integer, 
                IntHashTableCollisionChainNode next) {
            this.integer = integer;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Chain node, integer = " + integer;
        }
    }
    
    @Override
    public String toString() {
        return "size = " + size;
    }

    void add(int integer) {
        if (contains(integer)) {
            return;
        }

        size++;

        if (shouldExpand())
            expand();

        final int targetCollisionChainIndex = integer & mask;
        final IntHashTableCollisionChainNode newNode = 
                new IntHashTableCollisionChainNode(
                        integer, 
                        table[targetCollisionChainIndex]);

        newNode.next = table[targetCollisionChainIndex];
        table[targetCollisionChainIndex] = newNode;
    }

    boolean contains(int integer) {
        final int collisionChainIndex = integer & mask;
        IntHashTableCollisionChainNode node = table[collisionChainIndex];

        while (node != null) {
            if (node.integer == integer) {
                return true;
            }

            node = node.next;
        }

        return false;
    }

    void remove(int integer) {
        if (!contains(integer)) {
            return;
        }

        size--;

        if (shouldContract()) 
            contract();

        final int targetCollisionChainIndex = integer & mask;

        IntHashTableCollisionChainNode current = 
                table[targetCollisionChainIndex];

        IntHashTableCollisionChainNode previous = null;

        while (current != null) {
            IntHashTableCollisionChainNode next = current.next;

            if (current.integer == integer) {
                if (previous == null) {
                    table[targetCollisionChainIndex] = next;
                } else {
                    previous.next = next;
                }

                return;
            }

            previous = current;
            current = next;
        }
    }

    public void clear() {
         size = 0;
         table = new IntHashTableCollisionChainNode[INITIAL_CAPACITY];
         mask = table.length - 1;
    }

    private IntHashTableCollisionChainNode[] table = 
            new IntHashTableCollisionChainNode[INITIAL_CAPACITY];

    private int size = 0;
    private int mask = INITIAL_CAPACITY - 1;

    // Keep add(int) an amortized O(1)
    private boolean shouldExpand() {
        return size > table.length * MAXIMUM_LOAD_FACTOR;
    }

    // Keep remove(int) an amortized O(1)
    private boolean shouldContract() {
        if (table.length == INITIAL_CAPACITY) {
            return false;
        }

        return MAXIMUM_LOAD_FACTOR * size * 4 < table.length;
    }

    private void expand() {
        IntHashTableCollisionChainNode[] newTable = 
                new IntHashTableCollisionChainNode[table.length * 2];

        rehash(newTable);
        table = newTable;
        mask = table.length - 1;
    }

    private void contract() {
        IntHashTableCollisionChainNode[] newTable = 
                new IntHashTableCollisionChainNode[table.length / 4];

        rehash(newTable);
        table = newTable;
        mask = table.length - 1;
    }

    private void rehash(IntHashTableCollisionChainNode[] newTable) {
        for (IntHashTableCollisionChainNode node : table) {
            while (node != null) {
                final IntHashTableCollisionChainNode next = node.next;
                final int rehashedIndex = rehash(node.integer, newTable);

                node.next = newTable[rehashedIndex];
                newTable[rehashedIndex] = node;
                node = next;
            }
        }
    }

    private static int rehash(
            int integer, 
            IntHashTableCollisionChainNode[] newTable) {
        return integer & (newTable.length - 1);
    }
}
