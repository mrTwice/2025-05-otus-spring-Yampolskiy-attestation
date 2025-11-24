ALTER TABLE task_priorities
    ADD COLUMN deleted_at TIMESTAMP;
ALTER TABLE task_types ADD COLUMN deleted_at TIMESTAMP;
