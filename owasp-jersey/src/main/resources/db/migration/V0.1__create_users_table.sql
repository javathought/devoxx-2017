-- V0.2__create_users_table.sql

CREATE TABLE devoxx_tia.users (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  uuid        BINARY(16) UNIQUE,
  name        VARCHAR(25),
  password    VARCHAR(64)
);