package com.trang.kidsenglishai.model;

import com.google.gson.annotations.SerializedName;

public class Achievement {
    private int id;
    @SerializedName("user_id")
    private int userId;
    private int stars;
    private int badges;
    @SerializedName("streak_days")
    private int streakDays;
    @SerializedName("updated_at")
    private String updatedAt;

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getStars() { return stars; }
    public int getBadges() { return badges; }
    public int getStreakDays() { return streakDays; }
    public String getUpdatedAt() { return updatedAt == null ? "" : updatedAt; }
}
