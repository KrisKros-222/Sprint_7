import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderSteps {
    private String baseURI;

    public OrderSteps(String baseURI) {
        this.baseURI = baseURI;
    }

    // Шаги для MakeAnOrderTest

    @Step("Отправляем POST запрос на ручку /api/v1/orders")
    public Response sendPostRequestToCreateOrder(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> colour) {
        OrderData order = new OrderData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour);
        Response response = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
        return response;
    }

    @Step("Проверяем наличие параметра track")
    public void checkTrackNumber(Response response) {
        response.then().statusCode(201).and().assertThat().body("track", notNullValue());
    }

    @Step("Получаем значение track и отменяем заказ")
    public Response cancelOrderByTrack(Response response) {
        String trackNumber = response.then().extract().jsonPath().getString("track");
        String json = "{\"track\": " + trackNumber + "}";

        Response cancel = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(json)
                .when()
                .put("/api/v1/orders/cancel");
        return cancel;
    }

    //Шаги для ListOfOrders

    @Step("Отправляем GET запрос на ручку /api/v1/orders")
    public Response sendGetRequestToLookOrders() {
        Response response = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .when()
                .get("/api/v1/orders");
        return response;
    }

    @Step("Проверяем содержит ли ответ список заказов")
    public void checkTheListOfOrders(Response response) {
        response.then().statusCode(200).and().assertThat().body("orders", notNullValue());
    }

}
