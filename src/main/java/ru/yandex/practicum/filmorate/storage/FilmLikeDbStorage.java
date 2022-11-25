package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
    public FilmLikeDbStorage(JdbcTemplate jdbcTemplate, FilmMpaDbStorage filmMpaDbStorage, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMpaDbStorage = filmMpaDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public void create(Integer id, Integer userId) throws ValidationException {
        String sqlQuery = "insert into film_likes(film_id, user_id) " +
                "values ( ?, ?)";
        jdbcTemplate.update(sqlQuery,
                id,userId);

    }
    public void put(Integer id, Integer userId) throws ValidationException {
        String sqlQuery = "update film_likes set " +
                " film_id=?, user_id=?" +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery
                ,id,userId,id);

    }
    public void delete(Integer id, Integer userId) throws ValidationException {
        if(!userDbStorage.getUser(userId).isPresent()){
            throw new ObjectNotFoundException("");
        }
        String sqlQuery = "delete from film_likes where user_id = ?";
        jdbcTemplate.update(sqlQuery, userId) ;
    }


    public Collection<Film> getPopularFilms(Integer count) {
        String sqlQuery = "select f.*, count(fl.user_id) from films as f " +
                "left outer join film_likes as fl on f.id=fl.film_id group by f.id=fl.film_id order by count(fl.user_id)" +
                "" + "limit ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToLikes,count);
    }
    private Film mapRowToLikes(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(filmMpaDbStorage.getMpa(resultSet.getInt("rating_id")))
                .build();
    }


}
