package com.planbookai.ocr.service;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OmrProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(OmrProcessingService.class);

    static {
        OpenCV.loadLocally();
    }

    public Map<Integer, String> processOmrSheet(File imageFile) {
        logger.info("--- Đang xử lý OMR bằng OpenCV (Xử lý hàng thông minh) ---");
        Map<Integer, String> studentAnswers = new HashMap<>();

        Mat src = null;
        Mat gray = new Mat();
        Mat blurred = new Mat();
        Mat thresh = new Mat();
        Mat hierarchy = new Mat();

        try {
            src = Imgcodecs.imread(imageFile.getAbsolutePath());
            if (src.empty()) return studentAnswers;

            // 1. Tiền xử lý (Pre-processing)
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.GaussianBlur(gray, blurred, new Size(5, 5), 0);
            
            // Dùng Otsu để tự động tìm ngưỡng trắng đen tối ưu
            Imgproc.threshold(blurred, thresh, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

            // 2. Tìm các đường bao (Contours)
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            List<Rect> bubbleRects = new ArrayList<>();
            for (MatOfPoint contour : contours) {
                Rect rect = Imgproc.boundingRect(contour);
                float aspectRatio = (float) rect.width / rect.height;
                
                // Lọc kỹ hơn để tránh bắt nhầm chữ cái hoặc dấu chấm
                if (rect.width >= 20 && rect.height >= 20 && aspectRatio >= 0.8 && aspectRatio <= 1.2) {
                    bubbleRects.add(rect);
                }
            }

            // --- BƯỚC QUAN TRỌNG: SẮP XẾP THEO HÀNG (Row Grouping) ---
            // Sắp xếp theo Y trước
            bubbleRects.sort(Comparator.comparingInt(r -> r.y));

            List<List<Rect>> rows = new ArrayList<>();
            if (!bubbleRects.isEmpty()) {
                List<Rect> currentRow = new ArrayList<>();
                int currentY = bubbleRects.get(0).y;
                int thresholdY = 15; // Sai số Y cho phép trong cùng 1 hàng (tùy độ phân giải ảnh)

                for (Rect r : bubbleRects) {
                    if (Math.abs(r.y - currentY) <= thresholdY) {
                        currentRow.add(r);
                    } else {
                        // Sắp xếp các ô trong hàng theo X (từ trái sang phải)
                        currentRow.sort(Comparator.comparingInt(rect -> rect.x));
                        rows.add(new ArrayList<>(currentRow));
                        currentRow.clear();
                        currentRow.add(r);
                        currentY = r.y;
                    }
                }
                currentRow.sort(Comparator.comparingInt(rect -> rect.x));
                rows.add(currentRow);
            }

            // 3. Phân tích từng hàng để lấy đáp án
            String[] options = {"A", "B", "C", "D"};
            for (int i = 0; i < rows.size(); i++) {
                List<Rect> row = rows.get(i);
                if (row.size() < 4) continue; // Bỏ qua nếu hàng không đủ 4 ô

                int questionNumber = i + 1;
                int selectedOptionIndex = -1;
                double maxFilledRatio = 0;

                for (int j = 0; j < row.size(); j++) {
                    if (j >= 4) break; // Chỉ lấy A, B, C, D
                    
                    Rect bubble = row.get(j);
                    Mat roi = thresh.submat(bubble);
                    
                    int filledPixels = Core.countNonZero(roi);
                    double totalPixels = bubble.width * bubble.height;
                    double filledRatio = (double) filledPixels / totalPixels;

                    // Ngưỡng 0.35 để xác định là có tô
                    if (filledRatio > 0.35 && filledRatio > maxFilledRatio) {
                        maxFilledRatio = filledRatio;
                        selectedOptionIndex = j;
                    }
                    roi.release();
                }

                if (selectedOptionIndex != -1) {
                    studentAnswers.put(questionNumber, options[selectedOptionIndex]);
                } else {
                    studentAnswers.put(questionNumber, "NOT_FOUND");
                }
            }

        } catch (Exception e) {
            logger.error("Lỗi OpenCV: ", e);
        } finally {
            // Giải phóng bộ nhớ (Cực kỳ quan trọng với OpenCV)
            if (src != null) src.release();
            gray.release(); blurred.release(); thresh.release(); hierarchy.release();
        }

        return studentAnswers;
    }
}