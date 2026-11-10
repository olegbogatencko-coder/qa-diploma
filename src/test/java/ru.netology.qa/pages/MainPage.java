package ru.netology.qa.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

    // Находим кнопку "Купить" на главной странице (по тексту)
    private SelenideElement buyButton = $("button:contains(Купить)");

    // Находим кнопку "Купить в кредит"
    private SelenideElement buyInCreditButton = $("button:contains(Купить в кредит)");

    // Метод для открытия страницы
    public void openPage() {
        open("http://localhost:8080");
    }

    // Метод для клика по кнопке "Купить"
    public void clickBuy() {
        buyButton.click();
    }

    // Метод для клика по кнопке "Купить в кредит"
    public void clickBuyInCredit() {
        buyInCreditButton.click();
    }
}