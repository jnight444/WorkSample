package com.spreetail;

import static com.spreetail.Command.valueOf;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static final String DELIMITER = " ";
    private static final String USER_ENTRY_PREFIX = "> ";
    private static boolean run = true;

    private static final DictionaryHandler dictionaryHandler = new DictionaryHandler();

    /**
     * This is the main loop that allows the user to continue to enter commands until they enter the 'EXIT' command.
     * @param args Program input arguments, not needed for this program currently.
     */
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Multi-Value Dictionary App, type HELP to see supported commands.");
        System.out.println();

	    String userInput;

	    while (run) {
	        System.out.print(USER_ENTRY_PREFIX);
	        userInput = scanner.nextLine();

	        final String[] inputData = userInput.split(DELIMITER);

	        if (inputData.length == 0 || !isValidCommand(inputData[0])) {
	            System.out.println("ERROR, Invalid command.");
	            continue;
            }

            parseInput(inputData);
        }

        scanner.close();
    }

    /**
     * Method to translate the command entered by the user to a method to handle the command.
     * @param inputData string array containing the data entered by the user.
     */
    private static void parseInput(final String[] inputData) {

        switch (valueOf(inputData[0])) {
            case ADD:
                dictionaryHandler.handleAdd(inputData);
                break;
            case REMOVE:
                dictionaryHandler.handleRemove(inputData);
                break;
            case REMOVEALL:
                dictionaryHandler.handleRemoveAll(inputData);
                break;
            case KEYS:
                dictionaryHandler.handleKeys(inputData);
                break;
            case MEMBERS:
                dictionaryHandler.handleMembers(inputData);
                break;
            case CLEAR:
                dictionaryHandler.handleClear(inputData);
                break;
            case KEYEXISTS:
                dictionaryHandler.handleKeyExists(inputData);
                break;
            case MEMBEREXISTS:
                dictionaryHandler.handleMemberExists(inputData);
                break;
            case ALLMEMBERS:
                dictionaryHandler.handleAllMembers(inputData);
                break;
            case ITEMS:
                dictionaryHandler.handleItems(inputData);
                break;
            case HELP:
                printCommands();
                break;
            case EXIT:
                run = false;
                break;
        }
    }

    /**
     * Returns true if the input command is supported by the dictionary.
     * @param command the input command to be checked.
     * @return boolean showing if the command is supported.
     */
    private static boolean isValidCommand(final String command) {
        return Arrays.stream(Command.values())
                .map(Enum::name)
                .collect(Collectors.toList())
                .contains(command);
    }

    /**
     * Prints all of the supported dictionary commands, 1 per line.
     */
    private static void printCommands() {
        System.out.println("Supported Commands:");
        for (final Command value : Command.values()) {
            System.out.println(" - " + value);
        }
    }
}
