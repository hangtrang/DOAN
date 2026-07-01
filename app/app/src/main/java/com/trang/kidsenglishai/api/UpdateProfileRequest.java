package com.trang.kidsenglishai.api;

public class UpdateProfileRequest {
    private int user_id;
    private String name;
    private int age;
    private String email;
    public UpdateProfileRequest(int userId, String name, int age, String email) {
        this.user_id = userId; this.name = name; this.age = age; this.email = email;
    }
}
