package com.github.javachaos.chaosdungeons.utils;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Auto discarding blocking deque.
 *
 * @param <E> the type of elements in this queue
 */
public class AutoDiscardingDeque<E> extends LinkedBlockingDeque<E> {

  public AutoDiscardingDeque() {
    super();
  }

  public AutoDiscardingDeque(int capacity) {
    super(capacity);
  }

  @Override
  public synchronized boolean offerFirst(E e) {
    if (remainingCapacity() == 0) {
      removeLast();
    }
    super.offerFirst(e);
    return true;
  }

  @Override
  public synchronized boolean offerLast(E e) {
    if (remainingCapacity() == 0) {
      removeFirst();
    }
    super.offerLast(e);
    return true;
  }
}
