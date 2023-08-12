BEGIN;
CREATE SCHEMA IF NOT EXISTS app AUTHORIZATION swen90007_react_example_owner;
CREATE TABLE IF NOT EXISTS app.test (
	test_value varchar(255)
);
COMMIT;