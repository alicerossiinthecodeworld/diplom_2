import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CreateUserTest extends RestAssuredClient{
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
    public void createUniqueUserSuccessTest(){
        Response resp = createUser(user);
        assertEquals(200, resp.getStatusCode());
    }

    @Test
    public void createNonUniqueUserTest(){
        createUser(user);
        resp = createUser(user);
        assertEquals(403, resp.getStatusCode());
    }

    @Test
    public void createUserWithoutEmailTest(){
        resp = userClient.createUserWithoutEmail(user);
        assertEquals(403, resp.getStatusCode());
    }

    @Test
    public void createUserWithoutPasswordTest(){
        resp = userClient.createUserWithoutPassword(user);
        assertEquals(403, resp.getStatusCode());
    }

    @Test
    public void createUserWithoutNameTest(){
        resp = userClient.createUserWithoutName(user);
        assertEquals(403, resp.getStatusCode());
    }

    @Step("CreateUser")
    public Response createUser(User user) {
        return userClient.createUser(user);
}
    @Step("DeleteUser")
    public void deleteUser(User user){
         String token = userClient.login(user).body().path("accessToken");
         if (token != null){
             userClient.deleteUser(token);
        }
    }
}
