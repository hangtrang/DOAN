package com.trang.kidsenglishai.data.repository;

import com.trang.kidsenglishai.data.remote.AiCallback;
import com.trang.kidsenglishai.model.ChatMessage;
import com.trang.kidsenglishai.service.AiChatService;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private final AiChatService aiChatService;

    public ChatRepository() {
        aiChatService = new AiChatService();
    }

    public void askTutor(String message, List<ChatMessage> history, AiCallback callback) {
        aiChatService.ask(message, history == null ? new ArrayList<>() : history, new AiChatService.Callback() {
            @Override
            public void onSuccess(String response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void translateForKids(String detectedText, AiCallback callback) {
        aiChatService.translateText(detectedText, new AiChatService.Callback() {
            @Override
            public void onSuccess(String response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}
