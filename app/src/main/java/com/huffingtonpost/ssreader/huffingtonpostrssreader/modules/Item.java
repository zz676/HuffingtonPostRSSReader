package com.huffingtonpost.ssreader.huffingtonpostrssreader.modules;

import java.util.List;

/**
 * Created by Zhisheng on 6/24/2015.
 */
public class Item {
    private String link;
    private String title;
    private String pubDate;

    private String description;
    private List<String> imageUrls;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
