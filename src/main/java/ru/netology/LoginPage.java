package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class LoginPage {
    private final SelenideElement byeField = $("[data-test-id=#root > div > button:nth-child(3) > span > span");
    private final SelenideElement cardNumberField = $("[data-test-id=cardNumber] input");
    private final SelenideElement monthField = $("[data-test-id=month] input");
    private final SelenideElement yearField = $("[data-test-id=year] input");
    private final SelenideElement cardHolderField = $("[data-test-id=owner] input");
    private final SelenideElement cvcField = $("[data-test-id=cvc] input");
    private final SelenideElement submitButton = $("[data-test-id=submit]");
    private final SelenideElement successNotification = $("[data-test-id='success-notification'] .notification__content");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");


    public void verifySuccessNotification(String expectedText) {
        byeField.click();
        cardNumberField.setValue(info.getCardNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        cardHolderField.setValue(info.getCardHolder());
        cvcField.setValue(info.getCvc());
        submitButton.click();
        successNotification.shouldHave(exactText(expectedText)).shouldBe(visible);

    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }
}
