package com.planbookai.ocr.model;

public class LoginRequest {
    private String username;
    private String password;

    // --- Getter và Setter (Bắt buộc phải có để Spring đọc được dữ liệu) ---
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}