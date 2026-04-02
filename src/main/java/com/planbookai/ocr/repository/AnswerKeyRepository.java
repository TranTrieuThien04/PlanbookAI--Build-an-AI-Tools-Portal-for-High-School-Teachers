package com.planbookai.ocr.repository;

import com.planbookai.ocr.model.AnswerKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerKeyRepository extends JpaRepository<AnswerKey, Long> {
    // Tên hàm phải khớp chính xác với tên biến 'examCode' ở trên
    AnswerKey findByExamCode(String examCode);
}