package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.Order;
import com.planbookai.ocr.repository.OrderRepository; // Nhớ tạo Repo tương ứng nhé
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/orders")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerOrderController {

    private final OrderRepository orderRepository;

    public ManagerOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/pending")
    public List<Order> getPendingOrders() {
        return orderRepository.findAll(); // Thực tế nên filter theo status = 'PENDING'
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveOrder(@PathVariable Long id) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus("APPROVED");
            orderRepository.save(order);
            // Ở đây ông có thể viết thêm logic cộng aiLimit cho User
            return ResponseEntity.ok("Đã kích hoạt gói cước cho giáo viên!");
        }).orElse(ResponseEntity.notFound().build());
    }
}