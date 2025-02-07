import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CourierCreationTest {
    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("При передаче всех необходимых параметров можно создать курьера")
    public void createCourier() {
        Response response = sendPostRequestCreateCourier("fullBodyForCreation.json");
        response.then().statusCode(201);
        compareSuccessBody(response,"ok",true);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateTwoSimilarCouriers() {
        Response response = sendPostRequestCreateCourier("fullBodyForCreation.json");
        response.then().statusCode(409);
    }

    @Test
    @DisplayName("Если логина нет, запрос возвращает ошибку")
    public void creationWithoutLogin() {
        Response response = sendPostRequestCreateCourier("creationWithoutLogin.json");
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("Если пароля нет, запрос возвращает ошибку")
    public void creationWithoutPassword() {
        Response response = sendPostRequestCreateCourier("creationWithoutPassword.json");
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("Нельзя создать курьера с существующим логином")
    public void creationWithExistingLogin() {
        Response response = sendPostRequestCreateCourier("anotherPassword.json");
        response.then().statusCode(409);
    }

    @Step("Отправляем POST запрос на ручку /api/v1/courier")
    public Response sendPostRequestCreateCourier(String param) {
        File parameters = new File("src/main/resources/" + param);
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(parameters)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Проверяем тело ответа при успешном запросе")
    public void compareSuccessBody(Response response,String path, Boolean text) {
        response.then().assertThat().body(path,is(text));
    }

}