package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dto.SkillDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    public List<SkillDTO> getSkillsForCategory(String category, String month) {
        List<SkillDTO> allSkills = createDummySkills();
        return allSkills.stream()
                .filter(skill -> skill.getCategory().equals(category) && skill.getMonth().equals(month))
                .collect(Collectors.toList());
    }

    public List<String> getPastThreeMonths() {
        List<String> pastMonths = new ArrayList<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("M月");
    
        for (int i = 0; i < 3; i++) { // 当月 + 過去2ヶ月
            String dbMonth = now.minusMonths(i).format(dbFormat);
            String displayMonth = now.minusMonths(i).format(displayFormat);
            pastMonths.add(dbMonth + "," + displayMonth); // "2025-01,1月" のようにセット
        }
        return pastMonths;
    }

    private List<SkillDTO> createDummySkills() {
        List<SkillDTO> skills = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    
        LocalDate now = LocalDate.now();
        String currentMonth = now.format(formatter);
        String lastMonth = now.minusMonths(1).format(formatter);
        String twoMonthsAgo = now.minusMonths(2).format(formatter);
    
        // 当月データ
        skills.add(new SkillDTO("バックエンド", "Java", 10, currentMonth));
        skills.add(new SkillDTO("バックエンド", "Spring Boot", 10, currentMonth));
        skills.add(new SkillDTO("バックエンド", "Ruby", 10, currentMonth));
        skills.add(new SkillDTO("バックエンド", "PHP", 10, currentMonth));
        skills.add(new SkillDTO("フロントエンド", "React", 10, currentMonth));
        skills.add(new SkillDTO("フロントエンド", "Vue.js", 10, currentMonth));
        skills.add(new SkillDTO("インフラ", "AWS", 10, currentMonth));
    
        // 先月データ
        skills.add(new SkillDTO("バックエンド", "Java", 20, lastMonth));
        skills.add(new SkillDTO("バックエンド", "Spring Boot", 20, lastMonth));
        skills.add(new SkillDTO("フロントエンド", "React", 20, lastMonth));
        skills.add(new SkillDTO("フロントエンド", "Vue.js", 20, lastMonth));
        skills.add(new SkillDTO("インフラ", "AWS", 20, lastMonth));
    
        // 2ヶ月前データ
        skills.add(new SkillDTO("バックエンド", "Java", 30, twoMonthsAgo));
        skills.add(new SkillDTO("バックエンド", "Spring Boot", 30, twoMonthsAgo));
        skills.add(new SkillDTO("フロントエンド", "React", 30, twoMonthsAgo));
        skills.add(new SkillDTO("フロントエンド", "Vue.js", 30, twoMonthsAgo));
        skills.add(new SkillDTO("インフラ", "AWS", 30, twoMonthsAgo));
    
        return skills;
    }
    
}
