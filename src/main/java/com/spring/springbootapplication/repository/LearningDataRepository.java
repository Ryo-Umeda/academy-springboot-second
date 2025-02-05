package com.spring.springbootapplication.repository;

import com.spring.springbootapplication.entity.LearningDataEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface LearningDataRepository extends JpaRepository<LearningDataEntity, Integer> {

        // 学習データの重複チェック 同一ユーザー、月、スキル名が既に存在するか確認
        Optional<LearningDataEntity> findByUser_IdAndRecordedMonthAndSkillName(
                Long userId,
                LocalDate recordedMonth,
                String skillName
        );

        // 指定されたカテゴリ名と月の学習データを取得
        List<LearningDataEntity> findByUser_IdAndCategory_CategoryNameAndRecordedMonth(
                Long userId,
                String categoryName,
                LocalDate recordedMonth,
                Sort sort
        );

        // 学習時間の更新
        Optional<LearningDataEntity> findByIdAndUser_Id(Long id, Long userId);

        // 削除処理
        void deleteByIdAndUser_Id(Long id, Long userId);
}
