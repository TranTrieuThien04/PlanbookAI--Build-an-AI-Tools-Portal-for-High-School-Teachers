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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OmrProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(OmrProcessingService.class);

    // Load thư viện native của OpenCV một lần duy nhất khi Service được khởi tạo
    static {
        OpenCV.loadLocally();
    }

    /**
     * FR-06: Nhận diện đáp án trắc nghiệm (OMR - Bubble Detection)
     * Trả về một Map chứa: Số thứ tự câu hỏi -> Đáp án (A, B, C hoặc D)
     */
    public Map<Integer, String> processOmrSheet(File imageFile) {
        logger.info("Bắt đầu xử lý ảnh OMR bằng OpenCV: {}", imageFile.getName());
        Map<Integer, String> studentAnswers = new HashMap<>();

        try {
            // 1. Đọc ảnh
            Mat src = Imgcodecs.imread(imageFile.getAbsolutePath());
            if (src.empty()) {
                logger.error("Không thể đọc được ảnh OMR!");
                return studentAnswers;
            }

            // --- FR-03: Tiền xử lý ảnh (Image Preprocessing) ---
            Mat gray = new Mat();
            Mat blurred = new Mat();
            Mat thresh = new Mat();

            // Chuyển sang ảnh xám (Grayscale)
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
            
            // Giảm nhiễu (Noise Removal) bằng GaussianBlur
            Imgproc.GaussianBlur(gray, blurred, new Size(5, 5), 0);
            
            // Nhị phân hóa ảnh (Thresholding): Chữ đen/nền trắng -> Chữ trắng/nền đen để dễ tìm viền
            Imgproc.threshold(blurred, thresh, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

            // --- FR-06: Phát hiện vùng tô (Bubble Detection) ---
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            
            // Tìm các đường viền (Contour Detection)
            Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            List<Rect> bubbleRects = new ArrayList<>();

            // Lọc ra các contours có dạng hình tròn (là các bong bóng ABCD)
            for (MatOfPoint contour : contours) {
                Rect rect = Imgproc.boundingRect(contour);
                float aspectRatio = (float) rect.width / rect.height;
                
                // Giả định bong bóng có kích thước khoảng 20x20 pixel và tỷ lệ khung hình gần bằng 1
                if (rect.width >= 20 && rect.height >= 20 && aspectRatio >= 0.8 && aspectRatio <= 1.2) {
                    bubbleRects.add(rect);
                }
            }

            logger.info("Phát hiện được {} bong bóng (bubbles) trên phiếu.", bubbleRects.size());
            if (bubbleRects.size() >= 4) {
                String[] options = {"A", "B", "C", "D"};
                int questionNumber = 1;
                
                int maxPixels = 0;
                int selectedOptionIndex = -1;

                for (int i = 0; i < 4; i++) {
                    Rect bubble = bubbleRects.get(i);
                    // Cắt lấy vùng ảnh riêng của bong bóng đó
                    Mat roi = thresh.submat(bubble);
                    
                    // Đếm số lượng pixel trắng (phần được tô bút chì đen đã bị đảo màu ở bước Threshold)
                    int filledPixels = Core.countNonZero(roi);

                    // Ngưỡng (Threshold) quyết định bong bóng có được tô hay không
                    if (filledPixels > maxPixels && filledPixels > (bubble.width * bubble.height * 0.5)) { 
                        // Phải tô ít nhất 50% diện tích bong bóng
                        maxPixels = filledPixels;
                        selectedOptionIndex = i;
                    }
                }

                if (selectedOptionIndex != -1) {
                    studentAnswers.put(questionNumber, options[selectedOptionIndex]);
                    logger.info("Câu {}: Học sinh chọn đáp án {}", questionNumber, options[selectedOptionIndex]);
                } else {
                    logger.warn("Câu {}: Không tô đáp án hoặc tô quá mờ!", questionNumber);
                    studentAnswers.put(questionNumber, "UNANSWERED");
                }
            }

            // Giải phóng bộ nhớ (Quy tắc bắt buộc khi dùng OpenCV trong Java)
            src.release(); gray.release(); blurred.release(); thresh.release(); hierarchy.release();

        } catch (Exception e) {
            logger.error("Lỗi trong quá trình xử lý OMR: ", e);
        }

        return studentAnswers;
    }
}