package com.trang.kidsenglishai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.api.AchievementRequest;
import com.trang.kidsenglishai.api.ApiClient;
import com.trang.kidsenglishai.api.ApiResponse;
import com.trang.kidsenglishai.model.Vocabulary;
import com.trang.kidsenglishai.util.SessionManager;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonActivity extends AppCompatActivity {
    private List<Vocabulary> vocabularies;
    private int index = 0;
    private TextView tvLessonTitle, tvWord, tvMeaning, tvProgress;
    private ImageView tvEmoji;
    private ProgressBar progressBar;
    private TextToSpeech textToSpeech;
    private boolean ttsReady;
    private int topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        topicId = getIntent().getIntExtra("topicId", 1);
        String topicName = getIntent().getStringExtra("topicName");
        tvLessonTitle = findViewById(R.id.tvLessonTitle);
        tvEmoji = findViewById(R.id.tvEmoji);
        tvWord = findViewById(R.id.tvWord);
        tvMeaning = findViewById(R.id.tvMeaning);
        tvProgress = findViewById(R.id.tvProgress);
        progressBar = findViewById(R.id.progressLesson);
        Button btnPrev = findViewById(R.id.btnPrev);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnPractice = findViewById(R.id.btnPractice);
        Button btnSpeakSample = findViewById(R.id.btnSpeakSample);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        tvLessonTitle.setText(topicName == null ? "Lesson" : topicName + " • Chủ đề học");
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                textToSpeech.setSpeechRate(0.9f);
                ttsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED;
            }
        });

        loadWordsFromApi();

        btnPrev.setOnClickListener(v -> {
            if (vocabularies == null || vocabularies.isEmpty()) return;
            index = (index - 1 + vocabularies.size()) % vocabularies.size();
            renderCurrentWord();
        });
        btnNext.setOnClickListener(v -> {
            if (vocabularies == null || vocabularies.isEmpty()) return;
            index = (index + 1) % vocabularies.size();
            renderCurrentWord();
            saveLearningStar();
        });
        btnSpeakSample.setOnClickListener(v -> {
            if (ttsReady && vocabularies != null && !vocabularies.isEmpty()) {
                textToSpeech.speak(vocabularies.get(index).getWord(), TextToSpeech.QUEUE_FLUSH, null, "word");
            }
        });
        btnPractice.setOnClickListener(v -> {
            if (vocabularies == null || vocabularies.isEmpty()) return;
            Vocabulary current = vocabularies.get(index);
            Intent intent = new Intent(this, PracticeActivity.class);
            intent.putExtra("word", current.getWord());
            intent.putExtra("image_url", current.getImageUrl());
            startActivity(intent);
        });
    }

    private void loadWordsFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getService().getWords(topicId).enqueue(new Callback<ApiResponse<List<Vocabulary>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Vocabulary>>> call, Response<ApiResponse<List<Vocabulary>>> response) {
                progressBar.setVisibility(View.GONE);
                ApiResponse<List<Vocabulary>> body = response.body();
                if (!response.isSuccessful() || body == null || !body.isSuccess() || body.getData() == null) {
                    Toast.makeText(LessonActivity.this, body != null ? body.getMessage() : "Không tải được bài học", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                vocabularies = body.getData();
                if (vocabularies.isEmpty()) {
                    Toast.makeText(LessonActivity.this, "Chưa có từ vựng. Hãy thêm trong Web Admin.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                renderCurrentWord();
                saveLearningStar();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Vocabulary>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LessonActivity.this, "Không kết nối được API: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void renderCurrentWord() {
        if (vocabularies == null || vocabularies.isEmpty()) {
            Toast.makeText(this, "Chưa có từ vựng cho chủ đề này", Toast.LENGTH_SHORT).show();
            return;
        }

        if (index < 0) index = 0;
        if (index >= vocabularies.size()) index = vocabularies.size() - 1;

        Vocabulary current = vocabularies.get(index);

        String word = current.getWord() == null ? "" : current.getWord();
        String meaning = current.getMeaning() == null ? "" : current.getMeaning();
        String imageUrl = current.getImageUrl() == null ? "" : current.getImageUrl();
        String example = current.getExample() == null ? "" : current.getExample();

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(tvEmoji);

        tvWord.setText(word);

        if (example.trim().isEmpty()) {
            example = word.isEmpty() ? "" : "This is a " + word.toLowerCase(Locale.ROOT) + ".";
        }

        tvMeaning.setText(meaning + "\nExample: " + example);
        tvProgress.setText("Word " + (index + 1) + "/" + vocabularies.size()
                + " • Từ " + (index + 1) + "/" + vocabularies.size());
    }

    private void saveLearningStar() {
        int userId = new SessionManager(this).getUserId();
        if (userId <= 0) return;
        ApiClient.getService().saveAchievement(new AchievementRequest(userId, 1, 0, 1)).enqueue(new Callback<ApiResponse<Object>>() {
            @Override public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) { }
            @Override public void onFailure(Call<ApiResponse<Object>> call, Throwable t) { }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
