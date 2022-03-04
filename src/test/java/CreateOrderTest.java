import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
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
    @DisplayName("тест создания заказа с ингредиентами")
    public void createOrderWithIngredientsTest(){
        assertEquals(200, orderClient.createOrderUnauthorized(orderClient.getRandomIngredient()).statusCode());
    }

    @Test
    @DisplayName("тест создания заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest(){
        response = orderClient.createOrderUnauthorized("");
        assertEquals(400, response.statusCode());
        assertEquals("Ingredient ids must be provided", response.body().path("message"));
    }

    @Test
    @DisplayName("тест создания заказа авторизованным пользователем")
    public void createOrderAuthorizedTest(){
        userClient.createUser(user);
        String ingredient = orderClient.getRandomIngredient();
        response = orderClient.createOrderWithIngredientAuthorized(ingredient, userClient.getToken(user));
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("тест заказа с неправильным хешем ингредиентов")
    public void createOrderWithWrongIngredientsTest(){
        userClient.createUser(user);
        assertEquals(500, orderClient.createOrderWithIngredientAuthorized("2212",
                userClient.getToken(user)).statusCode());
    }


}
