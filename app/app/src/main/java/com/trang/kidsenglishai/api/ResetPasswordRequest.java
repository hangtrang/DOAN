package com.trang.kidsenglishai.api;

public class ResetPasswordRequest {
    private String email;
    private String new_password;
    public ResetPasswordRequest(String email, String newPassword) { this.email = email; this.new_password = newPassword; }
}
