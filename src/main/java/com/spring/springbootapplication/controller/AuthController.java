
package com.spring.springbootapplication.controller;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // 登録画面の表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userEntity", new UserEntity()); // 空のUserEntityオブジェクトをフォームに渡す
        return "register";  // register.html（新規登録画面）に遷移する
    }

    // 新規登録処理
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userEntity") UserEntity userEntity,BindingResult result,
                                HttpServletRequest request, HttpServletResponse response,Model model) {
        
        if (result.hasErrors()) {
            return "register";  // バリデーションエラー時は登録画面に戻る
        }

        // メールアドレスの重複チェック
        if (userService.findUserByEmail(userEntity.getEmail()).isPresent()) {
            model.addAttribute("emailError", "このメールアドレスは既に登録されています");
            return "register";
        }

        // 平文パスワードを一時変数に保持し、ハッシュ化後に認証処理で使用
        String rawPassword = userEntity.getPassword();  

        // DBに保存するパスワードをハッシュ化し、ユーザー登録
        userService.registerUser(userEntity);  

        // 自動ログイン処理
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userEntity.getEmail(), rawPassword)  // 平文パスワードを渡して認証
        );

        // SecurityContextRepository で認証情報をセッションに明示的に保存
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.saveContext(
            SecurityContextHolder.getContext(), request, response
        );

        // // デバッグ用ログ
        // System.out.println("認証成功: " + authentication.isAuthenticated());
        // System.out.println("認証されたユーザー名: " + authentication.getName());
        // System.out.println("認証された権限: " + authentication.getAuthorities());

        return "redirect:/home";  // 登録完了後はホームページへリダイレクトする
    }

    // ログイン画面を表示する
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";  // login.html （ログイン画面）を表示
    }

}

