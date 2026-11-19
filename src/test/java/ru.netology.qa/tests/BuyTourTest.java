package ru.netology.qa.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import ru.netology.qa.pages.MainPage;
import ru.netology.qa.pages.PaymentPage;

import static com.codeborne.selenide.Selenide.$;

public class BuyTourTest {

    @Test
    void shouldBuyTourWithApprovedCard() {
        Configuration.timeout = 10000;

        MainPage mainPage = new MainPage();
        PaymentPage paymentPage = new PaymentPage();

        mainPage.openPage();
        mainPage.clickBuy();

        paymentPage.fillForm("1111 2222 3333 4444", "12", "26", "IVAN IVANOV", "123");

        // Ищем блок с уведомлением и проверяем, что внутри него ЕСТЬ нужный текст
        $(".notification__content").shouldHave(Condition.text("Операция одобрена Банком"));
    }
}