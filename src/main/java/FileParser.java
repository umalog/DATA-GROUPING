import helper.IoHelper;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

/**
 * сумма всех операций за каждый день; суммы всех операций в каждой точке
 * пример запуска:
 * java -jar task2.jar operations.txt sums-by-date.txt sums-by-offices.txt
 */
class FileParser {
    private final DecimalFormat format = new DecimalFormat("#0.00");

    /**
     * @param input имя файла с операциями
     * @param out1  имя файла со статистикой по датам / сортировка по возрастанию дат!
     * @param out2  имя файла со статистикой по точкам продаж / сортировка по убывадию суммы!
     */
    public void parse(String input, String out1, String out2) {

        String[] unorderedData = IoHelper.readFile(input);

        String dateStatistics = sortByDate(unorderedData);
        IoHelper.writeData(out1.trim(), dateStatistics);

        String officeStatistics = sortByMoney(unorderedData);
        IoHelper.writeData(out2.trim(), officeStatistics);
    }

    private String sortByDate(String[] unorderedData) {

        TreeMap<LocalDate, Double> sortedData = new TreeMap<>();

        for (String anUnorderedData : unorderedData) {
            String[] line = anUnorderedData.split("__");
            LocalDate date = LocalDate.parse(line[0].trim());
            Double price = Double.valueOf(line[3].trim().replace(',', '.'));
            if (sortedData.containsKey(date)) {
                sortedData.put(date, sortedData.get(date) + price);
            } else {
                sortedData.put(date, price);
            }
        }
        StringBuilder result = new StringBuilder();
        sortedData.forEach((key, value) -> result.append(key)
                .append("__")
                .append(format.format(value))
                .append(System.lineSeparator()));
        return result.toString();
    }

    private String sortByMoney(String[] unorderedData) {

        HashMap<String, Double> unsortedOfficeStatistics = new HashMap<>();

        for (String anUnorderedData : unorderedData) {
            String[] line = anUnorderedData.split("__");
            String office = line[1].trim();
            Double price = Double.valueOf(line[3].trim().replace(',', '.'));
            if (unsortedOfficeStatistics.containsKey(office)) {
                unsortedOfficeStatistics.put(office, unsortedOfficeStatistics.get(office) + price);
            } else {
                unsortedOfficeStatistics.put(office, price);
            }
        }
        List<Map.Entry<String, Double>> list = new ArrayList<>(unsortedOfficeStatistics.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        StringBuilder result = new StringBuilder();
        list.forEach(entry -> result.append(entry.getKey())
                .append("__")
                .append(format.format(entry.getValue()))
                .append(System.lineSeparator()));
        return result.toString();
    }

}
