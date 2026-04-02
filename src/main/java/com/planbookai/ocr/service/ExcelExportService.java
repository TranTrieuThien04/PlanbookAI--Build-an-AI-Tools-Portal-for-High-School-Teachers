package com.planbookai.ocr.service;

import com.planbookai.ocr.model.OcrResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelExportService {
    public byte[] exportResultsToExcel(List<OcrResult> results) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bảng Điểm UTH");

        // Tạo Header
        Row header = sheet.createRow(0);
        String[] columns = {"STT", "Họ Tên", "Điểm Số", "Chi Tiết"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Đổ dữ liệu vào
        int rowIdx = 1;
        for (OcrResult res : results) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(rowIdx - 1);
            row.createCell(1).setCellValue(res.getStudentName());
            row.createCell(2).setCellValue(res.getScore());
            row.createCell(3).setCellValue(res.getResultJson());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}