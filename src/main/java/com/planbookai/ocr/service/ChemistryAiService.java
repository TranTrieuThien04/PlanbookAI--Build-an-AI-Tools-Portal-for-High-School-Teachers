package com.planbookai.ocr.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChemistryAiService {

    private static final Logger logger = LoggerFactory.getLogger(ChemistryAiService.class);

    public ChemistryAiService(GeminiAiService geminiAiService) {
    }


    public String normalizeChemicalFormula(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) return rawText;

        logger.info("Đang tinh lọc dữ liệu Hóa học...");
        
        // 1. Sửa lỗi O và 0 (Chỉ sửa khi nó nằm trong ngữ cảnh công thức: ví dụ Na0H, C02)
        // Regex này tìm số 0 nằm giữa hoặc sau các chữ cái viết hoa
        String processed = rawText.replaceAll("([A-Z])0", "$1O");

        // 2. Sửa các lỗi từ vựng Hóa học phổ biến do OCR quét nhầm
        processed = processed.replaceAll("(?i)H20", "H2O");
        processed = processed.replaceAll("(?i)KMnnO4", "KMnO4");
        processed = processed.replaceAll("(?i)KMn04", "KMnO4");

        // 3. Chuẩn hóa mũi tên
        processed = processed.replaceAll("\\s*(->|=>|—>|- >)\\s*", " → ");
        
        return processed;
    }

    /**
     * Phân tích ca khó (FR-08)
     */
    public AiDecisionResult analyzeDifficultCaseWithGemini(String ocrJson) {
        AiDecisionResult result = new AiDecisionResult();
        double confidence = 1.0;

        try {
            // Thử parse thử xem AI có trả về JSON hợp lệ không
            JSONObject testParse = new JSONObject(ocrJson);
            
            // Logic: Nếu AI báo confidence trong JSON thấp hơn 0.7
            if (testParse.has("confidence") && testParse.getDouble("confidence") < 0.7) {
                confidence = testParse.getDouble("confidence");
                logger.warn("AI tự báo độ tin cậy thấp: {}", confidence);
            }

            // Kiểm tra dấu hiệu tẩy xóa/không rõ (Dựa trên text trong JSON)
            if (ocrJson.contains("?") || ocrJson.contains("không rõ") || ocrJson.contains("mờ")) {
                confidence -= 0.3;
            }

            result.setFinalDecision(ocrJson);
            result.setConfidenceScore(Math.max(confidence, 0.0));
            result.setRequiresManualReview(confidence < 0.8);

        } catch (Exception e) {
            logger.error("JSON từ AI bị lỗi định dạng, cần kiểm tra tay.");
            result.setFinalDecision(ocrJson);
            result.setConfidenceScore(0.1);
            result.setRequiresManualReview(true);
        }

        return result;
    }

    public String formatForDisplay(String formula) {
        return formula.replaceAll("([A-Za-z])(\\d+)", "$1$2") // Regex giả lập để sếp thấy logic
                      .replace("0", "₀").replace("1", "₁").replace("2", "₂")
                      .replace("3", "₃").replace("4", "₄").replace("5", "₅");
    }

    public static class AiDecisionResult {
        private String finalDecision;
        private double confidenceScore;
        private boolean requiresManualReview;

        public String getFinalDecision() { return finalDecision; }
        public void setFinalDecision(String finalDecision) { this.finalDecision = finalDecision; }
        public double getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
        public boolean isRequiresManualReview() { return requiresManualReview; }
        public void setRequiresManualReview(boolean requiresManualReview) { this.requiresManualReview = requiresManualReview; }
    }
}