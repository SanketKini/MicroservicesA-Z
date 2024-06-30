INSERT INTO
    STATIC_ENVIRONMENTS (env_name, env_description, created_by, created_on, updated_by, updated_on)
VALUES
    ('DEV', 'dev', 'admin', current_timestamp, 'admin', current_timestamp),
    ('TEST', 'test', 'admin', current_timestamp, 'admin', current_timestamp),
    ('UAT', 'uat', 'admin', current_timestamp, 'admin', current_timestamp),
    ('PROD', 'prod', 'admin', current_timestamp, 'admin', current_timestamp),
    ('ALL', 'ALL', 'admin', current_timestamp, 'admin', current_timestamp);