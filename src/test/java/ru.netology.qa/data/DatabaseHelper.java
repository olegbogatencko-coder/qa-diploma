package ru.netology.qa.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    private static final String USER = "app";
    private static final String PASS = "pass";

    // Для обычной оплаты
    public static String getLastPaymentStatus() {
        String status = null;
        String query = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                status = rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    // Для кредита
    public static String getLastCreditStatus() {
        String status = null;
        String query = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                status = rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
}