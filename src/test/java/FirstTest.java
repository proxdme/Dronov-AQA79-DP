import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.DataBaseHelper;
import ru.netology.LoginPage;
import ru.netology.TestDataGenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.*;

public class FirstTest {
    private LoginPage loginPage;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    @BeforeEach
    void setUp() {
        // Открываем браузер с нужным URL через Selenide
        Selenide.open("http://localhost:8080/");
        loginPage = new LoginPage(); // Инициализируем страницу с формой


    }

    @Test
    public void testSuccessfulDebitCardPayment() {
        String cardNumber = "1111 2222 3333 4444"; // Номер карты
        String createdDate = TestDataGenerator.getCurrentDate(); // Получаем текущую дату для сравнения

        loginPage.fillFormAndSubmit(
                cardNumber,
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "123"
        );

        loginPage.verifySuccessNotification();

        // Теперь мы ищем payment_id по созданной дате
        //String paymentId = DataBaseHelper.getPaymentIdByCreatedDate(createdDate);
        //assertNotNull(paymentId); // Проверяем, что payment_id существует
    }


    @Test
    public void testDeclinedDebitCardPayment() {
        String cardNumber = "5555 6666 7777 8888"; // Номер карты
        String createdDate = TestDataGenerator.getCurrentDate(); // Получаем текущую дату

        loginPage.fillFormAndSubmit(
                cardNumber,
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyErrorNotification("Ошибка");

        // Теперь мы ищем payment_id по созданной дате
        String paymentId = DataBaseHelper.getPaymentIdByCreatedDate(createdDate);
        assertNotNull(paymentId); // Проверяем, что payment_id существует


    }

    @Test
    public void testInvalidDebitCardNumber() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444",       // Номер карты короче 16 символов
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testEmptyCardNumber() {
        loginPage.fillFormAndSubmit(
                "",                     // Пустое поле номера карты
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testInvalidMonth() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                "13",                  // Неверный месяц (больше 12)
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyErrorNotification("Неверно указан срок действия карты");
    }

    @Test
    public void testEmptyMonth() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                "",                    // Пустое поле месяца
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testInvalidYear() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                "23",                  // Неверный год (меньше текущего)
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyErrorNotification("Истёк срок действия карты");
    }

    @Test
    public void testEmptyYear() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                "",                    // Пустое поле года
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testInvalidCardHolder() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Иван Иванов",         // Владелец карты (на русском)
                "123"
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testEmptyCardHolder() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "",                    // Пустое поле владельца
                "123"
        );
        loginPage.verifyErrorNotification("Поле обязательно для заполнения");
    }

    @Test
    public void testInvalidCVC() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                ""                     // Пустое поле CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }

    @Test
    public void testIncompleteCVC() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "12"                   // Неполный CVC
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }
}