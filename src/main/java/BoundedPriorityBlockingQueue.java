package src.main.java;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Класс BoundedPriorityBlockingQueue представляет ограниченную приоритетную
 * блокирующую очередь.
 * Этот класс наследует функциональность от PriorityBlockingQueue и добавляет
 * ограничение на ее размер.
 *
 * @param <E> Тип элементов в очереди.
 */
public class BoundedPriorityBlockingQueue<E> extends PriorityBlockingQueue<E> {
  private final int maxSize; // Максимальный размер очереди

  /**
   * Конструктор класса BoundedPriorityBlockingQueue.
   *
   * @param maxSize    Максимальный размер очереди.
   * @param comparator Компаратор для сравнения элементов в очереди.
   */
  public BoundedPriorityBlockingQueue(int maxSize, Comparator<? super E> comparator) {
    super(maxSize, comparator);
    this.maxSize = maxSize;
  }

  /**
   * Метод добавления элемента в очередь.
   * Если очередь полная, новый элемент будет отклонен.
   *
   * @param e Элемент для добавления.
   * @return true, если элемент был успешно добавлен, false в противном случае.
   */
  @Override
  public synchronized boolean offer(E e) {
    // Если очередь полная, отклоняем новый элемент
    if (this.size() >= maxSize) {
      return false;
    }
    return super.offer(e);
  }

  /**
   * Метод извлечения элемента из очереди.
   *
   * @return Извлеченный элемент или null, если очередь пуста.
   */
  @Override
  public synchronized E poll() {
    return super.poll();
  }

  /**
   * Метод просмотра первого элемента в очереди без его удаления.
   *
   * @return Первый элемент в очереди или null, если очередь пуста.
   */
  @Override
  public synchronized E peek() {
    return super.peek();
  }
}
