package helper;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class IoHelperTest {

    @Test
    public void writeData() {
        try {
            IoHelper.writeData("C:/wrong address", "test");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("java.nio.file.NoSuchFileException: C:/wrong address", e.getMessage());
        }

    }

    @Test
    public void readFile() {
        String path = System.getProperty("user.home") + "/test.tmp";
        try {
            IoHelper.writeData(path, " test \n test ");
            String[] txt = IoHelper.readFile(path);
            Assert.assertEquals(txt, new String[]{" test ", " test "});
        } finally {
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (IOException e) {
                System.out.println("Извините, но вам самим придется удалить файл " + path);
                Assert.fail();
            }
        }
    }

    @Test
    public void validateArgs() {
        Assert.assertTrue(IoHelper.validateArgs(new String[]{"", "", ""}));
        Assert.assertFalse(IoHelper.validateArgs(new String[]{}));
        Assert.assertFalse(IoHelper.validateArgs(new String[]{"", ""}));
    }
}