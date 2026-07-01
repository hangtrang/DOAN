package com.trang.kidsenglishai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.api.ApiClient;
import com.trang.kidsenglishai.api.ApiResponse;
import com.trang.kidsenglishai.api.UpdateProfileRequest;
import com.trang.kidsenglishai.model.User;
import com.trang.kidsenglishai.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {
    private EditText etName, etAge, etEmail;
    private SessionManager sessionManager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) { goToLogin(); return; }
        etName = findViewById(R.id.etEditName);
        etAge = findViewById(R.id.etEditAge);
        etEmail = findViewById(R.id.etEditEmail);
        Button btnSaveAccount = findViewById(R.id.btnSaveAccount);
        Button btnCancelEdit = findViewById(R.id.btnCancelEdit);
        etName.setText(sessionManager.getChildName());
        etAge.setText(String.valueOf(sessionManager.getAge()));
        etEmail.setText(sessionManager.getEmail());
        btnSaveAccount.setOnClickListener(v -> saveAccount());
        btnCancelEdit.setOnClickListener(v -> finish());
    }

    private void saveAccount() {
        String name = etName.getText().toString().trim();
        String ageText = etAge.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(name)) { etName.setError("Không được bỏ trống họ tên bé"); etName.requestFocus(); return; }
        int age;
        try { age = Integer.parseInt(ageText); } catch (Exception e) { etAge.setError("Tuổi phải là số từ 6 đến 10"); etAge.requestFocus(); return; }
        if (age < 6 || age > 10) { etAge.setError("Tuổi từ 6 đến 10"); etAge.requestFocus(); return; }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.setError("Email phụ huynh không đúng định dạng"); etEmail.requestFocus(); return; }
        ApiClient.getService().updateProfile(new UpdateProfileRequest(sessionManager.getUserId(), name, age, email)).enqueue(new Callback<ApiResponse<User>>() {
            @Override public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                ApiResponse<User> body = response.body();
                if (response.isSuccessful() && body != null && body.isSuccess() && body.getData() != null) {
                    User user = body.getData();
                    sessionManager.saveLogin(user.getId(), user.getName(), user.getEmail(), user.getAge());
                    Toast.makeText(EditAccountActivity.this, "Đã lưu thay đổi tài khoản", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditAccountActivity.this, HomeActivity.class)); finish();
                } else Toast.makeText(EditAccountActivity.this, body != null ? body.getMessage() : "Lưu thất bại", Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Call<ApiResponse<User>> call, Throwable t) { Toast.makeText(EditAccountActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_LONG).show(); }
        });
    }
    private void goToLogin() { startActivity(new Intent(this, LoginActivity.class)); finish(); }
}
