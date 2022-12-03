package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.time.Duration;
import java.util.Set;


@Data
@Builder
public class Film {
    private Integer  id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MPA mpa;
    private Collection<Genres> genres;

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, MPA mpa, Collection<Genres> genresList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genresList;
    }

}
