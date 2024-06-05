DROP TABLE IF EXISTS vinyl_disc_artist;
DROP TABLE IF EXISTS song;
DROP TABLE IF EXISTS artist;
DROP TABLE IF EXISTS vinyl_disc;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS instrument;

CREATE TABLE genre (
    genre_id SERIAL PRIMARY KEY,
    name     VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE instrument (
    instr_id   SERIAL PRIMARY KEY,
    instr_name VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE artist (
    artist_id  SERIAL PRIMARY KEY NOT NULL,
    first_name VARCHAR(15)        NOT NULL,
    last_name  VARCHAR(15)        NOT NULL,
    instr_id   INTEGER            NOT NULL,
    FOREIGN KEY (instr_id) REFERENCES instrument (instr_id)
);

CREATE TABLE vinyl_disc (
    disc_id      SERIAL PRIMARY KEY,
    title        VARCHAR(30) NOT NULL,
    genre_id     INTEGER     NOT NULL,
    label        VARCHAR(20) NOT NULL,
    release_year DATE        NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id)
);

CREATE TABLE song (
    song_id  SERIAL PRIMARY KEY,
    title    VARCHAR(30) NOT NULL,
    duration INTERVAL    NOT NULL,
    disc_id  INTEGER     NOT NULL,
    FOREIGN KEY (disc_id) REFERENCES vinyl_disc (disc_id)
);

CREATE TABLE vinyl_disc_artist (
    disc_artist_id SERIAL PRIMARY KEY,
    artist_id      INTEGER NOT NULL,
    disc_id        INTEGER NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES artist (artist_id),
    FOREIGN KEY (disc_id) REFERENCES vinyl_disc (disc_id)
);
