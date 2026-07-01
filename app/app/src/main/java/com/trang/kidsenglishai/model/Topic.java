package com.trang.kidsenglishai.model;

import com.google.gson.annotations.SerializedName;

public class Topic {
    private int id;
    private String name;
    private String description;
    private String emoji;
    @SerializedName("image_url")
    private String imageUrl;

    public Topic() {}

    public Topic(int id, String name, String description, String emoji) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name == null ? "" : name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description == null ? "" : description; }
    public void setDescription(String description) { this.description = description; }
    public String getEmoji() { return emoji == null ? "" : emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public String getImageUrl() { return imageUrl == null ? "" : imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
