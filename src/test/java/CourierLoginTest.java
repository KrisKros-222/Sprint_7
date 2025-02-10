import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class CourierLoginTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private CourierSteps courier;

    @Before
    public void before() {
        courier = new CourierSteps(BASE_URI);
        courier.createNewCourier();
    }

    //Через Postman выдает 404 Not Found
    @Test
    @DisplayName("Проверка возможности авторизации курьера")
    public void successfulCourierAuthorization() {
        Response response = courier.sendPostRequestSuccessful();
        courier.checkCodeAndTextOfSuccess(response);
        courier.getIdAndDeleteInLoginTest();
    }

    @Test
    @DisplayName("Система вернет ошибку если неправильно указать логин")
    public void incorrectLogin() {
        Response response = courier.sendPostRequestIncorrectLogin();
        courier.checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
        courier.getIdAndDeleteInLoginTest();
    }

    @Test
    @DisplayName("Система вернет ошибку если неправильно указать пароль")
    public void incorrectPassword() {
        Response response = courier.sendPostRequestIncorrectPass();
        courier.checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
        courier.getIdAndDeleteInLoginTest();
    }

    @Test
    @DisplayName("Система вернет ошибку если логин отсутствует")
    public void withoutLogin() {
        Response response = courier.sendPostRequestWithoutLogin();
        courier.checkCodeAndTextOfError(response,400,"message","Недостаточно данных для входа");
        courier.getIdAndDeleteInLoginTest();
    }

    @Test
    @DisplayName("Система вернет ошибку если пароль отсутствует")
    public void withoutPassword() {
        Response response = courier.sendPostRequestWithoutPass();
        courier.checkCodeAndTextOfError(response,400,"message","Недостаточно данных для входа");
        courier.getIdAndDeleteInLoginTest();
    }

    //При использовании несуществующей пары логин/пароль код 201 - баг
    @Test
    @DisplayName("Система вернет ошибку если авторизоваться под несуществующим пользователем")
    public void nonexistentUser() {
        Response response = courier.sendPostRequestNonexistentUser();
        courier.checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
        courier.getIdAndDeleteInLoginTest();
    }

}
