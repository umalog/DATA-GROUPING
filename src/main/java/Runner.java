import helper.IoHelper;


public class Runner {

    /**
     * При большом размере входящего файла (размером от "Война и мир" и более),
     * переключить интерфейс FileParser с SimpleFileParser на ParallelFileParser.
     *
     * @param args массив из 3-х строк:
     *             путь до считываемого файла, выходной файл(дата-сумма), выходной файл(офис-сумма).
     */
    public static void main(String[] args) {
        if (IoHelper.validateArgs(args)) {
            FileParser parser = new SimpleFileParser();
            parser.parse(args[0], args[1], args[2]);
        } else {
            throw new IllegalArgumentException("Неверное количество передаваемых параметров. \n" +
                    "Ознакомьтесь c README.");
        }
    }
}
