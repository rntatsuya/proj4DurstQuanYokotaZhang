/*
 * File: SavedCache.java
 * Names: Robert Durst, Tracy Quan, Tatsuya Yokota, Tia Zhang
 * F18 CS361 Project 4
 * This file stores the hashmap for whether code areas have been saved or not
 * Date: 10/03/2018
 */


package proj4DurstQuanYokotaZhang;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class SavedCache {
    /** Keep a cache of opened/saved tabs
     *      key   - TextArea id
     *      value - SHA256(TextArea text)  
     */ 
    private HashMap<String, String> cache;

    SavedCache() {
        this.cache = new HashMap<>();
    }

    /**
     * Updates/adds a textarea's content to the cache.
     * 
     * @param key   String id of the textArea
     * @param value String content of the textArea
     */
    void append(String key, String value) {
        this.cache.put(key, hashAString(value));
    }

    /**
     * Removes a textarea's content to the cache.
     * 
     * @param key String id of the textArea
     */
    void remove(String key) {    
        this.cache.remove(key);
    }

    /**
     * 
     * @param key   String  the textArea id of the tab in question
     * @param value String  the textArea content of the tab in question 
     * @return      boolean whether the textArea's id is in the cache 
     *                      the textArea's value hasn't changed
     */
    boolean tabHasChanged(String key, String value) {
        String hashedText = hashAString(value);

        return this.cache.containsKey(key) && this.cache.get(key).equals(hashedText);
    }

    /**
     * Applies SHA-256 to a string.
     * 
     * @param text     a normal String 
     * @return String  a hashed String
     */
    private String hashAString(String text) {
        // default returns input
        String result = text;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());
            result = new String(messageDigest.digest());
          }
          catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
          }

        return result;
    }
}