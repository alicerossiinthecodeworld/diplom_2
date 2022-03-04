import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import org.json.JSONObject;

public class UserClient extends RestAssuredClient{
    private static final String REGISTER_PATH = "auth/register/";
    private static final String LOGIN_PATH = "auth/login";
    private static final String USER_DATA_PATH = "auth/user";


    @Step("cоздание случайного пользователя")
    public Response createUser(User user){
        return given()
                .spec(getBaseSpec())
                .body(user).when()
                .post(REGISTER_PATH);
    }

    @Step("создание пользователя без email")
    public Response createUserWithoutEmail(User user){
        JSONObject registerBodyWithoutEmail = new JSONObject();
        registerBodyWithoutEmail.put("password", user.password);
        registerBodyWithoutEmail.put("firstName", user.name);
        return given()
                .spec(getBaseSpec())
                .body(registerBodyWithoutEmail.toString()).when()
                .post(REGISTER_PATH);
    }

    @Step("создание пользователя без пароля")
    public Response createUserWithoutPassword(User user){
        JSONObject registerBodyWithoutPassword = new JSONObject();
        registerBodyWithoutPassword.put("email", user.email);
        registerBodyWithoutPassword.put("firstName", user.name);
        return given()
                .spec(getBaseSpec())
                .body(registerBodyWithoutPassword.toString()).when()
                .post(REGISTER_PATH);
    }

    @Step("Создание пользователя без имени")
    public Response createUserWithoutName(User user){
        JSONObject registerBodyWithoutName = new JSONObject();
        registerBodyWithoutName.put("email", user.email);
        registerBodyWithoutName.put("password", user.password);
        return given()
                .spec(getBaseSpec())
                .body(registerBodyWithoutName.toString()).when()
                .post(REGISTER_PATH);
    }

    @Step("логин")
    public Response login(User user){
        return given()
                .spec(getBaseSpec())
                .body(user).when()
                .post(LOGIN_PATH);
    }

    @Step("получение данных пользователя")
    public Response getUserData(String token){
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec()).when()
                .get(USER_DATA_PATH);
    }

    @Step("изменение данных пользователя")
    public Response changeUserData(String token, User user){
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec()).when()
                .body(user)
                .patch(USER_DATA_PATH);
    }

    @Step("удаление пользователя")
    public void deleteUser(String token){
                given()
                .header("Authorization", token)
                .spec(getBaseSpec()).when()
                .delete(USER_DATA_PATH).then().assertThat().statusCode(202);
    }

    @Step("получение токена")
    public String getToken(User user){
        return login(user).body().path("accessToken");
    }

    @Step("удаление пользователя")
    public void deleteUser(User user) {
        if (getToken(user) != null) {
            deleteUser(getToken(user));
        }
    }
}

