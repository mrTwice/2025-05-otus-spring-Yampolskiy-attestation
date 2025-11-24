![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-success)
![License](https://img.shields.io/badge/license-MIT-green)

# OTUS Spring Framework Attestation

Микросервисное приложение, разработанное в рамках курса **Разработчик на Spring Framework**. Проект включает управление пользователями, задачами и авторизацию на базе современных технологий: **Spring Boot, OAuth2, PostgreSQL, Docker и др.**

---

## 📚 Содержание

- [Быстрый старт](#-быстрый-старт)
- [Общий стек технологий](#-общий-стек-технологий)
- [Модули](#-модули)
  - [tt-config-server](#tt-config-server)
  - [tt-api-gateway](#tt-api-gateway)
  - [tt-oauth2-authorization-server](#-tt-oauth2-authorization-server)
  - [tt-user-service](#-tt-user-service)
  - [tt-task-service](#-tt-task-service)
- [Инициализация данных при запуске (CommandLineRunner)](#-инициализация-данных-при-запуске-commandlinerunner)
- [OAuth2-клиенты, регистрируемые сервером авторизации](#-oauth2-клиенты-регистрируемые-сервером-авторизации)
  - [1. internal-service-client (⚙системный клиент)](#1-internal-service-client--системный-клиент)
  - [2. swagger-client-user-service (Swagger-клиент для user-service)](#2-swagger-client-user-service--swagger-клиент-для-user-service)
  - [3. swagger-client-task-service (Swagger-клиент для task-service)](#3-swagger-client-task-service--swagger-клиент-для-task-service)
- [tt-task-service — начальные данные](#-tt-task-service--начальные-данные)
  - [1. TaskTypeInitializer](#1-tasktypeinitializer)
  - [2. TaskPriorityInitializer](#2-taskpriorityinitializer)
  - [3. TaskStatusInitializer](#3-taskstatusinitializer)
  - [Swagger UI — Task Service](http://localhost:8085/task/swagger-ui/index.html)
- [tt-user-service — начальные данные](#-tt-user-service--начальные-данные)
  - [1. RolePermissionInitializer](#1-rolepermissioninitializer)
  - [2. TestUserInitializer](#2-testuserinitializer)
  - [Swagger UI — User Service](http://localhost:8085/user/swagger-ui/index.html)
- [Лицензия](#-лицензия)

---

## Быстрый старт

```bash
cd development
docker-compose up -d
```

---

## Общий стек технологий

- Java 21
- Spring Boot 3+
- Spring Security / OAuth2 / Spring Authorization Server
- Spring Cloud Config
- Spring Cloud Gateway
- MapStruct, Lombok
- JPA / Hibernate
- PostgreSQL
- Flyway
- Gradle
- OpenID Connect (OIDC)
- JWT (JSON Web Token)
- Docker Compose
- OpenAPI (Swagger)
- AWS SDK / S3 (для вложений, пока не реализовано)

---

## Модули

### tt-config-server

Получение конфигурации для сервисов

### tt-api-gateway

Единая точка входа для внешних клиентов.

### `tt-oauth2-authorization-server`

Сервер авторизации по OAuth 2.1 и OpenID Connect.
Отвечает за выпуск токенов, валидацию, introspection и регистрацию клиентов и пользователей.

#### 🔧 Основные функции

- Authorization Code Flow + PKCE
- ID и Access токены (JWT)
- OpenID Connect (`userinfo`, `scope=openid`)
- JWK-ключи с ротацией
- Интеграция с `tt-user-service`
- REST API для introspection и client registration
---


### `tt-user-service`

Микросервис управления пользователями, ролями и правами доступа.

#### 🔧 Основные функции

- CRUD для пользователей, ролей, прав
- Регистрация, email-валидация, бизнес-логика
- JWT и Spring Security-интеграция
- Внутренний API (`InternalUserController`)
- Интеграция с `auth-server`

---

### `tt-task-service`

Микросервис для работы с задачами, приоритетами, комментариями и вложениями.

#### 🔧 Основные функции

- Полный CRUD задач и вложений
- Комментарии и типы задач
- Безопасность и валидация на уровне бизнес-логики
- Интеграция с OAuth2 и файловым хранилищем (S3 или совместимым)

---

# Инициализация данных при запуске (CommandLineRunner)

Микросервисы проекта автоматически заполняют базу данных определенным начальными значениями при запуске. Это сделано для того, чтобы обеспечить:
- полноценную работу авторизации и безопасности;
- удобство локального тестирования и разработки;
- демонстрационные сценарии (например, Swagger OAuth).

---

## OAuth2-клиенты, регистрируемые сервером авторизации

### 1. `internal-service-client` (⚙системный клиент)
- **Тип:** machine-to-machine
- **grant type:** `client_credentials`
- **authentication methods:** `client_secret_basic`, `client_secret_post`
- **scopes:**
  - `user:view`
  - `user:manage`
  - `user:delete`
- **Назначение:** используется **самим сервером авторизации (SAS)** и другими микросервисами для внутренних запросов к `tt-user-service`

### 2. `swagger-client-user-service` (Swagger-клиент для user-service)
- **grant types:** `authorization_code`, `refresh_token`
- **redirect URI:** `/user/swagger-ui/oauth2-redirect.html`
- **scopes:**
  - `openid`, `profile`, `offline_access`
  - `user:*`, `role:*`, `permission:*`
- **Назначение:** используется Swagger UI в `tt-user-service` для авторизации и тестирования защищённых эндпоинтов

### 3. `swagger-client-task-service` (Swagger-клиент для task-service)
- **grant types:** `authorization_code`, `refresh_token`
- **redirect URI:** `/task/swagger-ui/oauth2-redirect.html`
- **scopes:**
  - `openid`, `profile`, `offline_access`
  - `task:*`, `comment:*`, `attachment:*`, `task-type:*`, `task-status:*`, `task-priority:*`
- **Назначение:** авторизация через Swagger UI в `tt-task-service`, доступ ко всем функциональностям задач

---

## tt-task-service — начальные данные
### 1. `TaskTypeInitializer`
Добавляет **типы задач**:

| Code      | Title    | Description                  | Emoji |
|-----------|----------|------------------------------|--------|
| `bug`     | Bug      | A bug or defect              | 🐞     |
| `feature` | Feature  | New feature or improvement   | ✨     |
| `task`    | Task     | General task or action       | 📝     |


### 2. `TaskPriorityInitializer`
Добавляет **приоритеты задач**:

| Code     | Title   | Urgency         | Order | Color    | Default |
|----------|---------|-----------------|--------|----------|---------|
| `high`   | High    | High urgency    | 1      | `#FF3B30` | ✅ Да   |
| `medium` | Medium  | Normal urgency  | 2      | `#FF9500` | ❌ Нет  |
| `low`    | Low     | Low urgency     | 3      | `#34C759` | ❌ Нет  |


### 3. `TaskStatusInitializer`
Добавляет **статусы задач**:

| Code         | Title       | Description         | Final | Default | Order | Color     |
|--------------|-------------|---------------------|--------|---------|--------|-----------|
| `todo`       | To Do       | Task not started    | ❌     | ✅       | 1      | `#D3D3D3` |
| `in_progress`| In Progress | Work ongoing        | ❌     | ❌       | 2      | `#87CEEB` |
| `review`     | Review      | Awaiting review     | ❌     | ❌       | 3      | `#FFD700` |
| `done`       | Done        | Task completed      | ✅     | ❌       | 4      | `#32CD32` |
| `cancelled`  | Cancelled   | Task was cancelled  | ✅     | ❌       | 5      | `#A9A9A9` |

---

## `tt-user-service` — начальные данные

### 1. `RolePermissionInitializer`
Создаёт 4 базовые роли с соответствующими правами (permissions):

#### Роли и права:

| Роль     | Доступ к | Комментарии | Вложения | Типы/Статусы/Приоритеты | Пользователи | Роли/Права | Проекты |
|----------|----------|-------------|----------|--------------------------|--------------|------------|---------|
| `ADMIN`  | ✅ все    | ✅ все       | ✅ все    | ✅ все                   | ✅ все       | ✅ все     | –       |
| `MANAGER`| ✅ все    | ✅ все       | ✅ все    | ✅ чтение/обновление     | 👀/✏️        | 👀         | –       |
| `USER`   | ✏️        | ✏️/👀        | ✏️/👀     | 👀 только                | 👀           | –          | –       |
| `GUEST`  | 👀        | 👀           | 👀       | 👀 только                | –            | –          | 👀       |

> ✅ — создать, прочитать, обновить, удалить  
> ✏️ — создать, прочитать, обновить  
> 👀 — только просмотр

### 2. `TestUserInitializer`
Создаёт одного тестового пользователя:

| Поле            | Значение                       |
|-----------------|--------------------------------|
| **username**    | `test-user`                    |
| **email**       | `testuser@example.com`         |
| **роль**        | `ADMIN`                        |
| **пароль**      | `password` (в bcrypt-хэше)     |
| **email подтверждён** | ✅                        |
| **OIDC данные** | `oidcProvider=local`           |
| **Профиль**     | `https://example.com/profile/test-user` |
| **Avatar**      | `https://example.com/avatar.png` |
| **Address**     | `123 Main St, Springfield, USA` |

> Можно авторизоваться в Swagger через OAuth2 логин  `test-user` пароль `password`.

---

## Лицензия

MIT License.

---
