package com.trang.kidsenglishai.model;

import com.google.gson.annotations.SerializedName;

public class Vocabulary {

    private int id;
    @SerializedName("topic_id")
    private int topicId;

    @SerializedName("word")
    private String englishWord;
    @SerializedName("meaning")
    private String vietnameseMeaning;
    @SerializedName("image_url")
    private String imageUrl;
    private String example;

    public Vocabulary(){}

    public Vocabulary(
            int id,
            int topicId,
            String englishWord,
            String vietnameseMeaning,
            String imageUrl
    ){
        this.id=id;
        this.topicId=topicId;
        this.englishWord=englishWord;
        this.vietnameseMeaning=vietnameseMeaning;
        this.imageUrl=imageUrl;
        this.example="";
    }

    // getters setters
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getTopicId(){
        return topicId;
    }

    public void setTopicId(int topicId){
        this.topicId=topicId;
    }

    public String getEnglishWord(){
        return englishWord;
    }

    public void setEnglishWord(String englishWord){
        this.englishWord=englishWord;
    }

    public String getVietnameseMeaning(){
        return vietnameseMeaning;
    }

    public void setVietnameseMeaning(String vietnameseMeaning){
        this.vietnameseMeaning=vietnameseMeaning;
    }

    public String getImageUrl(){
        return imageUrl==null ? "" : imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl=imageUrl;
    }

    public String getExample(){
        return example==null ? "" : example;
    }

    public void setExample(String example){
        this.example=example;
    }


    // ======================
    // Compatibility methods
    // ======================

    // LessonActivity gọi getWord()
    public String getWord(){
        return englishWord;
    }

    public void setWord(String word){
        this.englishWord=word;
    }

    // LessonActivity gọi getMeaning()
    public String getMeaning(){
        return vietnameseMeaning;
    }

    public void setMeaning(String meaning){
        this.vietnameseMeaning=meaning;
    }

    // emoji cũ map tạm bằng image
    public String getEmoji(){
        return imageUrl;
    }

    public void setEmoji(String emoji){
        this.imageUrl=emoji;
    }

}