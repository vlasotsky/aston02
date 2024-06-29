INSERT INTO genre(name)
VALUES ('JAZZ'),
       ('ROCK'),
       ('METAL'),
       ('POP'),
       ('FUNK');

INSERT INTO instrument(instr_name)
VALUES ('VOCALS'),
       ('GUITAR'),
       ('BASS'),
       ('KEYS'),
       ('BANJO'),
       ('DRUMS');

INSERT INTO artist(first_name, last_name, instr_id)
VALUES ('David', 'Gilmour', 2),
       ('Roger', 'Waters', 3);

INSERT INTO vinyl_disc(title, genre_id, label, release_date)
VALUES ('The Dark Side of the Moon', 2, 'Harvest', '1973-03-10');

INSERT INTO song(title, duration, disc_id)
VALUES ('Speak to Me', 90, 1),
       ('Breathe', 163, 1),
       ('On the Run', 216, 1);

INSERT INTO vinyl_disc_artist(artist_id, disc_id)
VALUES (1, 1),
       (2, 1);
