package helper;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class IoHelper {
    private static final Logger LOGGER = Logger.getLogger(IoHelper.class);

    /**
     * @param path куда писать.
     * @param data что писать.
     */
    public static void writeData(String path, String data) {
        try {
            Files.write(Paths.get(path), data.getBytes());
        } catch (IOException e) {
            LOGGER.error("Неудачная попытка создания файла: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * @param fileName путь до файла.
     * @return массив строк из файла.
     */
    public static String[] readFile(String fileName) {
        try {
            String allLines = new String(Files.readAllBytes(Paths.get(fileName)));
            return allLines.split(System.lineSeparator());
        } catch (IOException e) {
            LOGGER.error("Неудачная попытка чтения файла: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean validateArgs(String[] args) {
        if (args.length == 0) {
            LOGGER.error("Запуск без аргументов не возможен!");
            return false;
        } else if (args.length != 3) {
            LOGGER.error("Должно быть 3 аргумента: путь до считываемого файла, выходной файл(даты), выходной файл(офисы)");
            return false;
        }
        return true;
    }

    public static Stream<String> getStreamOfFile(String path) {
        try {
            return Files.lines(Paths.get(path));
        } catch (IOException e) {
            LOGGER.error("Неудачная попытка чтения файла: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
