package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
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
        User user=userService.getInMemoryUserStorage().usersContainUser(userId);
        Film film=inMemoryFilmStorage.filmsContainfilm(id);
        if(film.getUserId()==null){
            film.setUserId(new HashSet<>());
            film.getUserId().add(userId);
        } else{
            film.getUserId().add(userId);
        }
        System.out.println("ggwp");
        System.out.println(film);
        return film;
    }

    public Film delete(Integer id, Integer userId) throws ValidationException {
        User user=userService.getInMemoryUserStorage().usersContainUser(userId);
        Film film=inMemoryFilmStorage.filmsContainfilm(id);
        if(user!=null && film!=null){
            film.getUserId().remove(userId);
        } else {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }
        return film;
    }


    public Collection<Film> findAll(Integer count) {
        return inMemoryFilmStorage.findAll().stream().sorted((p0, p1) -> compare(p0, p1))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        int result = p1.getUserId().size()- p0.getUserId().size(); //прямой порядок сортировки
        return result;
    }
}
