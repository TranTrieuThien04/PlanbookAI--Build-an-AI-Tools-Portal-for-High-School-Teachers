package com.planbookai.ocr.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ANSWER_KEY")
@Data
public class AnswerKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private Double point;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'ESSAY'")
    private String type = "ESSAY";
}