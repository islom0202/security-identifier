--liquibase formatted sql

--changeset antifraud:001-create-admin-details
CREATE TABLE admin_details (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR NOT NULL UNIQUE,
    fullname VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    role VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--changeset antifraud:002-create-links
CREATE TABLE links (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT NOT NULL,
    generated_link TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_expired BOOLEAN NOT NULL DEFAULT FALSE
);

--changeset antifraud:003-create-user-details
CREATE TABLE user_details (
    id BIGSERIAL PRIMARY KEY,
    user_phone VARCHAR,
    user_ip VARCHAR,
    user_location VARCHAR,
    is_fraud BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--changeset antifraud:004-create-linked-users
CREATE TABLE linked_users (
    id BIGSERIAL PRIMARY KEY,
    link_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    user_code VARCHAR NOT NULL,
    sent_at TIMESTAMP,
    clicked_at TIMESTAMP
);