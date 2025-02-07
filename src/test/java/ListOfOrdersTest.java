import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ListOfOrdersTest {
    OrderData order =
            new OrderData("Петр","Петров","Калинина","2","+79225554411",2,"2020-06-06","Удачи", List.of(""));

    @Before
    public void setUpAndCreateOrder() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        sendPostRequestToCreateOrder();
    }

    @Test
    @DisplayName("В тело ответа возвращается список заказов")
    public void listOfOrders() {
        Response response = sendGetRequestToLookOrders();
        checkTheListOfOrders(response);
    }

    @Step("Отправляем POST запрос на ручку /api/v1/orders")
    public Response sendPostRequestToCreateOrder() {
        Response response = given()
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
