import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetUserOrdersTest {
    private OrderClient orderClient;
    private Response resp;
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
        deleteUser(user);
    }

    @Test
    public void getUserOrdersAuthorizedTest(){
        createUser(user);
        createOrder(getRandomIngredient(), user);
        resp = getUserOrderDataAuthorized(user);
        assertEquals(200, resp.getStatusCode());
        assertNotNull(resp.body().path("orders._id"));
    }
    @Test
    public void getUserOrderDataUnauthorized(){
        resp = getUserOrderUnauthorized();
        assertEquals(401, resp.statusCode());
        assertEquals("You should be authorised", resp.body().path("message"));
    }

    @Step("CreateUser")
    public Response createUser(User user) {
        return userClient.createUser(user);
    }

    @Step("LoginUser")
    public Response loginUser(User user){
        return userClient.login(user);
    }

    @Step("GetToken")
    public String getToken(User user){
        return loginUser(user).body().path("accessToken");
    }
    @Step("deleteUser")
    public void deleteUser(User user) {
        if (getToken(user) != null) {
            userClient.deleteUser(getToken(user));
        }
    }

    @Step("createOrderAuthorized")
    public Response createOrder(String ingredient, User user){
        return orderClient.createOrderWithIngredientAuthorized(ingredient, getToken(user));
    }
    @Step("getIngredients")
    public Object getIngredients(){
        return orderClient.getIngredients().getBody().path("data._id");
    }

    @Step("getRandomIngredient")
    public String getRandomIngredient(){
        List<String> ingredients = (List) getIngredients();
        Random r = new Random();
        int randomInt = r.nextInt( ingredients.size()-1);
        return ingredients.get(randomInt);
    }
    @Step("get user orders data authorized")
    public Response getUserOrderDataAuthorized(User user){
        return orderClient.getUserOrders(getToken(user));
    }
    @Step("get user orders unauthorized")
    public Response getUserOrderUnauthorized(){
        return orderClient.getUserOrders("");
    }
}
