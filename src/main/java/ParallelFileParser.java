import helper.IoHelper;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ParallelFileParser implements FileParser {
    private static final Logger LOGGER = Logger.getLogger(ParallelFileParser.class);
    private ConcurrentHashMap<LocalDate, BigDecimal> dateMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BigDecimal> officeMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void parse(String input, String out1, String out2) {
        Stream<String> lines = IoHelper.getStreamOfFile(input);
        IoHelper.deleteData(out1);
        IoHelper.deleteData(out2);

        lines.parallel().forEach(this::computeStatistics);
        try {
            writeDateStatistics(new FileOutputStream(out1), new TreeMap<>(dateMap));
            writeOfficeStatistics(new FileOutputStream(out2), new HashMap<>(officeMap));
        } catch (IOException e) {
            LOGGER.error("Неудачная попытка создания файла: " + e.getMessage());
            throw new RuntimeException(e);
        }
        LOGGER.info("Обработанно операций = " + counter);
        LOGGER.info("Файл " + out1 + " создан.");
        LOGGER.info("Файл " + out2 + " создан.");
    }

    private void computeStatistics(String line) {
        String[] splitLine = line.split("__");
        String d = splitLine[0].trim();
        try {
            BigDecimal price = new BigDecimal(splitLine[3].trim());
            LocalDate date = LocalDate.parse(d.substring(0, d.indexOf(" ")));
            dateMap.merge(date, price, BigDecimal::add);
            officeMap.merge(splitLine[1].trim(), price, BigDecimal::add);
        } catch (NumberFormatException e) {
            LOGGER.warn("Некорректное значение суммы: " + splitLine[3] + ". Строка \"" + line +
                    "\" не обработана. Причина:" + e.getMessage());
        } catch (RuntimeException e) {
            LOGGER.warn("Некорректное значение даты: " + d + ". Строка \"" + line + "\" не обработана. " +
                    "Причина:" + e.getMessage());
        } finally {
            counter.incrementAndGet();
        }
    }

    private static void writeDateStatistics(OutputStream stream, TreeMap<LocalDate, BigDecimal> sortedDate) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(stream, StandardCharsets.UTF_8.newEncoder()))) {
            sortedDate.forEach((key, value) -> {
                try {
                    writer.write(key + "__");
                    writer.write(value.toString());
                    writer.newLine();
                } catch (IOException e) {
                    LOGGER.error("Неудачная попытка записи строки: " + e.getMessage());
                }
            });
        }
    }

    private static void writeOfficeStatistics(OutputStream stream, HashMap<String, BigDecimal> unsortedOfficeStatistics) throws IOException {
        List<Map.Entry<String, BigDecimal>> list = new ArrayList<>(unsortedOfficeStatistics.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(stream, StandardCharsets.UTF_8.newEncoder()))) {
            list.forEach(entry -> {
                try {
                    writer.write(entry.getKey() + "__");
                    writer.write(entry.getValue().toString());
                    writer.newLine();
                } catch (IOException e) {
                    LOGGER.error("Неудачная попытка записи строки: " + e.getMessage());
                }
            });
        }
    }
}
