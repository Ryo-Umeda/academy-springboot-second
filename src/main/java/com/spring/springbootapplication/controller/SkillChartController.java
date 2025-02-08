package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.SkillService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/skills")
public class SkillChartController {

    private final SkillService skillService;

    public SkillChartController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/chart-data")
    public Map<String, Map<String, Integer>> getSkillChartData(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            throw new IllegalStateException("ユーザー情報が取得できません。ログイン状態を確認してください。");
        }

        return skillService.getSkillChartData(user.getId());
    }
}
