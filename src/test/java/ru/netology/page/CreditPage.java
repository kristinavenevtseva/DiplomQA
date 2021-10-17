package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
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

    public void tryCredit(String number, String month, String year, String user, String cvc) {
        inputFields.get(0).setValue(number);
        inputFields.get(1).setValue(month);
        inputFields.get(2).setValue(year);
        inputFields.get(3).setValue(user);
        inputFields.get(4).setValue(cvc);
        button.click();
    }

    private SelenideElement success = $(byText("Успешно"));
    private SelenideElement notOk = $(".notification_status_ok");
    private SelenideElement failure = $(byText("Ошибка"));
    private SelenideElement notErr = $(".notification_status_error");
    private SelenideElement formField = $(".form-field");
    private ElementsCollection fields = $$(".input-group__input-case");

    public void successfulCredit() {
        success.shouldBe(visible, Duration.ofSeconds(15));
        notOk.shouldHave(text("Операция одобрена Банком."));
    }

    public void failedCredit() {
        failure.shouldBe(visible, Duration.ofSeconds(15));
        notErr.shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    public void incorrectNumber() {
        formField.shouldHave(text("Номер карты " + "Неверный формат"));
    }

    public void incorrectMonth() {
        fields.get(0).shouldHave(text("Месяц " + "Неверный формат"));
    }

    public void overdueMonth() {
        fields.get(0).shouldHave(text("Месяц " + "Неверно указан срок действия карты"));
    }

    public void incorrectYear() {
        fields.get(1).shouldHave(text("Год " + "Неверный формат"));
    }

    public void overdueCard() {
        fields.get(1).shouldHave(text("Год " + "Истёк срок действия карты"));
    }

    public void overdueYear() {
        fields.get(1).shouldHave(text("Год " + "Неверно указан срок действия карты"));
    }

    public void incorrectHolder() {
        fields.get(2).shouldHave(text("Владелец " + "Поле обязательно для заполнения"));
    }

    public void incorrectCvc() {
        fields.get(3).shouldHave(text("CVC/CVV " + "Неверный формат"));
    }
}
