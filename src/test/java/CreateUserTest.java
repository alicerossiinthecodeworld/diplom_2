import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CreateUserTest extends RestAssuredClient{
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
    @DisplayName("тест создания нового пользователя")
    public void createUniqueUserSuccessTest(){
        Response resp = userClient.createUser(user);
        assertEquals(200, resp.getStatusCode());
    }

    @Test
    @DisplayName("тест создания одинакового пользователя")
    public void createNonUniqueUserTest(){
        userClient.createUser(user);
        response = userClient.createUser(user);
        assertEquals(403, response.getStatusCode());
    }

    @Test
    @DisplayName("тест создания пользователя без почты")
    public void createUserWithoutEmailTest(){
        response = userClient.createUserWithoutEmail(user);
        assertEquals(403, response.getStatusCode());
    }

    @Test
    @DisplayName("тест создания пользователя без пароля")
    public void createUserWithoutPasswordTest(){
        response = userClient.createUserWithoutPassword(user);
        assertEquals(403, response.getStatusCode());
    }

    @Test
    @DisplayName("тест создания пользователя без имени")
    public void createUserWithoutNameTest(){
        response = userClient.createUserWithoutName(user);
        assertEquals(403, response.getStatusCode());
    }
}
