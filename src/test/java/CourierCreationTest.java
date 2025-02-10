import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class CourierCreationTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private CourierSteps courier;

    @Before
    public void before() {
        courier = new CourierSteps(BASE_URI);
    }

    @Test
    @DisplayName("При передаче всех необходимых параметров можно создать курьера")
    public void createCourier() {
        Response response = courier.sendPostRequestCreateCourier();
        response.then().statusCode(201);
        courier.checkBodySuccess(response,"ok",true);
        Response login = courier.sendPostRequestToGetId();
        Response delete = courier.getIdAndDelete(login);
        delete.then().statusCode(200);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateTwoSimilarCouriers() {
        courier.sendPostRequestCreateCourier();
        Response responseTwo = courier.sendPostRequestCreateCourier();
        responseTwo.then().statusCode(409);
        courier.checkBody(responseTwo,"message","Этот логин уже используется");
        Response login = courier.sendPostRequestToGetId();
        Response delete = courier.getIdAndDelete(login);
        delete.then().statusCode(200);
    }

    @Test
    @DisplayName("Если логина нет, запрос возвращает ошибку")
    public void creationWithoutLogin() {
        courier.sendPostRequestCreateCourier();
        Response response = courier.createCourierWithoutLogin();
        response.then().statusCode(400);
        courier.checkBody(response,"message","Недостаточно данных для создания учетной записи");
        Response login = courier.sendPostRequestToGetId();
        Response delete = courier.getIdAndDelete(login);
        delete.then().statusCode(200);
    }

    @Test
    @DisplayName("Если пароля нет, запрос возвращает ошибку")
    public void creationWithoutPassword() {
        courier.sendPostRequestCreateCourier();
        Response response = courier.createCourierWithoutPassword();
        response.then().statusCode(400);
        courier.checkBody(response,"message","Недостаточно данных для создания учетной записи");
        Response login = courier.sendPostRequestToGetId();
        Response delete = courier.getIdAndDelete(login);
        delete.then().statusCode(200);
    }

    @Test
    @DisplayName("Нельзя создать курьера с существующим логином")
    public void creationWithExistingLogin() {
        courier.sendPostRequestCreateCourier();
        Response response = courier.createCourierWithAnotherPassword();
        response.then().statusCode(409);
        courier.checkBody(response,"message","Этот логин уже используется");
        Response login = courier.sendPostRequestToGetId();
        Response delete = courier.getIdAndDelete(login);
        delete.then().statusCode(200);
    }

}