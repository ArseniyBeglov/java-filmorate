package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
@Component("filmLikeDbStorage")
public class FilmLikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmLikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public Collection<Film> getPopularFilms(Integer count) {
        String sqlQuery = "select film_id , count(user_id)" +
                "from film_likes group by film_id order by count(user_id) desc " + "limit ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser,count);
    }
    private Film mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return getFilm(resultSet.getInt("film_id"));
    }

    public void delete(Integer id, Integer userId) throws ValidationException {
        String sqlQuery = "delete from film_likes where user_id = ?";
        jdbcTemplate.update(sqlQuery, userId) ;
    }

    private Film getFilm(Integer id)  {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        // обрабатываем результат выполнения запроса
        if(filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name "),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date"),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rating_id "));
            return film;
        } else {

            return null;
        }
    }

}
