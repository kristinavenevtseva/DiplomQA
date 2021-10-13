package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.data.DbWorker;
import ru.netology.page.OfferPage;

import static com.codeborne.selenide.Selenide.*;

public class PurchaseTest {

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
    @DisplayName("Purchase with a card valid in January")
    void shouldSuccessfulPurchaseFirstMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, "01", "22", DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.successfulPurchase();
    }

    @Test
    @DisplayName("Purchase with a card valid in December")
    void shouldSuccessfulPurchaseLastMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        var year = DataGenerator.generateСurrentYear();
        purchasePage.tryPurchase(number, "12", year, DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.successfulPurchase();
    }

    @Test
    @DisplayName("Purchase with a card valid in current month and year")
    void shouldSuccessfulPurchaseCurrentMonthAndYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.successfulPurchase();
    }

    @Test
    @DisplayName("Purchase by card with expiration of validity in 5 years from now")
    void shouldSuccessfulPurchaseAfterFiveYears() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) + 5;
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.successfulPurchase();
    }

    @Test
    @DisplayName("Purchase by a declined card")
    void shouldFailedPurchaseByDeclinedCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getSecondCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.failedPurchase();
    }

    @Test
    @DisplayName("Purchase by a non-existent card")
    void shouldFailedPurchaseByNonexistentCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        purchasePage.tryPurchase("4444 4444 4444 4445", DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.failedPurchase();
    }

    @Test
    @DisplayName("Purchase by card with a short number")
    void shouldFailedPurchaseByShortCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        purchasePage.tryPurchase("4444 4444 4444 444", DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.incorrectNumber();
    }

    @Test
    @DisplayName("Purchase by card without number")
    void shouldFailedPurchaseByCardWithoutNumber() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        purchasePage.tryPurchase(null, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.incorrectNumber();
    }

    @Test
    @DisplayName("Purchase by card without month")
    void shouldFailedPurchaseByCardWithoutMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, null, DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.incorrectMonth();
    }

    @Test
    @DisplayName("Purchase by card with month under first")
    void shouldFailedPurchaseByCardUnderFirstMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, "00", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.overdueMonth();
    }

    @Test
    @DisplayName("Purchase by card with month over last")
    void shouldFailedPurchaseByCardOverLastMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, "13", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.overdueMonth();
    }

    @Test
    @DisplayName("Purchase by card without year")
    void shouldFailedPurchaseByCardWithoutYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), null, DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.incorrectYear();
    }

    @Test
    @DisplayName("Purchase by card with last year")
    void shouldFailedPurchaseLastYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) - 1;
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.overdueCard();
    }

    @Test
    @DisplayName("Purchase by card after six years")
    void shouldFailedPurchaseAfterSixYears() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) + 6;
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.overdueYear();
    }

    @Test
    @DisplayName("Purchase by card with last month current year")
    void shouldFailedPurchaseLastMonthCurrentYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, "09", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        purchasePage.overdueMonth();
    }

    @Test
    @DisplayName("Purchase by card with cardholder in Russian")
    void shouldFailedPurchaseWithCardholderInRussian() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), "Иванов Иван", DataGenerator.generateCVC());
        purchasePage.failedPurchase();
    }

    @Test
    @DisplayName("Purchase by card without cardholder")
    void shouldFailedPurchaseWithoutCardholder() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), null, DataGenerator.generateCVC());
        purchasePage.incorrectHolder();
    }

    @Test
    @DisplayName("Purchase by card with short cardholder")
    void shouldFailedPurchaseWithShortCardholder() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), "n", DataGenerator.generateCVC());
        purchasePage.failedPurchase();
    }

    @Test
    @DisplayName("Purchase by card without CVC")
    void shouldFailedPurchaseWithoutCvc() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), null);
        purchasePage.incorrectCvc();
    }

    @Test
    @DisplayName("Purchase by card with short CVC")
    void shouldFailedPurchaseWithShortCvc() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), "1");
        purchasePage.incorrectCvc();
    }
}
