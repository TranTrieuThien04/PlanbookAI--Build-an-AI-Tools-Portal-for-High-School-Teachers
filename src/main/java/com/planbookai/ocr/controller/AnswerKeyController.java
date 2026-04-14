package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.AnswerKey;
import com.planbookai.ocr.repository.AnswerKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ocr/answers")
public class AnswerKeyController {

    @Autowired private AnswerKeyRepository answerKeyRepository;

    @PostMapping("/save")
    public ResponseEntity<?> saveAnswerKey(@RequestBody AnswerKey key) {
        // Nếu mã đề đã tồn tại thì cập nhật, chưa có thì tạo mới
        AnswerKey existing = answerKeyRepository.findByExamCode(key.getExamCode());
        if (existing != null) {
            existing.setAnswersJson(key.getAnswersJson());
            return ResponseEntity.ok(answerKeyRepository.save(existing));
        }
        return ResponseEntity.ok(answerKeyRepository.save(key));
    }
}