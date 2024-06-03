package src.main.java;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс ClientGenerator генерирует клиентов и отправляет их в парикмахерскую.
 */
public class ClientGenerator implements Runnable {
    private final BarberShop shop;
    private final Barber barber;
    private final boolean isVIP;
    private final AtomicInteger clientIdGenerator;
    private final int haircutTime;

    /**
     * Конструктор класса ClientGenerator.
     *
     * @param shop              Парикмахерская, в которую будут входить клиенты.
     * @param barber            Парикмахер, обслуживающий клиентов.
     * @param isVIP             Флаг, указывающий, является ли клиент VIP-клиентом.
     * @param clientIdGenerator Генератор уникальных идентификаторов клиентов.
     * @param haircutTime       Время стрижки клиента.
     */
    public ClientGenerator(BarberShop shop, Barber barber, boolean isVIP, AtomicInteger clientIdGenerator,
            int haircutTime) {
        this.shop = shop;
        this.barber = barber;
        this.isVIP = isVIP;
        this.clientIdGenerator = clientIdGenerator;
        this.haircutTime = haircutTime;
    }

    /**
     * Метод run() создает новых клиентов, отправляет их в парикмахерскую и
     * определяет интервалы времени
     * между прибытием клиентов.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int clientId = clientIdGenerator.incrementAndGet();
            Client client = new Client(clientId, isVIP, shop, barber, haircutTime);
            client.enterShop();
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1500)); // случайное время поялвения клиентов
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
