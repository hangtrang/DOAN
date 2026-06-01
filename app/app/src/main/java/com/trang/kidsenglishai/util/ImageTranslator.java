package com.trang.kidsenglishai.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ImageTranslator {
    private final Map<String, String> localMap = new HashMap<>();

    public ImageTranslator() {
        seed();
    }

    private void seed() {
        localMap.put("hello", "Xin chào");
        localMap.put("goodbye", "Tạm biệt");
        localMap.put("dog", "Con chó");
        localMap.put("cat", "Con mèo");
        localMap.put("bird", "Con chim");
        localMap.put("fish", "Con cá");
        localMap.put("apple", "Quả táo");
        localMap.put("banana", "Quả chuối");
        localMap.put("orange", "Quả cam");
        localMap.put("red", "Màu đỏ");
        localMap.put("blue", "Màu xanh dương");
        localMap.put("yellow", "Màu vàng");
        localMap.put("one", "Số một");
        localMap.put("two", "Số hai");
        localMap.put("three", "Số ba");
        localMap.put("book", "Quyển sách");
        localMap.put("school", "Trường học");
        localMap.put("family", "Gia đình");
        localMap.put("father", "Bố");
        localMap.put("mother", "Mẹ");
        localMap.put("sister", "Chị/em gái");
        localMap.put("brother", "Anh/em trai");
    }

    public String translate(String rawText) {
        String cleaned = normalize(rawText);
        if (cleaned.isEmpty()) {
            return "Chưa nhận diện được chữ rõ ràng. Bé thử chụp gần hơn nhé.";
        }

        String exact = localMap.get(cleaned);
        if (exact != null) {
            return cleaned + " = " + exact;
        }

        String[] parts = cleaned.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            String translated = localMap.get(part);
            if (translated != null) {
                if (builder.length() > 0) builder.append("\n");
                builder.append(part).append(" = ").append(translated);
            }
        }

        if (builder.length() > 0) {
            return builder.toString();
        }

        return "Bé đã chụp được: \"" + rawText.trim() + "\"\n"
                + "Hiện app chưa có nghĩa sẵn cho từ này. Bé có thể hỏi thêm trong Chatbot AI nhé.";
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s?]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
