package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class OfferPage {
    private SelenideElement heading = $(byText("Путешествие дня"));

    public OfferPage() {
        heading.shouldBe(visible);
    }

    private ElementsCollection buttons = $$("[type='button']");

    public PurchasePage makePurchase() {
        buttons.get(0).click();
        return new PurchasePage();
    }

    public CreditPage makeCredit() {
        buttons.get(1).click();
        return new CreditPage();
    }
}
