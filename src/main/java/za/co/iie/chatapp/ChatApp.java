package za.co.iie.chatapp;

import java.util.Scanner;

/**
 * My console application (no GUI, as the brief requires).
 * Student Number: ST10362279
 */
public class ChatApp {

    public static void main(String[] args) {
        // I use Scanner to read what the user types in the console.
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=== REGISTRATION ===");
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();
        login.setUserNames(firstName, lastName);

        String message;
        // I keep looping until the user enters details that pass all my rules.
        do {
            System.out.print("Enter a username (must contain _ and be max 5 characters): ");
            String username = input.nextLine();
            System.out.print("Enter a password (8+ chars, capital, number, special): ");
            String password = input.nextLine();
            System.out.print("Enter your cell number (e.g. +27838968976): ");
            String cell = input.nextLine();

            message = login.registerUser(username, password, cell);
            System.out.println(message);
        } while (!message.startsWith("Username successfully captured."));

        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String loginUsername = input.nextLine();
        System.out.print("Password: ");
        String loginPassword = input.nextLine();

        // I print the welcome or the failure message returned by my Login class.
        System.out.println(login.returnLoginStatus(loginUsername, loginPassword));

        input.close();
    }
}
