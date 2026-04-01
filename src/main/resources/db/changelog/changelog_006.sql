--liquibase formatted sql

--changeset antifraud:009-add_admin_status_column

ALTER TABLE admin_details
ADD COLUMN is_active BOOLEAN DEFAULT TRUE;

alter table user_details
drop column is_active;