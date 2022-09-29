package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmLike {
    private int id;
    private int userId;
    private int filmId;
}
