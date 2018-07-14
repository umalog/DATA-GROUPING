import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class FileParserTest {


    @Test
    public void collectDateStatistics() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        FileParser fileParser = new FileParser();
        Method method = fileParser.getClass().getDeclaredMethod("collectDateStatistics", TreeMap.class);
        method.setAccessible(true);
        TreeMap<LocalDate, Double> map = new TreeMap<>();
        map.put(LocalDate.of(2005, 6, 18), 40_000.00);
        map.put(LocalDate.of(1985, 6, 18), 20_000.00);
        map.put(LocalDate.of(1995, 6, 18), 30_000.00);
        String s = (String) method.invoke(fileParser, map);
        Assert.assertEquals(s, "1985-06-18__20000,00\n" +
                "1995-06-18__30000,00\n" +
                "2005-06-18__40000,00\n");
    }

    @Test
    public void collectOfficeStatistics() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        FileParser fileParser = new FileParser();
        Method method = fileParser.getClass().getDeclaredMethod("collectOfficeStatistics", HashMap.class);
        method.setAccessible(true);
        HashMap<String, Double> map = new HashMap<>();
        map.put("004", 100_000.00);
        map.put("021", 10_000.00);
        map.put("001", 50_000.00);
        String s = (String) method.invoke(fileParser, map);
        Assert.assertEquals(s, "004__100000,00\n" +
                "001__50000,00\n" +
                "021__10000,00\n");
    }


}