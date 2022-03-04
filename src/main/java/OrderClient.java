import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient{

    private final static String INGREDIENT_PATH = "ingredients";
    private final static String ORDER_PATH = "orders";


    @Step("получение ингредиентов")
    public Response getIngredients(){
        return given()
                .spec(getBaseSpec())
                .get(INGREDIENT_PATH);
    }

    @Step("создание заказа с ингредиентами")
    public Response createOrderWithIngredient(String ingredient){
        JSONObject orderBody= new JSONObject();
        orderBody.put("ingredients", ingredient);
        return given()
                .spec(getBaseSpec())
                .body(orderBody.toString())
                .post(ORDER_PATH);
    }

    @Step("создать заказ с ингредиентами авторизованным пользователем")
    public Response createOrderWithIngredientAuthorized(String ingredient, String token){
        JSONObject orderBody= new JSONObject();
        orderBody.put("ingredients", ingredient);
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .body(orderBody.toString())
                .post(ORDER_PATH);
    }

    @Step("получение заказов пользователя")
    public Response getUserOrders(String token){
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .get(ORDER_PATH);
    }

    @Step("получение id ингредиентов")
    public Object getIngredientsId(){
        return getIngredients().getBody().path("data._id");
    }

    @Step("получение случайного ингредиента")
    public String getRandomIngredient(){
        List ingredients = (List) getIngredientsId();
        Random r = new Random();
        int randomInt = r.nextInt( ingredients.size()-1);
        return (String) ingredients.get(randomInt);
    }

    @Step("создание заказа без авторизации")
    public Response createOrderUnauthorized(String ingredient){
        return createOrderWithIngredient(ingredient);
    }
}
