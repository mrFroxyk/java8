package src.main.java;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;

/**
 * Главный класс приложения, отвечающий за запуск и управление симуляцией работы
 * парикмахерской.
 */
public class Main {
    private static final String HELP_TEXT = """
            Меню:
            1. Записать значения по умолчанию
            2. Начать симуляцию
            3. Выйти
            Выберите опцию:
                """;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            XMLConfigManager configManager = new XMLConfigManager("config.xml"); // Менеджер конфигурации

            while (true) {
                System.out.print(HELP_TEXT);

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        configManager.writeDefaultConfig();
                        System.out.println("Записаны значения по умолчанию.");
                        break;
                    case 2:
                        configManager.loadConfig();
                        startSimulation(configManager);
                        break;
                    case 3:
                        System.out.println("Выход...");
                        return;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    /**
     * Метод, запускающий симуляцию работы парикмахерской.
     *
     * @param configManager Менеджер конфигурации, содержащий настройки симуляции.
     */
    private static void startSimulation(XMLConfigManager configManager) {
        int timeSlot = configManager.getTimeSlot(); // Временной промежуток на стрижку
        int chairs = configManager.getChairs(); // Количество стульев

        // Создание парикмахерской и парикмахера
        BarberShop barberShop = new BarberShop(chairs);
        Barber barber = new Barber(barberShop);
        Thread barberThread = new Thread(barber);
        barberThread.start();

        AtomicInteger clientIdGenerator = new AtomicInteger(0);
        ExecutorService clientGenerator = Executors.newFixedThreadPool(2);
        clientGenerator.submit(new ClientGenerator(barberShop, barber, true, clientIdGenerator, timeSlot));
        clientGenerator.submit(new ClientGenerator(barberShop, barber, false, clientIdGenerator, timeSlot));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientGenerator.shutdownNow(); // Завершаем генерацию клиентов
        barber.stop(); // Останавливаем работу парикмахера
    }
}
