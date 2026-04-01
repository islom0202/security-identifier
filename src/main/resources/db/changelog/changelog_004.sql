--liquibase formatted sql

--changeset antifraud:006-add_admin_id_column

ALTER TABLE user_details
ADD COLUMN admin_id BIGINT;