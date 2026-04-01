--liquibase formatted sql

--changeset antifraud:005-update-user-location-types

ALTER TABLE user_details
ADD COLUMN latitude DOUBLE PRECISION,
ADD COLUMN longitude DOUBLE PRECISION,
ADD COLUMN ip_latitude DOUBLE PRECISION,
ADD COLUMN ip_longitude DOUBLE PRECISION,
ADD COLUMN log_message VARCHAR;