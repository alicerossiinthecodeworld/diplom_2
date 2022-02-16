import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginUserTest{
    private Response resp;
    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
    }
    @After
    public void tearDown(){
        deleteUser(user);
    }


    @Test
    public void loginTest(){
        createUser(user);
        resp = loginUser(user);
        assertEquals(200, resp.getStatusCode());
        System.out.print(resp.body().path("accessToken").toString());
    }
    @Test
    public void loginNonRegisteredTest(){
        resp = loginUser(user);
        assertEquals(401, resp.getStatusCode());
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
