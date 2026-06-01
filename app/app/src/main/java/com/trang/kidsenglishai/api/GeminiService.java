package com.trang.kidsenglishai.api;

import com.trang.kidsenglishai.config.AiConfig;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GeminiService {

    private static final OkHttpClient client = new OkHttpClient();

    public interface GeminiCallback {
        void onSuccess(String reply);

        void onError(String error);
    }

    public static void askGemini(String userMessage,
                                 GeminiCallback callback) {

        try {

            String url =
                    AiConfig.BASE_URL +
                            AiConfig.GEMINI_MODEL +
                            ":generateContent?key=" +
                            AiConfig.GEMINI_API_KEY;

            JSONObject textPart = new JSONObject();
            textPart.put("text", userMessage);

            JSONArray parts = new JSONArray();
            parts.put(textPart);

            JSONObject content = new JSONObject();
            content.put("parts", parts);

            JSONArray contents = new JSONArray();
            contents.put(content);

            JSONObject body = new JSONObject();
            body.put("contents", contents);

            RequestBody requestBody =
                    RequestBody.create(
                            body.toString(),
                            MediaType.parse("application/json")
                    );

            Request request =
                    new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();

            client.newCall(request)
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(
                                Call call,
                                IOException e
                        ) {
                            callback.onError(e.getMessage());
                        }

                        @Override
                        public void onResponse(
                                Call call,
                                Response response
                        ) throws IOException {

                            if (response.body() == null) {
                                callback.onError("Empty response");
                                return;
                            }

                            String json =
                                    response.body().string();

                            try {
                                JSONObject root =
                                        new JSONObject(json);

                                JSONArray candidates =
                                        root.getJSONArray(
                                                "candidates");

                                JSONObject first =
                                        candidates.getJSONObject(0);

                                JSONObject content =
                                        first.getJSONObject(
                                                "content");

                                JSONArray replyParts =
                                        content.getJSONArray(
                                                "parts");

                                String answer =
                                        replyParts
                                                .getJSONObject(0)
                                                .getString("text");

                                callback.onSuccess(answer);

                            } catch (Exception e) {
                                callback.onError(
                                        e.getMessage()
                                );
                            }

                        }
                    });

        } catch (Exception e) {
            callback.onError(e.getMessage());
        }

    }
}