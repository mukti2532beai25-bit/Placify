
package com.placement.model;

public class TopicTag {

    // Data members
    private int tagId;
    private String tagName;

    // Constructor
    public TopicTag(int tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    // Getter for tagId
    public int getTagId() {
        return tagId;
    }

    // Setter for tagId
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    // Getter for tagName
    public String getTagName() {
        return tagName;
    }

    // Setter for tagName
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    // Display the topic name
    @Override
    public String toString() {
        return tagName;
    }
}
