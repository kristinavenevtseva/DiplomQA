package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.DbWorker;
import ru.netology.page.OfferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        String url = System.getProperty("app.url");
        open(url);
    }

    @SneakyThrows
    @Test
    void shouldReturnCorrectAmount() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        Thread.sleep(10000);
        var amount = DbWorker.getAmount();
        assertEquals("45000", amount);
    }

    @SneakyThrows
    @Test
    void shouldReturnCorrectPaymentStatusSecondCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getSecondCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        Thread.sleep(10000);
        var status = DbWorker.getPaymentStatus();
        assertEquals("DECLINED", String.valueOf(status));
    }

    @SneakyThrows
    @Test
    void shouldReturnCorrectPaymentStatusFirstCard() {
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.tryPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        Thread.sleep(10000);
        var status = DbWorker.getPaymentStatus();
        assertEquals("APPROVED", String.valueOf(status));
    }

    @SneakyThrows
    @Test
    void shouldReturnCorrectCreditStatusSecondCard() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getSecondCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        Thread.sleep(10000);
        var status = DbWorker.getCreditStatus();
        assertEquals("DECLINED", status);
    }

    @SneakyThrows
    @Test
    void shouldReturnCorrectCreditStatusFirstCard() {
        var offerPage = new OfferPage();
        var creditPage = offerPage.makeCredit();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        creditPage.tryCredit(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        Thread.sleep(10000);
        var status = DbWorker.getCreditStatus();
        assertEquals("APPROVED", status);
    }
}
