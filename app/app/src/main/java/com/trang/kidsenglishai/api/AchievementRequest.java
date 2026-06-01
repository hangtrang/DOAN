package com.trang.kidsenglishai.api;

public class AchievementRequest {
    private int user_id;
    private int stars;
    private int badges;
    private int streak_days;

    public AchievementRequest(int userId, int stars, int badges, int streakDays) {
        this.user_id = userId;
        this.stars = stars;
        this.badges = badges;
        this.streak_days = streakDays;
    }
}
