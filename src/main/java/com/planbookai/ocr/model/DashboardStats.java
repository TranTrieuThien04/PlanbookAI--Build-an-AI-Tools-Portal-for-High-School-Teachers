package com.planbookai.ocr.model;

import java.math.BigDecimal;
import java.util.Map;

public class DashboardStats {
    private long totalTeachers;
    private long totalPapersGraded;
    private BigDecimal totalRevenue;
    private Map<String, Long> gradingByMonth; // Thống kê số bài theo tháng

    // --- Getter & Setter ---
    public long getTotalTeachers() { return totalTeachers; }
    public void setTotalTeachers(long totalTeachers) { this.totalTeachers = totalTeachers; }
    public long getTotalPapersGraded() { return totalPapersGraded; }
    public void setTotalPapersGraded(long totalPapersGraded) { this.totalPapersGraded = totalPapersGraded; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public Map<String, Long> getGradingByMonth() { return gradingByMonth; }
    public void setGradingByMonth(Map<String, Long> gradingByMonth) { this.gradingByMonth = gradingByMonth; }
}