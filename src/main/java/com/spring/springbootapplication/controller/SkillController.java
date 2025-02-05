package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.SkillDTO;
import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.SkillService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.net.URLEncoder;

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

    // 学習時間更新処理
    @PostMapping("/skills/update")
    public String updateLearningHours(
            @RequestParam("skillId") Long skillId,
            @RequestParam("learningHours") int learningHours,
            @RequestParam("month") String month,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("skillName") String skillName,
            @AuthenticationPrincipal UserEntity user,
            Model model
    ) {
        if (user == null) {
            throw new IllegalStateException("ユーザー情報が取得できません。ログイン状態を確認してください。");
        }

        // 学習時間の更新処理
        skillService.updateLearningHours(skillId, learningHours, user.getId());

        // URLエンコード
        String encodedSkillName = URLEncoder.encode(skillName, StandardCharsets.UTF_8);

        // 更新後、モーダル表示用のパラメータを付与してリダイレクト
        return "redirect:/skills?month=" + month + "&categoryId=" + categoryId + "&updateSuccess=" + skillId + "&skillName=" + encodedSkillName;
    }

    // 項目削除処理
    @PostMapping("/skills/delete")
    public String deleteSkill(
            @RequestParam("skillId") Long skillId,
            @RequestParam("skillName") String skillName,
            @RequestParam("month") String month,
            @RequestParam("categoryId") int categoryId,
            @AuthenticationPrincipal UserEntity user
    ) {
        if (user == null) {
            throw new IllegalStateException("ユーザー情報が取得できません。ログイン状態を確認してください。");
        }

        // 項目の削除処理
        skillService.deleteSkill(skillId, user.getId());

        // URLエンコード
        String encodedSkillName = URLEncoder.encode(skillName, StandardCharsets.UTF_8);

        // 更新後、モーダル表示用のパラメータを付与してリダイレクト
        return "redirect:/skills?month=" + month + "&categoryId=" + categoryId + "&deleteSuccess=" + encodedSkillName;
    }

}

