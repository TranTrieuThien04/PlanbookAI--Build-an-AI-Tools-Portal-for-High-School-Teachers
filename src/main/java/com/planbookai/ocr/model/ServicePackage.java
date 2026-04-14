package com.planbookai.ocr.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "service_packages")
public class ServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ví dụ: "Gói Học Kỳ 1", "Gói VIP"
    private BigDecimal price; // Giá tiền
    private Integer durationDays; // Thời hạn (VD: 30, 90, 365 ngày)
    private Integer aiLimit; // Giới hạn lượt chấm AI (VD: 100 bài/tháng)
    private String description;

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
    public Integer getAiLimit() { return aiLimit; }
    public void setAiLimit(Integer aiLimit) { this.aiLimit = aiLimit; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}