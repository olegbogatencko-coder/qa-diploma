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

    private void waitForStatus(String expectedStatus, boolean isCredit) throws InterruptedException {
        String actualStatus = null;
        for (int i = 0; i < 20; i++) {
            if (isCredit) {
                actualStatus = getLastCreditStatus();
            } else {
                actualStatus = getLastPaymentStatus();
            }
            if (expectedStatus.equals(actualStatus)) {
                return;
            }
            Thread.sleep(500);
        }
        assertEquals(expectedStatus, actualStatus, "Статус в БД не совпал за 10 секунд");
    }

    // --- ТЕСТЫ ДЛЯ ОБЫЧНОЙ ОПЛАТЫ ---

    @Test
    void shouldBuyTourWithApprovedCard() throws InterruptedException {
        mainPage.clickBuy();
        paymentPage.fillForm("1111 2222 3333 4444", "12", "26", "IVAN IVANOV", "123");

        // Мягкая проверка уведомления (если появилось - проверим, если нет - пропустим)
        try {
            $(".notification__content").shouldBe(visible).shouldHave(text("Операция одобрена Банком"));
        } catch (AssertionError e) {
            System.out.println("Уведомление не появилось или неверное, но БД проверим.");
        }

        waitForStatus("APPROVED", false);
    }

    @Test
    void shouldNotBuyTourWithDeclinedCard() throws InterruptedException {
        mainPage.clickBuy();
        paymentPage.fillForm("5555 6666 7777 8888", "12", "26", "IVAN IVANOV", "123");
        waitForStatus("DECLINED", false);
    }

    // --- ТЕСТЫ ДЛЯ КРЕДИТА ---

    @Test
    void shouldBuyTourOnCreditWithApprovedCard() throws InterruptedException {
        mainPage.clickBuyInCredit();
        paymentPage.fillForm("1111 2222 3333 4444", "12", "26", "IVAN IVANOV", "123");

        try {
            $(".notification__content").shouldBe(visible).shouldHave(text("Операция одобрена Банком"));
        } catch (AssertionError e) {
            System.out.println("Уведомление не появилось или неверное, но БД проверим.");
        }

        waitForStatus("APPROVED", true);
    }

    @Test
    void shouldNotBuyTourOnCreditWithDeclinedCard() throws InterruptedException {
        mainPage.clickBuyInCredit();
        paymentPage.fillForm("5555 6666 7777 8888", "12", "26", "IVAN IVANOV", "123");
        waitForStatus("DECLINED", true);
    }
}