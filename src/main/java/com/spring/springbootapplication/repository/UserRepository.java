package com.spring.springbootapplication.repository;

import com.spring.springbootapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


@Repository
public interface UserRepository  extends JpaRepository <UserEntity, Long> {
    
    // ユーザーをメールアドレスで検索（存在しない場合を考慮しOptional型を使用）
    Optional<UserEntity> findByEmail(String email);

    // 自己紹介文を更新するメソッド
    @Modifying
    @Query("UPDATE UserEntity u SET u.bio = :bio, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateBio(Long id, String bio);

    // プロフィール画像を更新するメソッド（画像はバイナリデータとして保存）
    @Modifying
    @Query("UPDATE UserEntity u SET u.image = :image, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateProfileImage(@Param("id") Long id, @Param("image") byte[] image);

    // ユーザーのプロフィール画像を取得（画像のバイナリデータを取得）
    @Query("SELECT u.image FROM UserEntity u WHERE u.id = :id")
    byte[] findUserImageById(@Param("id") Long id);
}
