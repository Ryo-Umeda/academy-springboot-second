-- 画像カラムのデータ型修正用マイグレーションスクリプト
-- PostgreSQLのバイナリデータ型の正しい定義を適用
ALTER TABLE users 
ALTER COLUMN image TYPE BYTEA 
USING image::BYTEA;

-- Flyway用のメタデータ保持のため、マイグレーションの履歴を残す
COMMENT ON COLUMN users.image IS 'プロフィール画像をバイナリデータで保存 (BYTEA型)';