import helper.IoHelper;


public class Runner {

    public static void main(String[] args) {
        if (IoHelper.validateArgs(args)) {
            new FileParser().parse(args[0], args[1], args[2]);
        }
    }

}