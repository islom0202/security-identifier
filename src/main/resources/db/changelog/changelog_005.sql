--liquibase formatted sql

--changeset antifraud:008-add_admin_status_column

ALTER TABLE user_details
ADD COLUMN is_active BOOLEAN DEFAULT TRUE;