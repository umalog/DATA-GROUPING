
/**
 *
 * сумма всех операций за каждый день; суммы всех операций в каждой точке
 * пример запуска:
 * java -jar task2.jar operations.txt sums-by-date.txt sums-by-offices.txt
 */
public interface FileParser {

    public void parse(String input, String out1, String out2);
}
