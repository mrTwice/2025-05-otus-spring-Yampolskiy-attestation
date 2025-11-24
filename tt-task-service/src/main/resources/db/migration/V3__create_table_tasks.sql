CREATE TABLE tasks
(
    id           UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    title        VARCHAR(255) NOT NULL,
    description  VARCHAR(5000),

    type_id      UUID         NOT NULL,
    type_code    VARCHAR(255) NOT NULL,

    status_id    UUID         NOT NULL,
    status_code  VARCHAR(255) NOT NULL,

    creator_id   UUID         NOT NULL,
    assignee_id  UUID,
    due_date     TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    deleted_at   TIMESTAMPTZ,

    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    parent_id    UUID,

    CONSTRAINT fk_tasks_parent FOREIGN KEY (parent_id) REFERENCES tasks (id)
);

CREATE INDEX idx_tasks_creator_id ON tasks (creator_id);
CREATE INDEX idx_tasks_assignee_id ON tasks (assignee_id);
CREATE INDEX idx_tasks_status_code ON tasks (status_code);
CREATE INDEX idx_tasks_type_code ON tasks (type_code);
CREATE INDEX idx_tasks_created_at ON tasks (created_at);