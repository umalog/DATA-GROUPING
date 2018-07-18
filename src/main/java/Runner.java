import helper.IoHelper;


public class Runner {

    /**
     * @param args массив из 3-х строк:
     *             путь до считываемого файла, выходной файл(дата-сумма), выходной файл(офис-сумма).
     */
    public static void main(String[] args) {
        if (IoHelper.validateArgs(args)) {
            FileParser parser = new ParallelFileParser();
            parser.parse(args[0], args[1], args[2]);
        } else {
            throw new IllegalArgumentException("Неверное количество передаваемых параметров. Ознакомьтесь c README.");
        }
    }
}
