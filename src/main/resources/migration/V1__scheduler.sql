

CREATE SEQUENCE "T_USER_SEQ" INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE "T_SCH_TASK_SEQ" INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE "T_USER_SECURITY_SEQ" INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE "T_REMOTE_JOB_SEQ" INCREMENT BY 1 START WITH 1;

/* USERS */

CREATE TABLE T_USER (
  C_USER_ID SERIAL PRIMARY KEY,
  C_USER_NAME VARCHAR(100) NOT NULL,
  CONSTRAINT I_USER_NAME_UC UNIQUE (C_USER_NAME)
); 

CREATE TABLE T_USER_SECURITY (
  T_USER_SECURITY_ID SERIAL PRIMARY KEY,
  C_USER_ID NUMBER NOT NULL REFERENCES T_USER (C_USER_ID) ON DELETE CASCADE,
  C_USER_LOGIN VARCHAR(16) NOT NULL,
  C_USER_PASSWORD VARCHAR(255) NOT NULL,
  C_USER_AUTH_STRING VARCHAR(255) NOT NULL,
  C_USER_ROLE VARCHAR(10) NOT NULL,
  CONSTRAINT I_USER_ID_UC UNIQUE (C_USER_ID),
  CONSTRAINT I_USER_LOGIN_UC UNIQUE (C_USER_LOGIN),
  CHECK (C_USER_ROLE in ('ADMIN', 'USER'))
);

/* SCHEDULER TASKS */

CREATE TABLE T_SCH_TASK (
  C_SCH_TASK_ID SERIAL NOT NULL PRIMARY KEY,
  C_USER_ID NUMBER NOT NULL REFERENCES T_USER (C_USER_ID) ON DELETE CASCADE,
  C_SCH_TASK_TYPE VARCHAR(10) NOT NULL,
  C_SCH_TASK_NAME VARCHAR(255) NOT NULL,
  C_SCH_TASK_DESCR CLOB NOT NULL,
  C_SCH_TASK_PARAM_JSON CLOB NOT NULL,
  CONSTRAINT I_SCH_TASK_NAME_UC UNIQUE (C_SCH_TASK_NAME),
  CHECK (C_SCH_TASK_TYPE in ('ONE_TIME', 'DAILY', 'WEEKLY', 'MONTHLY'))
);

CREATE TABLE T_REMOTE_JOB (
  C_REMOTE_JOB_ID SERIAL NOT NULL PRIMARY KEY,
  C_SCH_TASK_ID NUMBER NOT NULL REFERENCES T_SCH_TASK (C_SCH_TASK_ID) ON DELETE CASCADE,
  C_URL CLOB NOT NULL,
  C_REQUEST_METHOD VARCHAR(10) NOT NULL,
  C_POST_JSON CLOB NOT NULL,
  CONSTRAINT I_SCH_TASK_ID_UC UNIQUE (C_SCH_TASK_ID)
);
