package ru.netology;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseHelper {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/app";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "pass";
    private static final Logger LOGGER = Logger.getLogger(DataBaseHelper.class.getName());

    // Метод для получения соединения с базой данных
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Получаем последний payment_id, добавленный в таблицу order_entity
    public static String getLastGeneratedPaymentId() {
        String paymentId = null;
        String query = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            if (resultSet.next()) {
                paymentId = resultSet.getString("payment_id");
                LOGGER.info("Последний найденный payment_id: " + paymentId);
            } else {
                LOGGER.warning("Не удалось найти ни одной записи в базе данных.");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при выполнении запроса к базе данных", e);
        }

        return paymentId;
    }

    // Проверяем, что запись с этим payment_id добавлена в таблицу order_entity
    public static boolean isRecordAddedByPaymentId(String paymentId) {
        boolean isAdded = false;
        String query = "SELECT COUNT(*) FROM order_entity WHERE payment_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, paymentId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                isAdded = resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при выполнении запроса к базе данных", e);
        }

        return isAdded;
    }
}
