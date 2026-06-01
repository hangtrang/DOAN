package com.trang.kidsenglishai.api;

public class QuizSubmitRequest {
    private int user_id;
    private int score;
    private int total;

    public QuizSubmitRequest(int userId, int score, int total) {
        this.user_id = userId;
        this.score = score;
        this.total = total;
    }
}
