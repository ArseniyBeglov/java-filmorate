CREATE TABLE IF NOT EXISTS users
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY,
    name     varchar(200),
    login    varchar(200) NOT NULL,
    email    varchar(200) NOT NULL,
    birthday DATE,
    CONSTRAINT user_pk PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS user_friends
(
    id        INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id   INTEGER,
    friend_id INTEGER,
    CONSTRAINT user_fk1 FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT user_fk2 FOREIGN KEY (friend_id) REFERENCES users(id)
    );
CREATE TABLE IF NOT EXISTS rating_mpa
(
    id   INTEGER generated by default as identity,
    name varchar(200),
    CONSTRAINT rating_mpa_pk PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS films
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY,
    name         varchar(200) NOT NULL,
    description  varchar(200),
    duration     INTEGER      NOT NULL,
    rating_id    INTEGER      NOT NULL,
    release_date DATE,
    CONSTRAINT film_pk PRIMARY KEY (id),
    CONSTRAINT film_fk1 FOREIGN KEY (rating_id) REFERENCES rating_mpa (id),
    CONSTRAINT constr_example CHECK (duration > 0 )
    );
CREATE TABLE IF NOT EXISTS film_likes
(
    id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER REFERENCES users (id),
    film_id INTEGER REFERENCES films (id)
    );
CREATE TABLE IF NOT EXISTS genres
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100)
    );
CREATE TABLE IF NOT EXISTS film_genres
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id  INTEGER REFERENCES films (id),
    genre_id INTEGER REFERENCES genres (id)
    );