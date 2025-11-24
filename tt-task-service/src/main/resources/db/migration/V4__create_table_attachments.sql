CREATE TABLE attachments
(
    id           UUID PRIMARY KEY       DEFAULT uuid_generate_v4(),
    task_id      UUID          NOT NULL,
    file_name    VARCHAR(255)  NOT NULL,
    file_url     VARCHAR(1024) NOT NULL,
    file_size    BIGINT,
    content_type VARCHAR(255),
    uploaded_by  UUID          NOT NULL,
    uploaded_at  TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_attachments_task_id ON attachments (task_id);