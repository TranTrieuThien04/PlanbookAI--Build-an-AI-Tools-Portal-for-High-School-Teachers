package com.planbookai.ocr.model;

import jakarta.persistence.*;

@Entity
@Table(name = "answer_keys")
public class AnswerKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_code")
    private String examCode;

    private String type;
    private String keyword;
    private Double point;

    @Column(name = "answers_json", columnDefinition = "TEXT")
    private String answersJson;

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public Double getPoint() { return point; }
    public void setPoint(Double point) { this.point = point; }
    public String getAnswersJson() { return answersJson; }
    public void setAnswersJson(String answersJson) { this.answersJson = answersJson; }
}