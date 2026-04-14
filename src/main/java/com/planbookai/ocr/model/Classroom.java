package com.planbookai.ocr.model;

import jakarta.persistence.*;

@Entity
@Table(name = "classrooms")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;
    private String teacherUsername;

    @Column(columnDefinition = "TEXT")
    private String studentListJson;

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getTeacherUsername() { return teacherUsername; }
    public void setTeacherUsername(String teacherUsername) { this.teacherUsername = teacherUsername; }
    public String getStudentListJson() { return studentListJson; }
    public void setStudentListJson(String studentListJson) { this.studentListJson = studentListJson; }
}