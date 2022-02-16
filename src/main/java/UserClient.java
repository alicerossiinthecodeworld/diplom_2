import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import org.json.JSONObject;

public class UserClient extends RestAssuredClient{
    private static final String REGISTER_PATH = "auth/register/";
    private static final String LOGIN_PATH = "auth/login";
    private static final String USER_DATA_PATH = "auth/user";


    public Response createUser(User user){
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(user).when()
                .post(REGISTER_PATH);
    }

    public Response createUserWithoutEmail(User user){
        JSONObject registerBodyWithoutEmail = new JSONObject();
        registerBodyWithoutEmail.put("password", user.password);
        registerBodyWithoutEmail.put("firstName", user.name);
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(registerBodyWithoutEmail.toString()).when()
                .post(REGISTER_PATH);
    }
    public Response createUserWithoutPassword(User user){
        JSONObject registerBodyWithoutPassword = new JSONObject();
        registerBodyWithoutPassword.put("email", user.email);
        registerBodyWithoutPassword.put("firstName", user.name);
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(registerBodyWithoutPassword.toString()).when()
                .post(REGISTER_PATH);
    }

    public Response createUserWithoutName(User user){
        JSONObject registerBodyWithoutName = new JSONObject();
        registerBodyWithoutName.put("email", user.email);
        registerBodyWithoutName.put("password", user.password);
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(registerBodyWithoutName.toString()).when()
                .post(REGISTER_PATH);
    }

    public Response login(User user){
        return given()
                .log().all()
                .spec(getBaseSpec())
                .body(user).when()
                .post(LOGIN_PATH);
    }

    public Response getUserData(String token){
        return given()
                .log().all()
                .header("Authorization", token)
                .spec(getBaseSpec()).when()
                .get(USER_DATA_PATH);
    }

    public Response changeUserData(String token, User user){
        return given()
                .log().all()
                .header("Authorization", token)
                .spec(getBaseSpec()).when()
                .body(user)
                .patch(USER_DATA_PATH);
    }

    public void deleteUser(String token){
                given()
                .log().all()
                .header("Authorization", token)
                .spec(getBaseSpec()).when()
                .delete(USER_DATA_PATH).then().assertThat().statusCode(202);
    }
}

