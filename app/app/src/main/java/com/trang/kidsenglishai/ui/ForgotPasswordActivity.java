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
import com.trang.kidsenglishai.api.ResetPasswordRequest;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$");
    private EditText etEmail, etNewPassword, etConfirmPassword;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etEmail = findViewById(R.id.etForgotEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnResetPassword = findViewById(R.id.btnResetPassword);
        Button btnBackLogin = findViewById(R.id.btnBackLogin);
        String prefillEmail = getIntent().getStringExtra("prefill_email");
        if (prefillEmail != null && !prefillEmail.trim().isEmpty()) etEmail.setText(prefillEmail.trim());
        btnResetPassword.setOnClickListener(v -> resetPassword());
        btnBackLogin.setOnClickListener(v -> finish());
    }

    private void resetPassword() {
        String email = etEmail.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.setError("Email phụ huynh không đúng định dạng"); etEmail.requestFocus(); return; }
        if (TextUtils.isEmpty(newPassword) || !PASSWORD_PATTERN.matcher(newPassword).matches()) { etNewPassword.setError("Mật khẩu tối thiểu 6 ký tự, gồm chữ, số và ký tự đặc biệt."); etNewPassword.requestFocus(); return; }
        if (!newPassword.equals(confirmPassword)) { etConfirmPassword.setError("Mật khẩu nhập lại chưa khớp"); etConfirmPassword.requestFocus(); return; }
        ApiClient.getService().resetPassword(new ResetPasswordRequest(email, newPassword)).enqueue(new Callback<ApiResponse<Object>>() {
            @Override public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiResponse<Object> body = response.body();
                if (response.isSuccessful() && body != null && body.isSuccess()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Đổi mật khẩu thành công. Mời đăng nhập lại.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent.putExtra("prefill_email", email);
                    startActivity(intent); finish();
                } else Toast.makeText(ForgotPasswordActivity.this, body != null ? body.getMessage() : "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Call<ApiResponse<Object>> call, Throwable t) { Toast.makeText(ForgotPasswordActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_LONG).show(); }
        });
    }
}
