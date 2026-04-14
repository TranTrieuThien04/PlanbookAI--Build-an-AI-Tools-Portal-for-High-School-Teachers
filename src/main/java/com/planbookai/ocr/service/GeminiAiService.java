package com.planbookai.ocr.service;

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
    @Value("${gemini.api.key}") private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public String analyzeImageWithGemini(Path imagePath) throws IOException {
        String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(imagePath));
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        // Prompt tập trung 100% vào việc TRÍCH XUẤT trung thực bài làm của học sinh
        String prompt = "Bạn là máy quét OCR bài thi Hóa học chuyên nghiệp. Nhiệm vụ của bạn là đọc ảnh và trích xuất dữ liệu trung thực theo định dạng JSON:\n" +
                "{\n" +
                "  \"student_name\": \"Họ tên học sinh\",\n" +
                "  \"part_1\": [{\"question\": 1, \"choice\": \"A\"}, ...],\n" +
                "  \"part_2\": [{\"question\": 1, \"answers\": {\"a\": \"Đ\", \"b\": \"S\", \"c\": \"Đ\", \"d\": \"S\"}}, ...],\n" +
                "  \"part_3\": [{\"question\": 1, \"value\": \"12.5\"}, ...]\n" +
                "}\n" +
                "Lưu ý: Chỉ trả về JSON, không giải thích. Nếu không đọc được câu nào, hãy để giá trị là null hoặc chuỗi trống.";

        Map<String, Object> body = Map.of(
            "contents", List.of(Map.of("parts", List.of(
                Map.of("text", prompt),
                Map.of("inline_data", Map.of("mime_type", "image/jpeg", "data", base64))
            ))),
            "generationConfig", Map.of("response_mime_type", "application/json")
        );
        return callApi(url, body);
    }

    public String generateTextResponse(String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
        Map<String, Object> body = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));
        return callApi(url, body);
    }

    private String callApi(String url, Map<String, Object> body) {
        try {
            ResponseEntity<Map> res = restTemplate.postForEntity(url, new HttpEntity<>(body), Map.class);
            List<Map> candidates = (List<Map>) res.getBody().get("candidates");
            List<Map> parts = (List<Map>) ((Map) candidates.get(0).get("content")).get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) { return "{}"; }
    }
}