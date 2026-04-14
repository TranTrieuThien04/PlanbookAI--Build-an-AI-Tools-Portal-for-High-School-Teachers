package com.planbookai.ocr.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ocr_result")
@Data
public class OcrResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ocr_result_id")
    private Long ocrResultId;

    private String studentName; // Controller gọi setStudentName sẽ hết đỏ
    private Double score;       // Controller gọi setScore sẽ hết đỏ
    
    @Column(columnDefinition = "TEXT")
    private String resultJson;  // Controller gọi setResultJson sẽ hết đỏ
    
    private Double confidenceScore; // Controller gọi setConfidenceScore sẽ hết đỏ
    private Long examId;            // Controller gọi setExamId sẽ hết đỏ
    
    private Boolean requiresReview;
    private LocalDateTime gradedAt = LocalDateTime.now();
}