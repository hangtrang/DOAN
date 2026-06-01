package com.trang.kidsenglishai.data.remote;

import android.os.Handler;
import android.os.Looper;

import com.trang.kidsenglishai.model.Topic;
import com.trang.kidsenglishai.model.Vocabulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockLearningApi {
    public interface TopicsCallback {
        void onSuccess(List<Topic> topics);
        void onError(String message);
    }

    public interface VocabularyCallback {
        void onSuccess(List<Vocabulary> words);
        void onError(String message);
    }

    public void getTopics(TopicsCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> callback.onSuccess(seedTopics()), 700);
    }

    public void getWordsByTopic(int topicId, VocabularyCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> callback.onSuccess(seedVocabulary(topicId)), 700);
    }

    private List<Topic> seedTopics() {
        List<Topic> items = new ArrayList<>();
        items.add(new Topic(1, "Animals", "Động vật đáng yêu • Cute animals", "🐘"));
        items.add(new Topic(2, "Fruits", "Trái cây ngọt ngào • Sweet fruits", "🍎"));
        items.add(new Topic(3, "Colors", "Màu sắc rực rỡ • Bright colors", "🌈"));
        items.add(new Topic(4, "Numbers", "Số đếm mỗi ngày • Daily numbers", "🔢"));
        items.add(new Topic(5, "Family", "Gia đình thân yêu • My family", "👨‍👩‍👧"));
        items.add(new Topic(6, "School", "Đồ dùng học tập • School things", "🎒"));
        return items;
    }

    private List<Vocabulary> seedVocabulary(int topicId) {
        switch (topicId) {
            case 1:
                return Arrays.asList(
                        new Vocabulary(1, 1, "Elephant", "Con voi", "🐘"),
                        new Vocabulary(2, 1, "Lion", "Sư tử", "🦁"),
                        new Vocabulary(3, 1, "Tiger", "Hổ", "🐯"),
                        new Vocabulary(4, 1, "Dog", "Con chó", "🐶"),
                        new Vocabulary(5, 1, "Cat", "Con mèo", "🐱")
                );
            case 2:
                return Arrays.asList(
                        new Vocabulary(6, 2, "Apple", "Quả táo", "🍎"),
                        new Vocabulary(7, 2, "Banana", "Quả chuối", "🍌"),
                        new Vocabulary(8, 2, "Orange", "Quả cam", "🍊"),
                        new Vocabulary(9, 2, "Grapes", "Nho", "🍇"),
                        new Vocabulary(10, 2, "Watermelon", "Dưa hấu", "🍉")
                );
            case 3:
                return Arrays.asList(
                        new Vocabulary(11, 3, "Red", "Màu đỏ", "🔴"),
                        new Vocabulary(12, 3, "Blue", "Màu xanh dương", "🔵"),
                        new Vocabulary(13, 3, "Yellow", "Màu vàng", "🟡"),
                        new Vocabulary(14, 3, "Green", "Màu xanh lá", "🟢"),
                        new Vocabulary(15, 3, "Pink", "Màu hồng", "🩷")
                );
            case 4:
                return Arrays.asList(
                        new Vocabulary(16, 4, "One", "Số một", "1️⃣"),
                        new Vocabulary(17, 4, "Two", "Số hai", "2️⃣"),
                        new Vocabulary(18, 4, "Three", "Số ba", "3️⃣"),
                        new Vocabulary(19, 4, "Four", "Số bốn", "4️⃣"),
                        new Vocabulary(20, 4, "Five", "Số năm", "5️⃣")
                );
            case 5:
                return Arrays.asList(
                        new Vocabulary(21, 5, "Father", "Bố", "👨"),
                        new Vocabulary(22, 5, "Mother", "Mẹ", "👩"),
                        new Vocabulary(23, 5, "Brother", "Anh/em trai", "👦"),
                        new Vocabulary(24, 5, "Sister", "Chị/em gái", "👧"),
                        new Vocabulary(25, 5, "Baby", "Em bé", "👶")
                );
            default:
                return Arrays.asList(
                        new Vocabulary(26, 6, "Book", "Quyển sách", "📘"),
                        new Vocabulary(27, 6, "Bag", "Cặp sách", "🎒"),
                        new Vocabulary(28, 6, "Pencil", "Bút chì", "✏️"),
                        new Vocabulary(29, 6, "Ruler", "Thước", "📏"),
                        new Vocabulary(30, 6, "School", "Trường học", "🏫")
                );
        }
    }
}
