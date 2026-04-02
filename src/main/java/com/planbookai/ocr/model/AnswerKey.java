package com.planbookai.ocr.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "answer_keys")
@Data
public class AnswerKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PHẢI CÓ DÒNG NÀY Server mới không sập
    @Column(name = "exam_code")
    private String examCode;

    @Column(name = "type")
    private String type;

    @Column(columnDefinition = "TEXT")
    private String answersJson;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "point")
    private Double point;
}