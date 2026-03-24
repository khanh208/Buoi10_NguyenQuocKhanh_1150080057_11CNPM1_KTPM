create table if not exists api_test_execution_log (
    id bigserial primary key,
    suite_name varchar(255) not null,
    test_name varchar(255) not null,
    status varchar(20) not null,
    duration_ms bigint not null,
    executed_at timestamp not null default current_timestamp
);
