import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {

    @Before
    public void setUpAndCreateCourier() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        createNewCourier();
    }

    //Через Postman выдает 404 Not Found
    @Test
    @DisplayName("Проверка возможности авторизации курьера")
    public void successfulCourierAuthorization() {
        Response response = sendPostRequestSuccessful();
        checkCodeAndTextOfSuccess(response);
    }

    @Test
    @DisplayName("Система вернет ошибку если неправильно указать логин")
    public void incorrectLogin() {
        Response response = sendPostRequestIncorrectLogin();
        checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
    }

    @Test
    @DisplayName("Система вернет ошибку если неправильно указать пароль")
    public void incorrectPassword() {
        Response response = sendPostRequestIncorrectPass();
        checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
    }

    @Test
    @DisplayName("Система вернет ошибку если логин отсутствует")
    public void withoutLogin() {
        Response response = sendPostRequestWithoutLogin();
        checkCodeAndTextOfError(response,400,"message","Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Система вернет ошибку если пароль отсутствует")
    public void withoutPassword() {
        Response response = sendPostRequestWithoutPass();
        checkCodeAndTextOfError(response,400,"message","Недостаточно данных для входа");
    }

    //При использовании несуществующей пары логин/пароль код 201 - баг
    @Test
    @DisplayName("Система вернет ошибку если авторизоваться под несуществующим пользователем")
    public void nonexistentUser() {
        Response response = sendPostRequestNonexistentUser();
        checkCodeAndTextOfError(response,404,"message","Учетная запись не найдена");
    }

    @After
    public void deleteCourier() {
        sendDeleteRequest(getCourierId());
    }

    @Step("Создание нового курьера")
    public Response createNewCourier() {
        CourierData courier = new CourierData("Masha","5555");
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Отправляем POST запрос на ручку /api/v1/courier/login")
    public Response sendPostRequestSuccessful() {
        CourierData courier = new CourierData("Masha","5555");
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Отправляем POST запрос с неправильным логином в теле")
    public Response sendPostRequestIncorrectLogin() {
        CourierData courier = new CourierData("Pasha","5555");
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Отправляем POST запрос с неправильным паролем в теле")
    public Response sendPostRequestIncorrectPass() {
        CourierData courier = new CourierData("Masha","55535");
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Отправляем POST запрос без пароля в теле")
    public Response sendPostRequestWithoutLogin() {
        CourierData courier = new CourierData("","5555");
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Отправляем POST запрос без пароля в теле")
    public Response sendPostRequestWithoutPass() {
        CourierData courier = new CourierData("Masha","");
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Отправляем POST запрос без пароля в теле")
    public Response sendPostRequestNonexistentUser() {
        CourierData courier = new CourierData("Pasha","1234");
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Проверяем код и тело ответа при успешном запросе")
    public void checkCodeAndTextOfSuccess(Response response) {
        response.then().statusCode(200).and().assertThat().body("id", notNullValue());
    }

    @Step("Проверяем код и тело ответа при ошибках")
    public void checkCodeAndTextOfError(Response response, Integer code,String parameter, String text) {
        response.then().log().all().statusCode(code).and().assertThat().body(parameter,is(text));
    }

    @Step("Получаем id курьера")
    public Integer getCourierId() {
        Integer courierId = sendPostRequestSuccessful()
                .body()
                .as(CourierData.class).getId();
        return courierId;
    }

    @Step("Удаляем курьера")
    public Response sendDeleteRequest(Integer courierId) {
        Response response = given()
                .header("Content-type","application/json")
                .when()
                .delete("/api/v1/courier/"+ courierId);
        return response;
    }

}
