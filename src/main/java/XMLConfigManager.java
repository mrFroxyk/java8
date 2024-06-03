package src.main.java;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * Класс XMLConfigManager для управления конфигурацией приложения с
 * использованием XML-файла.
 * Этот класс позволяет загружать настройки из XML-файла и записывать значения
 * по умолчанию в файл.
 */
public class XMLConfigManager {
  private String filePath;
  private int timeSlot;
  private int chairs;

  /**
   * Конструктор, инициализирующий путь к конфигурационному файлу.
   *
   * @param filePath Путь к конфигурационному файлу.
   */
  public XMLConfigManager(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Метод загрузки конфигурации из XML-файла.
   * Если файл не существует или содержит неверные данные, записываются значения
   * по умолчанию.
   */
  public void loadConfig() {
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        writeDefaultConfig();
        System.out.println("Конфигурационный файл не найден. Записаны значения по умолчанию.");
        return;
      }

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);

      doc.getDocumentElement().normalize();

      this.timeSlot = Integer.parseInt(doc.getElementsByTagName("TimeSlot").item(0).getTextContent());
      this.chairs = Integer.parseInt(doc.getElementsByTagName("Chairs").item(0).getTextContent());

      if (this.timeSlot <= 0 || this.chairs <= 0) {
        writeDefaultConfig();
        System.out.println(
            """
                Конфигурационный файл заполнен неверно: Время стрижки и количество стульев
                должно быть положительным числом Записаны значения по умолчанию.
                """);
      }
    } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
      writeDefaultConfig();
      System.out.println("Ошибка при чтении конфигурационного файла. Записаны значения по умолчанию.");
    } catch (NumberFormatException e) {
      writeDefaultConfig();
      System.out.println("Ошибка при чтении конфигурационного файла: указано слишком большое число");
      System.out.println(e.getMessage());
    }
  }

  /**
   * Метод записи значений по умолчанию в конфигурационный файл.
   */
  public void writeDefaultConfig() {
    try {
      File file = new File(filePath);
      File parentDir = file.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs();
      }

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElement("BarberShopConfig");
      doc.appendChild(rootElement);

      Element timeSlotElement = doc.createElement("TimeSlot");
      timeSlotElement.appendChild(doc.createTextNode("3000"));
      rootElement.appendChild(timeSlotElement);

      Element chairsElement = doc.createElement("Chairs");
      chairsElement.appendChild(doc.createTextNode("4"));
      rootElement.appendChild(chairsElement);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(file);
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException e) {
      e.printStackTrace();
    }
    loadConfig();
  }

  /**
   * Получение значения временного интервала.
   *
   * @return Временной интервал (в миллисекундах).
   */
  public int getTimeSlot() {
    return timeSlot;
  }

  /**
   * Получение количества стульев.
   *
   * @return Количество стульев.
   */
  public int getChairs() {
    return chairs;
  }
}
