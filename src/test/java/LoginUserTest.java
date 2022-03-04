import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginUserTest{
    private Response response;
    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
    }
    @After
    public void tearDown(){
        userClient.deleteUser(user);
    }


    @Test
    @DisplayName("тест логина")
    public void loginTest(){
        userClient.createUser(user);
        response = userClient.login(user);
        assertEquals(200, response.getStatusCode());
        System.out.print(response.body().path("accessToken").toString());
    }
    @Test
    @DisplayName("тест логина незарегистрированного пользователя")
    public void loginNonRegisteredTest(){
        response = userClient.login(user);
        assertEquals(401, response.getStatusCode());
    }

}
