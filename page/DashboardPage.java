package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement heading = $("[h2 class]");

    public DashboardPage() {
        heading.shouldHave(text("Путешествие дня")).shouldBe(visible);
    }

}