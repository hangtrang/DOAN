package com.trang.kidsenglishai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.api.ApiClient;
import com.trang.kidsenglishai.api.ApiResponse;
import com.trang.kidsenglishai.model.Achievement;
import com.trang.kidsenglishai.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            goToLogin();
            return;
        }

        String name = sessionManager.getChildName();

        TextView tvGreeting = findViewById(R.id.tvGreeting);
        TextView tvStats = findViewById(R.id.tvStats);
        TextView tvGoal = findViewById(R.id.tvGoal);
        LinearLayout imgAvatar = findViewById(R.id.imgAvatar);
        LinearLayout cardTopics = findViewById(R.id.cardTopics);
        LinearLayout cardPractice = findViewById(R.id.cardPractice);
        LinearLayout cardTranslate = findViewById(R.id.cardTranslate);
        LinearLayout cardChatbot = findViewById(R.id.cardChatbot);
        LinearLayout cardAchievements = findViewById(R.id.cardAchievements);
        LinearLayout cardGameQuiz = findViewById(R.id.cardGameQuiz);

        String safeName = (name == null || name.trim().isEmpty()) ? "little star" : name.trim();
        tvGreeting.setText("Hello, " + safeName + "!\nXin chào bé yêu!");
        tvStats.setText("Đang tải thành tích từ API...");
        tvGoal.setText("Today's goal: learn 3 words, speak 1 time and play GameQuiz.");
        loadHomeStats(tvStats, tvGoal);

        imgAvatar.setOnClickListener(v -> showAccountDialog());
        cardTopics.setOnClickListener(v -> startActivity(new Intent(this, TopicListActivity.class)));
        cardPractice.setOnClickListener(v -> {
            Intent intent = new Intent(this, PracticeActivity.class);
            intent.putExtra("word", "Hello");
            startActivity(intent);
        });
        cardTranslate.setOnClickListener(v -> startActivity(new Intent(this, PhotoTranslateActivity.class)));
        cardChatbot.setOnClickListener(v -> startActivity(new Intent(this, ChatbotActivity.class)));
        cardAchievements.setOnClickListener(v -> startActivity(new Intent(this, AchievementActivity.class)));
        cardGameQuiz.setOnClickListener(v -> startActivity(new Intent(this, GameQuizActivity.class)));
    }

    private void loadHomeStats(TextView tvStats, TextView tvGoal) {
        int userId = sessionManager.getUserId();
        if (userId <= 0) return;
        ApiClient.getService().getAchievement(userId).enqueue(new Callback<ApiResponse<Achievement>>() {
            @Override public void onResponse(Call<ApiResponse<Achievement>> call, Response<ApiResponse<Achievement>> response) {
                ApiResponse<Achievement> body = response.body();
                if (response.isSuccessful() && body != null && body.isSuccess() && body.getData() != null) {
                    Achievement a = body.getData();
                    tvStats.setText("⭐ " + a.getStars() + " stars • 🏅 " + a.getBadges() + " badges • 🔥 " + a.getStreakDays() + " days");
                    tvGoal.setText("Mục tiêu hôm nay: học từ vựng, luyện nói và làm GameQuiz để nhận sao.");
                } else {
                    tvStats.setText("⭐ 0 stars • 🏅 0 badges");
                }
            }
            @Override public void onFailure(Call<ApiResponse<Achievement>> call, Throwable t) {
                tvStats.setText("Chưa kết nối được API thành tích");
            }
        });
    }

    private void showAccountDialog() {
        String message = "Tên bé: " + sessionManager.getChildName()
                + "\nTuổi: " + sessionManager.getAge()
                + "\nEmail phụ huynh: " + sessionManager.getEmail();

        new AlertDialog.Builder(this)
                .setTitle("Thông tin tài khoản")
                .setMessage(message)
                .setPositiveButton("Đóng", null)
                .setNeutralButton("Sửa tài khoản", (dialog, which) -> startActivity(new Intent(this, EditAccountActivity.class)))
                .setNegativeButton("Đăng xuất", (dialog, which) -> {
                    sessionManager.clear();
                    goToLogin();
                })
                .show();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
