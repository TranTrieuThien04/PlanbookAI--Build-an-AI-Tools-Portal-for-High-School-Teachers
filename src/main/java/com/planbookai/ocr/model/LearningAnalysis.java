package com.planbookai.ocr.model;

import jakarta.persistence.*;
import lombok.Data; // Nếu thầy dùng Lombok
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_analytics")
@Data
public class LearningAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_code")
    private String examCode;

    @Column(name = "class_name")
    private String className;

    @Column(name = "strength_analysis", columnDefinition = "TEXT")
    private String strengthAnalysis;

    @Column(name = "weakness_analysis", columnDefinition = "TEXT")
    private String weaknessAnalysis;

    @Column(name = "pedagogical_suggestions", columnDefinition = "TEXT")
    private String pedagogicalSuggestions;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt = LocalDateTime.now();
}