import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class TestCase {
    private static WebDriver driver;

    @BeforeEach
    public void init() {
        String path = System.getProperty("user.dir");
//        System.out.println(path);
        System.setProperty("webdriver.chrome.driver", path + "\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    @AfterEach
    public void clean() {
        System.out.println();
        driver.quit();
    }
}
