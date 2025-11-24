CREATE TABLE task_types
(
    id          UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    code        VARCHAR(255) NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    is_default  BOOLEAN      NOT NULL DEFAULT FALSE,
    sort_order  INTEGER,
    icon        VARCHAR(100),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);