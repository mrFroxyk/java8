package src.main.java;

/**
 * Класс Barber представляет парикмахера, который работает в парикмахерской.
 * Этот класс реализует интерфейс Runnable, чтобы парикмахер мог работать в отдельном потоке.
 */
public class Barber implements Runnable {
    private final BarberShop shop;
    private volatile boolean running = true;

    /**
     * Конструктор класса Barber.
     *
     * @param shop Парикмахерская, в которой работает парикмахер.
     */
    public Barber(BarberShop shop) {
        this.shop = shop;
    }

    /**
     * Метод run(), который запускается в отдельном потоке.
     * Парикмахер проверяет наличие клиентов и обслуживает их.
     * Если клиентов нет, парикмахер засыпает до появления новых клиентов.
     */
    @Override
    public void run() {
        while (running) {
            Client client = null;
            synchronized (this) {
                // Если нет клиентов и парикмахер продолжает работать, он засыпает
                while (!shop.hasClients() && running) {
                    try {
                        System.out.println("Парикмахер засыпает, так как нет клиентов.");
                        shop.setBarberWorking(false);
                        wait(); // Парикмахер засыпает
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // Получаем следующего клиента
                client = shop.getNextClient();
            }

            // Если работа парикмахера завершена, выходим из цикла
            if (!running) {
                break;
            }

            // Обслуживание клиента
            try {
                if (client != null) {
                    shop.setBarberWorking(true);
                    client.getHaircut();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Метод stop() завершает работу парикмахера.
     * Он устанавливает флаг running в false и будит парикмахера, если он спит.
     */
    public void stop() {
        running = false;
        synchronized (this) {
            notify(); // Будим парикмахера, чтобы завершить его работу
        }
    }
}
