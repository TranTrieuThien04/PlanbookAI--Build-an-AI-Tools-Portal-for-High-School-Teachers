package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.PromptTemplate;
import com.planbookai.ocr.repository.PromptTemplateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/staff/prompts")
@PreAuthorize("hasRole('STAFF')")
public class PromptStaffController {

    private final PromptTemplateRepository promptTemplateRepository;

    // Sửa lỗi răng cưa bằng Constructor thay vì @Autowired
    public PromptStaffController(PromptTemplateRepository promptTemplateRepository) {
        this.promptTemplateRepository = promptTemplateRepository;
    }

    @GetMapping
    public List<PromptTemplate> getAllPrompts() {
        return promptTemplateRepository.findAll();
    }

    @PostMapping
    public PromptTemplate createPrompt(@RequestBody PromptTemplate template) {
        return promptTemplateRepository.save(template);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromptTemplate> updatePrompt(@PathVariable Long id, @RequestBody PromptTemplate details) {
        return promptTemplateRepository.findById(id).map(p -> {
            p.setTitle(details.getTitle());
            p.setContent(details.getContent());
            p.setType(details.getType());
            return ResponseEntity.ok(promptTemplateRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrompt(@PathVariable Long id) {
        promptTemplateRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}