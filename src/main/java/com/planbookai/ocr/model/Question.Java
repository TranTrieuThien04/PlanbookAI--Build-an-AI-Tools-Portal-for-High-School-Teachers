package com.planbookai.ocr.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "questions")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject = "Chemistry"; // Scope đồ án: Hóa học
    private String topic; // VD: "Este - Lipit"
    private String difficultyLevel; // Dễ, Trung bình, Khó
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // Nội dung câu hỏi
    
    @Column(columnDefinition = "TEXT")
    private String optionsJson; // Chứa các đáp án A,B,C,D dạng JSON
    
    @Column(nullable = false)
    private String correctOption; // VD: "A"
    
    // Người tạo câu hỏi (Staff hoặc Teacher)
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}