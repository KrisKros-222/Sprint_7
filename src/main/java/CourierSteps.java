import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class CourierSteps {
    private String baseURI;

    public CourierSteps(String baseURI) {
        this.baseURI = baseURI;
    }

    // Шаги для CourierCreationTest

    @Step("Отправляем POST запрос на ручку /api/v1/courier")
    public Response createCourier(String login, String password, String firstName) {
        CourierData courier = new CourierData(login,password,firstName);
        Response response = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Проверяем тело ответа при успешном запросе")
    public void checkBodySuccess(Response response,String path, Boolean text) {
        response.then().assertThat().body(path,is(text));
    }

    @Step("Проверяем тело ответа при успешном запросе")
    public void checkBody(Response response,String path, String text) {
        response.then().assertThat().body(path,is(text));
    }

    @Step("Отправляем POST запрос на ручку /api/v1/courier/login")
    public Response sendPostRequestToGetId(String login, String password, String firstName) {
        CourierData courier = new CourierData(login,password,firstName);
        Response log = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return log;
    }

    @Step("Получение id курьера и удаление созданного курьера")
    public Response getIdAndDelete(Response login){
        String id = login.then().extract().jsonPath().getString("id");

        Response delete = given()
                .baseUri(baseURI)
                .when()
                .delete("/api/v1/courier/" + id);
        return delete;
    }

    // Шаги для CourierLoginTest

    @Step("Создание нового курьера")
    public Response createCourierLogin(String login, String password) {
        CourierData courier = new CourierData(login,password);
        Response newCourier = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return newCourier;
    }

    @Step("Отправляем POST запрос на ручку /api/v1/courier/login")
    public Response sendPostRequest(String login, String password) {
        CourierData courier = new CourierData(login,password);
        Response response = given()
                .log().all()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("api/v1/courier/login");
        return response;
    }

    @Step("Проверяем код и тело ответа при успешном запросе")
    public void checkCodeAndTextOfSuccess(Response response) {
        response.then().statusCode(200).and().assertThat().body("id", notNullValue());
    }

    @Step("Проверяем код и тело ответа при ошибках")
    public void checkCodeAndTextOfError(Response response, Integer code,String parameter, String text) {
        response.then().statusCode(code).and().assertThat().body(parameter, CoreMatchers.is(text));
    }

    @Step("Получение id курьера и удаление созданного курьера")
    public Response getIdAndDeleteInLoginTest(String login, String password){
        Response log = sendPostRequest(login, password);
        String id = log.then().extract().jsonPath().getString("id");

        Response delete = given()
                .baseUri(baseURI)
                .when()
                .delete("/api/v1/courier/" + id);
        return delete;
    }
}
