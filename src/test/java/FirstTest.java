import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.LoginPage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstTest {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        // Открываем браузер с нужным URL через Selenide
        Selenide.open("http://localhost:8080/");
        loginPage = new LoginPage(); // Инициализируем страницу с формой

        // Настройка базы данных
        dbUrl = "jdbc:postgresql://localhost:5432/app"; // URL вашей базы данных
        dbUser = "app"; // Имя пользователя базы данных
        dbPassword = "pass"; // Пароль пользователя базы данных
    }

    @Test
    public void testSuccessfulDebitCardPayment() {
        // Заполняем форму через Selenide и проверяем успешное сообщение
        loginPage.verifySuccessNotification(
                "1111 2222 3333 4444",   // Номер карты
                "05",                    // Месяц
                "25",                    // Год
                "Ivan Ivanov",            // Владелец карты
                "123",                    // CVC
                "Успешно"                 // Ожидаемый текст успешного уведомления
        );

        // Проверка записи в базе данных
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM payments WHERE card_number='1111 2222 3333 4444'");

            // Убедимся, что запись существует
            assertTrue(resultSet.next());
            assertEquals("SUCCESS", resultSet.getString("status"));

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
