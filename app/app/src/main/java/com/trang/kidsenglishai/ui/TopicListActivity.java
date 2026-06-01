package com.trang.kidsenglishai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.adapter.TopicAdapter;
import com.trang.kidsenglishai.api.ApiClient;
import com.trang.kidsenglishai.api.ApiResponse;
import com.trang.kidsenglishai.model.Topic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        recyclerView = findViewById(R.id.recyclerTopics);
        progressBar = findViewById(R.id.progressTopics);
        tvError = findViewById(R.id.tvError);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        loadTopicsFromApi();
    }

    private void loadTopicsFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);

        ApiClient.getService().getTopics().enqueue(new Callback<ApiResponse<List<Topic>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Topic>>> call, Response<ApiResponse<List<Topic>>> response) {
                progressBar.setVisibility(View.GONE);
                ApiResponse<List<Topic>> body = response.body();
                if (!response.isSuccessful() || body == null || !body.isSuccess() || body.getData() == null) {
                    showError(body != null ? body.getMessage() : "Không tải được danh sách chủ đề");
                    return;
                }
                List<Topic> topics = body.getData();
                if (topics.isEmpty()) {
                    showError("Chưa có chủ đề. Hãy thêm trong Web Admin.");
                    return;
                }
                recyclerView.setAdapter(new TopicAdapter(topics, topic -> {
                    Intent intent = new Intent(TopicListActivity.this, LessonActivity.class);
                    intent.putExtra("topicId", topic.getId());
                    intent.putExtra("topicName", topic.getName());
                    startActivity(intent);
                }));
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Topic>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Không kết nối được API: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }
}
