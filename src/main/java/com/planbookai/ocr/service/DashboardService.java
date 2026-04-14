package com.planbookai.ocr.service;

import com.planbookai.ocr.model.DashboardStats;
import com.planbookai.ocr.repository.OcrResultRepository;
import com.planbookai.ocr.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final OcrResultRepository ocrResultRepository;

    public DashboardService(UserRepository userRepository, OcrResultRepository ocrResultRepository) {
        this.userRepository = userRepository;
        this.ocrResultRepository = ocrResultRepository;
    }

    public DashboardStats getSystemOverview() {
        DashboardStats stats = new DashboardStats();
        
        // 1. Đếm tổng số giáo viên
        stats.setTotalTeachers(userRepository.count()); 

        // 2. Đếm tổng số bài thi đã chấm
        stats.setTotalPapersGraded(ocrResultRepository.count());

        // 3. Giả lập doanh thu (Trong thực tế sẽ sum từ bảng Orders)
        stats.setTotalRevenue(new BigDecimal("15500000")); 

        // 4. Giả lập dữ liệu biểu đồ
        Map<String, Long> chartData = new HashMap<>();
        chartData.put("Tháng 1", 120L);
        chartData.put("Tháng 2", 450L);
        chartData.put("Tháng 3", 890L);
        stats.setGradingByMonth(chartData);

        return stats;
    }
}