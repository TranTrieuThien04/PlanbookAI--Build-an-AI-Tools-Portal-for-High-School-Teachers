package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.Exam;
import com.planbookai.ocr.repository.ExamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher/exams")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherExamController {

    private final ExamRepository examRepository;

    public TeacherExamController(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @PostMapping("/create")
    public Exam createExam(@RequestBody Exam exam) {
        // Lấy tên giáo viên đang đăng nhập từ Security Context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        exam.setTeacherUsername(username);
        return examRepository.save(exam);
    }

    @GetMapping("/my-exams")
    public List<Exam> getMyExams() {
        // Chỉ trả về đề thi của chính giáo viên đó
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return examRepository.findByTeacherUsername(username);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExam(@PathVariable Long id) {
        return examRepository.findById(id).map(exam -> {
            examRepository.delete(exam);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}