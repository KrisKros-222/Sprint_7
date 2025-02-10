import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class ListOfOrdersTest {
        private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private OrderSteps orders;

    @Before
    public void before() {
        orders = new OrderSteps(BASE_URI);
        orders.sendPostRequestToCreateOrder();
    }

    @Test
    @DisplayName("В тело ответа возвращается список заказов")
    public void listOfOrders() {
        Response response = orders.sendGetRequestToLookOrders();
        orders.checkTheListOfOrders(response);
        orders.cancelOrderByTrack(response);
    }

}
