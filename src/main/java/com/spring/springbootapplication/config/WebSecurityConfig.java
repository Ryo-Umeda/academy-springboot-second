package com.spring.springbootapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 認証の設定: 記載したURLパターンを許可
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/auth/register", "/auth/login","/css/**","/js/*","/images/*").permitAll()  // 認証不要のリソース
                .anyRequest().authenticated()  // それ以外は認証が必要
            )
            // フォームログインの設定
            .formLogin(form -> form
                .loginPage("/auth/login")
                .usernameParameter("email")  
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)  // ログイン成功時の遷移先
                .failureUrl("/auth/login?error=true")  // ログイン失敗時の遷移先
                .permitAll()  // ログインページは誰でもアクセス可能
            )
            // ログアウトの設定
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")  // ログアウト成功時の遷移先
                .invalidateHttpSession(true)  // セッションの無効化
                .deleteCookies("JSESSIONID")  // Cookie削除
                .permitAll()  // ログアウトは誰でも実行可能
            )  

            // // セッション管理
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))  // セッションを必要時に新規作成する
            // CSRF設定
            .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/register", "/auth/login", "/auth/logout", "/profile/edit"));  // CSRF保護を無効化

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // パスワードのハッシュ化用エンコーダーであるBCryptPasswordEncoder を Spring のコンテキストに Bean として登録
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // 認証マネージャーの取得する
    }

}