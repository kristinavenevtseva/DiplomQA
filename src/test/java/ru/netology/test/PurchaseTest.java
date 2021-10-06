package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.data.DbWorker;
import ru.netology.page.OfferPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class PurchaseTest {

//    @BeforeAll
//    static void setUpAll() {
//        SelenideLogger.addListener("allure", new AllureSelenide());
//    }

//    @AfterAll
//    static void tearDownAll() {
//        SelenideLogger.removeListener("allure");
//    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

//    java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar aqa-shop.jar
//    или
//    java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar aqa-shop.jar

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
        purchasePage.validPurchase(number, "01", "22", DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_ok").shouldHave(text("Операция одобрена Банком."));
    }

    @Test
    @DisplayName("Purchase with a card valid in December")
    void shouldSuccessfulPurchaseLastMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        var year = DataGenerator.generateСurrentYear();
        purchasePage.validPurchase(number, "12", year, DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_ok").shouldHave(text("Операция одобрена Банком."));
    }

    @Test
    @DisplayName("Purchase with a card valid in current month and year")
    void shouldSuccessfulPurchaseCurrentMonthAndYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_ok").shouldHave(text("Операция одобрена Банком."));
    }

    @Test
    @DisplayName("Purchase by card with expiration of validity in 5 years from now")
    void shouldSuccessfulPurchaseAfterFiveYears() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) + 5;
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_ok").shouldHave(text("Операция одобрена Банком."));
    }

    @Test
    @DisplayName("Purchase by a declined card")
    void shouldFailedPurchaseByDeclinedCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getSecondCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_error").shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    @Test
    @DisplayName("Purchase by a non-existent card")
    void shouldFailedPurchaseByNonexistentCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        purchasePage.validPurchase("4444 4444 4444 4445", DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_error").shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    @Test
    @DisplayName("Purchase by card with a short number")
    void shouldFailedPurchaseByShortCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        purchasePage.validPurchase("4444 4444 4444 444", DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(".form-field").shouldHave(text("Номер карты " + "Неверный формат"));
    }

    @Test
    @DisplayName("Purchase by card without number")
    void shouldFailedPurchaseByCardWithoutNumber() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        purchasePage.validPurchase(null, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(".form-field").shouldHave(text("Номер карты " + "Неверный формат"));
    }

    @Test
    @DisplayName("Purchase by card without month")
    void shouldFailedPurchaseByCardWithoutMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, null, DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(0).shouldHave(text("Месяц " + "Неверный формат"));
    }

    @Test
    @DisplayName("Purchase by card with month under first")
    void shouldFailedPurchaseByCardUnderFirstMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, "00", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(0).shouldHave(text("Месяц " + "Неверно указан срок действия карты"));
    }

    @Test
    @DisplayName("Purchase by card with month over last")
    void shouldFailedPurchaseByCardOverLastMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, "13", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(0).shouldHave(text("Месяц " + "Неверно указан срок действия карты"));
    }

    @Test
    @DisplayName("Purchase by card without year")
    void shouldFailedPurchaseByCardWithoutYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), null, DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(1).shouldHave(text("Год " + "Неверный формат"));
    }

    @Test
    @DisplayName("Purchase by card with last year")
    void shouldFailedPurchaseLastYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) - 1;
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(1).shouldHave(text("Год " + "Истёк срок действия карты"));
    }

    @Test
    @DisplayName("Purchase by card after six years")
    void shouldFailedPurchaseAfterSixYears() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) + 6;
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(1).shouldHave(text("Год " + "Неверно указан срок действия карты"));
    }

    @Test
    @DisplayName("Purchase by card with last month current year")
    void shouldFailedPurchaseLastMonthCurrentYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, "09", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(0).shouldHave(text("Месяц " + "Неверно указан срок действия карты"));
    }

    @Test
    @DisplayName("Purchase by card with cardholder in Russian")
    void shouldFailedPurchaseWithCardholderInRussian() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), "Иванов Иван", DataGenerator.generateCVC());
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_error").shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    @Test
    @DisplayName("Purchase by card without cardholder")
    void shouldFailedPurchaseWithoutCardholder() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), null, DataGenerator.generateCVC());
        $$(".input-group__input-case").get(2).shouldHave(text("Владелец " + "Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Purchase by card with short cardholder")
    void shouldFailedPurchaseWithShortCardholder() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), "n", DataGenerator.generateCVC());
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_error").shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    @Test
    @DisplayName("Purchase by card without CVC")
    void shouldFailedPurchaseWithoutCvc() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), null);
        $$(".input-group__input-case").get(3).shouldHave(text("CVC/CVV " + "Неверный формат"));
    }

    @Test
    @DisplayName("Purchase by card with short CVC")
    void shouldFailedPurchaseWithShortCvc() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), "1");
        $$(".input-group__input-case").get(3).shouldHave(text("CVC/CVV " + "Неверный формат"));
    }
}
