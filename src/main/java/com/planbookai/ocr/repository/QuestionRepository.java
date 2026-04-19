package com.planbookai.ocr.repository;

import com.planbookai.ocr.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByTopicAndDifficultyLevel(String topic, String difficultyLevel);
}