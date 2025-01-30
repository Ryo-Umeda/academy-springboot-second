package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.SkillDTO;
import com.spring.springbootapplication.service.SkillService;
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
    public String showSkills(@RequestParam(value = "month", required = false) String month,Model model) {

        // デバック用
        System.out.println("取得した month パラメータ: " + month);

        // デフォルトは当月
        if (month == null || month.isEmpty()) {
            month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        // ダミーデータの取得
        List<SkillDTO> backendSkills = skillService.getSkillsForCategory("バックエンド", month);
        List<SkillDTO> frontendSkills = skillService.getSkillsForCategory("フロントエンド", month);
        List<SkillDTO> infraSkills = skillService.getSkillsForCategory("インフラ", month);

        // モデルに追加
        model.addAttribute("currentMonth", month);
        model.addAttribute("backendSkills", backendSkills);
        model.addAttribute("frontendSkills", frontendSkills);
        model.addAttribute("infraSkills", infraSkills);
        model.addAttribute("pastMonths", skillService.getPastThreeMonths());

        return "skills";
    }
}
