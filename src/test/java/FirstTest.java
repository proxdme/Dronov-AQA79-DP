import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.netology.LoginPage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstTest {
    private WebDriver driver;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        // Путь до драйвера может отличаться в зависимости от вашей системы

        driver = new ChromeDriver();
        driver.get("http://localhost:8080/"); // Переход на нужный URL

        // Инициализация базы данных перед тестами
        dbUrl = "jdbc:postgresql://localhost:5432/app"; // URL вашей базы данных
        dbUser = "app"; // Имя пользователя базы данных
        dbPassword = "pass"; // Пароль пользователя базы данных
    }

    @Test
    public void testSuccessfulDebitCardPayment() {
        WebElement cardNumber = driver.findElement(By.id("cardNumber"));
        cardNumber.sendKeys("1111 2222 3333 4444");

        WebElement month = driver.findElement(By.id("month"));
        month.sendKeys("05");

        WebElement year = driver.findElement(By.id("year"));
        year.sendKeys("24");

        WebElement cardHolder = driver.findElement(By.id("cardHolder"));
        cardHolder.sendKeys("Ivan Ivanov");

        WebElement cvc = driver.findElement(By.id("cvc"));
        cvc.sendKeys("123");

        WebElement submitButton = driver.findElement(By.id("submit"));
        submitButton.click();

        // Проверка успешного сообщения
        WebElement successMessage = driver.findElement(By.id("success"));
        assertTrue(successMessage.isDisplayed());

        // Проверка в базе данных
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM payments WHERE card_number='1111 2222 3333 4444'");
            assertTrue(resultSet.next());
            assertEquals("SUCCESS", resultSet.getString("status"));
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //@AfterEach
    //void tearDown() {
    //    if (driver != null) {
    //        driver.quit(); // Закрытие браузера
     //   }
    }
//}
