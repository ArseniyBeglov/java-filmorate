package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCount=0;
    private int makeNewId(){
        return ++idCount;
    }
    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if(film.getDescription().length()>200 || film.getDuration()<0 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895,10,28))) {
            throw new ValidationException("Фильм не соответсвует критериям.");
        }

        if(film.getId()==null){
            film.setId(makeNewId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) throws ValidationException {
        if(film.getDescription().length()>200 || film.getDuration()<0||
                film.getReleaseDate().isBefore(LocalDate.of(1895,10,28))) {
            throw new ValidationException("Фильм не соответсвует критериям.");
        }
        if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }

        return film;
    }
}
