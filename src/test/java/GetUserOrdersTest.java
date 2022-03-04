import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetUserOrdersTest {
    private OrderClient orderClient;
    private Response response;
    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        orderClient = new OrderClient();
    }

    @After
    public void tearDown(){
        userClient.deleteUser(user);
    }

    @Test
    @DisplayName("тест получения заказов авторизованного пользователя")
    public void getUserOrdersAuthorizedTest(){
        userClient.createUser(user);
        orderClient.createOrderWithIngredientAuthorized(orderClient.getRandomIngredient(), userClient.getToken(user));
        response = orderClient.getUserOrders(userClient.getToken(user));
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.body().path("orders._id"));
    }

    @Test
    @DisplayName("тест получения заказов пользователя без авторизации")
    public void getUserOrderDataUnauthorized(){
        response = orderClient.getUserOrders("");
        assertEquals(401, response.statusCode());
        assertEquals("You should be authorised", response.body().path("message"));
    }
}
