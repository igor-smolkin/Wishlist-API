--liquibase formatted sql

--changeset ataraxii:1
CREATE TABLE IF NOT EXISTS item
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name    varchar(32)  NOT NULL,
    url     varchar(256) NOT NULL,
    user_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS wishlist
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name    varchar(32) NOT NULL,
    user_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS item_wishlist
(
    id        BIGSERIAL PRIMARY KEY,
    item_id   uuid references item (id) ON DELETE CASCADE,
    wishlist_id uuid references wishlist ON DELETE CASCADE,
    UNIQUE (item_id, wishlist_id)
);

CREATE EXTENSION IF NOT EXISTS "pgcrypto";