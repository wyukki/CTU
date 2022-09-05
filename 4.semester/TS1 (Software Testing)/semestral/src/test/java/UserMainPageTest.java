import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserMainPageTest extends TestCase {
    private WebDriver driver;
    private UserMainPage mainPage;
    private final Random random = new Random();

    @BeforeEach
    public void setUp() {
        driver = getDriver();
        mainPage = new UserMainPage(driver);
    }


    @Test
    public void addItemToShoppingCartContinueShopping_ItemIsUnavailable() {
        Assertions.assertTrue(
                mainPage.clickOn("//*[@id=\"content\"]/div[2]/div[2]/div/div[2]/h4/a")
                        .clickOn("//*[@id=\"button-cart\"]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[4]/a")
                        .clickOn("//*[@id=\"content\"]/div[3]/div[1]/a")
                        .clickOn("//*[@id=\"content\"]/div[2]/div[1]/div/div[2]/h4/a")
                        .clickOn("//*[@id=\"button-cart\"]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[4]/a")
                        .isPresentOnPage("//*[@id=\"checkout-cart\"]/div[1]"));
    }

    @ParameterizedTest
    @CsvSource({"HP LP3065, 1111, Sweden, Blekinge, 10000, rIDAEdYsc4"})
    public void addItemToShoppingCartUseCouponCountEstimateShippingUseCertificateGoToCheckout_ItemIsAvailable
            (String itemName, String couponCode, String state,
             String region, String postalCode, String voucherNum) {
        Assertions.assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", itemName)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"button-cart\"]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[4]/a")
                        .clickOn("//*[@id=\"accordion\"]/div[1]/div[1]/h4/a")
                        .fillTextArea("//*[@id=\"input-coupon\"]", couponCode)
                        .clickOn("//*[@id=\"button-coupon\"]")
                        .clickOn("//*[@id=\"accordion\"]/div[2]/div[1]/h4/a")
                        .chooseInSlidingArea("//*[@id=\"input-country\"]", state)
                        .chooseInSlidingArea("//*[@id=\"input-zone\"]", region)
                        .fillTextArea("//*[@id=\"input-postcode\"]", postalCode)
                        .clickOn("//*[@id=\"button-quote\"]")
                        .clickOnShowingUpWindow("//*[@id=\"modal-shipping\"]/div/div/div[2]/div/label/input")
                        .clickOn("//*[@id=\"modal-shipping\"]/div/div/div[3]/button")
                        .clickOn("//*[@id=\"accordion\"]/div[3]/div[1]/h4/a")
                        .fillTextArea("//*[@id=\"input-voucher\"]", voucherNum)
                        .clickOn("//*[@id=\"button-voucher\"]")
                        .clickOn("//*[@id=\"content\"]/div[3]/div[2]/a")
                        .isPresentOnPage("//*[@id=\"content\"]/h1"));
    }

    @Test
    public void addItemToShoppingCartGoToCheckout_ItemIsAvailable() {
        Assertions.assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", "HP LP3065")
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"button-cart\"]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[4]/a")
                        .clickOn("//*[@id=\"content\"]/div[3]/div[2]/a")
                        .isPresentOnPage("//*[@id=\"content\"]/h1"));
    }

    @Test
    public void addItemToShoppingCartUseCouponGoToCheckout_ItemIsAvailable() {
        Assertions.assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", "HP LP3065")
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"button-cart\"]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[4]/a")
                        .clickOn("//*[@id=\"accordion\"]/div[1]/div[1]/h4/a")
                        .fillTextArea("//*[@id=\"input-coupon\"]", "1111")
                        .clickOn("//*[@id=\"button-coupon\"]")
                        .clickOn("//*[@id=\"content\"]/div[3]/div[2]/a")
                        .isPresentOnPage("//*[@id=\"content\"]/h1"));
    }

    @ParameterizedTest
    @CsvSource({"HP LP3065, 1111, Sweden, Blekinge, 10000"})
    public void addItemToShoppingCartUseCouponCountEstimateShippingGoToCheckout_ItemIsAvailable
            (String goodsName, String couponNum, String state, String region, String postalCode) {
        Assertions.assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", goodsName)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"button-cart\"]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[4]/a")
                        .clickOn("//*[@id=\"accordion\"]/div[1]/div[1]/h4/a")
                        .fillTextArea("//*[@id=\"input-coupon\"]", couponNum)
                        .clickOn("//*[@id=\"button-coupon\"]")
                        .clickOn("//*[@id=\"accordion\"]/div[2]/div[1]/h4/a")
                        .chooseInSlidingArea("//*[@id=\"input-country\"]", state)
                        .chooseInSlidingArea("//*[@id=\"input-zone\"]", region)
                        .fillTextArea("//*[@id=\"input-postcode\"]", postalCode)
                        .clickOn("//*[@id=\"button-quote\"]")
                        .clickOnShowingUpWindow("//*[@id=\"modal-shipping\"]/div/div/div[2]/div/label/input")
                        .clickOn("//*[@id=\"modal-shipping\"]/div/div/div[3]/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div[2]/a")
                        .isPresentOnPage("//*[@id=\"content\"]/h1"));
    }

    @ParameterizedTest
    @CsvSource({"Damian, ceoonly@gmail.com, When will iPhone will be on stock?"})
    public void writeMessageToTechSupport(String name, String mail, String enquiry) {
        Assertions.assertTrue(
                mainPage.clickOn("/html/body/footer/div/div/div[2]/ul/li[1]/a")
                        .fillTextArea("//*[@id=\"input-name\"]", name)
                        .fillTextArea("//*[@id=\"input-email\"]", mail)
                        .fillTextArea("//*[@id=\"input-enquiry\"]", enquiry)
                        .clickOn("//*[@id=\"content\"]/form/div/div/input")
                        .isPresentOnPage("//*[@id=\"content\"]/h1"));
    }

    @ParameterizedTest
    @CsvSource({"Marcus, Aurelius, iamgoogleceo@gmail.com, +111111111, 11111, laptop, 11111"})
    public void returnItem(String name, String surname, String mail,
                           String phoneNum, String id, String goodsName, String orderId) {
        Assertions.assertTrue(
                mainPage.clickOn("/html/body/footer/div/div/div[2]/ul/li[2]/a")
                        .fillTextArea("//*[@id=\"input-firstname\"]", name)
                        .fillTextArea("//*[@id=\"input-lastname\"]", surname)
                        .fillTextArea("//*[@id=\"input-email\"]", mail)
                        .fillTextArea("//*[@id=\"input-telephone\"]", phoneNum)
                        .fillTextArea("//*[@id=\"input-order-id\"]", id)
                        .clickOn("//*[@id=\"content\"]/form/fieldset[1]/div[6]/div/div/span/button")
                        .clickOn("/html/body/div[3]/div/div[1]/table/tbody/tr[1]/td[7]")
                        .fillTextArea("//*[@id=\"input-product\"]", goodsName)
                        .fillTextArea("//*[@id=\"input-model\"]", orderId)
                        .clickOn("//*[@id=\"content\"]/form/fieldset[2]/div[4]/div/div[1]/label/input")
                        .clickOn("//*[@id=\"content\"]/form/div/div[2]/input")
                        .isPresentOnPage("//*[@id=\"content\"]/h1"));
    }

    @ParameterizedTest
    @CsvSource({"iphone 365"})
    public void addItemToWishList_itemNotFound(String item) {
        assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", item)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .isPresentOnPage("//*[@id=\"content\"]/p[2]"));
    }

    @ParameterizedTest
    @CsvSource({"iphone 365, samsung galaxy 999"})
    public void addItemToWishList_twoItemsNotFound(String item1, String item2) {
        assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", item1)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clearArea("//*[@id=\"search\"]/input")
                        .fillTextArea("//*[@id=\"search\"]/input", item2)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .isPresentOnPage("//*[@id=\"content\"]/p[2]"));
    }

    @ParameterizedTest
    @CsvSource({"johndoe@examplemail.mail, 1234, HP"})
    public void addItemToWishList_registeredUser(String mail, String password, String item) {
        assertTrue(
                mainPage.clickOn("//*[@id=\"top-links\"]/ul/li[2]/a")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/ul/li[2]/a")
                        .fillTextArea("//*[@id=\"input-email\"]", mail)
                        .fillTextArea("//*[@id=\"input-password\"]", password)
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div/form/input")
                        .fillTextArea("//*[@id=\"search\"]/input", item)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div[1]/button[1]")
                        .isPresentOnPage("//*[@id=\"product-product\"]/div[1]"));
    }

    @ParameterizedTest
    @CsvSource({"johndoe@examplemail.mail, 1234, iphone 365, HP"})
    public void addItemToWishList_registeredUser_secondItemIsValid(String mail, String password,
                                                                   String notExistingItem, String existingItem) {
        assertTrue(
                mainPage.clickOn("//*[@id=\"top-links\"]/ul/li[2]/a")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/ul/li[2]/a")
                        .waitForRefresh()
                        .fillTextArea("//*[@id=\"input-email\"]", mail)
                        .fillTextArea("//*[@id=\"input-password\"]", password)
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div/form/input")
                        .fillTextArea("//*[@id=\"search\"]/input", notExistingItem)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clearArea("//*[@id=\"search\"]/input")
                        .fillTextArea("//*[@id=\"search\"]/input", existingItem)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div[1]/button[1]")
                        .isPresentOnPage("//*[@id=\"product-product\"]/div[1]"));
    }

    @ParameterizedTest
    @CsvSource({"HP, john, doe, asaqweqw@example.com, 123, 123, 123"})
    public void addItemToWishList_failedRegistration(String item, String firstName, String lastName, String mail,
                                                     String phone, String password) {
        assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", item)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div[1]/button[1]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/a")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/ul/li[1]/a")
                        .fillTextArea("//*[@id=\"input-firstname\"]", firstName)
                        .fillTextArea("//*[@id=\"input-lastname\"]", lastName)
                        .fillTextArea("//*[@id=\"input-email\"]", mail)
                        .fillTextArea("//*[@id=\"input-telephone\"]", phone)
                        .fillTextArea("//*[@id=\"input-password\"]", password)
                        .fillTextArea("//*[@id=\"input-confirm\"]", password)
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[1]")
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[2]")
                        .clearArea("//*[@id=\"input-password\"]")
                        .fillTextArea("//*[@id=\"input-password\"]", password)
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[2]")
                        .isPresentOnPage("//*[@id=\"content\"]/form/fieldset[2]/div[1]/div/div"));
    }

    @ParameterizedTest
    @CsvSource({"HP, john, doe, asaqweqwe, 123, 1234, 1234"})
    public void addItemToWishList_registerAddToList(String item, String firstName, String lastName,
                                                    String mail, String phone, String password) {
        assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", item)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div[1]/button[1]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/a")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/ul/li[1]/a")
                        .fillTextArea("//*[@id=\"input-firstname\"]", firstName)
                        .fillTextArea("//*[@id=\"input-lastname\"]", lastName)
                        .fillTextArea("//*[@id=\"input-email\"]", mail + random.nextInt(10000) + "@example.com")
                        .fillTextArea("//*[@id=\"input-telephone\"]", phone)
                        .fillTextArea("//*[@id=\"input-password\"]", password)
                        .fillTextArea("//*[@id=\"input-confirm\"]", password)
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[1]")
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[2]")
                        .fillTextArea("//*[@id=\"search\"]/input", item)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div[1]/button[1]")
                        .isPresentOnPage("//*[@id=\"product-product\"]/div[1]"));
    }

    @ParameterizedTest
    @CsvSource({"HP, john, doe, asaqweqwer, 123, 123, 1234"})
    public void addItemToWishList_failedRegistration_successfulRegistration(String item, String firstName, String lastName,
                                                                            String mail, String phone, String incorrectPassword, String correctPassword) {
        assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", item)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div[1]/button[1]")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/a")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/ul/li[1]/a")
                        .fillTextArea("//*[@id=\"input-firstname\"]", firstName)
                        .fillTextArea("//*[@id=\"input-lastname\"]", lastName)
                        .fillTextArea("//*[@id=\"input-email\"]", mail + random.nextInt(10000) + "@example.com")
                        .fillTextArea("//*[@id=\"input-telephone\"]", phone)
                        .fillTextArea("//*[@id=\"input-password\"]", incorrectPassword)
                        .fillTextArea("//*[@id=\"input-confirm\"]", incorrectPassword)
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[1]")
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[2]")
                        .clearArea("//*[@id=\"input-password\"]")
                        .clearArea("//*[@id=\"input-confirm\"]")
                        .fillTextArea("//*[@id=\"input-password\"]", correctPassword)
                        .fillTextArea("//*[@id=\"input-confirm\"]", correctPassword)
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[2]")
                        .fillTextArea("//*[@id=\"search\"]/input", item)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div[1]/button[1]")
                        .isPresentOnPage("//*[@id=\"product-product\"]/div[1]"));
    }

    @ParameterizedTest
    @CsvSource({"johndoe@examplemail.mail, 1234, HP"})
    public void buyFromWishList(String mail, String password, String itemName) {
        assertTrue(
                mainPage.clickOn("//*[@id=\"top-links\"]/ul/li[2]/a")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/ul/li[2]/a")
                        .fillTextArea("//*[@id=\"input-email\"]", mail)
                        .fillTextArea("//*[@id=\"input-password\"]", password)
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div/form/input")
                        .fillTextArea("//*[@id=\"search\"]/input", itemName)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"content\"]/div/div[2]/div[1]/button[1]")
                        .clickOn("//*[@id=\"wishlist-total\"]")
                        .clickOn("//*[@id=\"content\"]/div[1]/table/tbody/tr/td[6]/button")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-cart\"]")
                        .clickOn("//*[@id=\"cart\"]/button")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"cart\"]/ul/li[2]/div/p/a[2]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-payment-address\"]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-shipping-address\"]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-shipping-method\"]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"collapse-payment-method\"]/div/div[2]/div/input[1]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-payment-method\"]")
                        .waitForRefresh()
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-confirm\"]").
                        waitForRefresh().
                        isPresentOnPage("//*[@id=\"content\"]/h1"));
    }

    @ParameterizedTest
    @CsvSource({"HP, John, Doe, example@gmail.com, +420420420420, Address, Yoshi city, 123456, United States, Alabama"})
    public void buyAsGuest(String itemName, String firstName, String lastName, String mail,
                           String phone, String address, String city, String postcode, String country, String zone) {
        assertTrue(
                mainPage.fillTextArea("//*[@id=\"search\"]/input", itemName)
                        .clickOn("//*[@id=\"search\"]/span/button")
                        .clickOn("//*[@id=\"content\"]/div[3]/div/div/div[2]/div[1]/h4/a")
                        .clickOn("//*[@id=\"button-cart\"]")
                        .clickOn("//*[@id=\"cart\"]/button")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"cart\"]/ul/li[2]/div/p/a[2]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"collapse-checkout-option\"]/div/div/div[1]/div[2]/label/input")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-account\"]")
                        .waitForRefresh()
                        .fillTextArea("//*[@id=\"input-payment-firstname\"]", firstName)
                        .fillTextArea("//*[@id=\"input-payment-lastname\"]", lastName)
                        .fillTextArea("//*[@id=\"input-payment-email\"]", mail)
                        .fillTextArea("//*[@id=\"input-payment-telephone\"]", phone)
                        .fillTextArea("//*[@id=\"input-payment-address-1\"]", address)
                        .fillTextArea("//*[@id=\"input-payment-city\"]", city)
                        .fillTextArea("//*[@id=\"input-payment-postcode\"]", postcode)
                        .chooseInSlidingArea("//*[@id=\"input-payment-country\"]", country)
                        .chooseInSlidingArea("//*[@id=\"input-payment-zone\"]", zone)
                        .clickOn("//*[@id=\"button-guest\"]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-shipping-method\"]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"collapse-payment-method\"]/div/div[2]/div/input[1]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-payment-method\"]")
                        .waitForRefresh()
                        .clickOn("//*[@id=\"button-confirm\"]")
                        .waitForRefresh()
                        .isPresentOnPage("//*[@id=\"content\"]/h1"));
    }

    @ParameterizedTest
    @CsvSource({"John, Doe, newmail, +420420420420, heslo"})
    public void createNewAccount(String firstName, String lastName, String mail, String phone, String password) {
        assertTrue(
                mainPage.clickOn("//*[@id=\"top-links\"]/ul/li[2]/a")
                        .clickOn("//*[@id=\"top-links\"]/ul/li[2]/ul/li[1]/a")
                        .fillTextArea("//*[@id=\"input-firstname\"]", firstName)
                        .fillTextArea("//*[@id=\"input-lastname\"]", lastName)
                        .fillTextArea("//*[@id=\"input-email\"]", mail + random.nextInt(10000) + "@mail.com")
                        .fillTextArea("//*[@id=\"input-telephone\"]", phone)
                        .fillTextArea("//*[@id=\"input-password\"]", password)
                        .fillTextArea("//*[@id=\"input-confirm\"]", password)
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[1]")
                        .clickOn("//*[@id=\"content\"]/form/div/div/input[2]")
                        .isPresentOnPage("//*[@id=\"content\"]/h1"));
    }
}
