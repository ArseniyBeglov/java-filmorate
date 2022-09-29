package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MPAController {
    private final FilmService filmService;

    public MPAController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<MPA> findAll() {
        return filmService.getFilmMpaDbStorage().findAll();
    }
    @GetMapping("/{id}")
    public Optional<MPA> getGenresById(@PathVariable Integer id){
        return filmService.getFilmMpaDbStorage().getMpa(id);
    }
}
