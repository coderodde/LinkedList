package com.github.coderodde.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class LinkedListTest {

    private final LinkedList<Integer> list = new LinkedList<>();

    private static String getBar(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--- LinkedList.");
        stringBuilder.append(text);
        stringBuilder.append(" ---");
        return stringBuilder.toString();
    }
    
    private static void bar(String text) {
        System.out.println(getBar(text));
    }
    
    @Before
    public void setUp() {
        list.clear();
    }

    @Test
    public void add() {
        bar("add");
        
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());

        list.add(1);

        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        assertEquals(Integer.valueOf(1), list.get(0));

        list.add(2);

        assertEquals(2, list.size());
        assertFalse(list.isEmpty());

        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
        System.out.println(getBar("add done!"));
    }

    @Test
    public void addFirst() {
        bar("addFirst");
        
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());

        list.addFirst(1);

        assertEquals(1, list.size());
        assertFalse(list.isEmpty());

        assertEquals(Integer.valueOf(1), list.get(0));

        list.addFirst(2);

        assertEquals(2, list.size());
        assertFalse(list.isEmpty());

        assertEquals(Integer.valueOf(2), list.get(0));
        assertEquals(Integer.valueOf(1), list.get(1));
        
        bar("addFirst done!");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void throwsOnAccessingEmptyList() {
        list.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class) 
    public void throwsOnNegativeIndexInEmptyList() {
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class) 
    public void throwsOnNegativeIndexInNonEmptyList() {
        list.addFirst(10);
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class) 
    public void throwsOnTooLargeIndex() {
        list.addFirst(10);
        list.addLast(20);
        list.get(2);
    }

    @Test
    public void addIndexAndElement() {
        bar("addIndexedAndElement");
        
        list.add(0, 1);
        assertEquals(Integer.valueOf(1), list.get(0));

        list.add(0, 2);
        assertEquals(Integer.valueOf(2), list.get(0));
        assertEquals(Integer.valueOf(1), list.get(1));

        list.add(2, 10);

        assertEquals(Integer.valueOf(2), list.get(0));
        assertEquals(Integer.valueOf(1), list.get(1));
        assertEquals(Integer.valueOf(10), list.get(2));

        list.add(2, 100);

        assertEquals(Integer.valueOf(2), list.get(0));
        assertEquals(Integer.valueOf(1), list.get(1));
        assertEquals(Integer.valueOf(100), list.get(2));
        assertEquals(Integer.valueOf(10), list.get(3));
        
        bar("addIndexedAndElement done!");
    }

    @Test
    public void addCollectionOneElementToEmptyList() {
        bar("addCollectionOneElementToEmptyList");
        
        List<Integer> c = new ArrayList<>();
        c.add(100);

        list.addAll(c);

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(Integer.valueOf(100), list.get(0));
        
        bar("addCollectionOneElementToEmptyList done!");
    }

    @Test
    public void addCollectionThreeElementsToEmptyList() {
        bar("addCollectionThreeElementsToEmptyList");
        
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());

        List<Integer> c = Arrays.asList(1, 2, 3);

        list.addAll(c);
        assertFalse(list.isEmpty());
        assertEquals(3, list.size());

        for (int i = 0; i < list.size(); i++) {
            assertEquals(Integer.valueOf(i + 1), list.get(i));
        }
        
        bar("addCollectionThreeElementsToEmptyList done!");
    }

    @Test
    public void addCollectionAtIndex() {
        bar("testAddCollectionAtIndex");
        
        list.addAll(0, Arrays.asList(2, 3)); // setAll
        list.checkInvariant();
        list.addAll(0, Arrays.asList(0, 1)); // prependAll
        list.checkInvariant();
        list.addAll(4, Arrays.asList(6, 7)); // appendAll
        list.checkInvariant();
        list.addAll(4, Arrays.asList(4, 5)); // insertAll
        list.checkInvariant();

        for (int i = 0; i < list.size(); i++) {
            assertEquals(Integer.valueOf(i), list.get(i));
        }
        
        bar("testAddCollectionAtIndex done!");
    }

    @Test
    public void removeInt() {
        bar("testRemoveInt");
        
        list.addAll(Arrays.asList(0, 1, 2, 3, 4));

        // [0, 1, 2, 3, 4]
        assertEquals(Integer.valueOf(0), list.remove(0));
        // [1, 2, 3, 4]
        assertEquals(Integer.valueOf(4), list.remove(3));
        // [1, 2, 3]
        assertEquals(Integer.valueOf(2), list.remove(1));
        // [1, 3]
        assertEquals(Integer.valueOf(1), list.remove(0));
        // [3]
        assertEquals(Integer.valueOf(3), list.remove(0));
        // []
        bar("testRemoveInt done!");
    }

    @Test
    public void basicIteratorUsage() {
        bar("basicIteratorUsage");
        
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        Iterator<Integer> iterator = list.iterator();

        for (int i = 0; i < 1000; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(Integer.valueOf(i), iterator.next());
        }

        assertFalse(iterator.hasNext());
        
        bar("basicIteratorUsage done!");
    }

    @Test
    public void bruteForceAddCollectionAtIndex() {
        long seed = System.currentTimeMillis();
        
        bar("bruteForceAddCollectionAtIndex: seed = " + seed);
        
        Random random = new Random(seed);

        list.addAll(getIntegerList());

        java.util.LinkedList<Integer> referenceList = 
                new java.util.LinkedList<>(list);

        for (int op = 0; op < 100; op++) {
            int index = random.nextInt(list.size());
            Collection<Integer> coll = getIntegerList(random.nextInt(40));

            referenceList.addAll(index, coll);
            list.addAll(index, coll);

            if (!listsEqual(list, referenceList)) {
                fail("Lists not equal!");
            }
        }
        
        bar("bruteForceAddCollectionAtIndex done!");
    }

    @Test
    public void removeAtIndex() {
        bar("removeAtIndex");
        
        list.addAll(Arrays.asList(0, 1, 2, 3, 4));
        list.checkInvariant();
        
        // [0, 1, 2, 3, 4]
        assertEquals(Integer.valueOf(2), list.remove(2));
        list.checkInvariant();
        // [0, 1, 3, 4]
        assertEquals(Integer.valueOf(0), list.remove(0));
        list.checkInvariant();
        // [1, 3, 4]
        assertEquals(Integer.valueOf(4), list.remove(2));
        list.checkInvariant();
        // [1, 3]
        assertEquals(Integer.valueOf(3), list.remove(1));
        list.checkInvariant();
        // [1]
        assertEquals(Integer.valueOf(1), list.remove(0));
        list.checkInvariant();
        // []
        
        bar("removeAtIndex done!");
    }

    @Test
    public void removeObject() {
        bar("removeObject");
        
        list.addAll(Arrays.asList(0, 1, 2, 3, 4));

        assertFalse(list.remove(Integer.valueOf(10)));
        assertFalse(list.remove(null));

        list.add(3, null);

        assertTrue(list.remove(null));

        assertTrue(list.remove(Integer.valueOf(4)));
        assertTrue(list.remove(Integer.valueOf(0)));
        assertTrue(list.remove(Integer.valueOf(2)));
        assertFalse(list.remove(Integer.valueOf(2)));
        
        bar("removeObject done!");
    }

    @Test
    public void basicIteratorTraversal() {
        bar("basicIteratorTraversal");
        
        list.addAll(Arrays.asList(0, 1, 2, 3, 4));

        Iterator<Integer> iter = list.iterator();

        for (int i = 0; i < list.size(); i++) {
            assertTrue(iter.hasNext());
            assertEquals(Integer.valueOf(i), iter.next());
        }

        iter = list.iterator();

        class MyConsumer implements Consumer<Integer> {

            int total;

            @Override
            public void accept(Integer t) {
                total += t;
            }
        }

        MyConsumer myConsumer = new MyConsumer();

        list.iterator().forEachRemaining(myConsumer);
        assertEquals(10, myConsumer.total);
        
        bar("basicIteratorTraversal done!");
    }

    @Test
    public void basicIteratorRemoval() {
        bar("basicIteratorRemoval");
        
        list.addAll(Arrays.asList(0, 1, 2, 3, 4));
        Iterator<Integer> iter = list.iterator();

        iter.next();
        iter.next();
        iter.remove();

        assertEquals(4, list.size());

        iter = list.iterator();
        iter.next();
        iter.remove();

        assertEquals(3, list.size());

        assertEquals(Integer.valueOf(2), list.get(0));
        assertEquals(Integer.valueOf(3), list.get(1));
        assertEquals(Integer.valueOf(4), list.get(2));
        
        bar("basicIteratorRemoval done!");
    }

    @Test
    public void enhancedIteratorTraversal() {
        bar("enhancedIteratorTraversal");
        
        list.addAll(Arrays.asList(0, 1, 2, 3, 4));
        ListIterator<Integer> iter = list.listIterator();

        assertFalse(iter.hasPrevious());

        for (int i = 0; i < list.size(); i++) {
            assertTrue(iter.hasNext());
            assertEquals(Integer.valueOf(i), iter.next());
        }

        assertFalse(iter.hasNext());

        for (int i = 4; i >= 0; i--) {
            assertTrue(iter.hasPrevious());
            assertEquals(Integer.valueOf(i), iter.previous());
        }

        iter = list.listIterator(2);

        assertEquals(Integer.valueOf(2), iter.next());
        assertEquals(Integer.valueOf(2), iter.previous());

        iter = list.listIterator(3);

        assertEquals(Integer.valueOf(3), iter.next());
        assertEquals(Integer.valueOf(4), iter.next());

        assertFalse(iter.hasNext());
        assertTrue(iter.hasPrevious());
        
        bar("enhancedIteratorTraversal done!");
    }
    
    // Used to find a failing removal sequence:
    @Test
    public void removeAtFindFailing() {
        long seed = 1630297518343L; // System.currentTimeMillis();
        seed = 1630300708410L; // System.currentTimeMillis();
        bar("removeAtFindFailing: seed = " + seed);
        
        Random random = new Random(seed);
        
        while (true) {
            list.addAll(getIntegerList(10));
            
            List<Integer> indices = new ArrayList<>();
            
            while (!list.isEmpty()) {
                int index = random.nextInt(list.size());
                indices.add(index);
                
                try {
                    list.checkInvariant();
                    list.remove(index);
                    list.checkInvariant();
                } catch (AssertionError ae) {
                    System.out.println(indices);
                    bar("removeAtFindFailing done!");
                    return;
                }
            }
        }
    }
    
    @Test
    public void removeAtIndex1() {
        bar("removeAtIndex1");
        
        list.addAll(getIntegerList(10));
        int[] indices = { 9, 3, 3, 3, 1, 0 };
        
        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            System.out.println("ye = " + index);
            list.checkInvariant();
            list.remove(index);
            list.checkInvariant();
        }
        
        bar("removeAtIndex1 done!");
    }

    @Test
    public void enhancedIteratorAddition() {
        bar("enhancedIteratorAddition");
        
        list.addAll(Arrays.asList(1, 2, 3));
        ListIterator<Integer> iter = list.listIterator();

        iter.add(0);

        while (iter.hasNext()) {
            iter.next();
        }

        iter.add(4);
        iter = list.listIterator();

        for (int i = 0; i < list.size(); i++) {
            assertEquals(Integer.valueOf(i), iter.next());
        }

        iter = list.listIterator(2);
        iter.add(10);

        assertEquals(Integer.valueOf(10), list.get(2));
        
        bar("enhancedIteratorAddition done!");
    }

//    @Test
    public void findFailingIterator() {
        System.out.println(getBar("findFailingIterator"));
        
        list.addAll(getIntegerList(345_850));
        Iterator<Integer> iterator = list.iterator();
        int counter = 0;

        while (iterator.hasNext()) {
            System.out.println("size = " + list.size());
            Integer integer = iterator.next();

            // Remove every 10th element:
            if (counter % 10 == 0) {
                    iterator.remove();
            }

            counter++;
        }
        
        System.out.println(getBar("findFailingIterator done!"));
    }

//    @Test
    public void bruteForceIteratorRemove() throws Exception {
        System.out.println(getBar("bruteForceIteratorRemove"));
        
        list.addAll(getIntegerList(1000));
 
        int counter = 1;
        List<Integer> arrayList = new ArrayList<>(list);
        Iterator<Integer> iter = list.iterator();
        Iterator<Integer> arrayListIter = arrayList.iterator();
        int totalIterations = 0;

        while (iter.hasNext()) {
            System.out.println("total iters: " + totalIterations);
            
            iter.next();
            arrayListIter.next();
            list.checkInvariant();
            
            
            if (counter % 10 == 0) {

                try {
                    iter.remove();
                } catch (IllegalStateException ex) {
                    throw new Exception(ex);
                }

                arrayListIter.remove();
                counter = 0;
            } else {
                counter++;
            }

            if (!listsEqual(list, arrayList)) {
                throw new IllegalStateException(
                        "totalIterations = " + totalIterations);
            }

            totalIterations++;
        }
        
        System.out.println(getBar("bruteForceIteratorRemove done!"));
    }

////    @Test
//    public void bruteForceRemoveObjectBeforeIteratorRemove() {
//        System.out.println(
//                getBar("bruteForceRemoveObjectBeforeIteratorRemove"));
//        
//        LinkedList<String> ll = new com.github.coderodde.util.LinkedList<>();
//
//        ll.add("a");
//        ll.add("b");
//        ll.add("c");
//        ll.add("d");
//
//        ll.remove("b");
//        ll.remove("c");
//        ll.remove("d");
//        ll.remove("a");
//        
//        System.out.println(
//                getBar("bruteForceRemoveObjectBeforeIteratorRemove"));
//    }

//    @Test
    public void findFailingRemoveObject() {
        System.out.println(getBar("findFailingRemoveObject"));
        
        java.util.LinkedList<Integer> referenceList = 
                new java.util.LinkedList<>();

        list.addAll(getIntegerList(10));
        referenceList.addAll(list);

        Integer probe = list.get(1);

        list.remove(probe);
        referenceList.remove(probe);

        Iterator<Integer> iterator1 = list.iterator();
        Iterator<Integer> iterator2 = referenceList.iterator();

        Random random = new Random(100L);

        while (!list.isEmpty()) {
            if (!iterator1.hasNext()) {

                if (iterator2.hasNext()) {
                    throw new IllegalStateException();
                }

                iterator1 = list.iterator();
                iterator2 = referenceList.iterator();
                continue;
            }

            iterator1.next();
            iterator2.next();

            if (random.nextBoolean()) {
                iterator1.remove();
                iterator2.remove();
                assertTrue(listsEqual(list, referenceList));
            }
        }

        assertTrue(listsEqual(list, referenceList));

        System.out.println(getBar("findFailingRemoveObject done!"));
    }

    @Test
    public void iteratorAdd() {
        bar("iteratorAdd");
        
        list.addAll(getIntegerList(4));

        ListIterator<Integer> iterator = list.listIterator(1);

        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());

        iterator.next();

        assertEquals(2, iterator.nextIndex());
        assertEquals(1, iterator.previousIndex());

        iterator.add(Integer.valueOf(100));

        assertEquals(Integer.valueOf(0), list.get(0));
        assertEquals(Integer.valueOf(1), list.get(1));
        assertEquals(Integer.valueOf(100), list.get(2));
        assertEquals(Integer.valueOf(2), list.get(3));
        assertEquals(Integer.valueOf(3), list.get(4));
        
        bar("iteratorAdd done!");
    }

//    @Test
    public void bruteForceIteratorTest() {
        bar("bruteForceIteratorTest");
        
        list.addAll(getIntegerList(100));
        List<Integer> referenceList = new java.util.LinkedList<>(list);

        ListIterator<Integer> iterator1 = list.listIterator(2);
        ListIterator<Integer> iterator2 = referenceList.listIterator(2);

        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("- bruteForceIteratorTest: seed = " + seed);

        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                fail("Iterator mismatch on hasNext().");
            }

            iterator1.next();
            iterator2.next();

            int choice = random.nextInt(10);

            if (choice < 2) {
                Integer integer = Integer.valueOf(random.nextInt(100));
                iterator1.add(integer);
                iterator2.add(integer);
                assertTrue(listsEqual(list, referenceList));
            } else if (choice == 2) {
                iterator1.remove();
                iterator2.remove();
                assertTrue(listsEqual(list, referenceList));
            } else if (choice < 6) {
                if (iterator1.hasPrevious()) {
                    iterator1.previous();
                }

                if (iterator2.hasPrevious()) {
                    iterator2.previous();
                }
            } else {
                if (iterator1.hasNext()) {
                    iterator1.next();
                }

                if (iterator2.hasNext()) {
                    iterator2.next();
                }
            }
        }

        if (iterator2.hasNext()) {
            fail("Java List iterator has more to offer.");
        }
        
        bar("bruteForceIteratorTest done!");
    }

    @Test
    public void indexOf() {
        bar("indexOf");
        
        list.add(1);
        list.add(2);
        list.add(3);

        list.add(3);
        list.add(2);
        list.add(1);

        assertEquals(0, list.indexOf(1));
        assertEquals(1, list.indexOf(2));
        assertEquals(2, list.indexOf(3));

        assertEquals(3, list.lastIndexOf(3));
        assertEquals(4, list.lastIndexOf(2));
        assertEquals(5, list.lastIndexOf(1));
        
        bar("indexOf done!");
    }

    class MyIntegerConsumer implements Consumer<Integer> {

        List<Integer> ints = new ArrayList<>();

        @Override
        public void accept(Integer t) {
            ints.add(t);
        }
    }

    @Test
    @SuppressWarnings("empty-statement")
    public void basicSpliteratorUsage() {
        bar("basicSpliteratorUsage");
        
        list.addAll(getIntegerList(10_000));

        Spliterator<Integer> spliterator1 = list.spliterator();
        Spliterator<Integer> spliterator2 = spliterator1.trySplit();

        //// spliterator 2 : spliterator 1

        assertEquals(5000, spliterator1.getExactSizeIfKnown());
        assertEquals(5000, spliterator2.getExactSizeIfKnown());


        assertTrue(spliterator2.tryAdvance(
                i -> assertEquals(list.get(0), Integer.valueOf(0))));

        assertTrue(spliterator2.tryAdvance(
                i -> assertEquals(list.get(1), Integer.valueOf(1))));

        assertTrue(spliterator2.tryAdvance(
                i -> assertEquals(list.get(2), Integer.valueOf(2))));



        assertTrue(spliterator1.tryAdvance(
                i -> assertEquals(list.get(5000), Integer.valueOf(5000))));

        assertTrue(spliterator1.tryAdvance(
                i -> assertEquals(list.get(5001), Integer.valueOf(5001))));

        assertTrue(spliterator1.tryAdvance(
                i -> assertEquals(list.get(5002), Integer.valueOf(5002))));

        //// spliterator 3 : spliterator 2 : splitereator 1

        Spliterator<Integer> spliterator3 = spliterator2.trySplit();

        assertEquals(4997, spliterator1.getExactSizeIfKnown());

        assertTrue(spliterator3.tryAdvance(
                i -> assertEquals(list.get(3), Integer.valueOf(3))));

        assertTrue(spliterator3.tryAdvance(
                i -> assertEquals(list.get(4), Integer.valueOf(4))));

        assertTrue(spliterator3.tryAdvance(
                i -> assertEquals(list.get(5), Integer.valueOf(5))));

        //// 

        MyIntegerConsumer consumer = new MyIntegerConsumer();

        while (spliterator1.tryAdvance(consumer));

        for (int i = 0; i < consumer.ints.size(); i++) {
            Integer actualInteger = consumer.ints.get(i);
            Integer expectedInteger = 5003 + i;
            assertEquals(expectedInteger, actualInteger);
        }
        
        bar("basicSpliteratorUsage done!");
    }

    @Test
    public void spliteratorForEachRemaining() {
        bar("spliteratorForEachRemaining");
        
        list.addAll(getIntegerList(10_000));
        Spliterator<Integer> split = list.spliterator();
        MyIntegerConsumer consumer = new MyIntegerConsumer();

        split.forEachRemaining(consumer);

        for (int i = 0; i < 10_000; i++) {
            assertEquals(Integer.valueOf(i), consumer.ints.get(i));
        }
        
        bar("spliteratorForEachRemaining done!");
    }

    @Test
    public void spliteratorForEachRemainingTwoSpliterators() {
        bar("spliteratorForEachRemainingTwoSpliterators");
        
        list.addAll(getIntegerList(10_000));
        Spliterator<Integer> splitRight = list.spliterator();
        Spliterator<Integer> splitLeft = splitRight.trySplit();

        MyIntegerConsumer consumerRight = new MyIntegerConsumer();
        MyIntegerConsumer consumerLeft = new MyIntegerConsumer();

        splitRight.forEachRemaining(consumerRight);
        splitLeft.forEachRemaining(consumerLeft);

        for (int i = 0; i < 5_000; i++) {
            assertEquals(Integer.valueOf(i), consumerLeft.ints.get(i));
        }

        for (int i = 5_000; i < 10_000; i++) {
            assertEquals(Integer.valueOf(i), consumerRight.ints.get(i - 5_000));
        }
        
        bar("spliteratorForEachRemainingTwoSpliterators done!");
    }

    @Test
    public void spliteratorForEachRemainingWithAdvance() {
        bar("spliteratorForEachRemainingWithAdvance");
        
        list.addAll(getIntegerList(10_000));
        Spliterator<Integer> rightSpliterator = list.spliterator();

        assertTrue(
                rightSpliterator.tryAdvance(
                        i -> assertEquals(Integer.valueOf(0), i)));

        Spliterator<Integer> leftSpliterator = rightSpliterator.trySplit();

        assertEquals(4_999, rightSpliterator.getExactSizeIfKnown());
        assertEquals(5_000, leftSpliterator.getExactSizeIfKnown());

        // Check two leftmost elements of the left spliterator:
        assertTrue(leftSpliterator.tryAdvance(
                i -> assertEquals(Integer.valueOf(1), i)));

        assertTrue(leftSpliterator.tryAdvance(
                i -> assertEquals(Integer.valueOf(2), i)));

        // Check two leftmost elements of the right splliterator:
        assertTrue(rightSpliterator.tryAdvance(
                i -> assertEquals(Integer.valueOf(5_000), i)));

        assertTrue(rightSpliterator.tryAdvance(
                i -> assertEquals(Integer.valueOf(5_001), i)));

        bar("spliteratorForEachRemainingWithAdvance done!");
    }

    @Test
    public void spliterator() {
        bar("spliterator");
        
        list.addAll(getIntegerList(6_000));
        Spliterator split = list.spliterator();

        assertEquals(6_000L, split.getExactSizeIfKnown());
        assertEquals(6_000L, split.estimateSize());

        assertTrue(split.tryAdvance((i) -> assertEquals(list.get((int) i), i)));
        assertTrue(split.tryAdvance((i) -> assertEquals(list.get((int) i), i)));

        assertEquals(5998, split.getExactSizeIfKnown());

        // 5998 elements left / 2 = 2999 per spliterator:
        Spliterator leftSpliterator = split.trySplit();

        assertNotNull(leftSpliterator);
        assertEquals(2999, split.getExactSizeIfKnown());
        assertEquals(2999, leftSpliterator.getExactSizeIfKnown());

        //// leftSpliterator = [1, 2999]

        for (int i = 2; i < 3000; i++) {
            Integer integer = list.get(i);
            assertTrue(
                    leftSpliterator.tryAdvance(
                            (j) -> assertEquals(integer, j)));
        }

        //// split = [3001, 5999]

        assertTrue(split.tryAdvance(i -> assertEquals(2999, i)));
        assertTrue(split.tryAdvance(i -> assertEquals(3000, i)));
        assertTrue(split.tryAdvance(i -> assertEquals(3001, i)));

        while (split.getExactSizeIfKnown() > 0) {
            split.tryAdvance(i -> {});
        }

        assertFalse(split.tryAdvance(i -> {}));
        
        bar("spliterator done!");
    }

    @Test
    public void bruteforceSpliterator() {
        bar("bruteforceSpliterator");
        
        list.addAll(getIntegerList(1_000_000));
        Collections.<Integer>shuffle(list);

        List<Integer> newList = 
               list.parallelStream()
                   .map(i -> 2 * i)
                   .collect(Collectors.toList());

        assertEquals(newList.size(), list.size());

        for (int i = 0; i < list.size(); i++) {
            Integer integer1 = 2 * list.get(i);
            Integer integer2 = newList.get(i);
            assertEquals(integer1, integer2);
        }
        
        bar("bruteforceSpliterator done!");
    }

    private static final String SERIALIZATION_FILE_NAME = "LinkedList.ser";

    @Test
    public void serialization() {
        list.add(10);
        list.add(13);
        list.add(12);

        try {
            File file = new File(SERIALIZATION_FILE_NAME);

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(list);
            oos.flush();
            oos.close();

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            com.github.coderodde.util.LinkedList<Integer> ll =    
                    (com.github.coderodde.util.LinkedList<Integer>)
                    ois.readObject();

            ois.close();
            boolean equal = listsEqual(list, ll);
            assertTrue(equal);

            if (!file.delete()) {
                file.deleteOnExit();
            }

        } catch (IOException | ClassNotFoundException ex) {
            fail(ex.getMessage());
        }   
    }

    @Test
    public void bruteforceSerialization() {
        for (int i = 0; i < 20; i++) {
            list.addAll(getIntegerList(i));

            try {
                File file = new File(SERIALIZATION_FILE_NAME);

                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(list);
                oos.flush();
                oos.close();

                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                com.github.coderodde.util.LinkedList<Integer> ll =    
                        (com.github.coderodde.util.LinkedList<Integer>)
                        ois.readObject();

                ois.close();
                boolean equal = listsEqual(list, ll);
                assertTrue(equal);

                if (!file.delete()) {
                    file.deleteOnExit();
                }

            } catch (IOException | ClassNotFoundException ex) {
                fail(ex.getMessage());
            }   

            list.clear();
        }
    }
    
    @Test
    public void bugCheckInvariantAfterRemoval() {
        bar("bugCheckInvariantAfterRemoval");
        
        for (int i = 0; i < 4; i++) {
            list.add(i);
        }
        
        list.remove(Integer.valueOf(3));
        list.remove(1);
        assertEquals(list.size(), 2);
        assertEquals(Integer.valueOf(0), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
        
        bar("bugCheckInvariantAfterRemoval done!");
    }
    
//    @Test
    public void bruteForceRemoveAt1() {
        long seed = 1630132561853L; //System.currentTimeMillis();
        bar("bruteForceRemoveAt1: seed = " + seed);
        
        Random random = new Random(seed);
        
        list.addAll(getIntegerList(1000));
        List<Integer> referenceList = new ArrayList<>(list);
        
        Integer probe = Integer.valueOf(3);
        
        list.remove(probe);
        referenceList.remove(probe);
        
        int iters = 0;
        
        while (!list.isEmpty()) {
            System.out.println("iters = " + iters);
            iters++;
            
            int index = random.nextInt(list.size());
            list.remove(index);
            referenceList.remove(index);
            
            listsEqual(list, referenceList);
        } 
        
        bar("bruteForceRemoveA1t done!");
    }
    
//    @Test
    public void bruteForceRemoveAt2() {
        long seed = 1630133801156L; // System.currentTimeMillis();
        bar("bruteForceRemoveAt2: seed = " + seed);

        Random random = new Random(seed);
        int iters = 0;
        
        while (true) {
            list.addAll(getIntegerList(10));
            System.out.println("common: " + iters++);
            
            while (!list.isEmpty()) {
                int index = random.nextInt(list.size());
                System.out.println("random index = " + index);
                list.remove(index);
            }
        }
        
//        bar("bruteForceRemoveAt2 done!");
    }
    
//    @Test
    public void bugRemoveAt() {
        list.addAll(getIntegerList(10));
        
        list.checkInvariant();
        assertEquals(Integer.valueOf(5), list.remove(5));
        
        list.checkInvariant();
        assertEquals(Integer.valueOf(3), list.remove(3));
        
        list.checkInvariant();
        assertEquals(Integer.valueOf(2), list.remove(2));
        
        list.checkInvariant();
        assertEquals(Integer.valueOf(1), list.remove(1));
        
        list.checkInvariant();
        // list = [0, 4, 5, 7, 8, 8]
        assertEquals(Integer.valueOf(8), list.remove(4));
        
        list.checkInvariant();
    }
    
    // Should not throw anything:
    @Test
    public void bugRemoveFirst() {
        bar("bugRemoveFirst");
        
        list.addAll(getIntegerList(5));
        
        assertEquals(5, list.size());
        
        for (int i = 0; i < 2; i++) {
            list.removeFirst();
        }
        
        long seed = System.currentTimeMillis();
        bar("LinkedListTest.bugRemoveFirst: seed = " + seed);
        
        Random random = new Random(seed);
        List<Integer> referenceList = new ArrayList<>(list);
        
        while (!list.isEmpty()) {
            int index = random.nextInt(list.size());
            list.remove(index);
            referenceList.remove(index);
            assertTrue(listsEqual(list, referenceList));
        }
        
        bar("bugRemoveFirst done!");
    }
    
    // Should not throw anything:
    @Test
    public void bugRemoveLast() {
        bar("bugRemoveLast");
        
        list.addAll(getIntegerList(10));
        
        assertEquals(10, list.size());
        
        for (int i = 0; i < 5; i++) {
            list.removeLast();
        }
        
        long seed = System.currentTimeMillis();
        System.out.println("--- bugRemoveLast: seed = " + seed + " ---");
        Random random = new Random(seed);
        List<Integer> referenceList = new ArrayList<>(list);
        
        while (!list.isEmpty()) {
            int index = random.nextInt(list.size());
            list.remove(index);
            referenceList.remove(index);
            assertTrue(listsEqual(list, referenceList));
        }
        
        bar("bugRemoveLast done!");
    }

    private static boolean listsEqual(
            com.github.coderodde.util.LinkedList<Integer> list1, 
            java.util.List<Integer> list2) {

        if (list1.size() != list2.size()) {
            return false;
        }

        Iterator<Integer> iter1 = list1.iterator();
        Iterator<Integer> iter2 = list2.iterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            Integer int1 = iter1.next();
            Integer int2 = iter2.next();

            if (!int1.equals(int2)) {
                return false;
            }
        }

        if (iter1.hasNext() || iter2.hasNext()) {
            throw new IllegalStateException();
        }

        return true;
    }   

    private static List<Integer> getIntegerList() {
        return getIntegerList(100);
    }

    private static List<Integer> getIntegerList(int length) {
        List<Integer> list = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            list.add(i);
        }

        return list;
    }
}
