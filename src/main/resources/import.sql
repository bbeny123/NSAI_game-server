-- admin@admin / admin
INSERT INTO USERS (USR_ID, USR_EMAIL, USR_PASSWORD, USR_NAME, USR_TYPE, USR_ACTIVE) VALUES (NEXTVAL('SEQ_USR'), 'admin@admin', '$2a$10$Tbt2mTwOerxIn396uy0x4eOKz3vXYqYQ.5Ntd7E5bkrklT7NIAiqi', 'Admin Admin', 'A', TRUE);
-- user@user / user
INSERT INTO USERS (USR_ID, USR_EMAIL, USR_PASSWORD, USR_NAME, USR_TYPE, USR_ACTIVE) VALUES (NEXTVAL('SEQ_USR'), 'user@user', '$2a$10$il1TVNe5l6dlHQ5LGAdQOOQfv81fUS4fw.l8DqISUXhEN1A0n6q6W', 'User User', 'U', TRUE);