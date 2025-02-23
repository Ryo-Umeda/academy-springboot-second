package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.UserService;
import com.spring.springbootapplication.dto.ProfileEditDTO;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.ResponseEntity;  
import org.springframework.http.MediaType;

import jakarta.validation.Valid;

import java.io.IOException;


@Controller
@RequestMapping("/profile")
public class ProfileController {
    

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    // プロフィール編集画面の表示
    @GetMapping("/edit")
    public String showEditForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        UserEntity user = userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        // DTO に変換してフォームに渡す
        ProfileEditDTO profileEditDTO = new ProfileEditDTO();
        profileEditDTO.setBio(user.getBio());

        model.addAttribute("profileEditDTO", profileEditDTO);
        return "profile-edit";
    }

    // プロフィールの更新処理
    @PostMapping("/edit")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid @ModelAttribute("profileEditDTO") ProfileEditDTO profileEditDTO,
                                BindingResult result) {
        
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        if (result.hasErrors()) {
            return "profile-edit";
        }

        UserEntity user = userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        // 自己紹介文の更新
        userService.updateUserBio(user.getId(), profileEditDTO.getBio());

        // 画像ファイルの更新処理
        MultipartFile imageFile = profileEditDTO.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes();
                userService.updateUserProfileImage(user.getId(), imageBytes);
            } catch (IOException e) {
                result.rejectValue("image", "error.image", "画像のアップロードに失敗しました。");
                return "profile-edit";
            }
        }
        return "redirect:/home";  
    }

    // プロフィール画像の取得
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id) {

        byte[] image = userService.getUserProfileImage(id);

        if (image == null || image.length == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }
}