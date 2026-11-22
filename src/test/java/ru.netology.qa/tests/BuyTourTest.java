package ru.netology.qa.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.qa.pages.MainPage;
import ru.netology.qa.pages.PaymentPage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.qa.data.DatabaseHelper.getLastCreditStatus;
import static ru.netology.qa.data.DatabaseHelper.getLastPaymentStatus;

public class BuyTourTest {

    MainPage mainPage = new MainPage();
    PaymentPage paymentPage = new PaymentPage();

    @BeforeEach
    void setUp() throws SQLException {
        Configuration.timeout = 20000;

        // Очищаем базу перед каждым тестом
        String dbUrl = "jdbc:mysql://localhost:3306/app";
        String user = "app";
        String pass = "pass";
        try (Connection conn = DriverManager.getConnection(dbUrl, user, pass);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM payment_entity;");
            stmt.execute("DELETE FROM credit_request_entity;");
        }

        mainPage.openPage();
    }

    // --- ТЕСТЫ ДЛЯ ОБЫЧНОЙ ОПЛАТЫ ---

    @Test
    void shouldBuyTourWithApprovedCard() {
        mainPage.clickBuy();
        paymentPage.fillForm("1111 2222 3333 4444", "12", "26", "IVAN IVANOV", "123");

        $(".notification__content").shouldBe(visible).shouldHave(text("Операция одобрена Банком"));

        String actualStatus = getLastPaymentStatus();
        assertEquals("APPROVED", actualStatus, "В базе данных должен быть статус APPROVED");
    }

    @Test
    void shouldNotBuyTourWithDeclinedCard() throws InterruptedException {
        mainPage.clickBuy();
        paymentPage.fillForm("5555 6666 7777 8888", "12", "26", "IVAN IVANOV", "123");

        // Даем приложению время записать данные в БД
        Thread.sleep(8000);

        String actualStatus = getLastPaymentStatus();
        assertEquals("DECLINED", actualStatus, "В базе данных должен быть статус DECLINED");
    }

    // --- ТЕСТЫ ДЛЯ КРЕДИТА ---

    @Test
    void shouldBuyTourOnCreditWithApprovedCard() {
        mainPage.clickBuyInCredit();
        paymentPage.fillForm("1111 2222 3333 4444", "12", "26", "IVAN IVANOV", "123");

        $(".notification__content").shouldBe(visible).shouldHave(text("Операция одобрена Банком"));

        String actualStatus = getLastCreditStatus();
        assertEquals("APPROVED", actualStatus, "В базе данных должен быть статус APPROVED для кредита");
    }

    @Test
    void shouldNotBuyTourOnCreditWithDeclinedCard() throws InterruptedException {
        mainPage.clickBuyInCredit();
        paymentPage.fillForm("5555 6666 7777 8888", "12", "26", "IVAN IVANOV", "123");

        // Даем приложению время записать данные в БД
        Thread.sleep(8000);

        String actualStatus = getLastCreditStatus();
        assertEquals("DECLINED", actualStatus, "В базе данных должен быть статус DECLINED для кредита");
    }
}