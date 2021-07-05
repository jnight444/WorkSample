package com.spreetail;

import static com.spreetail.Command.*;

import java.util.List;
import java.util.Map;

/**
 * The CommandHandler class is designed to handle all of the different commands used
 * to perform actions on the MultiValueDictionary.
 */
class DictionaryHandler {

    static final String PREFIX = ") ";
    static final String ADDED = PREFIX + "Added";
    static final String REMOVED = PREFIX + "Removed";
    static final String CLEARED = PREFIX + "Cleared";
    static final String EMPTY_SET = PREFIX + "Empty Set";
    static final String ERROR_INVALID_ARGUMENTS = "ERROR, invalid number of arguments for ";

    private final MultiValueDictionary<String, String> dictionary = new MultiValueDictionary<>();

    void handleAdd(final String[] userData) {
        if (!validArguments(userData.length, 3, ADD)) {
            return;
        }

        try {
            dictionary.add(userData[1], userData[2]);
            System.out.println(ADDED);
        } catch (final DictionaryException de) {
            System.out.println(de.getMessage());
        }
    }

    void handleRemove(final String[] userData) {
        if (!validArguments(userData.length, 3, REMOVE)) {
            return;
        }

        try {
            dictionary.remove(userData[1], userData[2]);
            System.out.println(REMOVED);
        } catch (final DictionaryException de) {
            System.out.println(de.getMessage());
        }
    }

    void handleRemoveAll(final String[] userData) {
        if (!validArguments(userData.length, 2, REMOVEALL)) {
            return;
        }

        try {
            dictionary.removeAll(userData[1]);
            System.out.println(REMOVED);
        } catch (final DictionaryException de) {
            System.out.println(de.getMessage());
        }
    }

    void handleKeys(final String[] userData) {
        if (!validArguments(userData.length, 1, KEYS)) {
            return;
        }

        final List<String> keys = dictionary.keys();
        if (keys.isEmpty()) {
            System.out.println(EMPTY_SET);
            return;
        }

        for (int i = 0; i < keys.size(); i++) {
            System.out.println(i+1 + PREFIX + keys.get(i));
        }
    }

    void handleMembers(final String[] userData) {
        if (!validArguments(userData.length, 2, MEMBERS)) {
            return;
        }

        try {
            final List<String> members = dictionary.members(userData[1]);

            if (members.isEmpty()) {
                System.out.println(EMPTY_SET);
                return;
            }

            for (int i = 0; i < members.size(); i++) {
                System.out.println(i+1 + PREFIX + members.get(i));
            }
        } catch (final DictionaryException de) {
            System.out.println(de.getMessage());
        }
    }

    void handleClear(final String[] userData) {
        if (validArguments(userData.length, 1, CLEAR)) {
            dictionary.clear();
            System.out.println(CLEARED);
        }
    }

    void handleKeyExists(final String[] userData) {
        if (!validArguments(userData.length, 2, KEYEXISTS)) {
            return;
        }

        try {
            System.out.println(PREFIX + dictionary.keyExists(userData[1]));
        } catch (final DictionaryException de) {
            System.out.println(de.getMessage());
        }
    }

    void handleMemberExists(final String[] userData) {
        if (!validArguments(userData.length, 3, MEMBEREXISTS)) {
            return;
        }

        try {
            System.out.println(PREFIX + dictionary.memberExists(userData[1], userData[2]));
        } catch (final DictionaryException de) {
            System.out.println(de.getMessage());
        }
    }

    void handleAllMembers(final String[] userData) {
        if (!validArguments(userData.length, 1, ALLMEMBERS)) {
            return;
        }

        final List<String> members = dictionary.allMembers();

        if (members.isEmpty()) {
            System.out.println(EMPTY_SET);
            return;
        }

        for (int i = 0; i < members.size(); i++) {
            System.out.println(i+1 + PREFIX + members.get(i));
        }
    }

    void handleItems(final String[] userData) {
        if (!validArguments(userData.length, 1, ITEMS)) {
            return;
        }

        final Map<String, List<String>> items = dictionary.items();

        if (items.isEmpty()) {
            System.out.println(EMPTY_SET);
            return;
        }

        int i = 1;
        try {
            for (final String key : dictionary.keys()) {
                for (final String member : dictionary.members(key)) {
                    System.out.println(i + PREFIX + key + ": " + member);
                    i++;
                }
            }
        } catch (final DictionaryException de) {
            System.out.println(de.getMessage());
        }
    }

    static boolean validArguments(final int length, final int expectedLength, final Command command) {
        if (length != expectedLength) {
            System.out.println(ERROR_INVALID_ARGUMENTS + command.name());
            return false;
        }
        return true;
    }
}
