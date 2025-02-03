package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.dto.SkillNewDTO;
import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.SkillNewService;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;  // ログ出力用


@Slf4j
@Controller
@RequestMapping("/skills-new")
public class SkillNewController {

    private final SkillNewService skillNewService;

    public SkillNewController(SkillNewService skillNewService) {
        this.skillNewService = skillNewService;
    }

    // 項目追加ページの表示 (GET)
    @GetMapping
    public String showSkillNewPage(
            @RequestParam("month") String recordedMonth,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("categoryName") String categoryName,
            Model model) {


        // DTOに初期値をセット
        SkillNewDTO skillNewDTO = new SkillNewDTO();

        skillNewDTO.setRecordedMonth(LocalDate.parse(recordedMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        skillNewDTO.setCategoryId(categoryId);
        skillNewDTO.setCategoryName(categoryName);

        // モデルに追加してビューへ
        model.addAttribute("skillNewDTO", skillNewDTO);
        model.addAttribute("recordedMonth", recordedMonth); 
        model.addAttribute("categoryId", categoryId);       
        model.addAttribute("categoryName", categoryName);


        return "skills-new";
    }

    // 項目追加処理 (POST)
    @PostMapping
    public String addSkill(
            @ModelAttribute("skillNewDTO") SkillNewDTO skillNewDTO,
            BindingResult result,
            Model model,
            @AuthenticationPrincipal UserEntity user,
            RedirectAttributes redirectAttributes
    ) {

        //  ユーザーIDをDTOにセット
        skillNewDTO.setUserId(user.getId());


        //  バリデーションエラー時は再表示
        if (result.hasErrors()) {

            log.warn("バリデーションエラー発生: {}", result.getAllErrors());

            model.addAttribute("categoryName", skillNewDTO.getCategoryName());
            return "skills-new";
        }

        //  重複チェック（サービス層で実施）
        boolean isSuccess = skillNewService.saveSkill(skillNewDTO);

        if (!isSuccess) {
            result.rejectValue("skillName", "error.skillName",
                    skillNewDTO.getSkillName() + " は既に登録されています");
            model.addAttribute("categoryName", skillNewDTO.getCategoryName());
            return "skills-new";
        }

        //  成功時 → リダイレクトでモーダル表示用パラメータを付与
        redirectAttributes.addAttribute("month", skillNewDTO.getRecordedMonth().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        redirectAttributes.addAttribute("categoryId", skillNewDTO.getCategoryId());
        redirectAttributes.addAttribute("categoryName", skillNewDTO.getCategoryName());
        redirectAttributes.addAttribute("success", "true");
        redirectAttributes.addAttribute("addedSkillName", skillNewDTO.getSkillName());
        redirectAttributes.addAttribute("addedLearningHours", skillNewDTO.getLearningHours());

        return "redirect:/skills-new";
    }
}
