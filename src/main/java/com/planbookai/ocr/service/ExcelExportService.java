package com.planbookai.ocr.service;

import com.planbookai.ocr.model.OcrResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);

    public byte[] exportResultsToExcel(List<OcrResult> results) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bang Diem Lop Hoc");

            // 1. Tạo Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"STT", "Học Sinh", "Mã Đề", "Điểm Số", "Trạng Thái", "Ngày Chấm"};
            
            CellStyle headerCellStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // 2. Đổ dữ liệu từ Database
            int rowIdx = 1;
            for (OcrResult res : results) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(rowIdx - 1);
                row.createCell(1).setCellValue(res.getStudentName());
                row.createCell(2).setCellValue(res.getExamId() != null ? res.getExamId() : 0);
                row.createCell(3).setCellValue(res.getScore() != null ? res.getScore() : 0.0);
                row.createCell(4).setCellValue(res.getRequiresReview() != null && res.getRequiresReview() ? "Cần xem lại" : "Hợp lệ");
                row.createCell(5).setCellValue(res.getGradedAt().toString());
            }

            // Tự động căn chỉnh độ rộng cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            logger.error("Lỗi khi xuất file Excel: {}", e.getMessage());
            return new byte[0];
        }
    }
}