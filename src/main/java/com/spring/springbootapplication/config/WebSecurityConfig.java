package com.spring.springbootapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()  // すべてのリクエストを許可
            )
            .csrf(csrf -> csrf.disable())  // CSRFを無効化
            .httpBasic(httpBasic -> httpBasic.disable())  // Basic認証を無効化
            .formLogin(form -> form.disable());  // フォームログイン無効化（今後改変予定）

        return http.build();
    }
}