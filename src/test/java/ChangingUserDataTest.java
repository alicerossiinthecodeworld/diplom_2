import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChangingUserDataTest {
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
    @DisplayName("тест изменения данных с авторизацией")
    public void getUserDataAuthorizedSuccessTest(){
        userClient.createUser(user);
        response = userClient.getUserData(userClient.getToken(user));
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("тест получения данных пользователя")
    public void returnsUserDataTest(){
        userClient.createUser(user);
        response = userClient.getUserData(userClient.getToken(user));
        assertNotNull(response.body().path("user.email"));
        assertNotNull(response.body().path("user.name"));
    }

    @Test
    @DisplayName("тест изменения пароля")
    public void userPasswordChangedTest(){
        userClient.createUser(user);
        User newUser = new User(user.email, User.getRandomData(), user.name);
        response = userClient.changeUserData(userClient.getToken(user), newUser);
        assertEquals(200, response.getStatusCode());
        assertEquals(401, userClient.login(user).getStatusCode());
        userClient.deleteUser(newUser);
    }

    @Test
    @DisplayName("тест изменения email")
    public void userEmailChangedTest(){
        userClient.createUser(user);
        User newUser = new User(User.getRandomEmail(), user.password, user.name);
        response = userClient.changeUserData(userClient.getToken(user), newUser);
        assertEquals(200, response.getStatusCode());
        assertEquals(401, userClient.login(user).getStatusCode());
        userClient.deleteUser(newUser);
    }

    @Test
    @DisplayName("тест изменения имени пользователя")
    public void userNameChangedTest(){
        userClient.createUser(user);
        User newUser = new User(user.email, user.password, User.getRandomData());
        response = userClient.changeUserData(userClient.getToken(user), newUser);
        assertEquals(200, response.getStatusCode());
        userClient.deleteUser(newUser);
    }

    @Test
    @DisplayName("при изменении почты возвращается новый email")
    public void changeEmailReturnsNewEmail(){
        userClient.createUser(user);
        String email = User.getRandomEmail();
        User newUser = new User(email, user.password, user.name);
        response = userClient.changeUserData(userClient.getToken(user), newUser);
        assertEquals(email.toLowerCase(), response.body().path("user.email"));
        userClient.deleteUser(newUser);
    }

    @Test
    @DisplayName("при изменении имени возвращается новое имя")
    public void changeNameReturnsNewName(){
        userClient.createUser(user);
        String name = User.getRandomData();
        User newUser = new User(user.email, user.password, name);
        response =userClient.changeUserData(userClient.getToken(user), newUser);
        assertEquals(name, response.body().path("user.name"));
        userClient.deleteUser(newUser);
    }

    @Test
    @DisplayName("тест изменения данных без авторизации")
    public void changingDataUnauthorizedTest(){
        response = userClient.changeUserData("", user);
        assertEquals(401, response.getStatusCode());
        assertEquals("You should be authorised", response.body().path("message"));
    }


    @Test
    @DisplayName("тест ошибки авторизации при получении данных")
    public void getDataAuthorizationErrorTest(){
        response = userClient.getUserData("");
        assertEquals(401, response.getStatusCode());
        assertEquals("You should be authorised", response.body().path("message"));
    }

}
