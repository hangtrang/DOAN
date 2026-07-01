package com.trang.kidsenglishai.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.api.ApiClient;
import com.trang.kidsenglishai.api.ApiResponse;
import com.trang.kidsenglishai.api.QuizSubmitRequest;
import com.trang.kidsenglishai.model.QuizQuestion;
import com.trang.kidsenglishai.util.SessionManager;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.trang.kidsenglishai.api.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameQuizActivity extends AppCompatActivity {
    private TextView tvTitle, tvQuestion, tvScore;
    private Button btnA, btnB, btnC, btnD;
    private final List<QuizQuestion> questions = new ArrayList<>();
    private int index = 0;
    private int score = 0;
    private ImageView imgQuiz;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_quiz);
        tvTitle = findViewById(R.id.tvQuizTitle);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvQuizScore);
        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);
        imgQuiz = findViewById(R.id.imgQuiz);
        loadQuestions();
    }

    private void loadQuestions() {
        tvQuestion.setText("Đang tải câu hỏi từ API...");
        ApiClient.getService().getQuizQuestions().enqueue(new Callback<ApiResponse<List<QuizQuestion>>>() {
            @Override public void onResponse(Call<ApiResponse<List<QuizQuestion>>> call, Response<ApiResponse<List<QuizQuestion>>> response) {
                ApiResponse<List<QuizQuestion>> body = response.body();
                if (!response.isSuccessful() || body == null || !body.isSuccess() || body.getData() == null || body.getData().isEmpty()) {
                    Toast.makeText(GameQuizActivity.this, "Không có câu hỏi GameQuiz", Toast.LENGTH_SHORT).show();
                    return;
                }
                questions.clear();
                questions.addAll(body.getData());
                index = 0;
                score = 0;
                showQuestion();
            }
            @Override public void onFailure(Call<ApiResponse<List<QuizQuestion>>> call, Throwable t) {
                Toast.makeText(GameQuizActivity.this, "Không kết nối được API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showQuestion() {
        if (index >= questions.size()) { finishQuiz(); return; }
        QuizQuestion q = questions.get(index);
        tvTitle.setText("GameQuiz " + (index + 1) + "/" + questions.size());
        tvScore.setText("Điểm: " + score);
        tvQuestion.setText(q.getQuestion());
        loadQuizImage(q.getImageUrl());
        bindAnswer(btnA, "A", q.getOptionA());
        bindAnswer(btnB, "B", q.getOptionB());
        bindAnswer(btnC, "C", q.getOptionC());
        bindAnswer(btnD, "D", q.getOptionD());
    }

    private void bindAnswer(Button button, String key, String text) {
        button.setEnabled(true);
        button.setText(key + ". " + text);
        button.setOnClickListener(v -> chooseAnswer(key));
    }

    private void chooseAnswer(String selected) {
        QuizQuestion q = questions.get(index);
        if (selected.equalsIgnoreCase(q.getCorrectAnswer())) {
            score++;
            Toast.makeText(this, "Đúng rồi!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sai rồi. Đáp án đúng: " + q.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
        }
        index++;
        showQuestion();
    }

    private void finishQuiz() {
        tvTitle.setText("Hoàn thành GameQuiz");
        tvQuestion.setText("Bé đạt " + score + "/" + questions.size() + " câu đúng!");
        tvScore.setText("+" + score + " sao");
        btnA.setEnabled(false); btnB.setEnabled(false); btnC.setEnabled(false); btnD.setEnabled(false);
        int userId = new SessionManager(this).getUserId();
        if (userId > 0) {
            ApiClient.getService().saveQuizResult(new QuizSubmitRequest(userId, score, questions.size()))
                    .enqueue(new Callback<ApiResponse<Object>>() {
                        @Override public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) { }
                        @Override public void onFailure(Call<ApiResponse<Object>> call, Throwable t) { }
                    });
        }
    }
    private void loadQuizImage(String imageUrl) {

        if (imgQuiz == null) return;

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imgQuiz.setImageResource(R.drawable.owl_mascot);
            return;
        }

        String finalUrl = imageUrl.trim();

        if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {

            String baseUrl = ApiClient.BASE_URL;

            if (!baseUrl.endsWith("/") && !finalUrl.startsWith("/")) {
                finalUrl = baseUrl + "/" + finalUrl;
            } else {
                finalUrl = baseUrl + finalUrl;
            }
        }

        Glide.with(this)
                .load(finalUrl)
                .placeholder(R.drawable.owl_mascot)
                .error(R.drawable.owl_mascot)
                .into(imgQuiz);
    }
}
