package com.planbookai.ocr.repository;

import com.planbookai.ocr.model.AnswerKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerKeyRepository extends JpaRepository<AnswerKey, Long> {
    AnswerKey findByExamCode(String examCode);
}