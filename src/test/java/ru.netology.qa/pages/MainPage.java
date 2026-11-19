package ru.netology.qa.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

    // normalize-space() игнорирует лишние пробелы
    private SelenideElement buyButton = $x("//button[normalize-space()='Купить']");
    private SelenideElement buyInCreditButton = $x("//button[normalize-space()='Купить в кредит']");

    public void openPage() {
        open("http://localhost:8080");
    }

    public void clickBuy() {
        buyButton.click();
    }

    public void clickBuyInCredit() {
        buyInCreditButton.click();
    }
}