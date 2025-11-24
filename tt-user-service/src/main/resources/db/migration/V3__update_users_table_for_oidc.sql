-- Миграция: обновление таблицы users под стандарт OIDC
-- Дата: 2025-03-30 13:04:12

-- Добавление новых колонок
ALTER TABLE users
    ADD COLUMN middle_name VARCHAR(100),
    ADD COLUMN nickname VARCHAR(100),
    ADD COLUMN profile TEXT,
    ADD COLUMN website TEXT,
    ADD COLUMN gender VARCHAR(20),
    ADD COLUMN birthdate DATE,
    ADD COLUMN zoneinfo VARCHAR(50),
    ADD COLUMN phone_number VARCHAR(30),
    ADD COLUMN phone_number_verified BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN address JSONB,
    ADD COLUMN updated_at_oidc TIMESTAMPTZ;

-- Обновление существующих колонок
ALTER TABLE users
    ALTER COLUMN password SET NOT NULL,
    ALTER COLUMN oidc_provider SET NOT NULL,
    ALTER COLUMN oidc_subject SET NOT NULL;
