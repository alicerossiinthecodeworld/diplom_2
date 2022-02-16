import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChangingUserDataTest {
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
    public void getUserDataAuthorizedSuccessTest(){
        createUser(user);
        resp = getUserData(getToken(user));
        assertEquals(200, resp.getStatusCode());
    }

    @Test
    public void returnsUserDataTest(){
        createUser(user);
        resp = getUserData(getToken(user));
        assertNotNull(resp.body().path("user.email"));
        assertNotNull(resp.body().path("user.name"));
    }

    @Test
    public void userPasswordChangedTest(){
        createUser(user);
        User newUser = new User(user.email, User.getRandomData(), user.name);
        resp = changeUserData(getToken(user), newUser);
        assertEquals(200, resp.getStatusCode());
        assertEquals(401, loginUser(user).getStatusCode());
        deleteUser(newUser);
    }

    @Test
    public void userEmailChangedTest(){
        createUser(user);
        User newUser = new User(User.getRandomEmail(), user.password, user.name);
        resp = changeUserData(getToken(user), newUser);
        assertEquals(200, resp.getStatusCode());
        assertEquals(401, loginUser(user).getStatusCode());
        deleteUser(newUser);
    }

    @Test
    public void userNameChangedTest(){
        createUser(user);
        User newUser = new User(user.email, user.password, User.getRandomData());
        resp = changeUserData(getToken(user), newUser);
        assertEquals(200, resp.getStatusCode());
        deleteUser(newUser);
    }

    @Test
    public void changeEmailReturnsNewEmail(){
        createUser(user);
        String email = User.getRandomEmail();
        User newUser = new User(email, user.password, user.name);
        resp = changeUserData(getToken(user), newUser);
        assertEquals(email.toLowerCase(), resp.body().path("user.email"));
        deleteUser(newUser);
    }

    @Test
    public void changeNameReturnsNewName(){
        createUser(user);
        String name = User.getRandomData();
        User newUser = new User(user.email, user.password, name);
        resp = changeUserData(getToken(user), newUser);
        assertEquals(name, resp.body().path("user.name"));
        deleteUser(newUser);
    }

    @Test
    public void changingDataUnauthorizedTest(){
        resp = changeUserData("", user);
        assertEquals(401, resp.getStatusCode());
        assertEquals("You should be authorised", resp.body().path("message"));
    }


    @Test
    public void getDataAuthorizationErrorTest(){
        resp = getUserData("");
        assertEquals(401, resp.getStatusCode());
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

    @Step("GetUserData")
    public Response getUserData(String token){
        return userClient.getUserData(token);
    }

    @Step("ChangeUserData")
    public Response changeUserData(String token, User user){
        return userClient.changeUserData(token, user);
    }
    @Step("deleteUser")
    public void deleteUser(User user) {
        if (getToken(user) != null) {
            userClient.deleteUser(getToken(user));
        }
    }
}
