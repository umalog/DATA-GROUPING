package helper;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class IoHelperTest {

    @Test
    public void validateArgs() {
        Assert.assertTrue(IoHelper.validateArgs(new String[]{"", "", ""}));
        Assert.assertFalse(IoHelper.validateArgs(new String[]{}));
        Assert.assertFalse(IoHelper.validateArgs(new String[]{"", ""}));
    }
}