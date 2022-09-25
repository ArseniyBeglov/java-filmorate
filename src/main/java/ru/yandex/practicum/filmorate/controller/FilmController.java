package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getInMemoryFilmStorage().findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.getInMemoryFilmStorage().create(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.getInMemoryFilmStorage().put(film);
    }

    @DeleteMapping
    public  Film delete(@Valid @RequestBody Film film) {
        return filmService.getInMemoryFilmStorage().delete(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping ("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        return filmService.delete(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count)
            throws ValidationException {
        if (count <= 0) {
            throw new ValidationException("count");
        }
        return filmService.findAll(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id)  {
        return filmService.getInMemoryFilmStorage().filmsContainfilm(id);
    }
}
