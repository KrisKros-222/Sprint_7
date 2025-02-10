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
    public Response sendPostRequestToCreateOrder(List<String> colour) {
        OrderData order = new OrderData("Ванька","Иванов","Подмосковье","3","+79002556633",3 ,"2020-06-06","Хочу побыстрее", colour);
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

    @Step("Отправляем POST запрос на ручку /api/v1/orders")
    public Response sendPostRequestToCreateOrder() {
        OrderData order =
                new OrderData("Петр","Петров","Калинина","2","+79225554411",2,"2020-06-06","Удачи", List.of(""));

        Response response = given()
                .baseUri(baseURI)
                .header("Content-type","application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
        return response;
    }

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
