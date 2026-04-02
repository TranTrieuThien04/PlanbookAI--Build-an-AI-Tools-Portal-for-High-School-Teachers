package com.planbookai.ocr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class GeminiAiService {
    private static final Logger logger = LoggerFactory.getLogger(GeminiAiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @SuppressWarnings("unchecked")
    public String analyzeImageWithGemini(Path imagePath) throws IOException {
        logger.info("Đang gọi Gemini 2.5 Flash (Standard 2026) để phân tích ảnh...");
        
        byte[] imageBytes = Files.readAllBytes(imagePath);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // ĐÂY LÀ URL CHUẨN CHO NĂM 2026
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        // Prompt ép AI làm việc kỹ càng
        String strictPrompt = "Bạn là máy chấm thi THPT Quốc gia 2025. Trích xuất nội dung:\n" +
        "1. Họ tên học sinh: (Dòng đầu).\n" +
        "2. Đáp án phần I: Ghi dạng 'Câu X: Y' (Ví dụ: Câu 1: A).\n" +
        "3. Đáp án phần II (Đúng/Sai): Ghi dạng 'Câu X: (a) Đ (b) S (c) S (d) Đ'.\n" +
        "LƯU Ý: Tuyệt đối dùng chữ 'Đ' cho Đúng và 'S' cho Sai. Không được bỏ sót bất kỳ ý (a), (b), (c), (d) nào.";

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", strictPrompt);

        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mime_type", "image/jpeg");
        inlineData.put("data", base64Image);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", Arrays.asList(textPart, Collections.singletonMap("inline_data", inlineData)));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", Collections.singletonList(content));

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            Map<String, Object> contentRes = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentRes.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            logger.error("Lỗi gọi Gemini: {}", e.getMessage());
            return "Lỗi API: " + e.getMessage();
        }
    }
}