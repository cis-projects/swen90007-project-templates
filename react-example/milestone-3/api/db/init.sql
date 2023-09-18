BEGIN;
CREATE SCHEMA IF NOT EXISTS app AUTHORIZATION swen90007_react_example_owner;

CREATE TABLE app.vote (
                      id uuid NOT NULL UNIQUE,
                      name varchar(255),
                      email varchar(255) NOT NULL,
                      supporting bool NOT NULL,
                      status varchar(255) NOT NULL,
                      created timestamp with time zone NOT NULL,
                      PRIMARY KEY (id)
);

CREATE INDEX vote_is_supporting
    ON app.vote (supporting, status);

CREATE TABLE app.refresh_token (
                               id uuid NOT NULL UNIQUE,
                               token_id VARCHAR(36) NOT NULL,
                               username VARCHAR(255) NOT NULL,
                               PRIMARY KEY (id)
);

CREATE INDEX refresh_token_username
    ON app.refresh_token (username);

COMMIT;