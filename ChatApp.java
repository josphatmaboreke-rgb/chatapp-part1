package za.co.iie.chatapp;

import java.util.Scanner;

/**
 * My console application for PROG5121 PoE Parts 1, 2, and 3.
 * Part 1: Registration + Login.
 * Part 2: QuickChat Sending Messages.
 * Part 3: Parallel Arrays, JSON Reading, Search, Delete, and Reports.
 * Student Number: ST10362279
 */
public class ChatApp {

    // I use one Scanner for the whole console app.
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
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

        // The user may only access QuickChat after a successful login.
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Username: ");
            String loginUsername = input.nextLine();
            System.out.print("Password: ");
            String loginPassword = input.nextLine();
            System.out.println(login.returnLoginStatus(loginUsername, loginPassword));
            loggedIn = login.loginUser(loginUsername, loginPassword);
        }

        // I load any previously stored messages from messages.json into my Stored Messages array.
        Message.loadStoredMessagesFromJson();

        runQuickChat(login.getFullName());
        input.close();
    }

    /** My QuickChat main menu (Parts 2 and 3). */
    private static void runQuickChat(String senderName) {
        System.out.println("\nWelcome to QuickChat.");

        // I ask how many messages the user wants to enter when QuickChat starts.
        int maxMessages = readNumber("How many messages would you like to send? ");
        while (maxMessages < 1) {
            maxMessages = readNumber("Please enter a number of 1 or more: ");
        }

        int messagesEntered = 0;   // my counter of how many messages were entered so far
        boolean running = true;

        // My while loop keeps the app running until the user chooses 3) Quit.
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            int option = readNumber("Option: ");

            switch (option) {
                case 1:
                    messagesEntered = sendMessages(maxMessages, messagesEntered, senderName);
                    break;
                case 2:
                    System.out.println("\n--- Recently Sent Messages ---");
                    System.out.println(Message.printMessages());
                    break;
                case 3:
                    running = false;
                    break;
                case 4:
                    runStoredMessagesMenu();
                    break;
                default:
                    System.out.println("Invalid option, please enter 1, 2, 3 or 4.");
            }
        }

        System.out.println("\nTotal number of messages sent: " + Message.returnTotalMessages());
        System.out.println("Goodbye!");
    }

    /**
     * My Part 3 fourth main menu option ("Stored Messages") where the user can
     * inspect, search, delete, and generate reports from the parallel arrays.
     */
    private static void runStoredMessagesMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== STORED MESSAGES & REPORTS ===");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search for all messages for a particular recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display report of full details of all stored messages");
            System.out.println("g) Display report of all sent messages");
            System.out.println("x) Back to Main Menu");
            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    System.out.println("\n" + Message.displaySendersAndRecipients());
                    break;
                case "b":
                    System.out.println("\nLongest Message: " + Message.displayLongestMessage());
                    break;
                case "c":
                    System.out.print("Enter Message ID to search: ");
                    String searchId = input.nextLine().trim();
                    System.out.println(Message.searchByMessageId(searchId));
                    break;
                case "d":
                    System.out.print("Enter Recipient cell number (e.g. +27838884567): ");
                    String recipient = input.nextLine().trim();
                    System.out.println(Message.searchByRecipient(recipient));
                    break;
                case "e":
                    System.out.print("Enter Message Hash to delete: ");
                    String hash = input.nextLine().trim();
                    System.out.println(Message.deleteMessageByHash(hash));
                    break;
                case "f":
                    System.out.println("\n=== STORED MESSAGES REPORT ===");
                    System.out.println(Message.displayStoredMessagesReport());
                    break;
                case "g":
                    System.out.println("\n=== SENT MESSAGES REPORT ===");
                    System.out.println(Message.displaySentMessagesReport());
                    break;
                case "x":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a, b, c, d, e, f, g, or x.");
            }
        }
    }

    /**
     * My for loop only lets the user enter the number of messages they chose at the start.
     * I return the updated count so the limit still works if they come back to option 1.
     */
    private static int sendMessages(int maxMessages, int messagesEntered, String senderName) {
        if (messagesEntered >= maxMessages) {
            System.out.println("You have already entered all " + maxMessages + " messages.");
            return messagesEntered;
        }

        for (int i = messagesEntered; i < maxMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + maxMessages + " ---");

            // I keep asking until the recipient number is valid.
            String recipient;
            String cellStatus;
            do {
                System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                recipient = input.nextLine().trim();
                cellStatus = new Message(i, recipient, "x").checkRecipientCell();
                System.out.println(cellStatus);
            } while (!cellStatus.startsWith("Cell phone number successfully"));

            // I keep asking until the message is 250 characters or less (and not empty).
            String text;
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                text = input.nextLine();
                if (text.trim().isEmpty()) {
                    System.out.println("Message cannot be empty.");
                } else if (text.length() > Message.MAX_MESSAGE_LENGTH) {
                    System.out.println("Please enter a message of less than 250 characters.");
                } else {
                    System.out.println("Message ready to send.");
                    break;
                }
            }

            // My loop counter i is used as the message number in the hash.
            Message msg = new Message(i, recipient, text);
            msg.setSender(senderName);
            System.out.println(msg.getMessageIdStatus());

            System.out.println("\nWhat would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            int choice = readNumber("Choice: ");
            while (choice < 1 || choice > 3) {
                choice = readNumber("Please choose 1, 2 or 3: ");
            }

            String result = msg.sentMessage(choice);
            System.out.println(result);

            if (choice == 1) {
                System.out.println(msg.getMessageDetails());
            } else if (choice == 2) {
                System.out.println("Message disregarded.");
            }
            messagesEntered++;
        }

        System.out.println("\nAll messages entered. Total number of messages sent: "
                + Message.returnTotalMessages());
        return messagesEntered;
    }

    /** I read a whole number safely so letters don't crash my menu. */
    private static int readNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
