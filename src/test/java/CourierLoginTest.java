import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierLoginTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private CourierSteps courier;

    @Before
    public void before() {
        courier = new CourierSteps(BASE_URI);
        courier.createCourierLogin("Mashaaa","6666");
    }

    //Через Postman выдает 404 Not Found
    @Test
    @DisplayName("Проверка возможности авторизации курьера")
    public void successfulCourierAuthorization() {
        Response response = courier.sendPostRequest("Mashaaa","6666");
        courier.checkCodeAndTextOfSuccess(response);
    }

    @Test
    @DisplayName("Система вернет ошибку если неправильно указать логин")
    public void incorrectLogin() {
        Response response = courier.sendPostRequest("Mashas","6666");
        courier.checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
    }

    @Test
    @DisplayName("Система вернет ошибку если неправильно указать пароль")
    public void incorrectPassword() {
        Response response = courier.sendPostRequest("Mashaaa","66665");
        courier.checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
    }

    @Test
    @DisplayName("Система вернет ошибку если логин отсутствует")
    public void withoutLogin() {
        Response response = courier.sendPostRequest("","6666");
        courier.checkCodeAndTextOfError(response,400,"message","Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Система вернет ошибку если пароль отсутствует")
    public void withoutPassword() {
        Response response = courier.sendPostRequest("Mashaaa","");
        courier.checkCodeAndTextOfError(response,400,"message","Недостаточно данных для входа");
    }

    //При использовании несуществующей пары логин/пароль код 201 - баг
    @Test
    @DisplayName("Система вернет ошибку если авторизоваться под несуществующим пользователем")
    public void nonexistentUser() {
        Response response = courier.sendPostRequest("Mashad","66665");
        courier.checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
    }

    @After
    public void after() {
        courier.getIdAndDeleteInLoginTest("Masha","6666");
    }

}
