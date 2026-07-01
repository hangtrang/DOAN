package com.trang.kidsenglishai.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.adapter.HistoryAdapter;
import com.trang.kidsenglishai.api.ApiClient;
import com.trang.kidsenglishai.api.ApiResponse;
import com.trang.kidsenglishai.model.Achievement;
import com.trang.kidsenglishai.model.PracticeHistory;
import com.trang.kidsenglishai.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementActivity extends AppCompatActivity {
    private TextView tvStars, tvBadge, tvWeakWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        tvStars = findViewById(R.id.tvStars);
        tvBadge = findViewById(R.id.tvBadge);
        tvWeakWords = findViewById(R.id.tvWeakWords);
        RecyclerView recyclerHistory = findViewById(R.id.recyclerHistory);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerHistory.setAdapter(new HistoryAdapter(new ArrayList<>()));

        loadAchievementFromApi();
        loadHistoryFromApi(recyclerHistory);
    }

    private void loadHistoryFromApi(RecyclerView recyclerHistory) {
        int userId = new SessionManager(this).getUserId();
        if (userId <= 0) return;
        ApiClient.getService().getPracticeHistory(userId).enqueue(new Callback<ApiResponse<List<PracticeHistory>>>() {
            @Override public void onResponse(Call<ApiResponse<List<PracticeHistory>>> call, Response<ApiResponse<List<PracticeHistory>>> response) {
                ApiResponse<List<PracticeHistory>> body = response.body();
                if (response.isSuccessful() && body != null && body.isSuccess() && body.getData() != null) {
                    recyclerHistory.setAdapter(new HistoryAdapter(body.getData()));
                }
            }
            @Override public void onFailure(Call<ApiResponse<List<PracticeHistory>>> call, Throwable t) { }
        });
    }

    private void loadAchievementFromApi() {
        int userId = new SessionManager(this).getUserId();
        if (userId <= 0) {
            tvStars.setText("⭐ 0 sao");
            tvBadge.setText("Huy hiệu: Tiny Starter");
            tvWeakWords.setText("Bạn cần đăng nhập lại để đồng bộ thành tích.");
            return;
        }

        ApiClient.getService().getAchievement(userId).enqueue(new Callback<ApiResponse<Achievement>>() {
            @Override
            public void onResponse(Call<ApiResponse<Achievement>> call, Response<ApiResponse<Achievement>> response) {
                ApiResponse<Achievement> body = response.body();
                if (!response.isSuccessful() || body == null || !body.isSuccess() || body.getData() == null) {
                    Toast.makeText(AchievementActivity.this, "Không tải được thành tích", Toast.LENGTH_SHORT).show();
                    return;
                }
                Achievement a = body.getData();
                int stars = a.getStars();
                int badges = a.getBadges();
                int streak = a.getStreakDays();
                tvStars.setText("⭐ " + stars + " sao");
                tvBadge.setText("Huy hiệu: " + (badges > 0 ? badges + " huy hiệu" : (stars >= 20 ? "Super Speaker" : stars >= 10 ? "Happy Learner" : "Tiny Starter")));
                tvWeakWords.setText("Chuỗi học liên tiếp: " + streak + " ngày");
            }

            @Override
            public void onFailure(Call<ApiResponse<Achievement>> call, Throwable t) {
                Toast.makeText(AchievementActivity.this, "Không kết nối được API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
