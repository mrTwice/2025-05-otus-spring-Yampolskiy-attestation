-- Создаем таблицу для хранения ключей подписи (JWK)
CREATE TABLE jwk_keys
(
    kid        UUID PRIMARY KEY,
    key_data   TEXT                     NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_active  BOOLEAN                  NOT NULL DEFAULT TRUE
);

-- Индекс для ускорения поиска активных ключей (если планируется ротация)
CREATE INDEX idx_jwk_keys_active ON jwk_keys (is_active);

-- Комментарии к таблице (опционально)
COMMENT ON TABLE jwk_keys IS 'Хранилище ключей подписи JWT (JWK) для OAuth2 сервера';
COMMENT ON COLUMN jwk_keys.kid IS 'Идентификатор ключа (Key ID)';
COMMENT ON COLUMN jwk_keys.key_data IS 'JSON-данные ключа в формате JWK';
COMMENT ON COLUMN jwk_keys.created_at IS 'Время создания записи';
COMMENT ON COLUMN jwk_keys.is_active IS 'Активен ли ключ (для ротации)';