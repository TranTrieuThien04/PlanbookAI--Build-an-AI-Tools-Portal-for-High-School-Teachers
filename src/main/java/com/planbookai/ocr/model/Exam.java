package com.planbookai.ocr.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String examCode; // Mã đề (VD: 101, 102)
    private String subject;  // Môn học
    private String grade;    // Khối (10, 11, 12)
    private String term;     // Học kỳ
    
    @Column(columnDefinition = "TEXT")
    private String answerKeyJson; // Đáp án chuẩn để đối chiếu

    private String teacherUsername; // Chủ sở hữu đề thi này

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getAnswerKeyJson() { return answerKeyJson; }
    public void setAnswerKeyJson(String answerKeyJson) { this.answerKeyJson = answerKeyJson; }
    public String getTeacherUsername() { return teacherUsername; }
    public void setTeacherUsername(String teacherUsername) { this.teacherUsername = teacherUsername; }
}