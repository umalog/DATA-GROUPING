import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelFileParserTest {
    private String newLine = System.getProperty("line.separator");

    @Test
    public void computeStatistics() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        ParallelFileParser fileParser = new ParallelFileParser();
        Method method = fileParser.getClass().getDeclaredMethod("computeStatistics", String.class);
        method.setAccessible(true);
        method.invoke(fileParser, "2017-02-15 12:36__Екатеринбург, пл. Школьный, 703__21__74791.08");
        method.invoke(fileParser, "2017-02-24 00:43__Кемерово, пр. Южная, 715__22__19410.81");
        method.invoke(fileParser, "2017-02-15 12:36__Екатеринбург, пл. Школьный, 703__21__0.01");

        Field dateMap = fileParser.getClass().getDeclaredField("dateMap");
        dateMap.setAccessible(true);
        ConcurrentHashMap<LocalDate, BigDecimal> dates = (ConcurrentHashMap) dateMap.get(fileParser);
        Assert.assertEquals(dates.size(),2);
        Assert.assertEquals(dates.get(LocalDate.parse("2017-02-15")), new BigDecimal("74791.09"));
        Assert.assertEquals(dates.get(LocalDate.parse("2017-02-24")), new BigDecimal("19410.81"));

        Field officeMap = fileParser.getClass().getDeclaredField("officeMap");
        officeMap.setAccessible(true);
        ConcurrentHashMap<String, BigDecimal> offices = (ConcurrentHashMap)officeMap.get(fileParser);
        Assert.assertEquals(offices.size(),2);
        Assert.assertEquals(offices.get("Екатеринбург, пл. Школьный, 703"), new BigDecimal("74791.09"));
        Assert.assertEquals(offices.get("Кемерово, пр. Южная, 715"), new BigDecimal("19410.81"));
    }

    @Test
    public void writeDateStatistics() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ParallelFileParser fileParser = new ParallelFileParser();
        Method method = fileParser.getClass().getDeclaredMethod("writeDateStatistics", OutputStream.class, TreeMap.class);
        method.setAccessible(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TreeMap<LocalDate, BigDecimal> map = new TreeMap<>();
        map.put(LocalDate.of(2005, 6, 18), new BigDecimal(40_000.00));
        map.put(LocalDate.of(1985, 6, 18), BigDecimal.TEN);
        map.put(LocalDate.of(1995, 6, 18), new BigDecimal(30_000.00));

        method.invoke(fileParser, baos, map);
        String expected = "1985-06-18__10" + newLine + "1995-06-18__30000" + newLine + "2005-06-18__40000" + newLine;
        Assert.assertEquals(expected, baos.toString());
    }

    @Test
    public void writeOfficeStatistics() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ParallelFileParser fileParser = new ParallelFileParser();
        Method method = fileParser.getClass().getDeclaredMethod("writeOfficeStatistics", OutputStream.class, HashMap.class);
        method.setAccessible(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HashMap<String, BigDecimal> map = new HashMap<>();
        map.put("1", new BigDecimal("322541022.31"));
        map.put("4", new BigDecimal("78506361.09"));
        map.put("2", new BigDecimal("3.188899065088245E8"));
        map.put("5", new BigDecimal("321914875.12"));
        map.put("6", new BigDecimal("308174050.52"));
        map.put("3", new BigDecimal("75458391.23"));
        method.invoke(fileParser, baos, map);
        String expected = "1__322541022.31" + newLine +
                "5__321914875.12" + newLine +
                "2__318889906.5088245" + newLine +
                "6__308174050.52" + newLine +
                "4__78506361.09" + newLine +
                "3__75458391.23" + newLine;
        Assert.assertEquals(expected, baos.toString());
    }
}