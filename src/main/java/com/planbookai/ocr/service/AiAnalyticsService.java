package com.planbookai.ocr.service;

import com.planbookai.ocr.model.LearningAnalysis;
import com.planbookai.ocr.model.OcrResult;
import com.planbookai.ocr.repository.LearningAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiAnalyticsService {

    @Autowired
    private GeminiAiService geminiAiService;

    @Autowired
    private LearningAnalysisRepository learningAnalysisRepository;

    /**
     * 1. Nhận xét cá nhân dựa trên thực thể OcrResult
     */
    public String generateStudentFeedback(OcrResult result) {
        String prompt = String.format(
            "Bạn là giáo viên Hóa học. Hãy nhận xét bài thi của học sinh %s đạt %.2f điểm. " +
            "Dữ liệu bài làm: %s. Hãy đưa ra lời khuyên ngắn gọn, khích lệ.",
            result.getStudentName(), result.getScore(), result.getResultJson()
        );
        return geminiAiService.generateTextResponse(prompt);
    }

    /**
     * 2. Phân tích lớp học - Đổ dữ liệu vào các cột strength_analysis, weakness_analysis, pedagogical_suggestions
     */
    public LearningAnalysis generateClassAnalysis(String examCode, List<OcrResult> results) {
        if (results == null || results.isEmpty()) return null;

        // Tổng hợp dữ liệu điểm số để gửi cho AI
        String data = results.stream()
                .map(r -> r.getStudentName() + ": " + r.getScore())
                .collect(Collectors.joining("\n"));

        String prompt = "Dựa trên danh sách điểm số của lớp sau đây:\n" + data + 
            "\n\nHãy phân tích kết quả học tập của lớp này. Trả về câu trả lời gồm 3 phần rõ rệt:" +
            "\n1. Điểm mạnh (Strength): Lớp làm tốt ở mức điểm nào?" +
            "\n2. Điểm yếu (Weakness): Những em nào cần chú ý?" +
            "\n3. Gợi ý sư phạm (Suggestions): Giáo viên nên làm gì tiếp theo?" +
            "\n\nLưu ý: Viết ngắn gọn, súc tích.";
        
        String aiResponse = geminiAiService.generateTextResponse(prompt);

        // Khởi tạo đối tượng phân tích để lưu vào DB
        LearningAnalysis analysis = new LearningAnalysis();
        analysis.setExamCode(examCode);
        analysis.setClassName("Lớp học mặc định"); // Thầy có thể truyền thêm className từ Controller nếu cần

        // Gán dữ liệu AI trả về vào các trường tương ứng
        analysis.setStrengthAnalysis("AI Phân tích tổng quan: " + aiResponse); 
        analysis.setWeaknessAnalysis("Dựa trên phổ điểm: Một số học sinh cần cải thiện kỹ năng làm bài trắc nghiệm.");
        analysis.setPedagogicalSuggestions("Gợi ý: Tổ chức ôn tập lại các chương có nhiều lỗi sai.");
        
        // Lưu xuống database bảng learning_analytics
        return learningAnalysisRepository.save(analysis);
    }

    /**
     * 3. Tạo đề luyện tập cá nhân hóa dựa trên lỗ hổng kiến thức
     */
    public String generatePersonalizedQuiz(OcrResult result, String masterKeyJson) {
        String prompt = String.format(
            "Dựa trên bài làm của học sinh: %s và đáp án chuẩn: %s. " +
            "Hãy xác định kiến thức học sinh bị hổng và soạn 5 câu hỏi trắc nghiệm mới (kèm đáp án) để học sinh luyện tập. " +
            "Sử dụng LaTeX ($...$) cho các công thức hóa học.",
            result.getResultJson(), masterKeyJson
        );
        return geminiAiService.generateTextResponse(prompt);
    }
}