package ru.netology;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    //private final SelenideElement byeField = $$(".tab-item").find(exactText("Купить"));
    private final SelenideElement cardNumberField = $(" #root form fieldset div span input");
    private final SelenideElement monthField = $("#root form fieldset div:nth-child(2) span span:nth-child(1) .input__box input");
    private final SelenideElement yearField = $("#root form fieldset div:nth-child(2) span span:nth-child(2) .input__box input");
    private final SelenideElement cardHolderField = $("#root form fieldset div:nth-child(3) span span:nth-child(1) .input__box input");
    private final SelenideElement cvcField = $(" #root form fieldset div:nth-child(3) span span:nth-child(2) .input__box input");

    private final SelenideElement submitButton = $$("button").find(exactText("Продолжить"));
    //private final SelenideElement successNotification = $("");
    //private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");




    public void fillFormAndSubmit(String cardNumber, String month, String year, String cardHolder, String cvc) {
        cardNumberField.setValue(cardNumber);
        monthField.setValue(month);
        yearField.setValue(year);
        cardHolderField.setValue(cardHolder);
        cvcField.setValue(cvc);

        submitButton.click();
    }

    // Метод для проверки успешного уведомления
    public void verifySuccessNotification() {
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
    }

    // Метод для проверки сообщения об ошибке
    public void verifyErrorNotification(String expectedErrorMessage) {
        $(withText(expectedErrorMessage)).shouldBe(visible);
    }
}

