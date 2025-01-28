package com.spring.springbootapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.UserService;


@Controller
public class HomeController {  

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String showHomePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        
        if (userDetails == null) {
            return "redirect:/auth/login";  // 未ログインならログインページにリダイレクト
        }

        try {
            // データベースから最新のユーザー情報を取得
            UserEntity userEntity = userService.findUserByEmail(userDetails.getUsername())
                                            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));

            // ユーザー情報を取得してビューに渡す
            model.addAttribute("userName", userEntity.getName());
            model.addAttribute("bio", userEntity.getBio() != null ? userEntity.getBio() : "自己紹介はまだ登録されていません。");
            
            // プロフィール画像の取得（エンドポイント経由）
            String imageUrl = (userEntity.getImage() != null) ? "/profile/image/" + userEntity.getId() : "/images/default-profile.png";
            model.addAttribute("image", imageUrl);

        } catch (Exception e) {
            model.addAttribute("error", "ユーザー情報の取得に失敗しました");
            return "redirect:/auth/login";
        }

        return "home";  // home.html を返す
    }
}
