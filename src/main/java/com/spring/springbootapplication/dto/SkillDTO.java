package com.spring.springbootapplication.dto;

public class SkillDTO {
    private String category;

    private String skillName;

    private int learningHours;

    private String month;

    private Long id;

    public SkillDTO(String category, String skillName, int learningHours, String month, Long id) {
        this.category = category;
        this.skillName = skillName;
        this.learningHours = learningHours;
        this.month = month;
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getLearningHours() {
        return learningHours;
    }

    public String getMonth() {
        return month;
    }

    public Long getId() {
        return id;
    }
    
}
