package ru.netology.qa.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$x;

public class PaymentPage {

    private SelenideElement cardNumber = $x("//span[text()='Номер карты']/..//input");
    private SelenideElement month = $x("//span[text()='Месяц']/..//input");
    private SelenideElement year = $x("//span[text()='Год']/..//input");
    private SelenideElement owner = $x("//span[text()='Владелец']/..//input");
    private SelenideElement cvc = $x("//span[text()='CVC/CVV']/..//input");

    // Ищем кнопку, внутри которой (в любом месте) есть текст "Продолжить"
    // Или, на всякий случай, если это input с value="Продолжить"
    private SelenideElement continueButton = $x("//button[contains(., 'Продолжить')] | //input[@value='Продолжить']");

    public void fillForm(String card, String monthVal, String yearVal, String ownerVal, String cvcVal) {
        cardNumber.setValue(card);
        month.setValue(monthVal);
        year.setValue(yearVal);
        owner.setValue(ownerVal);
        cvc.setValue(cvcVal);
        continueButton.click();
    }
}