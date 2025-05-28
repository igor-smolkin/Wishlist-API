--liquibase formatted sql

--changeset ataraxii:1
CREATE TABLE IF NOT EXISTS users
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username   varchar(32) NOT NULL UNIQUE,
    password   varchar(60) NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS session
(
    session_id uuid PRIMARY KEY NOT NULL UNIQUE,
    user_id    uuid references users (id) ON DELETE CASCADE,
    created_at timestamptz      NOT NULL DEFAULT now(),
    expired_at timestamptz      NOT NULL
);

CREATE TABLE IF NOT EXISTS item
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name    varchar(32)  NOT NULL,
    url     varchar(256) NOT NULL,
    user_id uuid references users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS folder
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name    varchar(32) NOT NULL,
    user_id uuid references users (id) ON DELETE CASCADE,
    UNIQUE (user_id, name)
);

CREATE TABLE IF NOT EXISTS item_folder
(
    id        BIGSERIAL PRIMARY KEY,
    item_id   uuid references item (id) ON DELETE CASCADE,
    folder_id uuid references folder ON DELETE CASCADE,
    UNIQUE (item_id, folder_id)
);

CREATE EXTENSION IF NOT EXISTS "pgcrypto";