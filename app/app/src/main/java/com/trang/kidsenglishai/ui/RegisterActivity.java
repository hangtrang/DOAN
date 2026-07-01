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
import com.trang.kidsenglishai.api.RegisterRequest;
import com.trang.kidsenglishai.model.User;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$");

    private EditText etName, etAge, etEmail, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        Button btnGoLogin = findViewById(R.id.btnGoLogin);

        btnRegister.setOnClickListener(v -> registerWithApi());
        btnGoLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private void registerWithApi() {
        String name = etName.getText().toString().trim();
        String ageText = etAge.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) { etName.setError("Không được bỏ trống họ tên bé"); etName.requestFocus(); return; }
        if (TextUtils.isEmpty(ageText)) { etAge.setError("Không được bỏ trống tuổi"); etAge.requestFocus(); return; }

        int age;
        try { age = Integer.parseInt(ageText); }
        catch (NumberFormatException e) { etAge.setError("Tuổi phải là số từ 6 đến 10"); etAge.requestFocus(); return; }

        if (age < 6 || age > 10) { etAge.setError("Số tuổi không hợp lệ. Vui lòng nhập tuổi từ 6 đến 10."); etAge.requestFocus(); return; }
        if (TextUtils.isEmpty(email)) { etEmail.setError("Không được bỏ trống email phụ huynh"); etEmail.requestFocus(); return; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.setError("Email phụ huynh không đúng định dạng."); etEmail.requestFocus(); return; }
        if (TextUtils.isEmpty(password)) { etPassword.setError("Không được bỏ trống mật khẩu"); etPassword.requestFocus(); return; }
        if (!PASSWORD_PATTERN.matcher(password).matches()) { etPassword.setError("Mật khẩu tối thiểu 6 ký tự, gồm chữ, số và ký tự đặc biệt."); etPassword.requestFocus(); return; }

        btnRegister.setEnabled(false);
        ApiClient.getService().register(new RegisterRequest(name, age, email, password)).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                btnRegister.setEnabled(true);
                ApiResponse<User> body = response.body();
                if (!response.isSuccessful() || body == null || !body.isSuccess()) {
                    Toast.makeText(RegisterActivity.this, body != null ? body.getMessage() : "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công. Mời đăng nhập.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("prefill_email", email);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Không kết nối được API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
