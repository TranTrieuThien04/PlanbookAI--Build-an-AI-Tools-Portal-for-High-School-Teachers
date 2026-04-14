package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.Question;
import com.planbookai.ocr.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    // 1. Lấy toàn bộ danh sách câu hỏi
    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'STAFF', 'ADMIN')")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionRepository.findAll());
    }

    // 2. Tìm kiếm theo Chủ đề và Độ khó (Hết lỗi đỏ findBy...)
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('TEACHER', 'STAFF', 'ADMIN')")
    public ResponseEntity<List<Question>> searchQuestions(
            @RequestParam String topic, 
            @RequestParam String difficulty) {
        
        List<Question> results = questionRepository.findByTopicAndDifficultyLevel(topic, difficulty);
        return ResponseEntity.ok(results);
    }

    // 3. Thêm câu hỏi mới (Hết lỗi đỏ setSubject)
    @PostMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        // Gán mặc định môn Hóa học theo scope URD
        question.setSubject("Chemistry"); 
        return ResponseEntity.ok(questionRepository.save(question));
    }

    // 4. Xóa câu hỏi
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        questionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}