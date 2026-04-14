package com.planbookai.ocr.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GradingService {

    public double calculateNewCurriculumScore(String studentJson, String masterKeyJson) {
        JSONObject s = new JSONObject(studentJson);
        JSONObject m = new JSONObject(masterKeyJson);
        double total = 0.0;

        // Phần 1: Trắc nghiệm (0.25đ/câu)
        if (s.has("part_1")) {
            JSONArray p1 = s.getJSONArray("part_1");
            JSONObject k1 = m.getJSONObject("part_1");
            for (int i = 0; i < p1.length(); i++) {
                JSONObject q = p1.getJSONObject(i);
                if (k1.optString(String.valueOf(q.getInt("question"))).equalsIgnoreCase(q.optString("choice"))) total += 0.25;
            }
        }

        // Phần 2: Đúng/Sai (Điểm thưởng: 1 ý=0.1, 2 ý=0.25, 3 ý=0.5, 4 ý=1.0)
        if (s.has("part_2")) {
            JSONArray p2 = s.getJSONArray("part_2");
            JSONObject k2 = m.getJSONObject("part_2");
            for (int i = 0; i < p2.length(); i++) {
                JSONObject q = p2.getJSONObject(i);
                String qNum = String.valueOf(q.getInt("question"));
                if (k2.has(qNum)) {
                    int correctCount = 0;
                    JSONObject sAns = q.getJSONObject("answers");
                    JSONObject kAns = k2.getJSONObject(qNum);
                    for (String key : new String[]{"a", "b", "c", "d"}) {
                        if (sAns.optString(key).equalsIgnoreCase(kAns.optString(key))) correctCount++;
                    }
                    total += (correctCount == 4 ? 1.0 : correctCount == 3 ? 0.5 : correctCount == 2 ? 0.25 : correctCount == 1 ? 0.1 : 0);
                }
            }
        }
        
        // Phần 3: Trả lời ngắn (0.25đ/câu)
        if (s.has("part_3")) {
            JSONArray p3 = s.getJSONArray("part_3");
            JSONObject k3 = m.getJSONObject("part_3");
            for (int i = 0; i < p3.length(); i++) {
                JSONObject q = p3.getJSONObject(i);
                String qNum = String.valueOf(q.getInt("question"));
                if (k3.has(qNum) && k3.getString(qNum).equals(q.optString("value"))) total += 0.25;
            }
        }

        return Math.round(total * 100.0) / 100.0;
    }
}