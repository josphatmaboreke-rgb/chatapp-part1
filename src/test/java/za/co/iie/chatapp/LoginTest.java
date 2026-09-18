package za.co.iie.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * My JUnit 5 unit tests. I use exactly the test data given in the brief.
 * Student Number: ST10362279
 */
public class LoginTest {

    // ---------- assertTrue / assertFalse tests ----------

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongPass1!"));
    }

    // ---------- assertEquals tests (message checks) ----------

    @Test
    public void testRegisterUserMessagesForBadUsername() {
        Login login = new Login();
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void testRegisterUserMessageForBadPassword() {
        Login login = new Login();
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.registerUser("kyl_1", "password", "+27838968976"));
    }

    @Test
    public void testRegisterUserMessageForBadCellNumber() {
        Login login = new Login();
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.",
                login.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553"));
    }

    @Test
    public void testReturnLoginStatusSuccess() {
        Login login = new Login();
        login.setUserNames("Kyle", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testReturnLoginStatusFailure() {
        Login login = new Login();
        login.setUserNames("Kyle", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus("wrong", "wrong"));
    }
}
