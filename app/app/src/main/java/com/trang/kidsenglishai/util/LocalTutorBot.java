package com.trang.kidsenglishai.util;

import java.util.Locale;

public class LocalTutorBot {

    public static String reply(String message) {
        String m = message == null ? "" : message.trim().toLowerCase(Locale.ROOT);
        if (m.isEmpty()) {
            return "Xin chào bé! Bé muốn học từ vựng, luyện nói hay hỏi nghĩa của một từ tiếng Anh nào? 🌈";
        }
        if (m.contains("hello")) {
            return "Hello nghĩa là xin chào nhé bé. Ví dụ: Hello, teacher! 👋";
        }
        if (m.contains("dog")) {
            return "Dog là con chó đó bé. Bé đọc theo cô: dog /dɔg/ 🐶";
        }
        if (m.contains("cat")) {
            return "Cat là con mèo nhé. Ví dụ: The cat is cute. 🐱";
        }
        if (m.contains("apple")) {
            return "Apple là quả táo. Táo màu đỏ rất ngon! 🍎";
        }
        if (m.contains("red")) {
            return "Red là màu đỏ. Bé thử nói: a red apple. ❤️";
        }
        if (m.contains("how are you")) {
            return "Câu này nghĩa là: Bạn có khỏe không? Bé có thể trả lời: I am fine. 😊";
        }
        if (m.contains("bye")) {
            return "Bye bye bé nhé! Hẹn gặp lại trong bài học tiếp theo. 🌟";
        }
        if (m.contains("phát âm") || m.contains("pronounce") || m.contains("đọc")) {
            return "Mẹo phát âm: Bé đọc chậm, rõ từng âm và nhấn từ chính. Ví dụ với 'apple': ap-pờl 🍏";
        }
        if (m.contains("màu") || m.contains("color")) {
            return "Một số màu sắc nè bé: red = đỏ, blue = xanh dương, yellow = vàng. 🎨";
        }
        if (m.contains("animal") || m.contains("con vật")) {
            return "Các con vật dễ nhớ: dog = chó, cat = mèo, bird = chim, fish = cá. 🐶🐱🐦🐟";
        }
        return "Cô giáo AI hiểu câu hỏi của bé rồi nè. Bé thử hỏi ngắn hơn như: 'hello là gì', 'dog là gì', hoặc 'how are you nghĩa là gì' nhé! ✨";
    }
}
