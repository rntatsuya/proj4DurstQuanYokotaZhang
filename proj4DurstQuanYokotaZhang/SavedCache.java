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
import proj4DurstQuanYokotaZhang.Common.AlertBox;

/**
 * SavedCache is a class that encapsulates a HashMap which keeps
 * track of the state of saved content in the ide, specifically
 * per tab CodeArea in the tabPane.
 */
public class SavedCache {
    /** Keeps a cache of opened/saved tabs
     *      key   - pointer to CodeArea
     *      value - SHA256(TextArea text)  
     */ 
    private HashMap<CodeArea, TabData> cache;

    public SavedCache() {
        this.cache = new HashMap<>();
    }

    /**
     * addOrOverride adds a CodeArea key, TabData value pair to the
     * savedCache Hashmap. This was named addOrOverride because, this
     * function does not check if a key already exists, it simply adds
     * it to the cache (the cache acts the same way).
     *
     * @param key      pointer to CodeArea object
     * @param content  String
     * @param filepath String
     */
    public void addOrOverride(CodeArea key, String content, String filepath) {
        this.cache.put(key, new TabData(filepath, hashAString(content)));
    }

    /**
     * updateContent updates the content for a given CodeArea.
     *
     * @param key     pointer to CodeArea object
     * @param content String
     */
    public void updateContent(CodeArea key, String content) {
        this.cache.get(key).setContentHash(hashAString(content));
    }

    /**
     * getFileName returns the filepath for a CodeArea.
     *
     * @param key     pointer to CodeArea object
     * @return String the full/absolute filepath of the tab's
     *                CodeArea's saved content. If the CodeArea
     *                does not exist in the cache, returns null.
     */
    public String getFileName(CodeArea key) {
        if (this.cache.containsKey(key)) {
            return this.cache.get(key).getFilePath();
        }

        return null;
    }

    /**
     * Removes a textarea's content to the cache.
     * 
     * @param key String
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

        return !this.cache.containsKey(key) || !this.cache.get(key).getContentHash().equals(hashedText);
    }

    /**
     * Applies SHA-256 to a string.
     * 
     * @param text     String
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
              AlertBox.cannotLoadCrypto();
          }

        return result;
    }

    /**
     * TabData is a data structure containing the metadata
     * related to a tab's codeArea.
     */
    private class TabData {
        private String FilePath;
        private String ContentHash;

        /**
         * TabData constructor.
         *
         * @param filepath    String representing the full/absolute
         *                    path of the tab's saved content.
         * @param contentHash String representing the SHA256 hashed
         *                    content of the saved tab.
         */
        TabData(String filepath, String contentHash) {
            this.FilePath = filepath;
            this.ContentHash = contentHash;
        }

        /**
         * getContentHash returns the value of the ContentHash field.
         *
         * @return String the SHA256 hashed content of the saved tab.
         */
        public String getContentHash() {
            return this.ContentHash;
        }

        /**
         * setContentHash sets the contentHash field.
         *
         * @param contentHash String representing the SHA256 hashed
         *                    content of the saved tab.
         */
        public void setContentHash(String contentHash) {
            ContentHash = contentHash;
        }

        /**
         * getFilePath returns the values of the FilePath field.
         *
         * @return String representing the full/absolute
         *         path of the tab's saved content.
         */
        public String getFilePath() {
            return FilePath;
        }
    }
}