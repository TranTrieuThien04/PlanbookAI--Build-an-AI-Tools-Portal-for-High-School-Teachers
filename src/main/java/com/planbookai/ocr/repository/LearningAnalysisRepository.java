package com.planbookai.ocr.repository;

import com.planbookai.ocr.model.LearningAnalysis; // Phải có dòng này
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningAnalysisRepository extends JpaRepository<LearningAnalysis, Long> {
}