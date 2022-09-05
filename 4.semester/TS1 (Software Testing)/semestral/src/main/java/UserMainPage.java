import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;


import java.time.Duration;

public class UserMainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public UserMainPage(WebDriver driver) {
        this.driver = driver;
        driver.get("https://demo.opencart.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    public UserMainPage clickOn(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        wait.until(ExpectedConditions.visibilityOf(element));
        Utils.jsClick(element, driver);
        return this;
    }

    public UserMainPage clickOnShowingUpWindow(String xpath) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement element = driver.findElement(By.xpath(xpath));
        wait.until(ExpectedConditions.visibilityOf(element));
        Utils.jsClick(element, driver);
        return this;
    }

    public UserMainPage chooseInSlidingArea(String xpath, String text) {
        Select selectVar = new Select(driver.findElement(By.xpath(xpath)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        selectVar.selectByVisibleText(text);
        return this;
    }

    public boolean isPresentOnPage(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        return element.isDisplayed();
    }

    public UserMainPage fillTextArea(String xpath, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement textArea = driver.findElement(By.xpath(xpath));
        wait.until(ExpectedConditions.visibilityOf(textArea));
        textArea.sendKeys(text);
        return this;
    }

    public UserMainPage waitForRefresh() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public UserMainPage clearArea(String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        return this;
    }

}
