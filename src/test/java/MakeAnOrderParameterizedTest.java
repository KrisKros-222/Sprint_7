import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;

@RunWith(Parameterized.class)
public class MakeAnOrderParameterizedTest {
    private final List<String> colour;
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private OrderSteps order;

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
    public void before() {
        order = new OrderSteps(BASE_URI);
    }

    //Если запускать код без отмены заказа, то все проходит, а с отменой ошибка и в Postman, и здесь
    @Test
    @DisplayName("Создаем заказ")
    public void createAnOrder() {
        Response response = order.sendPostRequestToCreateOrder(colour);
        order.checkTrackNumber(response);
        Response cancel = order.cancelOrderByTrack(response);
        cancel.then().statusCode(200);
    }

}
