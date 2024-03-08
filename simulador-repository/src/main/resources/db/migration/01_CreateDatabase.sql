CREATE TABLE tb_cte_profile (
  id_profile INTEGER NOT NULL,
  ds_name VARCHAR(60) NOT NULL
);

ALTER TABLE tb_cte_profile
	ADD CONSTRAINT PK_tb_cte_profile PRIMARY KEY /*CLUSTERED*/ (id_profile /*ASC*/)


CREATE TABLE tb_cte_user (
  id_user INTEGER IDENTITY NOT NULL,
  id_profile INTEGER NOT NULL,
  ds_name VARCHAR(60) NOT NULL,
  ds_email VARCHAR(255) NOT NULL,
  fl_enabled BIT DEFAULT 1 NOT NULL,
  fl_change_password BIT DEFAULT 0 NULL,
  ds_password VARCHAR(255) NOT NULL
);
/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_user
ADD CONSTRAINT PK_tb_cte_user PRIMARY KEY CLUSTERED (id_user ASC);*/

ALTER TABLE tb_cte_user /*WITH NOCHECK*/
       ADD CONSTRAINT FK_tb_cte_user_id_profile FOREIGN KEY(id_profile) REFERENCES tb_cte_profile (id_profile);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_user_ds_email
       ON tb_cte_user (ds_email /*ASC*/);


CREATE TABLE tb_cte_role (
  id_role INTEGER NOT NULL,
  ds_name VARCHAR(60) NOT NULL
);

ALTER TABLE tb_cte_role
 ADD CONSTRAINT PK_tb_cte_role PRIMARY KEY /*CLUSTERED*/ (id_role /*ASC*/);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_role_ds_name
	ON tb_cte_role (ds_name /*ASC*/);

	

CREATE TABLE tb_cte_profile_role(
  id_profile_role INTEGER IDENTITY NOT NULL,
  id_profile INTEGER NOT NULL,
  id_role INTEGER NOT NULL
);
/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_profile_role
 ADD CONSTRAINT PK_tb_cte_profile_role PRIMARY KEY CLUSTERED (id_profile_role ASC);*/

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_profile_role_id_profile_id_role
       ON tb_cte_profile_role (id_profile /*ASC*/, id_role /*ASC*/);
 
ALTER TABLE tb_cte_profile_role /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_profile_role_id_profile FOREIGN KEY(id_profile) REFERENCES tb_cte_profile (id_profile);

ALTER TABLE tb_cte_profile_role /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_profile_role_id_role FOREIGN KEY(id_role) REFERENCES tb_cte_role (id_role);

	
CREATE TABLE tb_cte_project (
  id_project INTEGER IDENTITY NOT NULL,
  cd_project VARCHAR(32) NOT NULL,
  ds_project VARCHAR(256) NULL,
  dt_begin DATETIME NOT NULL,
  dt_end DATETIME NOT NULL
  );

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_project
 ADD CONSTRAINT PK_tb_cte_project PRIMARY KEY CLUSTERED (id_project ASC);*/

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_project_cd_project
    ON tb_cte_project (cd_project /*ASC*/);
  

CREATE TABLE tb_cte_projetc_user (
  id_project_user INTEGER IDENTITY NOT NULL,
  id_project INT NOT NULL,
  id_user INT NOT NULL
  );

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_projetc_user
 ADD CONSTRAINT PK_tb_cte_projetc_user PRIMARY KEY CLUSTERED (id_project_user ASC);*/

ALTER TABLE tb_cte_projetc_user /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_projetc_user_id_project FOREIGN KEY(id_project) REFERENCES tb_cte_project (id_project);

ALTER TABLE tb_cte_projetc_user /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_projetc_user_id_user FOREIGN KEY(id_user) REFERENCES tb_cte_user (id_user);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_projetc_user_id_project_id_user
	ON tb_cte_projetc_user (id_project /*ASC*/, id_user /*ASC*/);


CREATE TABLE tb_cte_transaction_type (
  id_transaction_type INTEGER NOT NULL,
  ds_name VARCHAR(60) NOT NULL,
  ds_codification CHAR(2) NOT NULL,
  vl_identification_bytes SMALLINT NOT NULL
);

ALTER TABLE tb_cte_transaction_type
	ADD CONSTRAINT PK_tb_cte_transaction_type PRIMARY KEY /*CLUSTERED*/ (id_transaction_type /*ASC*/);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_transaction_type_ds_name
	ON tb_cte_transaction_type (ds_name /*ASC*/);



CREATE TABLE tb_cte_transaction_encoding (
  id_transaction_encoding INTEGER NOT NULL,
  ds_name VARCHAR(60) NOT NULL
);

ALTER TABLE tb_cte_transaction_encoding
	ADD CONSTRAINT PK_tb_cte_transaction_encoding PRIMARY KEY /*CLUSTERED*/ (id_transaction_encoding /*ASC*/);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_transaction_encoding_ds_name
	ON tb_cte_transaction_encoding (ds_name /*ASC*/);


CREATE TABLE tb_cte_transaction (
  id_transaction INTEGER IDENTITY NOT NULL,
  id_transaction_type INTEGER NOT NULL,
  id_transaction_encoding INTEGER NOT NULL,
  cd_identification VARCHAR(8) NOT NULL,
  ds_description VARCHAR(60) NULL,
  id_transaction_parent INTEGER NULL,
  id_router_type INTEGER NULL,
  fl_parent BIT NOT NULL, 
  dt_last_access DATETIME NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
 ALTER TABLE tb_cte_transaction
	ADD CONSTRAINT PK_tb_cte_transaction PRIMARY KEY CLUSTERED (id_transaction ASC);*/

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_transaction_id_transaction_parent_cd_identification
       ON tb_cte_transaction (id_transaction_parent /*ASC*/, cd_identification /*ASC*/);

ALTER TABLE tb_cte_transaction /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_transaction_id_transaction_type FOREIGN KEY(id_transaction_type) REFERENCES tb_cte_transaction_type (id_transaction_type);

ALTER TABLE tb_cte_transaction /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_transaction_id_router_type FOREIGN KEY(id_router_type) REFERENCES tb_cte_transaction_type (id_transaction_type);

ALTER TABLE tb_cte_transaction /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_transaction_id_transaction_encoding FOREIGN KEY(id_transaction_encoding) REFERENCES tb_cte_transaction_encoding (id_transaction_encoding);


CREATE TABLE tb_cte_field_type (
  id_field_type INTEGER NOT NULL,
  ds_name VARCHAR(60) NOT NULL
);

ALTER TABLE tb_cte_field_type
	ADD CONSTRAINT PK_tb_cte_field_type PRIMARY KEY /*CLUSTERED*/ (id_field_type /*ASC*/);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_field_type_ds_name
	ON tb_cte_field_type (ds_name /*ASC*/);


CREATE TABLE tb_cte_layout_input (
  id_layout_input INTEGER IDENTITY NOT NULL,
  id_transaction INTEGER NOT NULL,
  id_field_type INTEGER NOT NULL,
  ds_name VARCHAR(30) NOT NULL,
  vl_size INTEGER NOT NULL,
  cd_ordinal INTEGER NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_layout_input
	ADD CONSTRAINT PK_tb_cte_layout_input PRIMARY KEY CLUSTERED (id_layout_input ASC);*/

ALTER TABLE tb_cte_layout_input /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_layout_input_id_transaction FOREIGN KEY(id_transaction) REFERENCES tb_cte_transaction (id_transaction);

ALTER TABLE tb_cte_layout_input /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_layout_input_id_field_type FOREIGN KEY(id_field_type) REFERENCES tb_cte_field_type (id_field_type);


CREATE TABLE tb_cte_layout_output_transaction (
  id_layout_output_transaction INTEGER IDENTITY NOT NULL,
  id_transaction INTEGER NOT NULL,
  ds_description VARCHAR(60) NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_layout_output_transaction
	ADD CONSTRAINT PK_tb_cte_layout_output_transaction PRIMARY KEY CLUSTERED (id_layout_output_transaction ASC);*/

ALTER TABLE tb_cte_layout_output_transaction /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_layout_output_transaction_id_transaction FOREIGN KEY(id_transaction) REFERENCES tb_cte_transaction (id_transaction);


CREATE TABLE tb_cte_layout_output (
  id_layout_output INTEGER IDENTITY NOT NULL,
  id_layout_output_transaction INTEGER NOT NULL,
  id_field_type INTEGER NOT NULL,
  ds_name VARCHAR(30) NOT NULL,
  vl_size INTEGER NOT NULL,
  cd_ordinal INTEGER NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_layout_output
	ADD CONSTRAINT PK_tb_cte_layout_output PRIMARY KEY CLUSTERED (id_layout_output ASC);*/

ALTER TABLE tb_cte_layout_output /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_layout_output_id_layout_output_transaction FOREIGN KEY(id_layout_output_transaction) REFERENCES tb_cte_layout_output_transaction (id_layout_output_transaction);

ALTER TABLE tb_cte_layout_output /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_layout_output_id_field_type FOREIGN KEY(id_field_type) REFERENCES tb_cte_field_type (id_field_type);

	
CREATE TABLE tb_cte_layout_version (
  id_layout_version INTEGER IDENTITY NOT NULL,
  id_layout_input INTEGER NOT NULL,
  id_layout_output_transaction INTEGER NOT NULL,
  ds_value VARCHAR(256) NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_layout_version
	ADD CONSTRAINT PK_tb_cte_layout_version PRIMARY KEY CLUSTERED (id_layout_version ASC);*/

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_layout_version_id_layout_input_id_layout_output_transaction_ds_value
    ON tb_cte_layout_version (id_layout_input /*ASC*/, id_layout_output_transaction /*ASC*/, ds_value /*ASC*/);

ALTER TABLE tb_cte_layout_version /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_layout_version_id_layout_input FOREIGN KEY(id_layout_input) REFERENCES tb_cte_layout_input (id_layout_input);

ALTER TABLE tb_cte_layout_version /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_layout_version_id_layout_output_transaction FOREIGN KEY(id_layout_output_transaction) REFERENCES tb_cte_layout_output_transaction (id_layout_output_transaction);
	

CREATE TABLE tb_cte_test_scenario (
  id_test_scenario INTEGER IDENTITY NOT NULL,
  id_transaction INTEGER NOT NULL,
  ds_description VARCHAR(60) NULL,
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
 ALTER TABLE tb_cte_transaction
	ADD CONSTRAINT PK_tb_cte_test_scenario PRIMARY KEY CLUSTERED (id_test_scenario ASC);*/

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_test_scenario_id_transaction_ds_description
       ON tb_cte_test_scenario (id_transaction /*ASC*/,ds_description /*ASC*/);

ALTER TABLE tb_cte_test_scenario /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_scenario_id_transaction FOREIGN KEY(id_transaction) REFERENCES tb_cte_transaction (id_transaction);

CREATE TABLE tb_cte_test_mass (
	id_test_mass  INTEGER IDENTITY NOT NULL,
	id_test_scenario INTEGER NOT NULL,
	id_layout_output_transaction INTEGER NOT NULL,
	id_project INTEGER NULL,
	ds_description VARCHAR(60) NULL,
	vl_error_average DECIMAL(5,0) NOT NULL,
	vl_timeout DECIMAL(5,0) NOT NULL,
	vl_occurrences DECIMAL(5,0) NOT NULL,
	ds_restart VARCHAR(255) NULL,
	vl_return_code DECIMAL(5,0) NOT NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_test_mass
	ADD CONSTRAINT PK_tb_cte_test_mass PRIMARY KEY CLUSTERED (id_test_mass ASC);*/

ALTER TABLE tb_cte_test_mass /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_mass_id_test_scenario FOREIGN KEY(id_test_scenario) REFERENCES tb_cte_test_scenario (id_test_scenario);

ALTER TABLE tb_cte_test_mass /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_mass_id_layout_output_transaction FOREIGN KEY(id_layout_output_transaction) REFERENCES tb_cte_layout_output_transaction (id_layout_output_transaction);

ALTER TABLE tb_cte_test_mass /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_mass_id_project FOREIGN KEY(id_project) REFERENCES tb_cte_project (id_project);
	
CREATE TABLE tb_cte_test_mass_input (
	id_test_mass_input INTEGER IDENTITY NOT NULL,
	id_test_mass INTEGER NOT NULL,
	id_layout_input INTEGER NOT NULL,
	ds_value VARCHAR(2048) NOT NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_test_mass_input
	ADD CONSTRAINT PK_tb_cte_test_mass_input PRIMARY KEY CLUSTERED (id_test_mass_input ASC);*/

ALTER TABLE tb_cte_test_mass_input /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_input_id_test_mass FOREIGN KEY(id_test_mass) REFERENCES tb_cte_test_mass (id_test_mass);

ALTER TABLE tb_cte_test_mass_input /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_mass_input_id_layout_input FOREIGN KEY(id_layout_input) REFERENCES tb_cte_layout_input (id_layout_input);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_test_mass_input_id_test_mass_id_layout_input
	ON tb_cte_test_mass_input (id_test_mass /*ASC*/, id_layout_input /*ASC*/);


CREATE TABLE tb_cte_test_mass_output (
	id_test_mass_output INTEGER IDENTITY NOT NULL,
	id_test_mass INTEGER NOT NULL,
	id_layout_output INTEGER NOT NULL,
	id_layout_input INTEGER NULL,
	ds_value VARCHAR(2048) NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_test_mass_output
	ADD CONSTRAINT PK_tb_cte_test_mass_output PRIMARY KEY CLUSTERED (id_test_mass_output ASC);*/

ALTER TABLE tb_cte_test_mass_output /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_output_id_test_mass FOREIGN KEY(id_test_mass) REFERENCES tb_cte_test_mass (id_test_mass);

ALTER TABLE tb_cte_test_mass_output /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_mass_output_id_layout_input FOREIGN KEY(id_layout_input) REFERENCES tb_cte_layout_input (id_layout_input);

ALTER TABLE tb_cte_test_mass_output /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_test_mass_output_id_layout_output FOREIGN KEY(id_layout_output) REFERENCES tb_cte_layout_output (id_layout_output);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_test_mass_output_id_test_mass_id_layout_output
	ON tb_cte_test_mass_output (id_test_mass /*ASC*/, id_layout_output /*ASC*/);

	
CREATE TABLE tb_cte_configuration (
  id_configuration INTEGER IDENTITY NOT NULL,
  id_transaction_type INTEGER NOT NULL,
  ds_name VARCHAR(40) NOT NULL,
  vl_port INTEGER NOT NULL,
  vl_timeout INTEGER NOT NULL,
  qt_bytes_access DECIMAL(5,0) NOT NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_configuration
	ADD CONSTRAINT PK_tb_cte_configuration PRIMARY KEY CLUSTERED (id_configuration ASC)*/

ALTER TABLE tb_cte_configuration /*WITH NOCHECK*/
	ADD CONSTRAINT FK_tb_cte_configuration_id_transaction_type FOREIGN KEY(id_transaction_type) REFERENCES tb_cte_transaction_type (id_transaction_type);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_configuration_ds_name
       ON tb_cte_configuration (ds_name /*ASC*/);

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_configuration_vl_port
       ON tb_cte_configuration (vl_port /*ASC*/);

       
CREATE TABLE dbo.tb_cte_statistics (
  id_cte_statistics INTEGER IDENTITY NOT NULL,
  id_transaction INTEGER NOT NULL,
  dt_ocurrence DATE NOT NULL,
  qt_ocurrences DECIMAL(10,0) NOT NULL
);

/*
 * An IDENTITY column is always treated as the primary key for the table: http://hsqldb.org/doc/guide/ch02.html#N104B3
ALTER TABLE tb_cte_statistics
	ADD CONSTRAINT PK_tb_cte_statistics PRIMARY KEY CLUSTERED (id_cte_statistics ASC);*/

CREATE UNIQUE /*NONCLUSTERED*/ INDEX AK_tb_cte_statistics_id_transaction_dt_ocurrence
	ON tb_cte_statistics (id_transaction /*ASC*/, dt_ocurrence /*ASC*/);

/*ALTER TABLE tb_cte_statistics WITH NOCHECK
	ADD CONSTRAINT FK_tb_cte_statistics_id_transaction FOREIGN KEY(id_transaction) REFERENCES tb_cte_transaction (id_transaction);*/

