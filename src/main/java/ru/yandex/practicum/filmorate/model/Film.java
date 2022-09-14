package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.time.Duration;


@Data
public class Film {
    private Integer  id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
