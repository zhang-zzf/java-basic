package com.github.learn.java.util.concurrent.blockingqueue;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedBlockingQueue<E> implements BlockingQueue<E> {

    // 单向链表
    static class Node<E> {

        private E item;
        Node<E> next;

        public Node(E e) {
            this.item = e;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private final int capacity;
    private final AtomicInteger counter = new AtomicInteger();

    private final Lock putLock = new ReentrantLock();
    private final Condition notFull = putLock.newCondition();

    private final Lock takeLock = new ReentrantLock();
    private final Condition notEmpty = takeLock.newCondition();

    public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.head = this.tail = new Node<>(null);
    }


    @Override
    public E take() throws InterruptedException {
        int c = -1;
        E x;
        final Lock takeLock = this.takeLock;
        final AtomicInteger counter = this.counter;

        takeLock.lockInterruptibly();
        try {
            while (counter.get() == 0) {
                notEmpty.await();
            }
            x = dequeue();
            c = counter.getAndDecrement();

            if (c > 1) {
                notEmpty.signal();
            }
        } finally {
            takeLock.lockInterruptibly();
        }

        if (c == capacity) {
            signalNotFull();
        }

        return x;
    }

    private void signalNotFull() {
        final Lock putLock = this.putLock;
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    @Override
    public void put(E e) throws InterruptedException {
        int c = -1;
        final Lock putLock = this.putLock;
        final AtomicInteger counter = this.counter;
        Node<E> x = new Node<>(e);

        putLock.lockInterruptibly();
        try {
            while (counter.get() == capacity) {
                notFull.await();
            }
            enqueue(x);
            c = counter.getAndIncrement(); // 获取原有队列大小，然后+1

            // todo 没有会如何？
            // 功能不影响
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }

        if (c == 0) {
            signalNotEmpty();
        }

    }

    private void signalNotEmpty() {
        final Lock takeLock = this.takeLock;
        // todo lock() or lockInterruptibly()
        // 必须用lock()
        // 数据已更新过，此时不能响应中断信号
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    private void enqueue(Node<E> x) {
        tail = tail.next = x;
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit)
        throws InterruptedException {
        return false;
    }

    private E dequeue() {
        Node<E> h = this.head;
        Node<E> first = h.next;
        h.next = null;

        E x = first.item;
        first.item = null;

        this.head = first;

        return x;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }
}
