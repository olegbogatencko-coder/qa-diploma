package ru.netology.qa.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import ru.netology.qa.pages.MainPage;
import ru.netology.qa.pages.PaymentPage;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.qa.data.DatabaseHelper.getLastPaymentStatus;

public class BuyTourTest {

    @Test
    void shouldBuyTourWithApprovedCard() throws InterruptedException { // Добавили throws
        Configuration.timeout = 10000;

        MainPage mainPage = new MainPage();
        PaymentPage paymentPage = new PaymentPage();

        mainPage.openPage();
        mainPage.clickBuy();

        paymentPage.fillForm("1111 2222 3333 4444", "12", "26", "IVAN IVANOV", "123");

        // 1. Проверяем UI
        $(".notification__content").shouldHave(Condition.text("Операция одобрена Банком"));

        // 2. Ждем 2 секунды, чтобы приложение точно записало данные в базу
        Thread.sleep(2000);

        // 3. Проверяем БД
        String actualStatus = getLastPaymentStatus();
        assertEquals("APPROVED", actualStatus, "В базе данных должен быть статус APPROVED");
    }
}