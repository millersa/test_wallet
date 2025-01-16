--liquibase formatted sql

--changeset miller87:0003_insert_value_to_wallet_table  dbms:postgresql
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from public.wallet where wallet_id = 'a079161c-9b68-4c2f-b8bb-a5dee27e0a43';

insert into wallet values ('a079161c-9b68-4c2f-b8bb-a5dee27e0a43', 500);