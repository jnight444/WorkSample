package com.spreetail;

import java.util.*;
import java.util.stream.Collectors;

import static com.spreetail.DictionaryHandler.PREFIX;

/**
 * The MultiValueDictionary class is an implementation of a dictionary
 * that allows multiple values under the same key.
 */
class MultiValueDictionary<K, V> {

    static String ERROR_MEMBER_EXISTS = PREFIX + "ERROR, member already exists for key.";
    static String ERROR_KEY_DOES_NOT_EXIST = PREFIX + "ERROR, key does not exist.";
    static String ERROR_MEMBER_DOES_NOT_EXIST = PREFIX + "ERROR, member does not exist.";
    static String ERROR_NULL_KEY = PREFIX + "ERROR, key cannot be null or empty";
    static String ERROR_NULL_MEMBER = PREFIX + "ERROR, member cannot be null or empty.";

    private final Map<K, List<V>> entries = new HashMap<>();

    /**
     * Returns a list of keys currently stored in the dictionary.
     * @return list of keys.
     */
    List<K> keys() {
        return new ArrayList<K>() {{
            addAll(entries.keySet());
        }};
    }

    /**
     * Returns a list of the members under the input key.
     * @param key the key in the dictionary.
     * @return list of members.
     * @throws DictionaryException if key does not exist in dictionary.
     */
    List<V> members(final K key) throws DictionaryException {
        validateKey(key);

        if (!keyExists(key)) {
            throw new DictionaryException(ERROR_KEY_DOES_NOT_EXIST);
        }

        return entries.get(key);
    }

    /**
     * Adds an entry to the dictionary.
     * Case 1: Key does not exist - adds key and member to dictionary.
     * Case 2: Key exists, but member does not exist under key - adds member under the key.
     * @param key the key to be stored.
     * @param member the member to be stored.
     * @throws DictionaryException
     * - If key is null or does not exist in dictionary.
     * - If member is null or does not exist in dictionary.
     * - If member already exists under the given key.
     */
    void add(final K key, final V member) throws DictionaryException {
        validateKey(key);
        validateMember(member);

        if (entries.containsKey(key)) {
            if (entries.get(key).contains(member)) {
                throw new DictionaryException(ERROR_MEMBER_EXISTS);
            }
            entries.get(key).add(member);

        } else {
            entries.put(key, new ArrayList<>(Collections.singletonList(member)));
        }
    }

    /**
     * Removes a member from under the given key in the dictionary.
     * Case 1: Single member under key - key also removed from dictionary.
     * Case 2: Multiple members under key - only given member removed from dictionary.
     * @param key key under which member is stored.
     * @param member member to be removed.
     * @throws DictionaryException
     * - If key is null or does not exist in the dictionary.
     * - If member is null or does not exist under key in dictionary.
     */
    void remove(final K key, final V member) throws DictionaryException {
        validateKey(key);
        validateMember(member);

        if (!keyExists(key)) {
            throw new DictionaryException(ERROR_KEY_DOES_NOT_EXIST);
        }

        if (!memberExists(key, member)) {
            throw new DictionaryException(ERROR_MEMBER_DOES_NOT_EXIST);
        }

        if (entries.get(key).size() == 1) {
            entries.remove(key);
        } else {
            entries.get(key).remove(member);
        }
    }

    /**
     * Removes all members under the given key as well as the key itself.
     * @param key key to be removed.
     * @throws DictionaryException if key is null or does not exist in dictionary.
     */
    void removeAll(final K key) throws DictionaryException {
        validateKey(key);

        if (!keyExists(key)) {
            throw new DictionaryException(ERROR_KEY_DOES_NOT_EXIST);
        }

        entries.remove(key);
    }

    /**
     * Remove all key-member(s) entries from the dictionary.
     */
    void clear() {
        entries.clear();
    }

    /**
     * Returns a boolean representing if the given key exists in the dictionary.
     * @param key key to be checked.
     * @return boolean representing if key is in dictionary.
     * @throws DictionaryException if key is null.
     */
    boolean keyExists(final K key) throws DictionaryException {
        validateKey(key);
        return entries.containsKey(key);
    }

    /**
     * Returns a boolean representing if the given key-member pair exists in the dictionary.
     * @param key key to be checked.
     * @param member member to be checked.
     * @return boolean representing if member exists under key in dictionary.
     * @throws DictionaryException
     * - If key is null or does not exist in dictionary.
     * - If member is null or does not exist in dictionary.
     */
    boolean memberExists(final K key, final V member) throws DictionaryException {
        validateKey(key);
        validateMember(member);

        return keyExists(key) && entries.get(key).contains(member);
    }

    /**
     * Returns a list of all members stored in the dictionary.
     * @return list of members in dictionary.
     */
    List<V> allMembers() {
        return entries.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Returns a map of all key-member(s) entries in the dictionary.
     * @return list of entries in the dictionary.
     */
    Map<K, List<V>> items() {
        return entries;
    }

    /**
     * Validates the given key by checking if it is null.
     * @param k key to be validated.
     * @throws DictionaryException if the given key is null;
     */
    private void validateKey(final K k) throws DictionaryException {
        Optional.ofNullable(k).orElseThrow(() -> new DictionaryException(ERROR_NULL_KEY));
    }

    /**
     * Validates the given member by checking if it is null.
     * @param v member to be validated.
     * @throws DictionaryException if the given member is null.
     */
    private void validateMember(final V v) throws DictionaryException {
        Optional.ofNullable(v).orElseThrow(() -> new DictionaryException(ERROR_NULL_MEMBER));
    }
}
