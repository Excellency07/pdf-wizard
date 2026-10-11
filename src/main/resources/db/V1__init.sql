CREATE SEQUENCE IF NOT EXISTS revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE accounts
(
    id         UUID         NOT NULL,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN      NOT NULL,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

CREATE TABLE pdf_metadata
(
    id           UUID         NOT NULL,
    file_name    VARCHAR(255),
    storage_name VARCHAR(255) NOT NULL,
    file_size    BIGINT       NOT NULL,
    account_id   UUID,
    created_at   TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_pdf_metadata PRIMARY KEY (id)
);

CREATE TABLE revchanges
(
    rev        BIGINT NOT NULL,
    entityname VARCHAR(255)
);

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

ALTER TABLE accounts
    ADD CONSTRAINT uc_accounts_email UNIQUE (email);

ALTER TABLE pdf_metadata
    ADD CONSTRAINT uc_pdf_metadata_file_name UNIQUE (file_name);

ALTER TABLE pdf_metadata
    ADD CONSTRAINT uc_pdf_metadata_storage_name UNIQUE (storage_name);

ALTER TABLE revchanges
    ADD CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev);