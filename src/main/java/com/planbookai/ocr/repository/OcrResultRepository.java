package com.planbookai.ocr.repository;

import com.planbookai.ocr.model.OcrResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcrResultRepository extends JpaRepository<OcrResult, Long> {
    // Spring Data JPA đã tự động cung cấp các hàm như save(), findAll(), findById()
}