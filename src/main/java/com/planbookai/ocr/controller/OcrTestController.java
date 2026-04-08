package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.AnswerKey;
import com.planbookai.ocr.model.OcrResult;
import com.planbookai.ocr.repository.AnswerKeyRepository;
import com.planbookai.ocr.repository.OcrResultRepository;
import com.planbookai.ocr.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ocr")
public class OcrTestController {
    @Autowired
    private OcrResultRepository ocrResultRepository;

    @Autowired
    private AnswerKeyRepository answerKeyRepository;

    @Autowired
    private ChemistryAiService chemistryAiService;

    @Autowired
    private OmrProcessingService omrProcessingService;

    @Autowired
    private GradingService gradingService;

    @Autowired
    private GeminiAiService geminiAiService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/ping")
    public String ping() {
        return "PlanbookAI - Hệ thống hỗ trợ Giáo viên THPT đã sẵn sàng!";
    }

    /**
     * STAFF: Lưu đáp án mẫu vào Question Bank
     */
    @PostMapping("/answers/save")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<AnswerKey> saveAnswerKey(
            @RequestBody Map<String, Object> answers, 
            @RequestParam String examCode) {
        
        AnswerKey existingKey = answerKeyRepository.findByExamCode(examCode);
        AnswerKey key = (existingKey != null) ? existingKey : new AnswerKey();
        
        key.setExamCode(examCode);
        key.setType("NEW_CURRICULUM_2025");
        key.setAnswersJson(new JSONObject(answers).toString());
        
        return ResponseEntity.ok(answerKeyRepository.save(key));
    }

    /**
     * TEACHER: Chấm bài đơn lẻ
     */
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('TEACHER', 'ROLE_ADMIN', 'ADMIN')")
    public Object uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "mode", defaultValue = "CHOICE_TEXT") String mode,
            @RequestParam(value = "examCode", defaultValue = "1032") String examCode
    ) {
        return processFile(file, mode, examCode);
    }

    /**
     * TEACHER: Chấm bài hàng loạt (Batch Upload)
     */
    @PostMapping("/upload-batch")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<Map<String, Object>>> uploadBatchImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "mode", defaultValue = "CHOICE_TEXT") String mode,
            @RequestParam(value = "examCode", defaultValue = "1032") String examCode) {
        
        List<Map<String, Object>> batchResults = new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> result = (Map<String, Object>) processFile(file, mode, examCode);
            batchResults.add(result);
        }
        return ResponseEntity.ok(batchResults);
    }

    /**
     * TEACHER & MANAGER: Xuất file Excel bảng điểm
     */
    @GetMapping("/export-excel")
    @PreAuthorize("hasAnyRole('TEACHER', 'MANAGER')")
    public ResponseEntity<byte[]> exportExcel() {
        try {
            List<OcrResult> allResults = ocrResultRepository.findAll();
            byte[] excelContent = excelExportService.exportResultsToExcel(allResults);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BangDiem_PlanbookAI.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- HÀM XỬ LÝ CORE TỔNG HỢP ---
    private Object processFile(MultipartFile file, String mode, String examCode) {
        Map<String, Object> response = new HashMap<>();
        java.io.File tempFile = null;

        if (file.isEmpty()) {
            response.put("status", "error");
            response.put("message", "File trống!");
            return response;
        }

        try {
            tempFile = Files.createTempFile("ocr_", file.getOriginalFilename()).toFile();
            file.transferTo(tempFile);

            String currentMode = mode.toUpperCase();
            double finalScore = 0.0;
            String studentName = "Ẩn danh";
            String rawAiContent = "";

            if ("OMR".equals(currentMode)) {
                // Xử lý phiếu trắc nghiệm bằng OpenCV
                Map<Integer, String> studentAnswers = omrProcessingService.processOmrSheet(tempFile);
                AnswerKey key = answerKeyRepository.findByExamCode(examCode);
                // Giả định OMR chấm thang 10 truyền thống
                finalScore = gradingService.calculateMultipleChoiceScore(studentAnswers, new HashMap<>(), 10.0);
                rawAiContent = studentAnswers.toString();
            } else {
                // Xử lý bài thi tự luận/trắc nghiệm 2025 bằng Gemini AI
                rawAiContent = geminiAiService.analyzeImageWithGemini(tempFile.toPath());
                
                // Chuẩn hóa công thức hóa học & tính độ tin cậy
                rawAiContent = chemistryAiService.normalizeChemicalFormula(rawAiContent);
                ChemistryAiService.AiDecisionResult aiLogic = chemistryAiService.analyzeDifficultCaseWithGemini(rawAiContent);
                
                // Chấm điểm theo cấu trúc 3 phần của Bộ GD 2025
                AnswerKey key = answerKeyRepository.findByExamCode(examCode);
                if (key != null) {
                    finalScore = gradingService.calculateNewCurriculumScore(aiLogic.getFinalDecision(), key.getAnswersJson());
                }
                
                // Trích xuất tên học sinh từ JSON AI trả về (Giả định parse từ String)
                studentName = new JSONObject(aiLogic.getFinalDecision()).optString("student_name", "Ẩn danh");
                response.put("confidence", aiLogic.getConfidenceScore());
                response.put("manual_review", aiLogic.isRequiresManualReview());
            }

            // Lưu kết quả vào DB (FR-12)
            OcrResult entity = new OcrResult();
            entity.setStudentName(studentName);
            entity.setScore(finalScore);
            entity.setResultJson(rawAiContent); 
            ocrResultRepository.save(entity);

            response.put("status", "success");
            response.put("file_name", file.getOriginalFilename());
            response.put("student_name", studentName);
            response.put("score", finalScore);

            return response;

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Lỗi: " + e.getMessage());
            return response;
        } finally {
            if (tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }
}