package com.planbookai.ocr.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ocr_result")
public class OcrResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ocr_result_id")
    private Long id;

    // Tạm thời dùng Long cho khóa ngoại exam_id để code không bị lỗi 
    // trong trường hợp bạn chưa tạo Entity Exam.
    // Khi nào tạo file Exam.java, bạn có thể chuyển thành @ManyToOne.
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "student_name", length = 100)
    private String studentName;

    @Column(name = "score")
    private Double score;

    @Column(name = "result_json", columnDefinition = "TEXT")
    private String resultJson;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    // --- Các trường mở rộng cho AI Module ---
    
    @Column(name = "confidence_score")
    private Double confidence;

    @Column(name = "requires_review")
    private Boolean requiresReview;

    // --- Constructors ---

    public OcrResult() {
        // Tự động gán thời gian hiện tại khi khởi tạo đối tượng
        this.gradedAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getResultJson() {
        return resultJson;
    }

    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Boolean getRequiresReview() {
        return requiresReview;
    }

    public void setRequiresReview(Boolean requiresReview) {
        this.requiresReview = requiresReview;
    }
}