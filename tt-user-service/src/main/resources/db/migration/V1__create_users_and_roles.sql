-- Подключаем расширение для работы с UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Таблица пользователей
CREATE TABLE users
(
    id             UUID PRIMARY KEY,
    username       VARCHAR(50) UNIQUE  NOT NULL,
    password       TEXT        DEFAULT NULL,
    email          VARCHAR(100) UNIQUE NOT NULL,
    is_active      BOOLEAN     DEFAULT true,
    first_name     VARCHAR(100),
    last_name      VARCHAR(100),
    picture_url    TEXT,
    locale         VARCHAR(10),
    email_verified BOOLEAN     DEFAULT false,

    -- OIDC (если пользователь входит через Google, Keycloak и т. д.)
    oidc_provider  VARCHAR(50) DEFAULT NULL,     -- Google, GitHub, Keycloak
    oidc_subject   VARCHAR(50) DEFAULT NULL, -- Уникальный ID пользователя в OIDC

    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP           NULL
);

-- Таблица ролей
CREATE TABLE roles
(
    id   UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Таблица прав доступа
CREATE TABLE permissions
(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description TEXT               NOT NULL
);

-- Таблица связи пользователей и ролей
CREATE TABLE user_roles
(
    user_id UUID REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    role_id UUID REFERENCES roles (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Таблица связи ролей и прав доступа
CREATE TABLE role_permissions
(
    role_id       UUID REFERENCES roles (id) ON DELETE CASCADE ON UPDATE CASCADE,
    permission_id UUID REFERENCES permissions (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Индексы для быстрого поиска
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_oidc_subject ON users (oidc_subject);
CREATE INDEX idx_roles_name ON roles (name);
CREATE INDEX idx_permissions_name ON permissions (name);
