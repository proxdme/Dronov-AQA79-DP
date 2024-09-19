import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.LoginPage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;
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
        $$(".button__content").find(exactText("Купить")).click();
        loginPage = new LoginPage(); // Инициализируем страницу с формой

        // Настройка базы данных
        dbUrl = "jdbc:postgresql://localhost:5432/app"; // URL вашей базы данных
        dbUser = "app"; // Имя пользователя базы данных
        dbPassword = "pass"; // Пароль пользователя базы данных
    }

    @Test
    public void testSuccessfulDebitCardPayment() {
        // Заполняем форму через Selenide и проверяем успешное сообщение
        loginPage.fillFormAndSubmit(
                "1111 2222 3333 4444",   // Номер карты
                "05",                    // Месяц
                "25",                    // Год
                "Ivan Ivanov",            // Владелец карты
                "123"                     // CVC
        );
        loginPage.verifySuccessNotification(); // Проверка успешного уведомления

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

    @Test
    public void testDeclinedDebitCardPayment() {
        loginPage.fillFormAndSubmit(
                "5555 6666 7777 8888",   // Номер отклонённой карты
                "05",                    // Месяц
                "25",                    // Год
                "Ivan Ivanov",            // Владелец карты
                "123"                     // CVC
        );
        loginPage.verifyErrorNotification("Ошибка"); // Проверка сообщения об ошибке

        // Проверка записи в базе данных
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM payments WHERE card_number='4442 4444 4444 4444'");

            // Убедимся, что запись существует
            assertTrue(resultSet.next());
            assertEquals("DECLINED", resultSet.getString("status"));

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidDebitCardNumber() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444",       // Номер карты короче 16 символов
                "05",                  // Месяц
                "25",                  // Год
                "Ivan Ivanov",         // Владелец карты
                "123"                  // CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testEmptyCardNumber() {
        loginPage.fillFormAndSubmit(
                "",                     // Пустое поле номера карты
                "05",                  // Месяц
                "25",                  // Год
                "Ivan Ivanov",         // Владелец карты
                "123"                  // CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testInvalidMonth() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444", // Номер карты
                "13",                  // Неверный месяц (больше 12)
                "25",                  // Год
                "Ivan Ivanov",         // Владелец карты
                "123"                  // CVC
        );
        loginPage.verifyErrorNotification("Неверно указан срок действия карты");
    }

    @Test
    public void testEmptyMonth() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444", // Номер карты
                "",                    // Пустое поле месяца
                "25",                  // Год
                "Ivan Ivanov",         // Владелец карты
                "123"                  // CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testInvalidYear() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444", // Номер карты
                "05",                  // Месяц
                "23",                  // Неверный год (меньше текущего)
                "Ivan Ivanov",         // Владелец карты
                "123"                  // CVC
        );
        loginPage.verifyErrorNotification("Истёк срок действия карты");
    }

    @Test
    public void testEmptyYear() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444", // Номер карты
                "05",                  // Месяц
                "",                    // Пустое поле года
                "Ivan Ivanov",         // Владелец карты
                "123"                  // CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testInvalidCardHolder() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444", // Номер карты
                "05",                  // Месяц
                "25",                  // Год
                "Иван Иванов",         // Владелец карты (на русском)
                "123"                  // CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testEmptyCardHolder() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444", // Номер карты
                "05",                  // Месяц
                "25",                  // Год
                "",                    // Пустое поле владельца
                "123"                  // CVC
        );
        loginPage.verifyErrorNotification("Поле обязательно для заполнения");
    }

    @Test
    public void testInvalidCVC() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444", // Номер карты
                "05",                  // Месяц
                "25",                  // Год
                "Ivan Ivanov",         // Владелец карты
                ""                     // Пустое поле CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testIncompleteCVC() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444", // Номер карты
                "05",                  // Месяц
                "24",                  // Год
                "Ivan Ivanov",         // Владелец карты
                "12"                   // Неполный CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");

    }
}
