
package com.placement.model;

import java.io.Serializable;

public class TopicTag implements Serializable {

    private static final long serialVersionUID = 1L;

    private int tagId;
    private String tagName;

    public TopicTag(int tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return tagName;
    }
}
