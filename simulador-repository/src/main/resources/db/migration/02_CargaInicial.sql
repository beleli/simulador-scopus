INSERT INTO tb_cte_transaction_type (id_transaction_type, ds_name, ds_codification, vl_identification_bytes) VALUES (1, 'CICS', 'EA', 4);
INSERT INTO tb_cte_transaction_type (id_transaction_type, ds_name, ds_codification, vl_identification_bytes) VALUES (2, 'IMS', 'EA', 8);
INSERT INTO tb_cte_transaction_type (id_transaction_type, ds_name, ds_codification, vl_identification_bytes) VALUES (3, 'CWS', 'EA', 8);
INSERT INTO tb_cte_transaction_type (id_transaction_type, ds_name, ds_codification, vl_identification_bytes) VALUES (4, 'Socket Bradescard', 'AA', 4);

INSERT INTO tb_cte_transaction_encoding (id_transaction_encoding, ds_name) VALUES (1, 'EBCDIC');
INSERT INTO tb_cte_transaction_encoding (id_transaction_encoding, ds_name) VALUES (2, 'ASCII');

INSERT INTO tb_cte_field_type (id_field_type, ds_name) VALUES (1, 'Alfanumerico');
INSERT INTO tb_cte_field_type (id_field_type, ds_name) VALUES (2, 'Byte');
INSERT INTO tb_cte_field_type (id_field_type, ds_name) VALUES (3, 'Decimal Compactado');
INSERT INTO tb_cte_field_type (id_field_type, ds_name) VALUES (4, 'Decimal Zonado');

INSERT INTO tb_cte_configuration (id_transaction_type, ds_name, vl_port, vl_timeout, qt_bytes_access) VALUES (1,'ISD - CICS',4091,3000,2);
INSERT INTO tb_cte_configuration (id_transaction_type, ds_name, vl_port, vl_timeout, qt_bytes_access) VALUES (2,'ISD - IMS',4092,3000,2);
INSERT INTO tb_cte_configuration (id_transaction_type, ds_name, vl_port, vl_timeout, qt_bytes_access) VALUES (1,'ServTrans',4093,3000,1);
INSERT INTO tb_cte_configuration (id_transaction_type, ds_name, vl_port, vl_timeout, qt_bytes_access) VALUES (3,'CWS',4094,3000,1000);
INSERT INTO tb_cte_configuration (id_transaction_type, ds_name, vl_port, vl_timeout, qt_bytes_access) VALUES (4,'Socket Bradescard',4095,3000,1001);

INSERT INTO tb_cte_profile (id_profile, ds_name) VALUES ('1', 'ADMINISTRADOR');
INSERT INTO tb_cte_profile (id_profile, ds_name) VALUES ('2', 'SUPORTE');
INSERT INTO tb_cte_profile (id_profile, ds_name) VALUES ('3', 'USUARIO');

INSERT INTO tb_cte_role (id_role, ds_name) VALUES (1, 'ROLE_VIEW_HOME');
INSERT INTO tb_cte_role (id_role, ds_name) VALUES (2, 'ROLE_VIEW_USER');
INSERT INTO tb_cte_role (id_role, ds_name) VALUES (3, 'ROLE_VIEW_CHANGE_PASSWORD');
INSERT INTO tb_cte_role (id_role, ds_name) VALUES (4, 'ROLE_VIEW_PROJECT');
INSERT INTO tb_cte_role (id_role, ds_name) VALUES (5, 'ROLE_VIEW_TRANSACTIONS');
INSERT INTO tb_cte_role (id_role, ds_name) VALUES (6, 'ROLE_VIEW_TEST_SCENARIO');
INSERT INTO tb_cte_role (id_role, ds_name) VALUES (7, 'ROLE_VIEW_TEST_MASS');
INSERT INTO tb_cte_role (id_role, ds_name) VALUES (8, 'ROLE_VIEW_CONFIGURATION');
INSERT INTO tb_cte_role (id_role, ds_name) VALUES (9, 'ROLE_VIEW_EXERCISE_MASS');

INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 1);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 2);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 3);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 4);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 5);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 6);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 7);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 8);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (1, 9);

INSERT INTO tb_cte_profile_role (id_profile, id_role) values (2, 1);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (2, 3);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (2, 4);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (2, 5);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (2, 6);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (2, 7);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (2, 8);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (2, 9);

INSERT INTO tb_cte_profile_role (id_profile, id_role) values (3, 1);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (3, 3);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (3, 6);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (3, 7);
INSERT INTO tb_cte_profile_role (id_profile, id_role) values (3, 9);
