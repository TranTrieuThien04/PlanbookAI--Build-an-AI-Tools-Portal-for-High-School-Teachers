package com.planbookai.ocr.controller;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.planbookai.ocr.model.AnswerKey;
import com.planbookai.ocr.model.OcrResult;
import com.planbookai.ocr.repository.AnswerKeyRepository;
import com.planbookai.ocr.repository.OcrResultRepository;
import com.planbookai.ocr.service.ChemistryAiService;
import com.planbookai.ocr.service.GeminiAiService;
import com.planbookai.ocr.service.GradingService;
import com.planbookai.ocr.service.OmrProcessingService;
import com.planbookai.ocr.service.ExcelExportService; // Nhớ tạo file này như tui chỉ nhé!

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
        return "Hệ thống AI Chấm thi THPT 2025 - PRO Version đã sẵn sàng!";
    }

    @PostMapping("/answers/save")
    public ResponseEntity<AnswerKey> saveAnswerKey(
            @RequestBody Map<String, String> answers, 
            @RequestParam String examCode) {
        
        AnswerKey existingKey = answerKeyRepository.findByExamCode(examCode);
        AnswerKey key = (existingKey != null) ? existingKey : new AnswerKey();
        
        key.setExamCode(examCode);
        key.setType("CHOICE_TEXT");
        key.setAnswersJson(new org.json.JSONObject(answers).toString());
        
        return ResponseEntity.ok(answerKeyRepository.save(key));
    }

    // --- 1. CHẤM 1 FILE ĐƠN ---
    @PostMapping("/upload")
    public Object uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "mode", defaultValue = "CHOICE_TEXT") String mode,
            @RequestParam(value = "examCode", defaultValue = "1032") String examCode
    ) {
        return processFile(file, mode, examCode);
    }

    // --- 2. CHỨC NĂNG CHẤM HÀNG LOẠT (BATCH UPLOAD) ---
    @PostMapping("/upload-batch")
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

    // --- 3. CHỨC NĂNG XUẤT EXCEL ---
    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel() {
        try {
            List<OcrResult> allResults = ocrResultRepository.findAll();
            byte[] excelContent = excelExportService.exportResultsToExcel(allResults);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BangDiem_THPT2025.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- HÀM XỬ LÝ CORE (Dùng chung cho cả đơn và loạt) ---
    private Object processFile(MultipartFile file, String mode, String examCode) {
        Map<String, Object> response = new HashMap<>();
        java.io.File tempFile = null;

        if (file.isEmpty()) {
            response.put("status", "error");
            response.put("message", "File trống: " + file.getOriginalFilename());
            return response;
        }

        try {
            tempFile = Files.createTempFile("ocr_", file.getOriginalFilename()).toFile();
            file.transferTo(tempFile);

            String currentMode = mode.toUpperCase();
            double finalScore = 0.0;
            String studentName = "Ẩn danh";
            String processedContent = "";

            if ("OMR".equals(currentMode)) {
                Map<Integer, String> studentAnswers = omrProcessingService.processOmrSheet(tempFile);
                Map<Integer, String> standardKeys = getMockStandardKeysForOMR();
                finalScore = gradingService.calculateMultipleChoiceScore(studentAnswers, standardKeys, 10.0);
                processedContent = studentAnswers.toString();
            } else {
                processedContent = geminiAiService.analyzeImageWithGemini(tempFile.toPath());
                studentName = extractStudentName(processedContent);
                processedContent = chemistryAiService.normalizeChemicalFormula(processedContent);

                AnswerKey key = answerKeyRepository.findByExamCode(examCode);
                if (key != null) {
                    finalScore = gradingService.calculateNewCurriculumScore(processedContent, key.getAnswersJson());
                }
            }

            // Lưu DB
            OcrResult entity = new OcrResult();
            entity.setStudentName(studentName);
            entity.setScore(finalScore);
            entity.setResultJson(processedContent); 
            ocrResultRepository.save(entity);

            // Clean nội dung cho nhẹ JSON
            String cleanContent = processedContent.replace("\n", " | ").replaceAll("\\s+", " ").trim();

            response.put("status", "success");
            response.put("file_name", file.getOriginalFilename());
            response.put("student_name", studentName);
            response.put("score", finalScore);
            response.put("content", cleanContent); 

            return response;

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Lỗi xử lý " + file.getOriginalFilename() + ": " + e.getMessage());
            return response;
        } finally {
            if (tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }

    private String extractStudentName(String text) {
        String regex = "(?i)(họ tên học sinh|họ và tên|họ tên|tên học sinh|tên)[:\\s\\*\\-]*([^\\n\\*\\|]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String name = matcher.group(2).trim();
            if (name.contains("\n")) {
                name = name.split("\n")[0].trim();
            }
            return name;
        }
        return "Ẩn danh";
    }

    private Map<Integer, String> getMockStandardKeysForOMR() {
        Map<Integer, String> keys = new HashMap<>();
        keys.put(1, "A"); keys.put(2, "C"); keys.put(3, "B"); keys.put(4, "D");
        return keys;
    }
}