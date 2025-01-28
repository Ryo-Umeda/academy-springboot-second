package com.spring.springbootapplication.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.springbootapplication.entity.UserEntity;
import com.spring.springbootapplication.repository.UserRepository;

import jakarta.validation.Valid;

// デバック用
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class UserService implements UserDetailsService{
    
    // デバック用
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;

    // UserRepository,PasswordEncoderを注入
    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;  // パスワードのハッシュ化用
    }

    // // ユーザー登録処理
    public void registerUser(@Valid UserEntity userEntity) {
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        
        // デバッグ用: 登録開始時の情報
        System.out.println("ユーザー登録処理開始...");
        System.out.println("登録ユーザー: " + userEntity.getEmail());
        System.out.println("画像データ型詳細 (登録前): " + (userEntity.getImage() != null ? userEntity.getImage().getClass().getCanonicalName() : "null"));

        // デフォルト画像の適用（null または空の場合にセット）
        if (userEntity.getImage() == null || userEntity.getImage().length == 0) {
            try (InputStream is = getClass().getResourceAsStream("/static/images/default-profile.png")) {
                if (is != null) {
                    byte[] defaultImage = is.readAllBytes();
                    userEntity.setImage(defaultImage);
                    
                    // デバッグ用: デフォルト画像適用確認
                    System.out.println("デフォルト画像が正常に適用されました。サイズ：" + defaultImage.length + " bytes");
                    System.out.println("画像データ型詳細: " + userEntity.getImage().getClass().getCanonicalName());
                } else {
                    throw new RuntimeException("デフォルト画像が見つかりません");
                }
            } catch (IOException e) {
                throw new RuntimeException("デフォルト画像の読み込みに失敗しました", e);
            }
        } else {
            System.out.println("ユーザーがカスタム画像をアップロード済み");
        }

        // デバッグ: 登録直前のデータ確認
        System.out.println("画像データ型詳細: " + (userEntity.getImage() != null ? userEntity.getImage().getClass().getCanonicalName() : "null"));
        System.out.println("画像データサイズ: " + (userEntity.getImage() != null ? userEntity.getImage().length : "null"));

        try {
            userRepository.save(userEntity);
            System.out.println("ユーザー登録完了: " + userEntity.getEmail());
        } catch (Exception e) {
            System.err.println("ユーザー登録エラー: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("ユーザー登録に失敗しました", e);
        }
    }


    // メールアドレスによるユーザー検索
    public Optional<UserEntity> findUserByEmail(String email) {

        logger.info("ユーザー検索処理: {}", email);

        return userRepository.findByEmail(email);
    }

    // 自己紹介の更新処理
    @Transactional
    public void updateUserBio(Long userId, String bio) {

        logger.info("DBに自己紹介文を更新: ユーザーID {}, 自己紹介: {}", userId, bio);

        userRepository.updateBio(userId, bio);
    }

    // プロフィール画像の更新処理
    @Transactional
    public void updateUserProfileImage(Long userId, byte[] imageBytes) {

        logger.info("DBにプロフィール画像を更新: ユーザーID {}", userId);

    if (imageBytes.length > 0) {
        userRepository.updateProfileImage(userId, imageBytes);

        logger.info("プロフィール画像の更新成功、サイズ: {} bytes", imageBytes.length);

    } else {

        logger.warn("空の画像データが渡されました");

        throw new IllegalArgumentException("空の画像データです");
    }
}


    // ユーザーのプロフィール画像を取得
    public byte[] getUserProfileImage(Long userId) {
        logger.info("プロフィール画像取得処理: ユーザーID {}", userId);
        byte[] image = userRepository.findUserImageById(userId);
        if (image != null) {
            logger.info("画像取得成功、サイズ: {} bytes", image.length);
        } else {
            logger.warn("画像が見つかりません。ユーザーID {}", userId);
        }
        return image;
    }
    
    // UserDetailsService インターフェースの実装
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);  // Optional でDBからユーザー情報受け取る

        UserEntity userEntity = optionalUserEntity
        .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません：" + email));

        // 権限を付与する
        return new User(
            userEntity.getEmail(),      // ユーザー名（メールアドレス）
            userEntity.getPassword(),   // ハッシュ化されたパスワード
            Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER") // ROLEの権限を設定
            )
        );
    }
}
