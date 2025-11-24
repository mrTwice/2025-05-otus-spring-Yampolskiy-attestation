ALTER TABLE tasks
    ADD COLUMN priority_id UUID;
CREATE TABLE task_priorities
(
    id          UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    code        VARCHAR(50)  NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    sort_order  INTEGER,
    color       VARCHAR(10),
    is_default  BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE tasks
    ADD CONSTRAINT fk_task_priority FOREIGN KEY (priority_id) REFERENCES task_priorities (id);
ALTER TABLE tasks
    ADD COLUMN priority_code VARCHAR(255) NOT NULL;