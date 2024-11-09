/*
 * The MIT License
 *
 * Copyright 2024 rodio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.coderodde.util;

/**
 * This static inner class implements the actual linked list node.
 *
 * @param <E> the type of the satellite data.
 */
final class Node<E> {

    /**
     * The actual satellite datum.
     */
    E item;

    /**
     * The previous node or {@code null} if this {@link Node} is the head of the
     * list.
     */
    Node<E> prev;

    /**
     * The next node or {@code null} if this {@link Node} is the tail of the
     * list.
     */
    Node<E> next;

    /**
     * Constructs a new {@link Node} object.
     *
     * @param item the satellite datum of the newly created {@link Node}.
     */
    Node(E item) {
        this.item = item;
    }

    /**
     * Returns the textual representation of this {@link Node}.
     *
     * @return the textual representation.
     */
    @Override
    public String toString() {
        return "[Node; item = " + item + "]";
    }
}
