package ru.netology;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class SellTourWebService {

    // Метод для клика по кнопке "Купить"
    private void clickBuyButton() {
        $$(".button__content").find(exactText("Купить")).click();
    }

    // Метод для заполнения формы и отправки данных
    public void fillFormAndSubmit(String cardNumber, String month, String year, String cardHolder, String cvc) {
        clickBuyButton(); // Кликаем по кнопке "Купить" перед заполнением формы
        $("#root form fieldset div span input").setValue(cardNumber);
        $("#root form fieldset div:nth-child(2) span span:nth-child(1) .input__box input").setValue(month);
        $("#root form fieldset div:nth-child(2) span span:nth-child(2) .input__box input").setValue(year);
        $("#root form fieldset div:nth-child(3) span span:nth-child(1) .input__box input").setValue(cardHolder);
        $("#root form fieldset div:nth-child(3) span span:nth-child(2) .input__box input").setValue(cvc);
        $$("button").find(exactText("Продолжить")).click(); // Нажимаем кнопку "Продолжить"
    }

    // Проверка успешного уведомления
    public void verifySuccessNotification() {
        $(".notification__title").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно"));
    }

    // Проверка ошибки
    public void verifyErrorNotification(String errorMessage) {
        // Проверка сообщения в блоке уведомления
        if ($(".notification__title").isDisplayed()) {
            $(".notification__title").shouldBe(visible, Duration.ofSeconds(15))
                    .shouldHave(text("Ошибка"));
        } else {
            // Проверка ошибок в полях ввода
            $$(".input__sub").findBy(text(errorMessage))
                    .shouldBe(visible, Duration.ofSeconds(15));
        }


    }
}