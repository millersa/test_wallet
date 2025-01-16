--liquibase formatted sql

--changeset miller87:0001_create_wallet_table  dbms:postgresql
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'wallet' and table_schema = 'public' and table_type = 'BASE TABLE'

CREATE TABLE wallet (
wallet_id UUID PRIMARY KEY,
balance BIGINT NOT NULL DEFAULT 0
);
comment on table wallet is 'wallet table';