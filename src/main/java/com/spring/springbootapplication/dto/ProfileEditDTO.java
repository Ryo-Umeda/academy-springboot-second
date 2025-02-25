package com.spring.springbootapplication.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Size;

public class ProfileEditDTO {
    
    @Size(min = 50, max = 200, message = "自己紹介は50文字以上200文字以下で入力してください")
    private String bio;  // 自己紹介文

    private MultipartFile image;  // アップロードされるプロフィール画像

    // 画像のバリデーション（ファイルサイズ5MB以下）
    public boolean isValidImageSize() {
        return (image == null || image.getSize() <= 5 * 1024 * 1024);
    }

    // Getter & Setter
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

}
