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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.qa.data.DatabaseHelper.getLastPaymentStatus;

public class BuyTourTest {

    MainPage mainPage = new MainPage();
    PaymentPage paymentPage = new PaymentPage();

    @BeforeEach
    void setUp() throws SQLException {
        Configuration.timeout = 10000;

        // Очищаем базу данных перед каждым тестом
        String dbUrl = "jdbc:mysql://localhost:3306/app";
        String user = "app";
        String pass = "pass";
        try (Connection conn = DriverManager.getConnection(dbUrl, user, pass);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM payment_entity;");
            stmt.execute("DELETE FROM credit_request_entity;");
        }

        mainPage.openPage();
        mainPage.clickBuy();
    }

    @Test
    void shouldBuyTourWithApprovedCard() throws InterruptedException {
        paymentPage.fillForm("1111 2222 3333 4444", "12", "26", "IVAN IVANOV", "123");
        Thread.sleep(6000); // Увеличили паузу до 3 секунд
        String actualStatus = getLastPaymentStatus();
        assertEquals("APPROVED", actualStatus, "В базе данных должен быть статус APPROVED");
    }

    @Test
    void shouldNotBuyTourWithDeclinedCard() throws InterruptedException {
        paymentPage.fillForm("5555 6666 7777 8888", "12", "26", "IVAN IVANOV", "123");
        Thread.sleep(3000); // Увеличили паузу до 3 секунд
        String actualStatus = getLastPaymentStatus();
        assertEquals("DECLINED", actualStatus, "В базе данных должен быть статус DECLINED");
    }
}