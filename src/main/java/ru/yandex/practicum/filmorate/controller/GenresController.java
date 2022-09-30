package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenresController {
    private final FilmService filmService;

    public GenresController(FilmService filmService) {
        this.filmService = filmService;
    }
    @GetMapping
    public Collection<Genres> findAll() {
        return filmService.findAllGenres();
    }
    @GetMapping("/{id}")
    public Genres getGenresById(@PathVariable Integer id){
        return filmService.getGenresById(id);
    }
}
