package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    private SelenideElement heading = $(byText("Кредит по данным карты"));

    public CreditPage() {
        heading.shouldBe(visible);
    }

    private ElementsCollection inputFields = $$(".input input");
    private SelenideElement button = $(byText("Продолжить"));

    public void validCredit(String number, String month, String year, String user, String cvc) {
        inputFields.get(0).setValue(number);
        inputFields.get(1).setValue(month);
        inputFields.get(2).setValue(year);
        inputFields.get(3).setValue(user);
        inputFields.get(4).setValue(cvc);
        button.click();
    }
}