package com.spreetail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.spreetail.Command.*;
import static com.spreetail.DictionaryHandler.*;
import static com.spreetail.MultiValueDictionary.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DictionaryHandlerTest {

    private static final String ERROR_INVALID_ARGUMENTS = "ERROR, invalid number of arguments for ";
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    private static final String MEMBER1 = "member1";
    private static final String MEMBER2 = "member2";
    private static final String NEWLINE = System.lineSeparator();

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private DictionaryHandler dictionaryHandler;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        dictionaryHandler = new DictionaryHandler();
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void handleAdd() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        assertEquals(ADDED, outContent.toString().trim());
    }

    @Test
    void handleAdd_dictionaryException() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        assertEquals(ADDED + NEWLINE + ERROR_MEMBER_EXISTS, outContent.toString().trim());
    }

    @Test
    void handleAdd_invalidArguments() {
        dictionaryHandler.handleAdd(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + ADD.name(), outContent.toString().trim());
    }

    @Test
    void handleRemove() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleRemove(new String[] {REMOVE.name(), KEY1, MEMBER1});
        assertEquals(ADDED + NEWLINE + REMOVED, outContent.toString().trim());
    }

    @Test
    void handleRemove_dictionaryException() {
        dictionaryHandler.handleRemove(new String[] {ADD.name(), KEY1, MEMBER1});
        assertEquals(ERROR_KEY_DOES_NOT_EXIST, outContent.toString().trim());
    }

    @Test
    void handleRemove_invalidArguments() {
        dictionaryHandler.handleRemove(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + REMOVE.name(), outContent.toString().trim());
    }

    @Test
    void handleRemoveAll() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleRemoveAll(new String[] {REMOVEALL.name(), KEY1});
        assertEquals(ADDED + NEWLINE + REMOVED, outContent.toString().trim());
    }

    @Test
    void handleRemoveAll_dictionaryException() {
        dictionaryHandler.handleRemoveAll(new String[] {REMOVEALL.name(), KEY1});
        assertEquals(ERROR_KEY_DOES_NOT_EXIST, outContent.toString().trim());
    }

    @Test
    void handleRemoveAll_invalidArguments() {
        dictionaryHandler.handleRemoveAll(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + REMOVEALL.name(), outContent.toString().trim());
    }

    @Test
    void handleKeys() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY2, MEMBER2});
        dictionaryHandler.handleKeys(new String[] {KEYS.name()});

        final String expectedOutput =
                ADDED + NEWLINE +
                ADDED + NEWLINE +
                "1) " + KEY1 + NEWLINE +
                "2) " + KEY2;

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void handleKeys_invalidArguments() {
        dictionaryHandler.handleKeys(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + KEYS.name(), outContent.toString().trim());
    }

    @Test
    void handleMembers() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER2});
        dictionaryHandler.handleMembers(new String[] {MEMBERS.name(), KEY1});

        final String expectedOutput =
                ADDED + NEWLINE +
                ADDED + NEWLINE +
                "1) " + MEMBER1 + NEWLINE +
                "2) " + MEMBER2;

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void handleMembers_dictionaryException() {
        dictionaryHandler.handleMembers(new String[] {MEMBERS.name(), KEY1});
        assertEquals(ERROR_KEY_DOES_NOT_EXIST, outContent.toString().trim());
    }

    @Test
    void handleMembers_invalidArguments() {
        dictionaryHandler.handleMembers(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + MEMBERS.name(), outContent.toString().trim());
    }

    @Test
    void handleClear() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER2});
        dictionaryHandler.handleClear(new String[] {CLEAR.name()});

        final String expectedOutput =
                ADDED + NEWLINE +
                ADDED + NEWLINE +
                CLEARED;

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void handleClear_invalidArguments() {
        dictionaryHandler.handleClear(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + CLEAR.name(), outContent.toString().trim());
    }

    @Test
    void keyExists_true() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleKeyExists(new String[] {KEYEXISTS.name(), KEY1});
        assertEquals(ADDED + NEWLINE + PREFIX + true, outContent.toString().trim());
    }

    @Test
    void keyExists_false() {
        dictionaryHandler.handleKeyExists(new String[] {KEYEXISTS.name(), KEY1});
        assertEquals(PREFIX + false, outContent.toString().trim());
    }

    @Test
    void keyExists_dictionaryException() {
        dictionaryHandler.handleKeyExists(new String[] {KEYEXISTS.name(), null});
        assertEquals(ERROR_NULL_KEY, outContent.toString().trim());
    }

    @Test
    void handleKeyExists_invalidArguments() {
        dictionaryHandler.handleKeyExists(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + KEYEXISTS.name(), outContent.toString().trim());
    }

    @Test
    void handleMemberExists_true() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleMemberExists(new String[] {MEMBEREXISTS.name(), KEY1, MEMBER1});
        assertEquals(ADDED + NEWLINE + PREFIX + true, outContent.toString().trim());
    }

    @Test
    void handleMemberExists_false() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleMemberExists(new String[] {MEMBEREXISTS.name(), KEY1, MEMBER2});
        assertEquals(ADDED + NEWLINE + PREFIX + false, outContent.toString().trim());
    }

    @Test
    void handleMemberExists_dictionaryException() {
        dictionaryHandler.handleMemberExists(new String[] {MEMBEREXISTS.name(), null, MEMBER1});
        assertEquals(ERROR_NULL_KEY, outContent.toString().trim());
    }

    @Test
    void handleMemberExists_invalidArguments() {
        dictionaryHandler.handleMemberExists(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + MEMBEREXISTS.name(), outContent.toString().trim());
    }

    @Test
    void handleAllMembers() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY2, MEMBER2});
        dictionaryHandler.handleAllMembers(new String[] {ALLMEMBERS.name()});

        final String expectedOutput =
                ADDED + NEWLINE +
                ADDED + NEWLINE +
                "1) " + MEMBER1 + NEWLINE +
                "2) " + MEMBER2;

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void handleAllMembers_empty() {
        dictionaryHandler.handleAllMembers(new String[] {ALLMEMBERS.name()});
        assertEquals(EMPTY_SET, outContent.toString().trim());
    }

    @Test
    void handleAllMembers_invalidArguments() {
        dictionaryHandler.handleAllMembers(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + ALLMEMBERS.name(), outContent.toString().trim());
    }

    @Test
    void handleItems() {
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER1});
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY1, MEMBER2});
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY2, MEMBER1});
        dictionaryHandler.handleAdd(new String[] {ADD.name(), KEY2, MEMBER2});
        dictionaryHandler.handleItems(new String[] {ITEMS.name()});

        final String expectedOutput =
                ADDED + NEWLINE +
                ADDED + NEWLINE +
                ADDED + NEWLINE +
                ADDED + NEWLINE +
                "1) " + KEY1 + ": " + MEMBER1 + NEWLINE +
                "2) " + KEY1 + ": " + MEMBER2 + NEWLINE +
                "3) " + KEY2 + ": " + MEMBER1 + NEWLINE +
                "4) " + KEY2 + ": " + MEMBER2;

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void handleItems_invalidArguments() {
        dictionaryHandler.handleItems(new String[] {});
        assertEquals(ERROR_INVALID_ARGUMENTS + ITEMS.name(), outContent.toString().trim());
    }
}