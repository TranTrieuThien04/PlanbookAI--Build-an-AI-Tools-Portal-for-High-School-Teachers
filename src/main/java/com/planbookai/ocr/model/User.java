package com.planbookai.ocr.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    // Lưu các role theo URD: ADMIN, MANAGER, STAFF, TEACHER
    @Column(nullable = false)
    private String role; 

    // THÊM TRƯỜNG NÀY ĐỂ HẾT LỖI DÒNG 30
    @Column(nullable = false)
    private boolean enabled = true; 
}