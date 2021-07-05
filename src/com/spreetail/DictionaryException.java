package com.spreetail;

/**
 * Custom exception for catching and handling errors within the MultiValueDictionary.
 */
class DictionaryException extends Exception {

     DictionaryException(final String message) {
        super(message);
    }

}
