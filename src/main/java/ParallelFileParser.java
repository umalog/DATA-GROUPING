import helper.IoHelper;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Использование FixedThreadPool приводит к повышению времени обработки на 20%, от показателей parallelStream.
 * Однопоточное исполнение приводит к повышению времени обработки на 40%, от показателей parallelStream.
 * Данные получены на двухядерном Core i5.
 */
public class ParallelFileParser implements FileParser {
    private static final Logger LOGGER = Logger.getLogger(ParallelFileParser.class);
    private ConcurrentHashMap<LocalDate, Double> dateMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Double> officeMap = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void parse(String input, String out1, String out2) {
        Stream<String> lines = IoHelper.getStreamOfFile(input);
        lines.parallel().forEach(line -> {
            String[] splitLine = line.split("__");
            String d = splitLine[0].trim();
            try {
                Double price = Double.valueOf(splitLine[3].trim().replace(',', '.'));
                LocalDate date = LocalDate.parse(d.substring(0, d.indexOf(" ")));
                dateMap.merge(date, price, (val, newVal) -> val + newVal);
                officeMap.merge(splitLine[1].trim(), price, (val, newVal) -> val + newVal);
            } catch (NumberFormatException e) {
                LOGGER.warn("Некорректное значение суммы: " + splitLine[3] + ". Строка \"" + line +
                        "\" не обработана. Причина:" + e.getMessage());
            } catch (RuntimeException e) {
                LOGGER.warn("Некорректное значение даты: " + d + ". Строка \"" + line + "\" не обработана. " +
                        "Причина:" + e.getMessage());
            } finally {
                counter.incrementAndGet();
            }
        });

        IoHelper.writeData(out1.trim(), collectDateStatistics(new TreeMap<LocalDate, Double>(dateMap)));
        IoHelper.writeData(out2.trim(), collectOfficeStatistics(new HashMap<String, Double>(officeMap)));
        LOGGER.info("Обработанно операций = " + counter);
    }

    private String collectDateStatistics(TreeMap<LocalDate, Double> sortedDate) {
        StringBuilder dateResult = new StringBuilder();
        sortedDate.forEach((key, value) -> dateResult.append(key)
                .append("__")
                .append(new DecimalFormat("#0.00").format(value))
                .append(System.lineSeparator()));
        return dateResult.toString();
    }

    private String collectOfficeStatistics(HashMap<String, Double> unsortedOfficeStatistics) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(unsortedOfficeStatistics.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        StringBuilder officeResult = new StringBuilder();
        list.forEach(entry -> officeResult.append(entry.getKey())
                .append("__")
                .append(new DecimalFormat("#0.00").format(entry.getValue()))
                .append(System.lineSeparator()));
        return officeResult.toString();
    }
}
