package src.main.java;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс BarberShop представляет парикмахерскую, в которой работает парикмахер.
 * Здесь клиенты могут войти в парикмахерскую, сесть в очередь и ожидать своей очереди.
 */
public class BarberShop {
  private final BoundedPriorityBlockingQueue<Client> clientQueue;
  private final AtomicBoolean isBarberWorking;

  /**
   * Конструктор класса BarberShop.
   *
   * @param maxSeats Максимальное количество стульев в парикмахерской.
   */
  public BarberShop(int maxSeats) {
    // Создаем очередь клиентов с ограниченной емкостью и приоритетом по типу клиента
    this.clientQueue = new BoundedPriorityBlockingQueue<>(maxSeats, (c1, c2) -> {
      if (c1.isVIP() && !c2.isVIP())
        return -1;
      if (!c1.isVIP() && c2.isVIP())
        return 1;
      return Integer.compare(c1.getId(), c2.getId());
    });
    // Флаг, указывающий, работает ли парикмахер
    this.isBarberWorking = new AtomicBoolean(false);
  }

  /**
   * Метод, позволяющий клиенту войти в парикмахерскую.
   *
   * @param client Клиент, который входит в парикмахерскую.
   */
  public void enterShop(Client client) {
    synchronized (isBarberWorking) {
      // Если парикмахер не работает, клиент будит его и садится в очередь
      if (!isBarberWorking.get()) {
        isBarberWorking.set(true);
        System.out.println(client + " разбудил парикмахера.");
        clientQueue.offer(client);
        synchronized (client.getBarber()) {
          client.getBarber().notify();
        }
      } else {
        // Если парикмахер работает, клиент садится в очередь
        boolean added = clientQueue.offer(client);
        if (added) {
          System.out.println(client + " вошел в парикмахерскую и сел в очередь.");
        } else {
          // Если все стулья заняты, клиент уходит
          System.out.println(client + " уходит, так как все стулья заняты.");
        }
      }
    }
  }

  /**
   * Метод получения следующего клиента из очереди.
   *
   * @return Следующий клиент.
   */
  public Client getNextClient() {
    return clientQueue.poll();
  }

  /**
   * Метод проверки наличия клиентов в очереди.
   *
   * @return true, если в очереди есть клиенты, в противном случае - false.
   */
  public boolean hasClients() {
    return !clientQueue.isEmpty();
  }

  /**
   * Метод установки состояния работы парикмахера.
   *
   * @param working true, если парикмахер работает, в противном случае - false.
   */
  public void setBarberWorking(boolean working) {
    isBarberWorking.set(working);
  }
}
