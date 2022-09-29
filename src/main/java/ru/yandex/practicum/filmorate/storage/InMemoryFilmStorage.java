package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCount=0;
    private int makeNewId(){
        return ++idCount;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) throws ValidationException {
        if(film.getDescription().length()>200 || film.getDuration()<0 ||
                film.getReleaseDate().before(Date.from(Instant.parse("1895,10,28")))) {
            throw new ValidationException("Фильм не соответсвует критериям.");
        }
        if(film.getId()==null){
            film.setId(makeNewId());
        }
//        if(film.getUserId()==null){
//            film.setUserId(new HashSet<>());
//        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) throws ValidationException {
        if(film.getDescription().length()>200 || film.getDuration()<0||
                film.getReleaseDate().before(Date.from(Instant.parse("1895,10,28")))) {
            throw new ValidationException("Фильм не соответсвует критериям.");
        }
        if(films.containsKey(film.getId())){
//            if(film.getUserId()==null){
//                film.setUserId(films.get(film.getId()).getUserId());
//            }
//            film.setUserId(films.get(film.getId()).getUserId());
            films.put(film.getId(), film);
        } else {
            throw new ObjectNotFoundException("Фильм не найден.");
        }
        return film;
    }

    @Override
    public Film delete(Film film) {
        if(films.containsKey(film.getId())){
            films.remove(film.getId());
        } else {
            throw new ObjectNotFoundException("Фильм не найден.");
        }
        return film;
    }
    public Film getFilm(Integer id)  {
        if(films.containsKey(id)){
            return  films.get(id);
        } else{
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public Collection<Film> getPopularFilmsByCount(Integer count) {
        return films.values().stream().sorted((p0, p1) -> compare(p0, p1))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film removeFilmLikeByUser(User user, Film film) throws ValidationException {
        if(user!=null && film!=null){
            //film.getUserId().remove(user.getId());
        } else {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }
        return film;
    }

    private int compare(Film p0, Film p1) {
        //int result = p1.getUserId().size()- p0.getUserId().size(); //прямой порядок сортировки
        return 0;
    }
}
