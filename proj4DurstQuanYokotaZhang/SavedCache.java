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
import org.fxmisc.richtext.CodeArea;

public class SavedCache {
    /** Keep a cache of opened/saved tabs
     *      key   - pointer to CodeArea
     *      value - SHA256(TextArea text)  
     */ 
    private HashMap<CodeArea, TabData> cache;

    public SavedCache() {
        this.cache = new HashMap<>();
    }


    public void add(CodeArea key, String content, String filepath) {
        this.cache.put(key, new TabData(filepath, hashAString(content)));
    }

    public void updateContent(CodeArea key, String content) {
        this.cache.get(key).ContentHash = hashAString(content);
    }

    /**
     * Removes a textarea's content to the cache.
     * 
     * @param key String id of the textArea
     */
    public void remove(CodeArea key) {
        this.cache.remove(key);
    }

    /**
     * 
     * @param key   String  the textArea id of the tab in question
     * @param value String  the textArea content of the tab in question 
     * @return      boolean whether the textArea's id is in the cache 
     *                      the textArea's value hasn't changed
     */
    public boolean hasChanged(CodeArea key, String value) {
        String hashedText = hashAString(value);

        return this.cache.containsKey(key) && this.cache.get(key).ContentHash.equals(hashedText);
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

    // I wish we had structs
    public class TabData {
        public String FilePath;
        public String ContentHash;

        TabData(String filepath, String contenthash) {
            this.FilePath = filepath;
            this.ContentHash = contenthash;
        }
    }
}