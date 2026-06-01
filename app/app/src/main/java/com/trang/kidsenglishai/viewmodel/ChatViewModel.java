package com.trang.kidsenglishai.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trang.kidsenglishai.data.remote.AiCallback;
import com.trang.kidsenglishai.data.repository.ChatRepository;
import com.trang.kidsenglishai.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private final ChatRepository repository = new ChatRepository();
    private final MutableLiveData<String> botReply = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<String> getBotReply() {
        return botReply;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void askTutor(String message, List<ChatMessage> history) {
        loading.setValue(true);
        repository.askTutor(message, history == null ? new ArrayList<>() : history, new AiCallback() {
            @Override
            public void onSuccess(String result) {
                loading.postValue(false);
                botReply.postValue(result);
            }

            @Override
            public void onError(String err) {
                loading.postValue(false);
                error.postValue(err);
            }
        });
    }
}
