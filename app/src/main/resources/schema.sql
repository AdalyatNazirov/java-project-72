DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);


CREATE TABLE url_checks
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY,
    url_id      BIGINT REFERENCES urls (id),
    status_code INT       NOT NULL,
    h1          VARCHAR(1024),
    title       VARCHAR(1024),
    description VARCHAR(1024),
    created_at  TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
