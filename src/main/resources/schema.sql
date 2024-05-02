DROP TABLE IF EXISTS users, follows, ratings, genres, films, likes, genres_films CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS follows (
    following_id INTEGER NOT NULL REFERENCES users(id),
    followed_id INTEGER NOT NULL REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS ratings (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR (200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(200),
    description VARCHAR(200),
    release DATE,
    duration INTEGER NOT NULL,
    rating_id INTEGER, FOREIGN KEY (rating_id) REFERENCES ratings(id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id INTEGER NOT NULL REFERENCES films(id),
    user_id INTEGER NOT NULL REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS genres_films (
    genre_id INTEGER NOT NULL REFERENCES genres(id),
    film_id INTEGER NOT NULL REFERENCES films(id)
);