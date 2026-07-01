package com.trang.kidsenglishai.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.api.ApiClient;
import com.trang.kidsenglishai.api.ApiResponse;
import com.trang.kidsenglishai.api.PracticeRequest;
import com.trang.kidsenglishai.util.SessionManager;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeActivity extends AppCompatActivity {

    private static final int REQ_AUDIO = 1001;

    private TextView tvTargetWord;
    private TextView tvResult;
    private TextView tvGuide;

    private ImageView tvEmoji;

    private String word;
    private String imageUrl;

    private TextToSpeech tts;

    private boolean ttsReady = false;

    private final ActivityResultLauncher<Intent> speechLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            ArrayList<String> results =
                                    result.getData().getStringArrayListExtra(
                                            RecognizerIntent.EXTRA_RESULTS
                                    );

                            if (results != null && !results.isEmpty()) {

                                String bestResult =
                                        results.get(0)
                                                .toLowerCase()
                                                .trim();

                                evaluate(bestResult);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        word = getIntent().getStringExtra("word");
        imageUrl = getIntent().getStringExtra("image_url");

        if (word == null || word.trim().isEmpty()) {
            word = "Hello";
        }

        tvTargetWord = findViewById(R.id.tvTargetWord);
        tvEmoji = findViewById(R.id.tvEmoji);
        tvResult = findViewById(R.id.tvResult);
        tvGuide = findViewById(R.id.tvGuide);

        ImageView btnMic = findViewById(R.id.btnMic);

        Button btnFinish = findViewById(R.id.btnFinish);
        Button btnHear = findViewById(R.id.btnHearWord);

        setupTextToSpeech();

        tvTargetWord.setText(word);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(tvEmoji);

        tvGuide.setText("Bấm micro và đọc thật rõ: " + word);

        btnHear.setOnClickListener(v -> speak(word));

        btnMic.setOnClickListener(v -> startRecognition());

        btnFinish.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                this,
                                AchievementActivity.class
                        )
                )
        );
    }

    private void setupTextToSpeech() {

        tts = new TextToSpeech(this, status -> {

            if (status == TextToSpeech.SUCCESS) {

                int result = tts.setLanguage(Locale.US);

                tts.setPitch(1.0f);

                tts.setSpeechRate(0.88f);

                ttsReady =
                        result != TextToSpeech.LANG_MISSING_DATA
                                && result != TextToSpeech.LANG_NOT_SUPPORTED;
            }
        });
    }

    private void startRecognition() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQ_AUDIO
            );

            return;
        }

        Intent intent =
                new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "en-US"
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en-US"
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
                "en-US"
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_MAX_RESULTS,
                5
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Say clearly: " + word
        );

        try {

            speechLauncher.launch(intent);

        } catch (ActivityNotFoundException e) {

            Toast.makeText(
                    this,
                    "Thiết bị chưa hỗ trợ nhận diện giọng nói",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void evaluate(String spokenText) {

        String normalizedSpoken = normalize(spokenText);

        String normalizedWord = normalize(word);

        int score;

        String label;

        if (normalizedSpoken.equals(normalizedWord)) {

            score = 100;
            label = "Excellent";

        } else if (normalizedSpoken.contains(normalizedWord)
                || normalizedWord.contains(normalizedSpoken)) {

            score = 85;
            label = "Very Good";

        } else {

            int distance =
                    levenshtein(
                            normalizedSpoken,
                            normalizedWord
                    );

            int maxLen =
                    Math.max(
                            normalizedSpoken.length(),
                            normalizedWord.length()
                    );

            score =
                    Math.max(
                            35,
                            100 - (distance * 100 / Math.max(maxLen, 1))
                    );

            if (score >= 80) {
                label = "Good";
            } else if (score >= 60) {
                label = "Not Bad";
            } else {
                label = "Try Again";
            }
        }

        String feedback =
                label
                        + "\n\nYou said: "
                        + spokenText
                        + "\n\nScore: "
                        + score
                        + "%";

        tvResult.setText(feedback);
        savePracticeToApi(spokenText, score, label);
    }

    private void savePracticeToApi(
            String spokenText,
            int score,
            String label
    ) {
        int userId =
                new SessionManager(this).getUserId();

        if (userId <= 0) return;

        ApiClient.getService()
                .savePractice(
                        new PracticeRequest(
                                userId,
                                word,
                                spokenText,
                                score,
                                label
                        )
                )
                .enqueue(new Callback<ApiResponse<Object>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<Object>> call,
                            Response<ApiResponse<Object>> response
                    ) {
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse<Object>> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                PracticeActivity.this,
                                "Chưa lưu được luyện nói lên API",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private String normalize(String text) {

        return text == null
                ? ""
                : text.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ");
    }

    private int levenshtein(String a, String b) {

        int[][] dp =
                new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {

            for (int j = 1; j <= b.length(); j++) {

                int cost =
                        a.charAt(i - 1) == b.charAt(j - 1)
                                ? 0
                                : 1;

                dp[i][j] =
                        Math.min(
                                Math.min(
                                        dp[i - 1][j] + 1,
                                        dp[i][j - 1] + 1
                                ),
                                dp[i - 1][j - 1] + cost
                        );
            }
        }

        return dp[a.length()][b.length()];
    }

    private void speak(String text) {

        if (!ttsReady) return;

        if (tts == null) return;

        if (text == null || text.trim().isEmpty()) return;

        tts.stop();

        tts.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "practice_voice"
        );
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == REQ_AUDIO
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startRecognition();

        } else {

            Toast.makeText(
                    this,
                    "Cần quyền micro để luyện nói",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onDestroy() {

        if (tts != null) {

            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}