package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.SkillDTO;
import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.SkillService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills")
    public String showSkills(
            @RequestParam(value = "month", required = false) String month,
            Model model,
            @AuthenticationPrincipal UserEntity user  // ログイン中のユーザー情報を取得
    ) {
        if (user == null) {
            throw new IllegalStateException("ユーザー情報が取得できません。ログイン状態を確認してください。");
        }

        // デフォルトは当月
        if (month == null || month.isEmpty()) {
            month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        // ログイン中のユーザーIDを渡す
        List<SkillDTO> backendSkills = skillService.getSkillsForCategory(user.getId(), "バックエンド", month);
        List<SkillDTO> frontendSkills = skillService.getSkillsForCategory(user.getId(), "フロントエンド", month);
        List<SkillDTO> infraSkills = skillService.getSkillsForCategory(user.getId(), "インフラ", month);

        // モデルに追加
        model.addAttribute("currentMonth", month);
        model.addAttribute("backendSkills", backendSkills);
        model.addAttribute("frontendSkills", frontendSkills);
        model.addAttribute("infraSkills", infraSkills);
        model.addAttribute("pastMonths", skillService.getPastThreeMonths());

        return "skills";
    }
}
