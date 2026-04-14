package com.planbookai.ocr.model;

public enum UserRole {

    ROLE_ADMIN,    // Quản trị viên: Quản lý user, cấu hình hệ thống
    ROLE_MANAGER,  // Quản lý: Gói cước, doanh thu, phê duyệt nội dung
    ROLE_STAFF,    // Nhân viên: Soạn giáo án mẫu, ngân hàng câu hỏi, Prompt AI
    ROLE_TEACHER   // Giáo viên: Tạo bài thi, chấm điểm OCR, xem phân tích
}
    