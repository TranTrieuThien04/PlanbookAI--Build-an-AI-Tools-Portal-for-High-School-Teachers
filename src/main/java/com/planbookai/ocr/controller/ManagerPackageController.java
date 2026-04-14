package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.ServicePackage;
import com.planbookai.ocr.repository.ServicePackageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/packages")
@PreAuthorize("hasRole('MANAGER')") // Chỉ MANAGER mới có quyền vào đây theo đúng URD 3.4.b
public class ManagerPackageController {

    private final ServicePackageRepository packageRepository;
    public ManagerPackageController(ServicePackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @GetMapping
    public List<ServicePackage> getAllPackages() {
        return packageRepository.findAll();
    }

    @PostMapping
    public ServicePackage createPackage(@RequestBody ServicePackage servicePackage) {
        return packageRepository.save(servicePackage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePackage> updatePackage(@PathVariable Long id, @RequestBody ServicePackage details) {
        return packageRepository.findById(id).map(p -> {
            p.setName(details.getName());
            p.setPrice(details.getPrice());
            p.setDurationDays(details.getDurationDays());
            p.setAiLimit(details.getAiLimit());
            p.setDescription(details.getDescription());
            return ResponseEntity.ok(packageRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        packageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}