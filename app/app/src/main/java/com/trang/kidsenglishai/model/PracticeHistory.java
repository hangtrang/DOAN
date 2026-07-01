package com.trang.kidsenglishai.model;

public class PracticeHistory {
    private int id;
    private String word;
    private String spokenText;
    private int score;
    private String resultLabel;

    public PracticeHistory(int id, String word, String spokenText, int score, String resultLabel) {
        this.id = id;
        this.word = word;
        this.spokenText = spokenText;
        this.score = score;
        this.resultLabel = resultLabel;
    }

    public int getId() { return id; }
    public String getWord() { return word; }
    public String getSpokenText() { return spokenText; }
    public int getScore() { return score; }
    public String getResultLabel() { return resultLabel; }
}
