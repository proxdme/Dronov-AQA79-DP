package ru.netology;

import java.sql.*;

public class DataBaseHelper {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/app";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "pass";

    // Метод для получения соединения с базой данных
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static String getPaymentIdByCreatedDate(String createdDate) {
        String paymentId = null;
        String query = "SELECT payment_id FROM order_entity WHERE created = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, createdDate);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                paymentId = resultSet.getString("payment_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentId;
    }

}
