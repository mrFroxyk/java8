package src.main.java;

/**
 * Класс Client представляет клиента парикмахерской.
 * Клиент может быть обычным или VIP-клиентом.
 */
public class Client {
    private final int id;
    private final boolean isVIP;
    private final BarberShop shop;
    private final Barber barber;
    private final int haircutTime;

    /**
     * Конструктор класса Client.
     *
     * @param id          Уникальный идентификатор клиента.
     * @param isVIP       Признак VIP-клиента.
     * @param shop        Парикмахерская, в которую входит клиент.
     * @param barber      Парикмахер, обслуживающий клиента.
     * @param haircutTime Время, необходимое на стрижку.
     */
    public Client(int id, boolean isVIP, BarberShop shop, Barber barber, int haircutTime) {
        this.id = id;
        this.isVIP = isVIP;
        this.shop = shop;
        this.barber = barber;
        this.haircutTime = haircutTime;
    }

    /**
     * Метод, определяющий, является ли клиент VIP-клиентом.
     *
     * @return true, если клиент является VIP-клиентом, в противном случае - false.
     */
    public boolean isVIP() {
        return isVIP;
    }

    /**
     * Метод получения уникального идентификатора клиента.
     *
     * @return Уникальный идентификатор клиента.
     */
    public int getId() {
        return id;
    }

    /**
     * Метод получения парикмахера, обслуживающего клиента.
     *
     * @return Парикмахер, обслуживающий клиента.
     */
    public Barber getBarber() {
        return barber;
    }

    /**
     * Метод, который имитирует процесс стрижки клиента.
     *
     * @throws InterruptedException Если поток был прерван во время ожидания.
     */
    public void getHaircut() throws InterruptedException {
        System.out.println(this + " стрижется.");
        Thread.sleep(haircutTime);
        System.out.println(this + " закончил стрижку.");
    }

    /**
     * Метод, который позволяет клиенту войти в парикмахерскую.
     */
    public void enterShop() {
        shop.enterShop(this);
    }

    /**
     * Переопределенный метод toString для удобного вывода информации о клиенте.
     *
     * @return Строковое представление объекта Client.
     */
    @Override
    public String toString() {
        return (isVIP ? "VIP-клиент" : "Обычный клиент") + " с id " + id;
    }
}
