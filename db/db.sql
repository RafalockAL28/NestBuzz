-- Database: quarkus-social

--DROP DATABASE IF EXISTS "quarkus-social";

CREATE DATABASE "nestbuzz"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE TABLE USERS(
	id bigserial not null primary key,
	name varchar(100) not null,
	age integer not null
);



CREATE TABLE POSTS(
	id bigserial not null primary key,
	text varchar(255) not null,
	dateTime timestamp not null,
	user_id bigint not null references USERS(id)
);

CREATE TABLE followers (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	user_id BIGINT NOT NULL REFERENCES users(id),
	follower_id BIGINT NOT NULL REFERENCES users(id)
);

