package com.planbookai.ocr.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "questions")
@Data // Cực kỳ quan trọng: Để tự sinh hàm setSubject, setTopic...
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject; // Dòng này giúp hết đỏ setSubject
    private String topic;
    private String difficultyLevel; // Lưu ý tên biến này

    @Column(columnDefinition = "TEXT")
    private String content;
    private String correctOption;
}