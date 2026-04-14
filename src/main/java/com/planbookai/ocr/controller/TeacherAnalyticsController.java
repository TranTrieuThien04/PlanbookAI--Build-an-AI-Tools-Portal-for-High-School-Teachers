package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.LearningAnalysis;
import com.planbookai.ocr.model.OcrResult;
import com.planbookai.ocr.repository.OcrResultRepository;
import com.planbookai.ocr.service.AiAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher/analytics")
@PreAuthorize("hasRole('TEACHER')") // Chỉ giáo viên mới xem được phân tích lớp mình dạy
public class TeacherAnalyticsController {

    private final AiAnalyticsService analyticsService;
    private final OcrResultRepository ocrResultRepository;

    public TeacherAnalyticsController(AiAnalyticsService analyticsService, OcrResultRepository ocrResultRepository) {
        this.analyticsService = analyticsService;
        this.ocrResultRepository = ocrResultRepository;
    }

    @GetMapping("/{examCode}")
    public ResponseEntity<LearningAnalysis> getClassAnalysis(@PathVariable String examCode) {
        // Lấy toàn bộ kết quả chấm bài của kỳ thi này
        List<OcrResult> results = ocrResultRepository.findAll(); // Trong thực tế nên filter theo examCode
        
        LearningAnalysis analysis = analyticsService.generateClassAnalysis(examCode, results);
        
        if (analysis != null) {
            return ResponseEntity.ok(analysis);
        }
        return ResponseEntity.internalServerError().build();
    }
}