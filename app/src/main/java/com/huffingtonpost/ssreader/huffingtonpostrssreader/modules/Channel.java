package com.huffingtonpost.ssreader.huffingtonpostrssreader.modules;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zhisheng on 2015/6/29.
 */
public class Channel implements Serializable {

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    private List<Item> items;
    private String title;
    private String link;
    private String description;

    public Channel() {
        setItems(null);
        setTitle(null);
        setLink(null);
        setDescription(null);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}