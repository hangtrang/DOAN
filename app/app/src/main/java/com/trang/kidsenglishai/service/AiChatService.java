package com.trang.kidsenglishai.service;

import android.os.Handler;
import android.os.Looper;
import com.trang.kidsenglishai.config.AiConfig;

import com.trang.kidsenglishai.model.ChatMessage;
import com.trang.kidsenglishai.util.LocalTutorBot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AiChatService {

    public interface Callback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void ask(String message, Callback callback) {
        ask(message, new ArrayList<>(), callback);
    }

    public void ask(String message, List<ChatMessage> history, Callback callback) {
        if (!AiConfig.isConfigured()) {
            postSuccess(callback, LocalTutorBot.reply(message)
                    + "\n\n(Chế độ demo offline: chưa cấu hình GEMINI_API_KEY trong local.properties.)");
            return;
        }

        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(AiConfig.buildGenerateContentUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(30000);

                JSONObject body = buildGeminiRequest(message, history);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(body.toString().getBytes(StandardCharsets.UTF_8));
                }

                int code = connection.getResponseCode();
                String responseText = readResponse(connection, code);

                if (code >= 200 && code < 300) {
                    postSuccess(callback, parseGeminiText(responseText));
                } else {
                    String reason = parseApiError(responseText, code);
                    String fallback = LocalTutorBot.reply(message)
                            + "\n\n(Gemini chưa phản hồi được: " + reason + ")";
                    postSuccess(callback, fallback);
                }
            } catch (Exception e) {
                String fallback = LocalTutorBot.reply(message)
                        + "\n\n(Không kết nối được Gemini API. Kiểm tra Internet/API key rồi thử lại nhé.)";
                postSuccess(callback, fallback);
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }

    public void translateText(String text, Callback callback) {

        if (!AiConfig.isConfigured()) {
            postError(callback,"Chưa cấu hình Gemini API");
            return;
        }

        // lọc rác OCR
        text = text
                .replace("GIGABYTE","")
                .replace("Tiếng Anh","")
                .trim();

        // prompt ép dịch đúng, không sáng tạo
        String prompt =
                "Dịch câu tiếng Anh sau sang tiếng Việt chính xác. " +
                        "Chỉ trả về bản dịch, không giải thích, không thêm lời chào, " +
                        "không đóng vai giáo viên:\n\n" +
                        text;

        new Thread(() -> {
            HttpURLConnection connection = null;

            try{
                URL url = new URL(AiConfig.buildGenerateContentUrl());

                connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8");
                connection.setDoOutput(true);

                JSONObject body=
                        buildGeminiRequest(prompt,new ArrayList<>());

                try(OutputStream os=connection.getOutputStream()){
                    os.write(
                            body.toString()
                                    .getBytes(StandardCharsets.UTF_8)
                    );
                }

                int code=connection.getResponseCode();

                String responseText=
                        readResponse(connection,code);

                if(code>=200 && code<300){
                    postSuccess(
                            callback,
                            parseGeminiText(responseText)
                    );
                }else{
                    postError(
                            callback,
                            parseApiError(responseText,code)
                    );
                }

            }catch(Exception e){
                postError(callback,"Không kết nối được Gemini API");
            }finally{
                if(connection!=null)
                    connection.disconnect();
            }

        }).start();
    }

    private JSONObject buildGeminiRequest(String userMessage, List<ChatMessage> history) throws Exception {
        JSONObject root = new JSONObject();

        JSONObject systemInstruction = new JSONObject();
        JSONArray systemParts = new JSONArray();
        systemParts.put(new JSONObject().put("text",
                "Bạn là cô giáo tiếng Anh dễ thương cho trẻ 6-10 tuổi. " +
                        "Luôn trả lời bằng tiếng Việt dễ hiểu, tích cực, an toàn cho trẻ em. " +
                        "Ưu tiên dạy từ vựng, phát âm, ví dụ đơn giản. Không trả lời nội dung không phù hợp với trẻ em."));
        systemInstruction.put("parts", systemParts);
        root.put("systemInstruction", systemInstruction);

        JSONArray contents = new JSONArray();
        if (history != null) {
            int start = Math.max(0, history.size() - 8);
            for (int i = start; i < history.size(); i++) {
                ChatMessage msg = history.get(i);
                if (msg == null || msg.getText() == null || msg.getText().trim().isEmpty()) continue;
                JSONObject item = new JSONObject();
                item.put("role", msg.isFromUser() ? "user" : "model");
                JSONArray parts = new JSONArray();
                parts.put(new JSONObject().put("text", msg.getText()));
                item.put("parts", parts);
                contents.put(item);
            }
        }

        JSONObject userContent = new JSONObject();
        userContent.put("role", "user");
        JSONArray userParts = new JSONArray();
        userParts.put(new JSONObject().put("text", userMessage));
        userContent.put("parts", userParts);
        contents.put(userContent);
        root.put("contents", contents);

        JSONObject generationConfig = new JSONObject();
        generationConfig.put("temperature", 0.1);
        generationConfig.put("topP", 0.9);
        generationConfig.put("maxOutputTokens", 350);
        root.put("generationConfig", generationConfig);

        JSONArray safetySettings = new JSONArray();
        String[] categories = new String[]{
                "HARM_CATEGORY_HARASSMENT",
                "HARM_CATEGORY_HATE_SPEECH",
                "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                "HARM_CATEGORY_DANGEROUS_CONTENT"
        };
        for (String category : categories) {
            safetySettings.put(new JSONObject()
                    .put("category", category)
                    .put("threshold", "BLOCK_MEDIUM_AND_ABOVE"));
        }
        root.put("safetySettings", safetySettings);

        return root;
    }

    private String readResponse(HttpURLConnection connection, int code) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream(),
                StandardCharsets.UTF_8));
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            return sb.toString();
        } finally {
            reader.close();
        }
    }

    private String parseGeminiText(String jsonText) {
        try {
            JSONObject json = new JSONObject(jsonText);
            JSONArray candidates = json.optJSONArray("candidates");
            if (candidates != null && candidates.length() > 0) {
                JSONObject first = candidates.optJSONObject(0);
                if (first != null) {
                    JSONObject content = first.optJSONObject("content");
                    if (content != null) {
                        JSONArray parts = content.optJSONArray("parts");
                        if (parts != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < parts.length(); i++) {
                                JSONObject part = parts.optJSONObject(i);
                                String value = part == null ? "" : part.optString("text", "");
                                if (!value.trim().isEmpty()) {
                                    if (sb.length() > 0) sb.append("\n");
                                    sb.append(value.trim());
                                }
                            }
                            if (sb.length() > 0) return sb.toString();
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return "Cô giáo AI chưa trả lời rõ lắm. Bé thử hỏi lại một câu ngắn hơn nhé ✨";
    }

    private String parseApiError(String jsonText, int code) {
        try {
            JSONObject json = new JSONObject(jsonText);
            JSONObject error = json.optJSONObject("error");
            if (error != null) {
                String message = error.optString("message", "");
                if (!message.trim().isEmpty()) return "mã " + code + " - " + message;
            }
        } catch (Exception ignored) {}
        if (code == 400) return "mã 400 - request/model chưa đúng";
        if (code == 401 || code == 403) return "mã " + code + " - API key không hợp lệ hoặc chưa có quyền";
        if (code == 429) return "mã 429 - vượt quota API";
        if (code >= 500) return "mã " + code + " - lỗi máy chủ Gemini";
        return "mã " + code;
    }

    private void postSuccess(Callback callback, String value) {
        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(value));
    }

    private void postError(Callback callback, String error) {
        new Handler(Looper.getMainLooper()).post(() -> callback.onError(error));
    }
}
