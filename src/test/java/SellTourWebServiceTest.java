import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.DataBaseHelper;
import ru.netology.SellTourWebService;
import ru.netology.TestDataGenerator;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.*;

public class SellTourWebServiceTest {
    private SellTourWebService loginPage;


    @BeforeEach
    void setUp() {

        Selenide.open("http://localhost:8080/");
        loginPage = new SellTourWebService(); // Инициализируем страницу с формой


    }
    @BeforeAll
       static void setUpAll(){
              SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
       static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void testSuccessfulDebitCardPayment() {
        String cardNumber = "1111 2222 3333 4444";


        loginPage.fillFormAndSubmit(
                cardNumber,
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "123"
        );


        loginPage.verifySuccessNotification();


        try {
            Thread.sleep(10000); // Ожидание 5 секунд
        } catch (InterruptedException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ошибка при ожидании", e);
        }


        String paymentId = DataBaseHelper.getLastGeneratedPaymentId();
        assertNotNull(paymentId, "payment_id не был сгенерирован или запись не добавлена");


        boolean isRecordAdded = DataBaseHelper.isRecordAddedByPaymentId(paymentId);
        assertTrue(isRecordAdded, "Запись не была добавлена в базу данных");

    }

        @Test
    public void testDeclinedDebitCardPayment() {
        String cardNumber = "5555 6666 7777 8888";


        loginPage.fillFormAndSubmit(
                cardNumber,
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getValidYear(),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyErrorNotification("Ошибка");

            try {
                Thread.sleep(10000); // Ожидание 5 секунд
            } catch (InterruptedException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Ошибка при ожидании", e);
            }


            String paymentId = DataBaseHelper.getLastGeneratedPaymentId();
            assertNotNull(paymentId, "payment_id не был сгенерирован или запись не добавлена");


            boolean isRecordAdded = DataBaseHelper.isRecordAddedByPaymentId(paymentId);
            assertTrue(isRecordAdded, "Запись не была добавлена в базу данных");

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
                TestDataGenerator.getInvalidYear(),
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
                "Иван Иванов",
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
                "",
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
                ""
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
                "12"
        );
        loginPage.verifyErrorNotification("Неверный формат");
    }
}