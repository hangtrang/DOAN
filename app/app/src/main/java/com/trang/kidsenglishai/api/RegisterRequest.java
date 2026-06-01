package com.trang.kidsenglishai.api;

public class RegisterRequest {
    private String name;
    private int age;
    private String email;
    private String password;
    public RegisterRequest(String name, int age, String email, String password) {
        this.name = name; this.age = age; this.email = email; this.password = password;
    }
}
