--liquibase formatted sql

--changeset antifraud:002-add-new-column
alter table user_details add column user_device_id varchar;