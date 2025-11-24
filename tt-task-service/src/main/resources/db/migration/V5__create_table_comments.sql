CREATE TABLE comments
(
    id         UUID PRIMARY KEY       DEFAULT uuid_generate_v4(),
    task_id    UUID          NOT NULL,
    author_id  UUID          NOT NULL,
    content    VARCHAR(5000) NOT NULL,
    created_at TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comments_task_id ON comments (task_id);