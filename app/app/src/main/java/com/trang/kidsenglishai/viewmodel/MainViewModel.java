package com.trang.kidsenglishai.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public final MutableLiveData<Integer> currentVocabularyIndex = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> currentScore = new MutableLiveData<>(0);
}
