package com.spring.springbootapplication.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


// 項目追加用のデータ転送オブジェクト (DTO)
public class SkillNewDTO {

    @NotBlank(message = "項目名は必ず入力してください")
    @Size(max = 50, message = "項目名は50文字以内で入力してください")
    private String skillName;  // 項目名

    @NotNull(message = "学習時間は必ず入力してください")
    @Min(value = 0, message = "学習時間は0以上の数字で入力してください")
    private Integer learningHours;  // 学習時間（0以上の整数）

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "実施月は必ず入力してください")
    private LocalDate recordedMonth;  // 実施月（プルダウンで選択した月）

    @NotNull(message = "カテゴリIDは必須です")
    private Integer categoryId;  // カテゴリID

    private String categoryName;  // カテゴリ名（画面表示用）

    @NotNull(message = "ユーザーIDは必須です")
    private Long userId;  // ログイン中のユーザーID（サーバー側で取得）

    // ===== Getter & Setter =====

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getLearningHours() {
        return learningHours;
    }

    public void setLearningHours(Integer learningHours) {
        this.learningHours = learningHours;
    }

    public LocalDate getRecordedMonth() {
        return recordedMonth;
    }

    public void setRecordedMonth(LocalDate recordedMonth) {
        this.recordedMonth = recordedMonth;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
