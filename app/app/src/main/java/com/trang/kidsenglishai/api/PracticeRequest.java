package com.trang.kidsenglishai.api;

public class PracticeRequest {
    private int user_id;
    private String word;
    private String spoken_text;
    private int score;
    private String result_label;

    public PracticeRequest(int userId, String word, String spokenText, int score, String resultLabel) {
        this.user_id = userId;
        this.word = word;
        this.spoken_text = spokenText;
        this.score = score;
        this.result_label = resultLabel;
    }
}
