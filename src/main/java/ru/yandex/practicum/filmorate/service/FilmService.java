package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;


import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@Getter
public class FilmService  {
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


    public Optional<Film> addLike(Integer id, Integer userId) throws ValidationException {
        filmLikeDbStorage.create(id,userId);
        return filmDbStorage.getFilm(id);
    }

    public Optional<Film> delete(Integer id, Integer userId) throws ValidationException {
        filmLikeDbStorage.delete(id,userId);
        return filmDbStorage.getFilm(id);
    }


    public Collection<Film> getPopularFilms(Integer count) {
        return filmLikeDbStorage.getPopularFilms(count);
    }


}
