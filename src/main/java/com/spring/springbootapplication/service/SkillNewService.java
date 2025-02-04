package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dto.SkillNewDTO;
import com.spring.springbootapplication.entity.CategoryEntity;
import com.spring.springbootapplication.entity.LearningDataEntity;
import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.repository.CategoryRepository;
import com.spring.springbootapplication.repository.LearningDataRepository;
import com.spring.springbootapplication.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// ログ出力用
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SkillNewService {

    private final LearningDataRepository learningDataRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public SkillNewService(LearningDataRepository learningDataRepository,
                            CategoryRepository categoryRepository,
                            UserRepository userRepository) {
        this.learningDataRepository = learningDataRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // スキルの保存処理
    @Transactional
    public boolean saveSkill(SkillNewDTO skillNewDTO) {

        // 既存のスキルが存在するかチェック
        Optional<LearningDataEntity> existingSkill = learningDataRepository
                    .findByUser_IdAndRecordedMonthAndSkillName(
                        skillNewDTO.getUserId(),
                        skillNewDTO.getRecordedMonth(),
                        skillNewDTO.getSkillName()
                );

        if (existingSkill.isPresent()) {

            // 既に同じスキルが存在する場合は登録しない
            return false;
        }

        // ユーザー情報とカテゴリ情報の取得 
        UserEntity user = userRepository.findById(skillNewDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));

        CategoryEntity category = categoryRepository.findById(skillNewDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("カテゴリーが存在しません"));

        // 新しい学習データの作成 
        LearningDataEntity newSkill = new LearningDataEntity();
        newSkill.setUser(user);
        newSkill.setCategory(category);
        newSkill.setSkillName(skillNewDTO.getSkillName());
        newSkill.setLearningHours(skillNewDTO.getLearningHours());
        newSkill.setRecordedMonth(skillNewDTO.getRecordedMonth());

        // データベースに保存
        learningDataRepository.save(newSkill);

        return true;
    }
}
