import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient{

    private final static String INGREDIENT_PATH = "ingredients";
    private final static String ORDER_PATH = "orders";

    public Response getIngredients(){
        return given()
                .log().all()
                .spec(getBaseSpec())
                .get(INGREDIENT_PATH);
    }

    public Response createOrderWithIngredient(String ingredient){
        JSONObject orderBody= new JSONObject();
        orderBody.put("ingredients", ingredient);
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(orderBody.toString())
                .post(ORDER_PATH);
    }

    public Response createOrderWithIngredientAuthorized(String ingredient, String token){
        JSONObject orderBody= new JSONObject();
        orderBody.put("ingredients", ingredient);
        return given()
                .log().all()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .body(orderBody.toString())
                .post(ORDER_PATH);
    }

    public Response getUserOrders(String token){
        return given()
                .log().all()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .get(ORDER_PATH);
    }
}
