package com.planbookai.ocr.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teacherUsername;
    private Long packageId;
    private String status; // PENDING, APPROVED, CANCELLED
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getter & Setter (Viết tay để tránh lỗi đỏ)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTeacherUsername() { return teacherUsername; }
    public void setTeacherUsername(String teacherUsername) { this.teacherUsername = teacherUsername; }
    public Long getPackageId() { return packageId; }
    public void setPackageId(Long packageId) { this.packageId = packageId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}