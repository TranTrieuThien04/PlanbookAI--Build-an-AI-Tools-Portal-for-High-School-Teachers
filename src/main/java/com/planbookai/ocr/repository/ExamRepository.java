package com.planbookai.ocr.repository;

import com.planbookai.ocr.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    List<Exam> findByTeacherUsername(String teacherUsername);
    
    // Tìm đề thi theo mã đề (Dùng để đối chiếu đáp án khi chấm OCR)
    Optional<Exam> findByExamCode(String examCode);
}