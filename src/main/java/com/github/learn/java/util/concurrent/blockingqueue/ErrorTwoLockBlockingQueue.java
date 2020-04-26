package com.github.learn.java.util.concurrent.blockingqueue;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ErrorTwoLockBlockingQueue<E> implements BlockingQueue<E> {

    private final Object notEmpty = new Object();
    private final Object notFull = new Object();
    private Queue<E> data = new LinkedList<>();
    private final int capacity = 1;

    @Override
    public void put(E e) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": " + 1);
        synchronized (notEmpty) {
            System.out.println(Thread.currentThread().getName() + ": " + 2);
            if (data.size() == 0) {
                notEmpty.notifyAll();
            }
            synchronized (notFull) {
                System.out.println(Thread.currentThread().getName() + ": " + 3);
                while (data.size() == capacity) {
                    notFull.wait();
                }
            }
            data.add(e);
            System.out.println(Thread.currentThread().getName() + ": " + 4);
        }
        System.out.println(Thread.currentThread().getName() + ": " + 5);
    }

    @Override
    public E take() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": " + 11);
        synchronized (notEmpty) {
            System.out.println(Thread.currentThread().getName() + ": " + 12);
            if (data.size() == 0) {
                notEmpty.wait();
            }
            synchronized (notFull) {
                System.out.println(Thread.currentThread().getName() + ": " + 13);
                if (data.size() == capacity) {
                    notFull.notifyAll();
                }
            }
            System.out.println(Thread.currentThread().getName() + ": " + 14);
        }
        System.out.println(Thread.currentThread().getName() + ": " + 15);
        return null;
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


    @Override
    public boolean offer(E e, long timeout, TimeUnit unit)
        throws InterruptedException {
        return false;
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
