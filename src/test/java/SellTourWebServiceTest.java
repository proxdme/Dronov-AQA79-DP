import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.DataBaseHelper;
import ru.netology.SellTourWebService;
import ru.netology.TestDataGenerator;

import static org.junit.jupiter.api.Assertions.*;

public class SellTourWebServiceTest {
    private SellTourWebService loginPage;


    @BeforeEach
    void setUp() {

        DataBaseHelper.clearOrderEntityTable();

        Selenide.open("http://localhost:8080/");
        loginPage = new SellTourWebService(); // Инициализируем страницу с формой


    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void testSuccessfulDebitCardPayment() {


        loginPage.fillFormAndSubmit(
                TestDataGenerator.getApprovedCardNumber(),
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getYearWithOffset(1),
                "Ivan Ivanov",
                "123"
        );


        loginPage.verifySuccessNotification();


        String paymentId = DataBaseHelper.getLastGeneratedPaymentId();
        assertNotNull(paymentId, "payment_id не был сгенерирован или запись не добавлена");


        boolean isRecordAdded = DataBaseHelper.isRecordAddedByPaymentId(paymentId);
        assertTrue(isRecordAdded, "Запись не была добавлена в базу данных");

    }

    @Test
    public void testDeclinedDebitCardPayment() {


        loginPage.fillFormAndSubmit(
                TestDataGenerator.getDeclinedCardNumber(),
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getYearWithOffset(1),
                "Ivan Ivanov",
                "123"
        );
        // Здесь мы ожидаем системную ошибку (отказ в оплате)
        loginPage.verifyErrorNotification();


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
                TestDataGenerator.getYearWithOffset(1),
                "Ivan Ivanov",
                "123"
        );
        // Здесь мы ожидаем ошибку в поле ввода (неверный формат номера карты)
        loginPage.verifyInputFieldError("Неверный формат");
    }

    @Test
    public void testEmptyCardNumber() {
        loginPage.fillFormAndSubmit(
                "",                     // Пустое поле номера карты
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getYearWithOffset(1),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyInputFieldError("Неверный формат");
    }

    @Test
    public void testInvalidMonth() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                "13",                  // Неверный месяц (больше 12)
                TestDataGenerator.getYearWithOffset(1),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyInputFieldError("Неверно указан срок действия карты");
    }

    @Test
    public void testEmptyMonth() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                "",                    // Пустое поле месяца
                TestDataGenerator.getYearWithOffset(1),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyInputFieldError("Неверный формат");
    }

    @Test
    public void testInvalidYear() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getYearWithOffset(-1),
                "Ivan Ivanov",
                "123"
        );
        loginPage.verifyInputFieldError("Истёк срок действия карты");
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
        loginPage.verifyInputFieldError("Неверный формат");
    }

    @Test
    public void testInvalidCardHolder() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getYearWithOffset(1),
                "Иван Иванов",
                "123"
        );
        loginPage.verifyInputFieldError("Неверный формат");
    }

    @Test
    public void testEmptyCardHolder() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getYearWithOffset(1),
                "",
                "123"
        );
        loginPage.verifyInputFieldError("Поле обязательно для заполнения");
    }

    @Test
    public void testInvalidCVC() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getYearWithOffset(1),
                "Ivan Ivanov",
                ""
        );
        loginPage.verifyInputFieldError("Неверный формат");
    }

    @Test
    public void testIncompleteCVC() {
        loginPage.fillFormAndSubmit(
                "4444 4444 4444 4444",
                TestDataGenerator.getValidMonth(),
                TestDataGenerator.getYearWithOffset(1),
                "Ivan Ivanov",
                "12"
        );
        loginPage.verifyInputFieldError("Неверный формат");
    }
}