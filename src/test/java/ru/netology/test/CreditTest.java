package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.data.DbWorker;
import ru.netology.page.OfferPage;

import static com.codeborne.selenide.Selenide.*;

public class CreditTest {

    @BeforeEach
    void setUp() {
        String url = System.getProperty("app.url");
        open(url);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterAll
    static void clean() {
        DbWorker.cleanTables();
    }

    @Test
    @DisplayName("Credit with a card valid in January")
    void shouldSuccessfulCreditFirstMonth() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, "01", "22", DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.successfulCredit();
    }

    @Test
    @DisplayName("Credit with a card valid in December")
    void shouldSuccessfulCreditLastMonth() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        var year = DataGenerator.generateĞ¡urrentYear();
        creditPage.tryCredit(number, "12", year, DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.successfulCredit();
    }

    @Test
    @DisplayName("Credit with a card valid in current month and year")
    void shouldSuccessfulCreditCurrentMonthAndYear() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.successfulCredit();
    }

    @Test
    @DisplayName("Credit by card with expiration of validity in 5 years from now")
    void shouldSuccessfulCreditAfterFiveYears() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateĞ¡urrentYear()) + 5;
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.successfulCredit();
    }

    @Test
    @DisplayName("Credit by a declined card")
    void shouldFailedCreditByDeclinedCard() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getSecondCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.failedCredit();
    }

    @Test
    @DisplayName("Credit by a non-existent card")
    void shouldFailedCreditByNonexistentCard() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        creditPage.tryCredit("4444 4444 4444 4445", DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.failedCredit();
    }

    @Test
    @DisplayName("Credit by card with a short number")
    void shouldFailedCreditByShortCard() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        creditPage.tryCredit("4444 4444 4444 444", DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.incorrectNumber();
    }

    @Test
    @DisplayName("Credit by card without number")
    void shouldFailedCreditByCardWithoutNumber() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        creditPage.tryCredit(null, DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.incorrectNumber();
    }

    @Test
    @DisplayName("Credit by card without month")
    void shouldFailedCreditByCardWithoutMonth() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, null, DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.incorrectMonth();
    }

    @Test
    @DisplayName("Credit by card with month under first")
    void shouldFailedCreditByCardUnderFirstMonth() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, "00", DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.overdueMonth();
    }

    @Test
    @DisplayName("Credit by card with month over last")
    void shouldFailedCreditByCardOverLastMonth() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, "13", DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.overdueMonth();
    }

    @Test
    @DisplayName("Credit by card without year")
    void shouldFailedCreditByCardWithoutYear() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), null, DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.incorrectYear();
    }

    @Test
    @DisplayName("Credit by card with last year")
    void shouldFailedCreditLastYear() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateĞ¡urrentYear()) - 1;
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.overdueCard();
    }

    @Test
    @DisplayName("Credit by card after six years")
    void shouldFailedCreditAfterSixYears() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateĞ¡urrentYear()) + 6;
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.overdueYear();
    }

    @Test
    @DisplayName("Credit by card with last month current year")
    void shouldFailedCreditLastMonthCurrentYear() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, "09", DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        creditPage.overdueMonth();
    }

    @Test
    @DisplayName("Credit by card with cardholder in Russian")
    void shouldFailedCreditWithCardholderInRussian() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), "ĞĞ²Ğ°Ğ½Ğ¾Ğ² ĞĞ²Ğ°Ğ½", DataGenerator.generateCVC());
        creditPage.failedCredit();
    }

    @Test
    @DisplayName("Credit by card without cardholder")
    void shouldFailedCreditWithoutCardholder() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), null, DataGenerator.generateCVC());
        creditPage.incorrectHolder();
    }

    @Test
    @DisplayName("Credit by card with short cardholder")
    void shouldFailedCreditWithShortCardholder() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), "n", DataGenerator.generateCVC());
        creditPage.failedCredit();
    }

    @Test
    @DisplayName("Credit by card without CVC")
    void shouldFailedCreditWithoutCvc() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), null);
        creditPage.incorrectCvc();
    }

    @Test
    @DisplayName("Credit by card with short CVC")
    void shouldFailedCreditWithShortCvc() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateĞ¡urrentMonth(), DataGenerator.generateĞ¡urrentYear(), DataGenerator.generateName(), "1");
        creditPage.incorrectCvc();
    }
}
