CREATE TABLE USERS
(
    ID                       UUID         NOT NULL,
    USERNAME                 VARCHAR(28)  NOT NULL,
    HASHED_PASSWORD          VARCHAR(64),
    EMAIL                    VARCHAR(255) NOT NULL,
    "social-security-number" VARCHAR(9),
    ROLE                     SMALLINT     NOT NULL,
    IS_EXPIRED               BOOLEAN      NOT NULL,
    IS_LOCKED                BOOLEAN      NOT NULL,
    AUTH_PROVIDER            VARCHAR(255),
    AUTH_CLIENT_NAME         VARCHAR(255) NOT NULL,
    AUTH_PROVIDER_ID         TEXT,
    HOLDER_ID                BIGINT,
    CONSTRAINT PK_USERS PRIMARY KEY (ID)
);

ALTER TABLE EMPLOYEES
    ADD ADDRESS_ID BIGINT;

ALTER TABLE HOLDERS
    ADD USER_ID UUID;

ALTER TABLE USERS
    ADD CONSTRAINT UC_USERS_EMAIL UNIQUE (EMAIL);

ALTER TABLE USERS
    ADD CONSTRAINT UC_USERS_USERNAME UNIQUE (USERNAME);

ALTER TABLE EMPLOYEES
    ADD CONSTRAINT FK_EMPLOYEES_ON_ADDRESS FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS (ID);

ALTER TABLE HOLDERS
    ADD CONSTRAINT FK_HOLDERS_ON_USER FOREIGN KEY (USER_ID) REFERENCES USERS (ID);

ALTER TABLE USERS
    ADD CONSTRAINT FK_USERS_ON_HOLDER FOREIGN KEY (HOLDER_ID) REFERENCES HOLDERS (ID);

ALTER TABLE HOLDERS
    DROP COLUMN EMAIL;

ALTER TABLE HOLDERS
    DROP COLUMN PASSWORD;

ALTER TABLE HOLDERS
    DROP COLUMN "social-security-number";

ALTER TABLE HOLDERS
    DROP COLUMN USERNAME;

ALTER SEQUENCE EMPLOYEE_SEQ INCREMENT BY 1;

ALTER SEQUENCE HOLDERS_SEQ INCREMENT BY 1;

ALTER TABLE TRANSACTIONS
    ALTER COLUMN AMOUNT TYPE DECIMAL USING (AMOUNT::DECIMAL);

ALTER TABLE ACCOUNTS
    ALTER COLUMN BALANCE TYPE DECIMAL USING (BALANCE::DECIMAL);

ALTER TABLE TRANSACTIONS
    ALTER COLUMN FEE TYPE DECIMAL USING (FEE::DECIMAL);

ALTER TABLE ADDRESS
    DROP COLUMN FLOOR;

ALTER TABLE ADDRESS
    ADD FLOOR SMALLINT;

ALTER TABLE ADDRESS
    ALTER COLUMN ZIP_CODE TYPE VARCHAR(5) USING (ZIP_CODE::VARCHAR(5));