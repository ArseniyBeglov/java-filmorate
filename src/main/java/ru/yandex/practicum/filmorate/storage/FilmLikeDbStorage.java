package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component("filmLikeDbStorage")
public class FilmLikeDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMpaDbStorage filmMpaDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmGenresDbStorage filmGenresDbStorage;
    private final FilmDbStorage filmDbStorage;

    public FilmLikeDbStorage(JdbcTemplate jdbcTemplate, FilmMpaDbStorage filmMpaDbStorage, UserDbStorage userDbStorage,
                             FilmGenresDbStorage filmGenresDbStorage, FilmDbStorage filmDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMpaDbStorage = filmMpaDbStorage;
        this.userDbStorage = userDbStorage;
        this.filmGenresDbStorage = filmGenresDbStorage;
        this.filmDbStorage = filmDbStorage;
    }

    public void create(Integer id, Integer userId) throws ValidationException {
        String sqlQuery = "insert into film_likes(film_id, user_id) " +
                "values ( ?, ?)";
        jdbcTemplate.update(sqlQuery,
                id, userId);

    }

    public void put(Integer id, Integer userId) throws ValidationException {
        String sqlQuery = "update film_likes set " +
                " film_id=?, user_id=?" +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery
                , id, userId, id);

    }

    public void delete(Integer id, Integer userId) throws ValidationException {
        if (!userDbStorage.getUser(userId).isPresent()) {
            throw new ObjectNotFoundException("");
        }
        String sqlQuery = "delete from film_likes where user_id = ?";
        jdbcTemplate.update(sqlQuery, userId);
    }


    public Collection<Film> getPopularFilms(Integer count) {
        String sqlQuery = "select film_id, count(user_id) from film_likes "
                + "group by film_id order by count(user_id) desc "
                + "limit ?";
        Collection<Film> collection = jdbcTemplate.query(sqlQuery, this::mapRowToLikes, count);
        if (collection.isEmpty()) {
            collection = filmDbStorage.findAll();
        }
        return collection;
    }

    private Film mapRowToLikes(ResultSet resultSet, int rowNum) throws SQLException {
        return filmDbStorage.getFilm(resultSet.getInt("film_id"));
    }


}
