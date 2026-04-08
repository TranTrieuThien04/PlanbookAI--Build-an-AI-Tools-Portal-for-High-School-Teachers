package com.planbookai.ocr.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // Đảm bảo map đúng vào bảng users trong DB
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    
    // Cột lưu quyền (ví dụ: TEACHER, ADMIN)
    private String role; 
    
    private String email;

    @Column(name = "full_name")
    private String fullName;

    private boolean enabled;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ==========================================================
    // KHU VỰC GETTER & SETTER (THUỐC CHỮA CÁC VẠCH ĐỎ)
    // ==========================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Đây chính là hàm "cứu tinh" cho cái vạch đỏ getRole() lúc nãy nè thầy:
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}