package com.spring.springbootapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/auth")
public class AuthController {
    
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 登録画面の表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userEntity", new UserEntity()); // 空のUserEntityオブジェクトをフォームに渡す
        return "register";  // register.html（新規登録画面）に遷移する
    }

    // 新規登録処理
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userEntity") UserEntity userEntity,BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            return "register";  // バリデーションエラー時は登録画面に戻る
        }

        // メールアドレスの重複チェック
        if (userService.findUserByEmail(userEntity.getEmail()) != null) {
            model.addAttribute("emailError", "このメールアドレスは既に登録されています");
            return "register";
        }

        userService.registerUser(userEntity);   // ユーザーを登録
        return "redirect:/home";  // 登録完了後はホームページへリダイレクトする
    }

    // ログイン画面を表示する
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // login.html（ログイン画面）へ遷移する
    }
}