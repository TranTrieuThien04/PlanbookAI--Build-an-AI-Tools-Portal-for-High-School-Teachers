package com.planbookai.ocr.model;

public class JwtResponse {
    private String token;
    private String username;

    // Constructor (Hàm khởi tạo)
    public JwtResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    // --- Getter và Setter ---
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}