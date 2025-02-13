import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ListOfOrdersTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private OrderSteps orders;
    private Response response;

    @Before
    public void before() {
        orders = new OrderSteps(BASE_URI);
        response = orders.sendPostRequestToCreateOrder("Ванька","Иванов","Подмосковье","3","+79002556633",3 ,"2020-06-06","Хочу побыстрее", List.of(""));
    }

    @Test
    @DisplayName("В тело ответа возвращается список заказов")
    public void listOfOrders() {
        Response list = orders.sendGetRequestToLookOrders();
        orders.checkTheListOfOrders(list);
    }

    @After
    public void after() {
            orders.cancelOrderByTrack(response);
    }
}