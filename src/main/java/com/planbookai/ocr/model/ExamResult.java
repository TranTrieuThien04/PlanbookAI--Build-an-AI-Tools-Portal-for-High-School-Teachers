package com.planbookai.ocr.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_results")
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private Double score;
    private String fileName;
    private Double confidence;

    @Column(columnDefinition = "TEXT")
    private String details; // Lưu chuỗi JSON chi tiết bài làm

    private LocalDateTime createdAt;

    // Tự động gán thời gian khi lưu
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getter và Setter 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}