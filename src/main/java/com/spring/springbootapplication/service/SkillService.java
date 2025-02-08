package com.spring.springbootapplication.service;

import com.spring.springbootapplication.dto.SkillDTO;
import com.spring.springbootapplication.entity.LearningDataEntity;
import com.spring.springbootapplication.repository.LearningDataRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Map;

@Service
public class SkillService {

    private final LearningDataRepository learningDataRepository;

    public SkillService(LearningDataRepository learningDataRepository) {
        this.learningDataRepository = learningDataRepository;
    }

    // ユーザーIDを引数として受け取る
    public List<SkillDTO> getSkillsForCategory(Long userId, String categoryName, String month) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate recordedMonth = LocalDate.parse(month + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // DBからデータを取得
        List<LearningDataEntity> learningDataList = learningDataRepository
            .findByUser_IdAndCategory_CategoryNameAndRecordedMonth(
                userId,  
                categoryName,
                recordedMonth,
                Sort.by(Sort.Direction.ASC, "createdAt")
            );

        // DTO に変換
        return learningDataList.stream()
                .map(data -> new SkillDTO(
                        data.getCategory().getCategoryName(),
                        data.getSkillName(),
                        data.getLearningHours(),
                        data.getRecordedMonth().format(formatter),
                        data.getId().longValue()
                ))
                .collect(Collectors.toList());
    }

    // 学習時間の更新処理
    @Transactional
    public void updateLearningHours(Long skillId, int learningHours, Long userId) {
        Optional<LearningDataEntity> skillOpt = learningDataRepository.findByIdAndUser_Id(skillId, userId);

        if (skillOpt.isPresent()) {
            LearningDataEntity skill = skillOpt.get();
            skill.setLearningHours(learningHours);
            learningDataRepository.save(skill);  // 更新処理
        } else {
            throw new IllegalArgumentException("指定されたスキルが存在しないか、権限がありません。");
        }
    }

    // スキル削除処理
    @Transactional
    public void deleteSkill(Long skillId, Long userId) {
        Optional<LearningDataEntity> skillOpt = learningDataRepository.findByIdAndUser_Id(skillId, userId);

        if (skillOpt.isPresent()) {
        // ユーザーIDも条件に含めて削除
            learningDataRepository.deleteByIdAndUser_Id(skillId, userId);
        } else {
        throw new IllegalArgumentException("指定されたスキルが存在しないか、権限がありません。");
        }
    }

    // 過去3ヶ月分の月リストを取得する
    public List<String> getPastThreeMonths() {
        DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("M月");

        return List.of(
                LocalDate.now().format(dbFormat) + "," + LocalDate.now().format(displayFormat),
                LocalDate.now().minusMonths(1).format(dbFormat) + "," + LocalDate.now().minusMonths(1).format(displayFormat),
                LocalDate.now().minusMonths(2).format(dbFormat) + "," + LocalDate.now().minusMonths(2).format(displayFormat)
        );
    }

    // スキルチャートデータ取得
    public Map<String, Map<String, Integer>> getSkillChartData(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate threeMonthsAgo = now.minusMonths(2).withDayOfMonth(1); // 先々月の1日
        LocalDate currentMonth = now.withDayOfMonth(1); // 今月の1日

        List<LearningDataEntity> dataList =
                learningDataRepository.findByUser_IdAndRecordedMonthBetween(userId, threeMonthsAgo, currentMonth);

        Map<String, Map<String, Integer>> skillChartData = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (LearningDataEntity data : dataList) {
            String month = data.getRecordedMonth().format(formatter); // "YYYY-MM"
            String category = data.getCategory().getCategoryName();

            skillChartData.putIfAbsent(month, new HashMap<>());
            skillChartData.get(month).put(category,
                    skillChartData.get(month).getOrDefault(category, 0) + data.getLearningHours());
        }

        // カテゴリの学習時間がない場合のデフォルト処理
        for (String month : List.of(threeMonthsAgo.format(formatter), now.minusMonths(1).format(formatter), now.format(formatter))) {
            skillChartData.putIfAbsent(month, new HashMap<>());
            skillChartData.get(month).putIfAbsent("バックエンド", 0);
            skillChartData.get(month).putIfAbsent("フロントエンド", 0);
            skillChartData.get(month).putIfAbsent("インフラ", 0);
        }

        return skillChartData;
    }
}
