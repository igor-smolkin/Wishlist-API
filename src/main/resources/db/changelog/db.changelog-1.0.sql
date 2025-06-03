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

CREATE TABLE IF NOT EXISTS wishlist
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name    varchar(32) NOT NULL,
    user_id uuid references users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item_wishlist
(
    id        BIGSERIAL PRIMARY KEY,
    item_id   uuid references item (id) ON DELETE CASCADE,
    wishlist_id uuid references wishlist ON DELETE CASCADE,
    UNIQUE (item_id, wishlist_id)
);

CREATE EXTENSION IF NOT EXISTS "pgcrypto";