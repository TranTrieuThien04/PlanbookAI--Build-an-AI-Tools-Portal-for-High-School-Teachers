package com.planbookai.ocr.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GradingService {

    // --- 1. HÀM CHẤM ĐIỂM THEO CHƯƠNG TRÌNH THPT MỚI 2025 (PHẦN CHA VỪA LÀM) ---
    public double calculateNewCurriculumScore(String studentText, String jsonAnswerKey) {
        double totalScore = 0.0;
        JSONObject masterKey = new JSONObject(jsonAnswerKey);

        // Chấm Phần I: Trắc nghiệm 4 lựa chọn (0.25đ/câu)
        for (int i = 1; i <= 40; i++) {
            String key = "Câu " + i;
            if (masterKey.has(key)) {
                String correctAnswer = masterKey.getString(key);
                String patternStr = "(?i)Câu\\s*" + i + "[:\\s-]*([A-D])";
                Matcher m = Pattern.compile(patternStr).matcher(studentText);
                if (m.find() && m.group(1).equalsIgnoreCase(correctAnswer)) {
                    totalScore += 0.25;
                }
            }
        }

        // Chấm Phần II: Trắc nghiệm Đúng/Sai (Lũy tiến 0.1, 0.25, 0.5, 1.0)
        for (int i = 1; i <= 40; i++) {
            String key = "Câu " + i + "_DS";
            if (masterKey.has(key)) {
                String correctDS = masterKey.getString(key).toUpperCase();
                String patternStr = "(?i)Câu\\s*" + i + "[:\\s]*\\(a\\)\\s*([ĐS])\\s*\\(b\\)\\s*([ĐS])\\s*\\(c\\)\\s*([ĐS])\\s*\\(d\\)\\s*([ĐS])";
                Matcher m = Pattern.compile(patternStr).matcher(studentText);
                
                if (m.find()) {
                    int correctCount = 0;
                    for (int j = 0; j < 4; j++) {
                        String studentAns = m.group(j + 1);
                        if (studentAns.equalsIgnoreCase(String.valueOf(correctDS.charAt(j)))) {
                            correctCount++;
                        }
                    }
                    if (correctCount == 1) totalScore += 0.1;
                    else if (correctCount == 2) totalScore += 0.25;
                    else if (correctCount == 3) totalScore += 0.5;
                    else if (correctCount == 4) totalScore += 1.0;
                }
            }
        }
        return Math.min(totalScore, 10.0);
    }

    // --- 2. HÀM CHẤM OMR (CÁI NÀY ĐANG BỊ ĐỎ NÈ CHA NỘI) ---
    public double calculateMultipleChoiceScore(Map<Integer, String> studentAnswers, Map<Integer, String> standardKeys, double maxScore) {
        if (standardKeys == null || standardKeys.isEmpty()) return 0.0;
        
        int correctCount = 0;
        for (Map.Entry<Integer, String> entry : standardKeys.entrySet()) {
            Integer questionNum = entry.getKey();
            String correctAnswer = entry.getValue();
            if (studentAnswers.containsKey(questionNum) && studentAnswers.get(questionNum).equalsIgnoreCase(correctAnswer)) {
                correctCount++;
            }
        }
        return (double) correctCount / standardKeys.size() * maxScore;
    }
}