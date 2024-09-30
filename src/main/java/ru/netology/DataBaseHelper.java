package ru.netology;

import java.sql.*;
import java.util.logging.Logger;

import lombok.SneakyThrows;

public class DataBaseHelper {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/app";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "pass";
    private static final Logger LOGGER = Logger.getLogger(DataBaseHelper.class.getName());

    // Метод для получения соединения с базой данных
    @SneakyThrows
    private static Connection getConnection() {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Получаем последний payment_id, добавленный в таблицу order_entity
    @SneakyThrows
    public static String getLastGeneratedPaymentId() {
        String paymentId = null;
        String query = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            if (resultSet.next()) {
                paymentId = resultSet.getString("payment_id");
                LOGGER.info("Последний найденный payment_id: " + paymentId);
            } else {
                LOGGER.warning("Не удалось найти ни одной записи в базе данных.");
            }
        }
        return paymentId;
    }

    // Проверяем, что запись с этим payment_id добавлена в таблицу order_entity
    @SneakyThrows
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
        }
        return isAdded;
    }

    @SneakyThrows
    public static void clearOrderEntityTable() {
        String query = "DELETE FROM order_entity"; // SQL-запрос для удаления всех записей

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.executeUpdate(); // Выполнение запроса
        }
    }
}
