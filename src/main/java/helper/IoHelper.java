package helper;

import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class IoHelper {
    private static final Logger LOGGER = Logger.getLogger(IoHelper.class);

    public static void deleteData(String path) {
        try {
            if (Files.deleteIfExists(Paths.get(path))) {
                LOGGER.info("Файл " + path + " будет перезаписан");
            }
        } catch (IOException e) {
            LOGGER.error("Неудачная попытка удаления уже существующего файла " + path);
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
