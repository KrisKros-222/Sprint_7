import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class MakeAnOrderParameterizedTest {
    private final List<String> colour;

    public MakeAnOrderParameterizedTest(List<String> colour) {
        this.colour = colour;
    }

    @Parameterized.Parameters
    public static Object[][] getColourData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("")},
                {List.of("BLACK", "GREY")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Создаем заказ")
    public void createAnOrder() {
        Response response = sendPostRequestToCreateOrder(colour);
        checkTrackNumber(response);
    }

    @Step("Отправляем POST запрос на ручку /api/v1/orders")
    public Response sendPostRequestToCreateOrder(List<String> colour) {
        OrderData order = new OrderData("Ванек","Иванов","Подмосковье","3","+79002556633",3 ,"2020-06-06","Хочу побыстрее", colour);
        Response response = given()
                .header("Content-type","application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
        return response;
    }

    @Step("Проверяем наличие параметра track")
    public void checkTrackNumber(Response response) {
        response.then().assertThat().body("track", notNullValue());
    }
}
