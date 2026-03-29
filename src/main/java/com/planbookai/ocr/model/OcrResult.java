package com.planbookai.ocr.model; // Phải là .model

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "OCR_RESULT")
@Data
public class OcrResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentName;
    private Double score;
    @Column(columnDefinition = "TEXT")
    private String resultJson;
}