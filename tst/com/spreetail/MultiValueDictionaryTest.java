package com.spreetail;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MultiValueDictionaryTest<K, V> {

    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";

    private static final String MEMBER1 = "value1";
    private static final String MEMBER2 = "value2";

    @ParameterizedTest
    @MethodSource("doubleKeySingleMemberParameters")
    void testKeys(final K k1, final K k2, final V v) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();
        final List<K> expectedKeys = new ArrayList<>(Arrays.asList(k1, k2));

        dictionary.add(k1, v);
        dictionary.add(k2, v);

        assertEquals(expectedKeys, dictionary.keys());
    }

    @ParameterizedTest
    @MethodSource("singleKeyDoubleMemberParameters")
    void testMembers(final K k, final V v1, final V v2) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();
        final List<V> expectedMembers = new ArrayList<>(Arrays.asList(v1, v2));

        dictionary.add(k, v1);
        dictionary.add(k, v2);

        assertEquals(expectedMembers, dictionary.members(k));
    }

    @Test
    void testMembers_nullKey() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<K, V>().members(null));

        assertEquals(MultiValueDictionary.ERROR_NULL_KEY, de.getMessage());
    }

    @ParameterizedTest
    @MethodSource("singleKeySingleMemberParameters")
    void testAdd_memberExists(final K k, final V v) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k, v);

        final DictionaryException de = assertThrows(DictionaryException.class, () -> dictionary.add(k, v));

        assertEquals(MultiValueDictionary.ERROR_MEMBER_EXISTS, de.getMessage());
    }

    @Test
    void testAdd_nullKey() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<K, V>().add(null, null));

        assertEquals(MultiValueDictionary.ERROR_NULL_KEY, de.getMessage());
    }

    @Test
    void testAdd_nullMember() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<String, V>().add("NotNull", null));

        assertEquals(MultiValueDictionary.ERROR_NULL_MEMBER, de.getMessage());
    }

    @ParameterizedTest
    @MethodSource("singleKeySingleMemberParameters")
    void testRemove_withSingleMember(final K k, final V v) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k, v);
        assertTrue(dictionary.keyExists(k));

        dictionary.remove(k, v);
        assertFalse(dictionary.keyExists(k));
    }

    @ParameterizedTest
    @MethodSource("singleKeyDoubleMemberParameters")
    void testRemove_withMultipleMembers(final K k, final V v1, final V v2) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k, v1);
        dictionary.add(k, v2);
        assertTrue(dictionary.keyExists(k));

        dictionary.remove(k, v1);
        assertTrue(dictionary.keyExists(k));
        assertFalse(dictionary.memberExists(k, v1));
        assertTrue(dictionary.memberExists(k, v2));
    }

    @ParameterizedTest
    @MethodSource("singleKeySingleMemberParameters")
    void testRemove_keyDoesNotExist(final K k, final V v) {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        final DictionaryException de = assertThrows(DictionaryException.class, () -> dictionary.remove(k, v));

        assertEquals(MultiValueDictionary.ERROR_KEY_DOES_NOT_EXIST, de.getMessage());
    }

    @ParameterizedTest
    @MethodSource("singleKeyDoubleMemberParameters")
    void testRemove_memberDoesNotExist(final K k, final V v1, final V v2) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k, v1);

        final DictionaryException de = assertThrows(DictionaryException.class, () -> dictionary.remove(k, v2));

        assertEquals(MultiValueDictionary.ERROR_MEMBER_DOES_NOT_EXIST, de.getMessage());
    }

    @Test
    void testRemove_nullKey() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<K, V>().remove(null, null));

        assertEquals(MultiValueDictionary.ERROR_NULL_KEY, de.getMessage());
    }

    @Test
    void testRemove_nullMember() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<String, V>().remove("NotNull", null));

        assertEquals(MultiValueDictionary.ERROR_NULL_MEMBER, de.getMessage());
    }

    @ParameterizedTest
    @MethodSource("singleKeyDoubleMemberParameters")
    void testRemoveAll(final K k, final V v1, final V v2) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k, v1);
        dictionary.add(k, v2);
        assertTrue(dictionary.keyExists(k));

        dictionary.removeAll(k);
        assertFalse(dictionary.keyExists(k));
    }

    @ParameterizedTest
    @MethodSource("singleKeyParameters")
    void testRemoveAll_keyDoesNotExist(final K k) {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        final DictionaryException de = assertThrows(DictionaryException.class, () -> dictionary.removeAll(k));

        assertEquals(MultiValueDictionary.ERROR_KEY_DOES_NOT_EXIST, de.getMessage());
    }

    @Test
    void testRemoveAll_nullKey() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<K, V>().removeAll(null));

        assertEquals(MultiValueDictionary.ERROR_NULL_KEY, de.getMessage());
    }

    @ParameterizedTest
    @MethodSource("doubleKeyDoubleMemberParameters")
    void testClear(final K k1, final K k2, final V v1, final V v2) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k1, v1);
        dictionary.add(k2, v2);
        dictionary.clear();

        assertTrue(dictionary.keys().isEmpty());
    }

    @Test
    void testClear_empty() {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();
        dictionary.clear();
        assertTrue(dictionary.keys().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("singleKeySingleMemberParameters")
    void testKeyExists_true(final K k, final V v) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k, v);
        assertTrue(dictionary.keyExists(k));
    }

    @ParameterizedTest
    @MethodSource("singleKeyParameters")
    void testKeyExists_false(final K k) throws DictionaryException {
        assertFalse(new MultiValueDictionary<K, V>().keyExists(k));
    }

    @Test
    void testKeyExists_nullKey() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<K, V>().keyExists(null));

        assertEquals(MultiValueDictionary.ERROR_NULL_KEY, de.getMessage());
    }

    @ParameterizedTest
    @MethodSource("singleKeySingleMemberParameters")
    void testMemberExists_true(final K k, final V v) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k, v);
        assertTrue(dictionary.memberExists(k, v));
    }

    @ParameterizedTest
    @MethodSource("singleKeyDoubleMemberParameters")
    void testMemberExists_false(final K k, final V v1, final V v2) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k, v1);
        assertFalse(dictionary.memberExists(k, v2));
    }

    @Test
    void testMemberExists_nullKey() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<K, V>().memberExists(null, null));

        assertEquals(MultiValueDictionary.ERROR_NULL_KEY, de.getMessage());
    }

    @Test
    void testMemberExists_nullMember() {
        final DictionaryException de = assertThrows(DictionaryException.class,
                () -> new MultiValueDictionary<String, V>().memberExists("NotNull", null));

        assertEquals(MultiValueDictionary.ERROR_NULL_MEMBER, de.getMessage());
    }

    @ParameterizedTest
    @MethodSource("doubleKeyDoubleMemberParameters")
    void testAllMembers(final K k1, final K k2, final V v1, final V v2) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();
        final List<V> expectedMembers = new ArrayList<>(Arrays.asList(v1, v2));

        dictionary.add(k1, v1);
        dictionary.add(k2, v2);

        assertEquals(expectedMembers, dictionary.allMembers());
    }

    @Test
    void testAllMembers_empty() {
        assertTrue(new MultiValueDictionary<>().allMembers().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("doubleKeyDoubleMemberParameters")
    void testItems(final K k1, final K k2, final V v1, final V v2) throws DictionaryException {
        final MultiValueDictionary<K, V> dictionary = new MultiValueDictionary<>();

        dictionary.add(k1, v1);
        dictionary.add(k2, v2);

        final Map<K, List<V>> expectedItems = new HashMap<>() {{
            put(k1, Collections.singletonList(v1));
            put(k2, Collections.singletonList(v2));
        }};

        assertEquals(expectedItems, dictionary.items());
    }

    @Test
    void testItems_empty() {
        assertTrue(new MultiValueDictionary<>().items().isEmpty());
    }

    private static Stream<Arguments> singleKeyParameters() {
        return Stream.of(
                Arguments.of(KEY1),
                Arguments.of(0)
        );
    }

    private static Stream<Arguments> singleKeySingleMemberParameters() {
        return Stream.of(
                Arguments.of(KEY1, MEMBER1),
                Arguments.of(KEY1, 0),
                Arguments.of(0, MEMBER1),
                Arguments.of(0, 1)
        );
    }

    private static Stream<Arguments> singleKeyDoubleMemberParameters() {
        return Stream.of(
                Arguments.of(0, 1, 2),
                Arguments.of(0, MEMBER1, MEMBER2),
                Arguments.of(KEY1, MEMBER1, MEMBER2),
                Arguments.of(KEY1, 0, 1)
        );
    }

    private static Stream<Arguments> doubleKeySingleMemberParameters() {
        return Stream.of(
                Arguments.of(0, 1, 2),
                Arguments.of(0, 1, MEMBER1),
                Arguments.of(KEY1, KEY2, MEMBER1),
                Arguments.of(KEY1, KEY2, 0)
        );
    }

    private static Stream<Arguments> doubleKeyDoubleMemberParameters() {
        return Stream.of(
                Arguments.of(0, 1, 2, 3),
                Arguments.of(0, 1, MEMBER1, MEMBER2),
                Arguments.of(KEY1, KEY2, MEMBER1, MEMBER2),
                Arguments.of(KEY1, KEY2, 0, 1)
        );
    }
}