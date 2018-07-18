import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RunnerTest {

    @Test
    public void main() {
        String message = "Неверное количество передаваемых параметров. Ознакомьтесь c README.";
        try {
            Runner.main(new String[]{"", "", "", ""});
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(message, e.getMessage());
        }
        try {
            Runner.main(new String[]{"", ""});
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(message, e.getMessage());
        }
        try {
            Runner.main(new String[]{""});
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(message, e.getMessage());
        }
        try {
            Runner.main(new String[]{});
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }
}