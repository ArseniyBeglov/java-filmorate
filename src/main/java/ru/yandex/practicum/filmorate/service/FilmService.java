package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.*;


import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@Getter
public class FilmService implements FilmStorage {
    private final UserService userService;
    @Qualifier("filmDbStorage")
    private final FilmDbStorage filmDbStorage;
    @Qualifier("filmGenresDbStorage")
    private final FilmGenresDbStorage filmGenresDbStorage;
    @Qualifier("filmLikeDbStorage")
    private final FilmLikeDbStorage filmLikeDbStorage;
    @Qualifier("filmMpaDbStorage")
    private final FilmMpaDbStorage filmMpaDbStorage;

    @Autowired
    public FilmService(UserService userService, FilmDbStorage filmDbStorage, FilmGenresDbStorage filmGenresDbStorage,
                       FilmLikeDbStorage filmLikeDbStorage, FilmMpaDbStorage filmMpaDbStorage) {
        this.userService = userService;
        this.filmDbStorage = filmDbStorage;
        this.filmGenresDbStorage = filmGenresDbStorage;
        this.filmLikeDbStorage = filmLikeDbStorage;
        this.filmMpaDbStorage = filmMpaDbStorage;
    }


    public Film addLike(Integer id, Integer userId) throws ValidationException {
        filmLikeDbStorage.create(id, userId);
        return filmDbStorage.getFilm(id);
    }

    public Film deletelike(Integer id, Integer userId) throws ValidationException {
        filmLikeDbStorage.delete(id, userId);
        return filmDbStorage.getFilm(id);
    }


    public Collection<Film> getPopularFilms(Integer count) {
        return filmLikeDbStorage.getPopularFilms(count);
    }


    @Override
    public Collection<Film> findAll() {
        return filmDbStorage.findAll();
    }

    @Override
    public Film create(Film film) throws ValidationException {
        return filmDbStorage.create(film);
    }

    @Override
    public Film put(Film film) throws ValidationException {
        return filmDbStorage.put(film);
    }

    @Override
    public Film delete(Film film) throws ValidationException {
        return filmDbStorage.delete(film);
    }

    public Film getFilmById( Integer id) {
        return filmDbStorage.getFilm(id);
    }

    public Collection<Genres> findAllGenres() {
        return filmGenresDbStorage.findAll();
    }

    public Genres getGenresById( Integer id) {
        return filmGenresDbStorage.getGenre(id);
    }

    public Collection<MPA> findAllMPA() {
        return filmMpaDbStorage.findAll();
    }

    public MPA getMpaById( Integer id)  {
        return filmMpaDbStorage.getMpa(id);
    }
}
