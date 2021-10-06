package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.DbWorker;
import ru.netology.page.OfferPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

public class CreditTest {

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
    @DisplayName("Credit with a card valid in January")
    void shouldSuccessfulCreditFirstMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, "01", "22", DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_ok").shouldHave(text("Операция одобрена Банком."));
    }

    @Test
    @DisplayName("Credit with a card valid in December")
    void shouldSuccessfulCreditLastMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        var year = DataGenerator.generateСurrentYear();
        purchasePage.validCredit(number, "12", year, DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_ok").shouldHave(text("Операция одобрена Банком."));
    }

    @Test
    @DisplayName("Credit with a card valid in current month and year")
    void shouldSuccessfulCreditCurrentMonthAndYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_ok").shouldHave(text("Операция одобрена Банком."));
    }

    @Test
    @DisplayName("Credit by card with expiration of validity in 5 years from now")
    void shouldSuccessfulCreditAfterFiveYears() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) + 5;
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_ok").shouldHave(text("Операция одобрена Банком."));
    }

    @Test
    @DisplayName("Credit by a declined card")
    void shouldFailedCreditByDeclinedCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getSecondCardInfo().getNumber();
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_error").shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    @Test
    @DisplayName("Credit by a non-existent card")
    void shouldFailedCreditByNonexistentCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        purchasePage.validCredit("4444 4444 4444 4445", DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_error").shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    @Test
    @DisplayName("Credit by card with a short number")
    void shouldFailedCreditByShortCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        purchasePage.validCredit("4444 4444 4444 444", DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(".form-field").shouldHave(text("Номер карты " + "Неверный формат"));
    }

    @Test
    @DisplayName("Credit by card without number")
    void shouldFailedCreditByCardWithoutNumber() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        purchasePage.validCredit(null, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $(".form-field").shouldHave(text("Номер карты " + "Неверный формат"));
    }

    @Test
    @DisplayName("Credit by card without month")
    void shouldFailedCreditByCardWithoutMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, null, DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(0).shouldHave(text("Месяц " + "Неверный формат"));
    }

    @Test
    @DisplayName("Credit by card with month under first")
    void shouldFailedCreditByCardUnderFirstMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, "00", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(0).shouldHave(text("Месяц " + "Неверно указан срок действия карты"));
    }

    @Test
    @DisplayName("Credit by card with month over last")
    void shouldFailedCreditByCardOverLastMonth() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, "13", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(0).shouldHave(text("Месяц " + "Неверно указан срок действия карты"));
    }

    @Test
    @DisplayName("Credit by card without year")
    void shouldFailedCreditByCardWithoutYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), null, DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(1).shouldHave(text("Год " + "Неверный формат"));
    }

    @Test
    @DisplayName("Credit by card with last year")
    void shouldFailedCreditLastYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) - 1;
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(1).shouldHave(text("Год " + "Истёк срок действия карты"));
    }

    @Test
    @DisplayName("Credit by card after six years")
    void shouldFailedCreditAfterSixYears() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        int year = Integer.parseInt(DataGenerator.generateСurrentYear()) + 6;
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), String.valueOf(year), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(1).shouldHave(text("Год " + "Неверно указан срок действия карты"));
    }

    @Test
    @DisplayName("Credit by card with last month current year")
    void shouldFailedCreditLastMonthCurrentYear() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, "09", DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        $$(".input-group__input-case").get(0).shouldHave(text("Месяц " + "Неверно указан срок действия карты"));
    }

    @Test
    @DisplayName("Credit by card with cardholder in Russian")
    void shouldFailedCreditWithCardholderInRussian() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), "Иванов Иван", DataGenerator.generateCVC());
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_error").shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    @Test
    @DisplayName("Credit by card without cardholder")
    void shouldFailedCreditWithoutCardholder() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), null, DataGenerator.generateCVC());
        $$(".input-group__input-case").get(2).shouldHave(text("Владелец " + "Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Credit by card with short cardholder")
    void shouldFailedCreditWithShortCardholder() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), "n", DataGenerator.generateCVC());
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification_status_error").shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    @Test
    @DisplayName("Credit by card without CVC")
    void shouldFailedCreditWithoutCvc() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), null);
        $$(".input-group__input-case").get(3).shouldHave(text("CVC/CVV " + "Неверный формат"));
    }

    @Test
    @DisplayName("Credit by card with short CVC")
    void shouldFailedCreditWithShortCvc() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), "1");
        $$(".input-group__input-case").get(3).shouldHave(text("CVC/CVV " + "Неверный формат"));
    }
}
