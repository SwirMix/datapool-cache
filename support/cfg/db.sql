-- DROP SCHEMA datapool;

CREATE SCHEMA datapool AUTHORIZATION perfcona;

-- DROP SEQUENCE datapool.users_id_seq;

CREATE SEQUENCE datapool.users_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE datapool.users_id_seq OWNER TO perfcona;
GRANT ALL ON SEQUENCE datapool.users_id_seq TO perfcona;
-- datapool.settings definition

-- Drop table

-- DROP TABLE datapool.settings;

CREATE TABLE datapool.settings (
	"name" varchar(200) NOT NULL,
	value text NULL,
	CONSTRAINT settings_pkey PRIMARY KEY (name)
);

-- Permissions

ALTER TABLE datapool.settings OWNER TO perfcona;
GRANT ALL ON TABLE datapool.settings TO perfcona;


-- datapool.users definition

-- Drop table

-- DROP TABLE datapool.users;

CREATE TABLE datapool.users (
	id serial4 NOT NULL,
	login varchar(200) NULL,
	"password" varchar(200) NULL,
	email varchar(200) NULL,
	"role" varchar(50) NULL,
	CONSTRAINT users_login_key UNIQUE (login),
	CONSTRAINT users_pk PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE datapool.users OWNER TO perfcona;
GRANT ALL ON TABLE datapool.users TO perfcona;


-- datapool.projects definition

-- Drop table

-- DROP TABLE datapool.projects;

CREATE TABLE datapool.projects (
	id varchar(200) NOT NULL,
	"name" varchar(200) NULL,
	owner_id int4 NULL,
	description varchar(800) NOT NULL,
	CONSTRAINT projects_pkey PRIMARY KEY (id),
	CONSTRAINT projects_fk FOREIGN KEY (owner_id) REFERENCES datapool.users(id)
);

-- Permissions

ALTER TABLE datapool.projects OWNER TO perfcona;
GRANT ALL ON TABLE datapool.projects TO perfcona;




-- Permissions

GRANT ALL ON SCHEMA datapool TO perfcona;

CREATE TABLE datapool.projects (
	id varchar(200) NOT NULL,
	"name" varchar(200) NULL,
	owner_id int4 NULL,
	description varchar(800) NOT NULL,
	CONSTRAINT projects_pkey PRIMARY KEY (id),
	CONSTRAINT projects_fk FOREIGN KEY (owner_id) REFERENCES datapool.users(id)
);

CREATE TABLE datapool.scripts (
    scriptName varchar(300),
    projectId varchar(200) references datapool.projects,
    description varchar(500),
    transactions text[],
    script_default_parameters jsonb,
    primary key (scriptName, projectId)
)

CREATE TABLE datapool.parameters (
    id varchar(200) primary key,
    parameters jsonb
)

CREATE TABLE datapool.profiles (
    id varchar(200) primary key,
    name varchar(200),
    description varchar(500),
    projectId varchar(200) references datapool.projects,
    update_date TIMESTAMP WITH TIME ZONE,
    profile jsonb default '{}'
)
