package za.co.iie.chatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * My Message class for PROG5121 PoE Part 2 & Part 3 (QuickChat - Store Data and Display Report).
 * Student Number: ST10362279
 *
 * I keep all my message logic and parallel arrays inside this class so that every
 * feature can be tested cleanly with JUnit.
 */
public class Message {

    // I use these constants so I don't repeat "magic numbers" in my code.
    public static final int MAX_MESSAGE_LENGTH = 250;
    public static final String JSON_FILE = "messages.json";

    // My five required Part 3 arrays (populated dynamically with no hard-coding).
    private static String[] sentMessages = new String[0];
    private static String[] disregardedMessages = new String[0];
    private static String[] storedMessages = new String[0];
    private static String[] messageHashes = new String[0];
    private static String[] messageIds = new String[0];

    // My parallel arrays that keep sender, recipient, message text, and flag aligned with messageIds and messageHashes.
    private static String[] allMessages = new String[0];
    private static String[] messageSenders = new String[0];
    private static String[] messageRecipients = new String[0];
    private static String[] messageFlags = new String[0];

    private static int totalMessagesSent = 0;
    private static final Random random = new Random();

    // My details for one single message.
    private final String messageId;
    private final int messageNumber;
    private String sender;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    /** I create a message with a randomly generated ten-digit ID. */
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageId(), messageNumber, "QuickChat User", recipient, messageText);
    }

    /** I allow a fixed ID so my unit tests can check exact hashes and IDs. */
    public Message(String messageId, int messageNumber, String recipient, String messageText) {
        this(messageId, messageNumber, "QuickChat User", recipient, messageText);
    }

    /** I also allow setting the sender name so I can display sender and recipient in Part 3. */
    public Message(String messageId, int messageNumber, String sender, String recipient, String messageText) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.sender = (sender == null || sender.trim().isEmpty()) ? "QuickChat User" : sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    /** I let ChatApp attach the logged-in user's name as the sender of this message. */
    public void setSender(String sender) {
        if (sender != null && !sender.trim().isEmpty()) {
            this.sender = sender;
        }
    }

    /**
     * I generate a random ten-digit number and pad it with leading zeros
     * so that it is always ten characters long.
     */
    public static String generateMessageId() {
        long number = (long) (random.nextDouble() * 10_000_000_000L);
        return String.format("%010d", number);
    }

    /** I check that the message ID is not more than ten characters. */
    public boolean checkMessageID() {
        return messageId != null && messageId.length() <= 10;
    }

    /** I return the Message ID status text required by the brief. */
    public String getMessageIdStatus() {
        return "Message ID generated: " + messageId;
    }

    /**
     * I check that the recipient cell phone number starts with +27 and has
     * no more than ten digits after the international code.
     *
     * Reference: Oracle (2024) Class Pattern (java.util.regex), Java SE 17 API Documentation.
     * Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html
     */
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{1,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /** I check that the message is 250 characters or less, or say by how much it exceeds the limit. */
    public String checkMessageLength() {
        int length = (messageText == null) ? 0 : messageText.length();
        if (length <= MAX_MESSAGE_LENGTH) {
            return "Message ready to send.";
        }
        int over = length - MAX_MESSAGE_LENGTH;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    /**
     * I build my message hash using string manipulation:
     * first two digits of the ID : message number : first word + last word in uppercase.
     */
    public String createMessageHash() {
        String idPrefix = (messageId != null && messageId.length() >= 2) ? messageId.substring(0, 2) : "00";
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    /**
     * I handle the user's choice (1 = Send, 2 = Disregard, 3 = Store) and
     * populate my parallel arrays dynamically with no hard-coding.
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages = appendToArray(sentMessages, messageText);
                recordInParallelArrays("Sent");
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                disregardedMessages = appendToArray(disregardedMessages, messageText);
                recordInParallelArrays("Disregard");
                return "Press 0 to delete the message.";
            case 3:
                recordInParallelArrays("Stored");
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option, please choose 1, 2 or 3.";
        }
    }

    /** I add this message's details to all my parallel arrays at the same index. */
    private void recordInParallelArrays(String flag) {
        messageIds = appendToArray(messageIds, messageId);
        messageHashes = appendToArray(messageHashes, messageHash);
        allMessages = appendToArray(allMessages, messageText);
        messageSenders = appendToArray(messageSenders, sender);
        messageRecipients = appendToArray(messageRecipients, recipient);
        messageFlags = appendToArray(messageFlags, flag);
    }

    /** I grow a String array by one slot and place the new value at the end. */
    private static String[] appendToArray(String[] array, String value) {
        String[] updated = Arrays.copyOf(array, array.length + 1);
        updated[array.length] = value;
        return updated;
    }

    /** I remove one element at a given index from a String array. */
    private static String[] removeAtIndex(String[] array, int indexToRemove) {
        if (indexToRemove < 0 || indexToRemove >= array.length) {
            return array;
        }
        String[] updated = new String[array.length - 1];
        int pos = 0;
        for (int i = 0; i < array.length; i++) {
            if (i != indexToRemove) {
                updated[pos++] = array[i];
            }
        }
        return updated;
    }

    /** I remove the first matching text value from a category array (Sent or Stored). */
    private static String[] removeFirstMatch(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return removeAtIndex(array, i);
            }
        }
        return array;
    }

    /** I format the details of one message in order: ID, Hash, Recipient, Message. */
    public String getMessageDetails() {
        return "Message ID: " + messageId
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    /** I return every sent message recorded while the program is running. */
    public static String printMessages() {
        if (sentMessages.length == 0) {
            return "No messages have been sent.";
        }
        StringBuilder sb = new StringBuilder();
        int printed = 0;
        for (int i = 0; i < allMessages.length; i++) {
            if ("Sent".equals(messageFlags[i])) {
                if (printed > 0) {
                    sb.append("\n----------------\n");
                }
                sb.append("Message ID: ").append(messageIds[i])
                  .append("\nMessage Hash: ").append(messageHashes[i])
                  .append("\nRecipient: ").append(messageRecipients[i])
                  .append("\nMessage: ").append(allMessages[i]);
                printed++;
            }
        }
        return sb.toString();
    }

    /** I return the total number of sent messages. */
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    /**
     * I write all currently stored messages to messages.json, and then I read
     * the JSON file back into my storedMessages array so my array always matches the file.
     *
     * Reference: Ecma International (2017) ECMA-404 The JSON Data Interchange Syntax.
     * Available at: https://www.json.org/json-en.html
     * Reference: Oracle (2024) Class FileWriter, Java SE 17 API Documentation.
     * Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileWriter.html
     */
    public boolean storeMessage() {
        boolean written = writeStoredMessagesToJson(JSON_FILE);
        if (written) {
            loadStoredMessagesFromJson(JSON_FILE);
        }
        return written;
    }

    /** I write all stored entries from my parallel arrays into the JSON file. */
    private static boolean writeStoredMessagesToJson(String filePath) {
        StringBuilder file = new StringBuilder("[\n");
        int count = 0;
        for (int i = 0; i < allMessages.length; i++) {
            if ("Stored".equals(messageFlags[i])) {
                if (count > 0) {
                    file.append(",\n");
                }
                file.append("  {")
                    .append("\"messageId\": \"").append(escape(messageIds[i])).append("\", ")
                    .append("\"messageHash\": \"").append(escape(messageHashes[i])).append("\", ")
                    .append("\"sender\": \"").append(escape(messageSenders[i])).append("\", ")
                    .append("\"recipient\": \"").append(escape(messageRecipients[i])).append("\", ")
                    .append("\"message\": \"").append(escape(allMessages[i])).append("\"")
                    .append("}");
                count++;
            }
        }
        file.append("\n]");

        // I use try-with-resources so the FileWriter always closes cleanly.
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(file.toString());
            return true;
        } catch (IOException e) {
            System.out.println("Could not save message to JSON: " + e.getMessage());
            return false;
        }
    }

    /**
     * I read the JSON file into my storedMessages array using Java's Files API
     * and regular expression pattern matching for JSON key-value pairs.
     *
     * Reference: JSON file reading and parsing adapted from
     * Oracle (2024) Class Files (java.nio.file.Files) & Class Pattern (java.util.regex), Java SE 17 API Documentation.
     * Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Files.html
     * Available at: https://www.json.org/json-en.html
     */
    public static String[] loadStoredMessagesFromJson() {
        return loadStoredMessagesFromJson(JSON_FILE);
    }

    /** I read the specified JSON file into my storedMessages array and sync my parallel arrays. */
    public static String[] loadStoredMessagesFromJson(String filePath) {
        storedMessages = new String[0];
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            return storedMessages;
        }

        try {
            String content = Files.readString(path);
            Pattern objectPattern = Pattern.compile("\\{([^{}]*)\\}");
            Matcher objectMatcher = objectPattern.matcher(content);

            while (objectMatcher.find()) {
                String obj = objectMatcher.group(1);
                String id = extractJsonValue(obj, "messageId");
                String hash = extractJsonValue(obj, "messageHash");
                String msgSender = extractJsonValue(obj, "sender");
                String msgRecipient = extractJsonValue(obj, "recipient");
                String msgText = extractJsonValue(obj, "message");

                if (!msgText.isEmpty()) {
                    storedMessages = appendToArray(storedMessages, msgText);
                    // If this stored message came from an earlier run, I also add it to my parallel arrays.
                    if (!id.isEmpty() && indexOfId(id) == -1) {
                        messageIds = appendToArray(messageIds, id);
                        messageHashes = appendToArray(messageHashes, hash);
                        allMessages = appendToArray(allMessages, msgText);
                        messageSenders = appendToArray(messageSenders, msgSender.isEmpty() ? "QuickChat User" : msgSender);
                        messageRecipients = appendToArray(messageRecipients, msgRecipient);
                        messageFlags = appendToArray(messageFlags, "Stored");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read stored messages from JSON: " + e.getMessage());
        }
        return Arrays.copyOf(storedMessages, storedMessages.length);
    }

    /** I extract a string field value from a flat JSON object snippet. */
    private static String extractJsonValue(String jsonObject, String key) {
        Pattern fieldPattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = fieldPattern.matcher(jsonObject);
        if (matcher.find()) {
            return matcher.group(1).replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return "";
    }

    /** I find the index of a message ID in my messageIds array, or return -1 if not found. */
    private static int indexOfId(String id) {
        for (int i = 0; i < messageIds.length; i++) {
            if (messageIds[i].equals(id)) {
                return i;
            }
        }
        return -1;
    }

    // ==================== PART 3 ARRAY SEARCH, DELETE & REPORT METHODS ====================

    /**
     * Part 3 (2a): I display the sender and recipient of all stored messages
     * by looping through my parallel arrays.
     */
    public static String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allMessages.length; i++) {
            if ("Stored".equals(messageFlags[i])) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append("Sender: ").append(messageSenders[i])
                  .append(" | Recipient: ").append(messageRecipients[i]);
            }
        }
        return sb.length() == 0 ? "No stored messages found." : sb.toString();
    }

    /**
     * Part 3 (2b): I search my parallel arrays to find and return the longest
     * active (sent or stored) message.
     */
    public static String displayLongestMessage() {
        String longest = "";
        for (int i = 0; i < allMessages.length; i++) {
            if (!"Disregard".equals(messageFlags[i]) && allMessages[i].length() > longest.length()) {
                longest = allMessages[i];
            }
        }
        return longest.isEmpty() ? "No messages available." : longest;
    }

    /**
     * Part 3 (2c): I search my messageIds parallel array for a matching ID
     * and return the corresponding recipient and message.
     */
    public static String searchByMessageId(String searchId) {
        for (int i = 0; i < messageIds.length; i++) {
            if (messageIds[i].equals(searchId)) {
                return "Recipient: " + messageRecipients[i] + "\nMessage: " + allMessages[i];
            }
        }
        return "Message ID not found.";
    }

    /** I return only the message text matching a given Message ID (used by my unit test). */
    public static String getMessageTextById(String searchId) {
        for (int i = 0; i < messageIds.length; i++) {
            if (messageIds[i].equals(searchId)) {
                return allMessages[i];
            }
        }
        return "Message ID not found.";
    }

    /**
     * Part 3 (2d): I search my messageRecipients parallel array and return all
     * sent or stored messages for that recipient.
     */
    public static String searchByRecipient(String recipientNumber) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messageRecipients.length; i++) {
            if (messageRecipients[i].equals(recipientNumber) && !"Disregard".equals(messageFlags[i])) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(allMessages[i]);
            }
        }
        return sb.length() == 0 ? "No messages found for recipient: " + recipientNumber : sb.toString();
    }

    /**
     * Part 3 (2e): I search my messageHashes parallel array and delete the
     * matching message from all arrays (and update messages.json if it was stored).
     */
    public static String deleteMessageByHash(String hash) {
        for (int i = 0; i < messageHashes.length; i++) {
            if (messageHashes[i].equalsIgnoreCase(hash)) {
                String deletedText = allMessages[i];
                String flag = messageFlags[i];

                if ("Sent".equals(flag)) {
                    sentMessages = removeFirstMatch(sentMessages, deletedText);
                    if (totalMessagesSent > 0) {
                        totalMessagesSent--;
                    }
                } else if ("Stored".equals(flag)) {
                    storedMessages = removeFirstMatch(storedMessages, deletedText);
                } else if ("Disregard".equals(flag)) {
                    disregardedMessages = removeFirstMatch(disregardedMessages, deletedText);
                }

                messageIds = removeAtIndex(messageIds, i);
                messageHashes = removeAtIndex(messageHashes, i);
                allMessages = removeAtIndex(allMessages, i);
                messageSenders = removeAtIndex(messageSenders, i);
                messageRecipients = removeAtIndex(messageRecipients, i);
                messageFlags = removeAtIndex(messageFlags, i);

                if ("Stored".equals(flag)) {
                    writeStoredMessagesToJson(JSON_FILE);
                }

                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    /**
     * Part 3 (2f - Stored Report): I build a full report listing all details
     * of every stored message in my parallel arrays.
     */
    public static String displayStoredMessagesReport() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < allMessages.length; i++) {
            if ("Stored".equals(messageFlags[i])) {
                if (count > 0) {
                    sb.append("\n----------------\n");
                }
                sb.append("Message ID: ").append(messageIds[i])
                  .append("\nMessage Hash: ").append(messageHashes[i])
                  .append("\nSender: ").append(messageSenders[i])
                  .append("\nRecipient: ").append(messageRecipients[i])
                  .append("\nMessage: ").append(allMessages[i]);
                count++;
            }
        }
        return count == 0 ? "No stored messages to display." : sb.toString();
    }

    /**
     * Part 3 (Unit Test Report): I build a report showing all sent messages,
     * including the Message Hash, Recipient, and Message.
     */
    public static String displaySentMessagesReport() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < allMessages.length; i++) {
            if ("Sent".equals(messageFlags[i])) {
                if (count > 0) {
                    sb.append("\n----------------\n");
                }
                sb.append("Message Hash: ").append(messageHashes[i])
                  .append("\nRecipient: ").append(messageRecipients[i])
                  .append("\nMessage: ").append(allMessages[i]);
                count++;
            }
        }
        return count == 0 ? "No sent messages to display." : sb.toString();
    }

    /** I turn this single message into one JSON object. */
    public String toJson() {
        return "{\"messageId\": \"" + escape(messageId) + "\", "
                + "\"messageNumber\": " + messageNumber + ", "
                + "\"messageHash\": \"" + escape(messageHash) + "\", "
                + "\"sender\": \"" + escape(sender) + "\", "
                + "\"recipient\": \"" + escape(recipient) + "\", "
                + "\"message\": \"" + escape(messageText) + "\"}";
    }

    // I escape quotes and backslashes so my JSON stays valid.
    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /** I reset all my arrays and counters so every unit test starts with clean data. */
    public static void resetForTesting() {
        sentMessages = new String[0];
        disregardedMessages = new String[0];
        storedMessages = new String[0];
        messageHashes = new String[0];
        messageIds = new String[0];
        allMessages = new String[0];
        messageSenders = new String[0];
        messageRecipients = new String[0];
        messageFlags = new String[0];
        totalMessagesSent = 0;
    }

    // Getters for my five required Part 3 arrays and single-message properties.
    public static String[] getSentMessages() {
        return Arrays.copyOf(sentMessages, sentMessages.length);
    }

    public static String[] getDisregardedMessages() {
        return Arrays.copyOf(disregardedMessages, disregardedMessages.length);
    }

    public static String[] getStoredMessages() {
        return Arrays.copyOf(storedMessages, storedMessages.length);
    }

    public static String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, messageHashes.length);
    }

    public static String[] getMessageIds() {
        return Arrays.copyOf(messageIds, messageIds.length);
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessageHash() {
        return messageHash;
    }
}
