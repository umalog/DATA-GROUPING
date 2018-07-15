
/**
 * сумма всех операций за каждый день; суммы всех операций в каждой точке
 * пример запуска:
 * java -jar task2.jar operations.txt sums-by-date.txt sums-by-offices.txt
 */
public interface FileParser {

    /**
     * @param input имя файла с операциями
     * @param out1  имя файла со статистикой по датам / сортировка по возрастанию дат!
     * @param out2  имя файла со статистикой по точкам продаж / сортировка по убывадию суммы!
     */
    public void parse(String input, String out1, String out2);
}
