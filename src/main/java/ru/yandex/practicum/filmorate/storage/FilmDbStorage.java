package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage{
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final FilmMpaDbStorage filmMpaDbStorage;
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMpaDbStorage filmMpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMpaDbStorage = filmMpaDbStorage;
    }
    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    private Film mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(filmMpaDbStorage.getMpa(resultSet.getInt("rating_id")))
                .build();
    }

    @Override
    public Film create(Film film) throws ValidationException {
        String sqlQuery = "insert into films( name, description,release_date, duration, rating_id ) " +
                "values ( ?, ?,?,?,?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        return findAll().stream().filter(x->x.getName().equals(film.getName())).findFirst().get();
    }

    @Override
    public Film put(Film film) throws ValidationException {
        if(getFilm(film.getId())!=null) {
            String sqlQuery = "update films set " +
                    " name = ?, description = ?,release_date = ?,duration=?,rating_id=? " +
                    "where id = ?";

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            return film;
        } else {
            throw new ObjectNotFoundException("Пользователь  не найден.");
        }
    }

    @Override
    public Film delete(Film film) throws ValidationException {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, film.getId()) ;
        return film;
    }

    public Film getFilm(Integer id)  {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        // обрабатываем результат выполнения запроса
        if(filmRows.next()) {
            Film film = Film.builder().
                    id(filmRows.getInt("id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(filmRows.getDate("release_date").toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(filmMpaDbStorage.getMpa(filmRows.getInt("rating_id")))
                    .build();

            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            return film;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Пользователь  не найден.");
        }
    }
}
