package com.spring.springbootapplication.controller;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.UserService;

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
            return "redirect:/auth/login";  // 認証されていない場合ログインページへ
        }

        UserEntity user = userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        model.addAttribute("userEntity", user);
        return "profile-edit";
    }

    // プロフィールの更新処理
    @PostMapping("/edit")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid @ModelAttribute("userEntity") UserEntity updatedUser,
                                BindingResult result,
                                @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        if (result.hasErrors()) {
            return "profile-edit";
        }

        UserEntity user = userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        // 自己紹介文の更新
        userService.updateUserBio(user.getId(), updatedUser.getBio());

        // 画像ファイルが選択された場合の処理
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes();
                userService.updateUserProfileImage(user.getId(), imageBytes);
            } catch (Exception e) {
                result.rejectValue("bio", "error.image", "画像のアップロードに失敗しました。");
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