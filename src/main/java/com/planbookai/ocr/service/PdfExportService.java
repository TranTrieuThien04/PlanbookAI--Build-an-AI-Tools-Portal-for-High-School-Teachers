package com.planbookai.ocr.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.planbookai.ocr.model.OcrResult;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfExportService {

    public byte[] createStudentReport(OcrResult result, String aiFeedback) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Cấu hình Font tiếng Việt (Đảm bảo đường dẫn file .ttf chính xác)
            BaseFont bf = BaseFont.createFont("src/main/resources/fonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(bf, 18, Font.BOLD, BaseColor.BLUE);
            Font normalFont = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);
            Font italicFont = new Font(bf, 12, Font.ITALIC, BaseColor.DARK_GRAY);

            // Tiêu đề
            Paragraph title = new Paragraph("PHIẾU NHẬN XÉT KẾT QUẢ HỌC TẬP", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Thông tin chi tiết
            document.add(new Paragraph("Học sinh: " + result.getStudentName(), normalFont));
            document.add(new Paragraph("Mã đề: " + result.getExamId(), normalFont));
            document.add(new Paragraph("Điểm số: " + result.getScore(), titleFont));
            document.add(new Paragraph("Ngày chấm: " + result.getGradedAt(), normalFont));
            
            document.add(new Paragraph("\n--------------------------------------------------\n"));

            // Nhận xét từ AI
            document.add(new Paragraph("NHẬN XÉT CỦA TRỢ LÝ AI:", normalFont));
            Paragraph feedback = new Paragraph(aiFeedback, italicFont);
            document.add(feedback);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}