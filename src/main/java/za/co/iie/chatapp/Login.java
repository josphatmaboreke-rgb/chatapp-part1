package za.co.iie.chatapp;

/**
 * My Login class for PROG5121 PoE Part 1.
 * Student Number: ST10362279
 *
 * I keep all my registration and login rules inside this class so that my code
 * is testable with JUnit (no input/output happens in here, only logic).
 */
public class Login {

    // I store the details the user registered with, so that I can check them again at login.
    private String registeredUsername = "";
    private String registeredPassword = "";
    private String registeredCellPhone = "";
    private String firstName = "";
    private String lastName = "";

    /**
     * I check that the username contains an underscore and is no more than
     * five characters long.
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * I check my password complexity rules: at least 8 characters, a capital
     * letter, a number and a special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // I loop through every character and flag what I find.
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * I use a regular expression to check that the cell phone number starts with
     * the South African international country code (+27) and that the number
     * after the code is no more than ten characters long.
     *
     * Reference: Regular expression syntax adapted from
     * Oracle (2024) Class Pattern (java.util.regex), Java SE 17 API Documentation.
     * Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        return cellPhoneNumber.matches("^\\+27\\d{1,10}$");
    }

    /**
     * I register the user and return the correct message for each rule that
     * fails, or the success message when everything is valid.
     */
    public String registerUser(String username, String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // Everything passed, so I save the details for the login step.
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellPhone = cellPhoneNumber;
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    /** I store the user's name so that I can greet them personally when they log in. */
    public void setUserNames(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /** I verify that the details entered at login match the details I stored at registration. */
    public boolean loginUser(String username, String password) {
        return registeredUsername.equals(username) && registeredPassword.equals(password);
    }

    /** I return the welcome message on success, or the error message on failure. */
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getRegisteredCellPhone() {
        return registeredCellPhone;
    }
}
