import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
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
        Response response = courier.createCourier("ghghgh","6666","sysysys");
        response.then().statusCode(201);
        courier.checkBodySuccess(response,"ok",true);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateTwoSimilarCouriers() {
        courier.createCourier("ghghgh","6666","sysysys");
        Response responseTwo = courier.createCourier("ghghgh","6666","sysysys");
        responseTwo.then().statusCode(409);
        courier.checkBody(responseTwo,"message","Этот логин уже используется");
    }

    @Test
    @DisplayName("Если логина нет, запрос возвращает ошибку")
    public void creationWithoutLogin() {
        courier.createCourier("ghghgh","6666","sysysys");
        Response response = courier.createCourier("","6666","sysysys");
        response.then().statusCode(400);
        courier.checkBody(response,"message","Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Если пароля нет, запрос возвращает ошибку")
    public void creationWithoutPassword() {
        courier.createCourier("ghghgh","6666","sysysys");
        Response response = courier.createCourier("ghghgh","","sysysys");
        response.then().statusCode(400);
        courier.checkBody(response,"message","Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Нельзя создать курьера с существующим логином")
    public void creationWithExistingLogin() {
        courier.createCourier("ghghgh","6666","sysysys");
        Response response = courier.createCourier("ghghgh","0000","sysysys");
        response.then().statusCode(409);
        courier.checkBody(response,"message","Этот логин уже используется");
    }

    @After
    public void after() {
        Response log = courier.sendPostRequestToGetId("ghghgh","6666","sysysys");
        Response delete = courier.getIdAndDelete(log);
        delete.then().statusCode(200);
    }

}