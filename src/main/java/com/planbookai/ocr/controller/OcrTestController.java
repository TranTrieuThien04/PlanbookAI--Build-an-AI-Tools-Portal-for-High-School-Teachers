package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.*;
import com.planbookai.ocr.repository.*;
import com.planbookai.ocr.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/api/v1/ocr")
public class OcrTestController {

    @Autowired private OcrResultRepository ocrResultRepository;
    @Autowired private AnswerKeyRepository answerKeyRepository;
    @Autowired private GradingService gradingService;
    @Autowired private GeminiAiService geminiAiService;
    @Autowired private AiAnalyticsService aiAnalyticsService;
    @Autowired private ExcelExportService excelExportService;
    @Autowired private PdfExportService pdfExportService;

    /**
     * 1. Chấm điểm đơn lẻ (Single Upload)
     * Quyền hạn: TEACHER
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam String examCode) {
        try {
            OcrResult result = processAndGrade(file, examCode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi xử lý file: " + e.getMessage());
        }
    }

    /**
     * 2. Chấm điểm hàng loạt (Batch Upload cho cả Folder)
     */
    @PostMapping("/upload-batch")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> uploadBatch(@RequestParam("files") MultipartFile[] files, @RequestParam String examCode) {
        List<OcrResult> results = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                results.add(processAndGrade(file, examCode));
                // Rate limit: Nghỉ 1s giữa các lần gọi Gemini AI bản Free
                Thread.sleep(1000); 
            } catch (Exception e) {
                System.err.println("Lỗi tại file " + file.getOriginalFilename() + ": " + e.getMessage());
            }
        }
        return ResponseEntity.ok(results);
    }

    /**
     * 3. Lấy nhận xét AI cá nhân hóa (Personalized Feedback)
     */
    @GetMapping("/analyze/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getFeedback(@PathVariable Long id) {
        return ocrResultRepository.findById(id)
                .map(res -> ResponseEntity.ok(Map.of("feedback", aiAnalyticsService.generateStudentFeedback(res))))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 4. Báo cáo phân tích lớp học (Class Analysis)
     */
    @GetMapping("/class-report/{examCode}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getReport(@PathVariable String examCode) {
        List<OcrResult> results = ocrResultRepository.findByExamId(Long.parseLong(examCode));
        return ResponseEntity.ok(aiAnalyticsService.generateClassAnalysis(examCode, results));
    }

    /**
     * 5. Xuất bảng điểm Excel
     */
    @GetMapping("/export/excel/{examId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportExcel(@PathVariable Long examId) {
        List<OcrResult> results = ocrResultRepository.findByExamId(examId);
        byte[] data = excelExportService.exportResultsToExcel(results);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BangDiem_De_" + examId + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    /**
     * 6. Xuất phiếu nhận xét PDF
     */
    @GetMapping("/export/pdf/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {
        OcrResult res = ocrResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả ID: " + id));
        
        String feedback = aiAnalyticsService.generateStudentFeedback(res);
        byte[] data = pdfExportService.createStudentReport(res, feedback);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PhieuNhanXet_HS_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    // Logic xử lý lõi (Private Helper)
    private OcrResult processAndGrade(MultipartFile file, String examCode) throws Exception {
        // Tạo file tạm để xử lý OCR
        File tempFile = Files.createTempFile("pba_ocr_", file.getOriginalFilename()).toFile();
        file.transferTo(tempFile);
        
        // 1. Gọi Gemini AI thực hiện OCR (Xử lý được cả chữ viết tay)
        String rawJson = geminiAiService.analyzeImageWithGemini(tempFile.toPath());
        
        // 2. Lấy đáp án mẫu từ ngân hàng
        AnswerKey key = answerKeyRepository.findByExamCode(examCode);
        
        // 3. Tính toán điểm số theo quy chế 2025 (Phần 1, 2, 3)
        double score = (key != null) ? gradingService.calculateNewCurriculumScore(rawJson, key.getAnswersJson()) : 0.0;

        // 4. Khởi tạo thực thể kết quả
        OcrResult res = new OcrResult();
        JSONObject responseObj = new JSONObject(rawJson);
        res.setStudentName(responseObj.optString("student_name", "Ẩn danh - " + file.getOriginalFilename()));
        res.setScore(score);
        res.setResultJson(rawJson);
        res.setConfidenceScore(0.98); 
        res.setExamId(Long.parseLong(examCode));
        
        // 5. Lưu trữ và xóa file tạm
        tempFile.delete();
        return ocrResultRepository.save(res);
    }
}