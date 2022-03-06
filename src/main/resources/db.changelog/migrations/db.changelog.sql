--liquibase formatted sql
--changeset eduardo.comerlato:1
CREATE TABLE IF NOT EXISTS status
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY,
    name                    VARCHAR (10),
    PRIMARY KEY (id)
);

--changeset eduardo.comerlato:2
CREATE TABLE IF NOT EXISTS subscription
(
    id                      VARCHAR (50),
    status_id               BIGINT,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (status_id) REFERENCES status(id)
);

--changeset eduardo.comerlato:3
CREATE TABLE IF NOT EXISTS event_history
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY,
    type                    VARCHAR (25),
    subscription_id         VARCHAR (50),
    created_at              TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (subscription_id) REFERENCES subscription(id)
);