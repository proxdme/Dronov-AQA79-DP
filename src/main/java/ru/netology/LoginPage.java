package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class LoginPage {
    private final SelenideElement byeField = $("#root > div > button:nth-child(3)");
    private final SelenideElement cardNumberField = $("#root > div > form > fieldset > div:nth-child(1) > span > span > span.input__box > input");
    private final SelenideElement monthField = $("#root > div > form > fieldset > div:nth-child(2) > span > span:nth-child(1) > span > span > span.input__box > input");
    private final SelenideElement yearField = $("#root > div > form > fieldset > div:nth-child(2) > span > span:nth-child(2) > span > span > span.input__box > input");
    private final SelenideElement cardHolderField = $("#root > div > form > fieldset > div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__box > input");
    private final SelenideElement cvcField = $("#root > div > form > fieldset > div:nth-child(3) > span > span:nth-child(2) > span > span > span.input__box > input");
    private final SelenideElement submitButton = $("#root > div > form > fieldset > div:nth-child(4) > button > span > span");
    private final SelenideElement successNotification = $("#root > div > div.notification.notification_visible.notification_status_ok.notification_has-closer.notification_stick-to_right.notification_theme_alfa-on-white > div.notification__title");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");


    public void verifySuccessNotification(String cardNumber, String month, String year, String cardHolder, String cvc, String expectedText) {
        byeField.click();
        cardNumberField.setValue(cardNumber);
        monthField.setValue(month);
        yearField.setValue(year);
        cardHolderField.setValue(cardHolder);
        cvcField.setValue(cvc);
        submitButton.click();
        successNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    // Метод для проверки ошибки при отправке формы
    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }
}
