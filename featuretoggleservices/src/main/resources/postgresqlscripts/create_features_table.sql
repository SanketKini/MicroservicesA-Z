CREATE TABLE FEATURES (
	feature_name VARCHAR (240) NOT NULL,
	environment VARCHAR (10) NOT NULL,
	enabled BOOLEAN NOT NULL,
	created_by VARCHAR (100) NOT NULL,
	created_on TIMESTAMP NOT NULL,
    updated_by VARCHAR (100) NOT NULL,
	updated_on TIMESTAMP NOT NULL,
	supporting_ticket VARCHAR (100) NOT NULL,
	description VARCHAR (2048) NOT NULL,
	archived BOOLEAN NOT NULL,
	service_set VARCHAR (1000),
	PRIMARY KEY (feature_name, environment),
	UNIQUE (feature_name, environment)
);