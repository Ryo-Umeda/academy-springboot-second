package com.spring.springbootapplication.repository;

import com.spring.springbootapplication.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    // カテゴリ名からカテゴリ情報を取得
    Optional<CategoryEntity> findByCategoryName(String categoryName);
}
