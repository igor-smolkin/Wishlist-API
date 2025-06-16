--liquibase formatted sql

--changeset ataraxii:1.1
ALTER TABLE wishlist
    ADD COLUMN shared boolean NOT NULL default false;