package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.AnswerKey;
import com.planbookai.ocr.model.OcrResult;
import com.planbookai.ocr.repository.AnswerKeyRepository;
import com.planbookai.ocr.repository.OcrResultRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/ocr")
public class OcrTestController {

    @Autowired
    private OcrResultRepository ocrResultRepository;

    @Autowired
    private AnswerKeyRepository answerKeyRepository;

    @GetMapping("/ping")
    public String ping() {
        return "Hệ thống AI Multi-mode: ESSAY, CHOICE_TEXT, OMR đã sẵn sàng!";
    }

    @PostMapping("/upload")
    public Object uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "mode", defaultValue = "ESSAY") String mode
    ) {
        Map<String, Object> response = new HashMap<>();
        File tempFile = null;

        if (file.isEmpty()) {
            response.put("status", "error");
            response.put("message", "Vui lòng chọn một file ảnh!");
            return response;
        }

        try {
            tempFile = Files.createTempFile("ocr_", file.getOriginalFilename()).toFile();
            file.transferTo(tempFile);

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("tessdata");
            tesseract.setLanguage("vie+eng");

            String rawText = tesseract.doOCR(tempFile); // Đây là nội dung bài làm
            String studentName = extractStudentName(rawText);

            String currentMode = mode.toUpperCase();
            double finalScore = ("OMR".equals(currentMode))
                    ? processOMRScore(tempFile)
                    : calculateScoreFromText(rawText, currentMode);

            OcrResult entity = new OcrResult();
            entity.setStudentName(studentName);
            entity.setScore(finalScore);
            entity.setResultJson(rawText);
            OcrResult savedEntity = ocrResultRepository.save(entity);

            response.put("status", "success");
            response.put("id", savedEntity.getId());
            response.put("student_name", studentName);
            response.put("score", finalScore);
            response.put("mode", currentMode);

            response.put("content", rawText);

            return response;

        } catch (IOException | TesseractException e) {
            response.put("status", "error");
            response.put("message", "Lỗi xử lý: " + e.getMessage());
            return response;
        } finally {
            if (tempFile != null && tempFile.exists()) {
                boolean isDeleted = tempFile.delete();
                if (!isDeleted) {
                    System.err.println("Không thể xóa file tạm: " + tempFile.getName());
                }
            }
        }
    }
    private double calculateScoreFromText(String text, String mode) {
        double score = 0.0;
        List<AnswerKey> keys = answerKeyRepository.findAll();

        for (AnswerKey key : keys) {
            if (key.getType() == null || key.getType().equalsIgnoreCase(mode)) {

                if ("CHOICE_TEXT".equals(mode)) {
                    String patternString = "(?i)" + key.getKeyword() + "([\\s\\.\\-\\:]*)([A-D])";
                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(text.toUpperCase());
                    if (matcher.find()) {
                        score += key.getPoint();
                    }
                } else {
                    if (text.toLowerCase().contains(key.getKeyword().toLowerCase())) {
                        score += key.getPoint();
                    }
                }
            }
        }
        return Math.min(score, 10.0);
    }

    private double processOMRScore(File imageFile) {
        return 0.0;
    }

    private String extractStudentName(String text) {
        String regex = "(?i)(họ và tên|họ tên|tên học sinh|tên)\\s*[:\\-]?\\s*(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String line = matcher.group(2).trim();
            String[] parts = line.split("(?i)Nhận xét|Lớp|Trường|Số báo danh|MSHS");
            return parts[0].replaceAll("[,.:;]+$", "").trim();
        }
        return "Ẩn danh";
    }
}