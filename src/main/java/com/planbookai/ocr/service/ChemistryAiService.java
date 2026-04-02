package com.planbookai.ocr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChemistryAiService {

    private static final Logger logger = LoggerFactory.getLogger(ChemistryAiService.class);

    /**
     * FR-07: Xử lý đặc thù môn Hóa (Chemical Processing)
     * Chuẩn hóa text sau khi nhận diện từ Tesseract.
     * Giải quyết các vấn đề OCR nhầm lẫn phổ biến.
     */
    public String normalizeChemicalFormula(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return rawText;
        }

        logger.info("Bắt đầu chuẩn hóa dữ liệu OCR môn Hóa học...");
        String processedText = rawText;

        // 1. Sửa lỗi nhận diện nhầm O (Oxy) và 0 (Số không)
        // Nếu số 0 đứng ngay sau một chữ cái viết hoa (VD: C02 -> CO2, H20 -> H2O)
        processedText = processedText.replaceAll("(?<=[A-Z])0", "O");

        // 2. Chuyển đổi các chỉ số dưới (Subscript) cho các chất phổ biến
        // Cấu trúc thay thế chuỗi trực tiếp để tránh lỗi logic
        String[][] subscriptMappings = {
                {"H2O", "H₂O"},
                {"CO2", "CO₂"},
                {"SO2", "SO₂"},
                {"H2SO4", "H₂SO₄"},
                {"HNO3", "HNO₃"},
                {"CaCO3", "CaCO₃"},
                {"BaSO4", "BaSO₄"},
                {"KMnO4", "KMnO₄"}
        };

        for (String[] mapping : subscriptMappings) {
            processedText = processedText.replace(mapping[0], mapping[1]);
        }

        // 3. Sửa ký hiệu mũi tên phản ứng thường bị OCR đọc nhầm thành '-', '->' hoặc '=>'
        // Regex: Tìm các dạng mũi tên và thay bằng mũi tên chuẩn ' → '
        processedText = processedText.replaceAll("(?i)\\s*(->|=>|—>|- >)\\s*", " → ");

        logger.info("Hoàn tất chuẩn hóa. Kết quả: {}", processedText);
        return processedText;
    }

    /**
     * FR-08: Xử lý thông minh bằng AI (AI Decision Layer)
     * Phân tích các trường hợp OCR có độ tin cậy thấp hoặc ngữ cảnh không rõ ràng.
     */
    public AiDecisionResult analyzeDifficultCaseWithGemini(String questionContext, String ocrAnswer) {
        logger.info("Gửi request tới AI Gateway để xử lý ngữ cảnh phức tạp. OCR Answer: {}", ocrAnswer);
        AiDecisionResult result = new AiDecisionResult();

        // ---------------------------------------------------------
        // MOCK LOGIC (Tạm thời giả lập phản hồi từ AI)
        // ---------------------------------------------------------
        if (ocrAnswer.toLowerCase().contains("gạch") || ocrAnswer.toLowerCase().contains("xóa") || ocrAnswer.length() < 2) {
            result.setFinalDecision("Nghi ngờ gạch xóa/Không đọc được");
            result.setConfidenceScore(0.45);
            result.setRequiresManualReview(true);
            logger.warn("AI cảnh báo: Cần giáo viên kiểm tra thủ công bài này.");
        } else {
            // Giả lập AI tự động điền phần còn thiếu nhờ hiểu ngữ cảnh (VD: KMnO -> KMnO4)
            if (ocrAnswer.contains("KMnO") && !ocrAnswer.contains("₄") && !ocrAnswer.contains("4")) {
                result.setFinalDecision("KMnO₄");
                result.setConfidenceScore(0.88);
                result.setRequiresManualReview(false);
                logger.info("AI suy luận logic: {} -> KMnO₄", ocrAnswer);
            } else {
                result.setFinalDecision(ocrAnswer);
                result.setConfidenceScore(0.95);
                result.setRequiresManualReview(false);
            }
        }
        // ---------------------------------------------------------

        return result;
    }

    /**
     * Lớp DTO (Data Transfer Object) nội bộ chứa kết quả trả về từ phương thức phân tích AI.
     */
    public static class AiDecisionResult {
        private String finalDecision;
        private double confidenceScore;
        private boolean requiresManualReview;

        // --- Getters & Setters ---

        public String getFinalDecision() {
            return finalDecision;
        }

        public void setFinalDecision(String finalDecision) {
            this.finalDecision = finalDecision;
        }

        public double getConfidenceScore() {
            return confidenceScore;
        }

        public void setConfidenceScore(double confidenceScore) {
            this.confidenceScore = confidenceScore;
        }

        public boolean isRequiresManualReview() {
            return requiresManualReview;
        }

        public void setRequiresManualReview(boolean requiresManualReview) {
            this.requiresManualReview = requiresManualReview;
        }
    }
}