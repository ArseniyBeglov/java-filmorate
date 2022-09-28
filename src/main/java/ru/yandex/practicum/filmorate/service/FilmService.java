package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Getter
public class FilmService  {
    private final UserService userService;
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(UserService userService, InMemoryFilmStorage inMemoryFilmStorage) {
        this.userService = userService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }


    public Film addLike(Integer id, Integer userId)  {
        User user=userService.getInMemoryUserStorage().getUser(userId);
        Film film=inMemoryFilmStorage.getFilm(id);
        if(film.getUserId()==null){
            film.setUserId(new HashSet<>());
            film.getUserId().add(userId);
        } else{
            film.getUserId().add(userId);
        }
        return film;
    }

    public Film delete(Integer id, Integer userId) throws ValidationException {
        User user=userService.getInMemoryUserStorage().getUser(userId);
        Film film=inMemoryFilmStorage.getFilm(id);
        return inMemoryFilmStorage.removeFilmLikeByUser(user,film);
    }


    public Collection<Film> getPopularFilms(Integer count) {
        return inMemoryFilmStorage.getPopularFilmsByCount(count);
    }


}
