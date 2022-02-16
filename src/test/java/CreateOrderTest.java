import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
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
    public void createOrderWithIngredientsTest(){
        assertEquals(200, createOrderUnauthorized(getRandomIngredient()).statusCode());
    }

    @Test
    public void createOrderWithoutIngredientsTest(){
        resp = createOrderUnauthorized("");
        assertEquals(400,resp.statusCode());
        assertEquals("Ingredient ids must be provided", resp.body().path("message"));
    }

    @Test
    public void createOrderAuthorizedTest(){
        createUser(user);
        resp = createOrderAuthorized(getRandomIngredient(),user);
        assertEquals(200, resp.statusCode());
    }

    @Test
    public void createOrderWithWrongIngredientsTest(){
        createUser(user);
        assertEquals(500, createOrderAuthorized("2212", user).statusCode());
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

    @Step("createOrder")
    public Response createOrderUnauthorized(String ingredient){
        return orderClient.createOrderWithIngredient(ingredient);
    }

    @Step("createOrderAuthorized")
    public Response createOrderAuthorized(String ingredient, User user){
        return orderClient.createOrderWithIngredientAuthorized(ingredient, getToken(user));
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
}
