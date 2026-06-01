package com.trang.kidsenglishai.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trang.kidsenglishai.data.remote.AiCallback;
import com.trang.kidsenglishai.data.repository.ChatRepository;
import com.trang.kidsenglishai.util.ImageTranslator;

public class PhotoTranslateViewModel extends ViewModel {
    private final ChatRepository repository = new ChatRepository();
    private final ImageTranslator offlineTranslator = new ImageTranslator();
    private final MutableLiveData<String> translation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<String> getTranslation() {
        return translation;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void translateDetectedText(String detectedText) {
        if (detectedText == null || detectedText.trim().isEmpty()) {
            translation.setValue("Không có chữ để dịch. Bé hãy chụp lại rõ hơn nhé.");
            return;
        }
        loading.setValue(true);
        repository.translateForKids(detectedText.trim(), new AiCallback() {
            @Override
            public void onSuccess(String result) {
                loading.postValue(false);
                translation.postValue(result);
            }

            @Override
            public void onError(String error) {
                loading.postValue(false);
                String fallback = offlineTranslator.translate(detectedText);
                translation.postValue(fallback + "\n\n(App đang dùng từ điển offline vì AI chưa phản hồi: " + error + ")");
            }
        });
    }
}
