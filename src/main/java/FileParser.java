import helper.IoHelper;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * сумма всех операций за каждый день; суммы всех операций в каждой точке
 * пример запуска:
 * java -jar task2.jar operations.txt sums-by-date.txt sums-by-offices.txt
 */
class FileParser {
    private final DecimalFormat format = new DecimalFormat("#0.00");
    private String dateStatistics;
    private String officeStatistics;

    public String getDateStatistics() {
        return dateStatistics;
    }

    public void setDateStatistics(String dateStatistics) {
        this.dateStatistics = dateStatistics;
    }

    public String getOfficeStatistics() {
        return officeStatistics;
    }

    public void setOfficeStatistics(String officeStatistics) {
        this.officeStatistics = officeStatistics;
    }

    /**
     * @param input имя файла с операциями
     * @param out1  имя файла со статистикой по датам / сортировка по возрастанию дат!
     * @param out2  имя файла со статистикой по точкам продаж / сортировка по убывадию суммы!
     */
    public void parse(String input, String out1, String out2) {

        String[] unorderedData = IoHelper.readFile(input);
        updateStatistics(unorderedData);
        IoHelper.writeData(out1.trim(), getDateStatistics());
        IoHelper.writeData(out2.trim(), getOfficeStatistics());
    }

    private void updateStatistics(String[] unorderedData) {
        TreeMap<LocalDate, Double> sortedDate = new TreeMap<>();
        HashMap<String, Double> unsortedOfficeStatistics = new HashMap<>();

        for (String anUnorderedData : unorderedData) {
            String[] line = anUnorderedData.split("__");
            LocalDate date = LocalDate.parse(line[0].trim());
            String office = line[1].trim();
            Double price = Double.valueOf(line[3].trim().replace(',', '.'));
            sortedDate.merge(date, price, (val, newVal) -> val + newVal);
            unsortedOfficeStatistics.merge(office, price, (val, newVal) -> val + newVal);
        }

        StringBuilder dateResult = new StringBuilder();
        sortedDate.forEach((key, value) -> dateResult.append(key)
                .append("__")
                .append(format.format(value))
                .append(System.lineSeparator()));
        setDateStatistics(dateResult.toString());

        List<Map.Entry<String, Double>> list = new ArrayList<>(unsortedOfficeStatistics.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        StringBuilder officeResult = new StringBuilder();
        list.forEach(entry -> officeResult.append(entry.getKey())
                .append("__")
                .append(format.format(entry.getValue()))
                .append(System.lineSeparator()));
        setOfficeStatistics(officeResult.toString());
    }
}
