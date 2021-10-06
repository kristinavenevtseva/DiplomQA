package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.DbWorker;
import ru.netology.page.OfferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    @Test
    void shouldReturnCorrectAmount() {
        open("http://localhost:8080");
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        var amount = DbWorker.getAmount();
        assertEquals("45000", amount);
    }

    @Test
    void shouldReturnCorrectStatusSecondCard() {
        open("http://localhost:8080");
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getSecondCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        var status = DbWorker.getStatus();
        assertEquals("DECLINED", status);
    }

    @Test
    void shouldReturnCorrectStatusFirstCard() {
        open("http://localhost:8080");
        var offerPage = new OfferPage();
        var purchasePage = offerPage.makePurchase();
        var number = DataGenerator.getFirstCardInfo().getNumber();
        purchasePage.validPurchase(number, DataGenerator.generateСurrentMonth(), DataGenerator.generateСurrentYear(), DataGenerator.generateName(), DataGenerator.generateCVC());
        var status = DbWorker.getStatus();
        assertEquals("APPROVED", status);
    }

//    @Test
//    void shouldReturnCorrectTime() {
//        var time = DbWorker.getTime();
//        var now = Date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//        assertEquals(now, time);
//    }
}
