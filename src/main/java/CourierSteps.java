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
    public Response sendPostRequestCreateCourier() {
        CourierData courier = new CourierData("Pashuuumdlh","12345","Sssss");
        Response response = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Создаем курьера без логина")
    public Response createCourierWithoutLogin() {
        CourierData courier = new CourierData("","12345","Sssss");
        Response response = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Создаем курьера без пароля")
    public Response createCourierWithoutPassword() {
        CourierData courier = new CourierData("Pashuuumdlh","","Sssss");
        Response response = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Создаем курьера с существующим логином, но другим паролем")
    public Response createCourierWithAnotherPassword() {
        CourierData courier = new CourierData("Pashuuumdlh","1111","Sssss");
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
    public Response sendPostRequestToGetId() {
        CourierData courier = new CourierData("Pashuuumdlh","12345","Sssss");
        Response login = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
        return login;
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
    public Response createNewCourier() {
        CourierData courier = new CourierData("Masha","5555");
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
    public Response sendPostRequestSuccessful() {
        CourierData courier = new CourierData("Masha","5555");
        Response response = given()
                .baseUri(baseURI)
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
                .baseUri(baseURI)
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
                .baseUri(baseURI)
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
                .baseUri(baseURI)
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
                .baseUri(baseURI)
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
                .baseUri(baseURI)
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
        response.then().statusCode(code).and().assertThat().body(parameter, CoreMatchers.is(text));
    }

    @Step("Получение id курьера и удаление созданного курьера")
    public Response getIdAndDeleteInLoginTest(){
        Response login = sendPostRequestSuccessful();
        String id = login.then().extract().jsonPath().getString("id");

        Response delete = given()
                .baseUri(baseURI)
                .when()
                .delete("/api/v1/courier/" + id);
        return delete;
    }
}
