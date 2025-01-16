--liquibase formatted sql

--changeset miller87:0002_create_transaction_log_table  dbms:postgresql
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'transaction_log' and table_schema = 'public' and table_type = 'BASE TABLE'

CREATE TABLE transaction_log (
    id SERIAL PRIMARY KEY,
    wallet_id UUID NOT NULL,
    operation_type VARCHAR(100) NOT NULL,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
comment on table transaction_log is 'Transaction log table';

ALTER TABLE transaction_log
ADD CONSTRAINT fk_wallet_id FOREIGN KEY (wallet_id) REFERENCES wallet(wallet_id);