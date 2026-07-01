package com.trang.kidsenglishai.model;

import com.google.gson.annotations.SerializedName;

public class QuizQuestion {

    private int id;
    private String question;

    @SerializedName("option_a")
    private String optionA;

    @SerializedName("option_b")
    private String optionB;

    @SerializedName("option_c")
    private String optionC;

    @SerializedName("option_d")
    private String optionD;

    @SerializedName("correct_answer")
    private String correctAnswer;

    @SerializedName("image_url")
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question == null ? "" : question;
    }

    public String getOptionA() {
        return optionA == null ? "" : optionA;
    }

    public String getOptionB() {
        return optionB == null ? "" : optionB;
    }

    public String getOptionC() {
        return optionC == null ? "" : optionC;
    }

    public String getOptionD() {
        return optionD == null ? "" : optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer == null ? "" : correctAnswer;
    }

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }
}