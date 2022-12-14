package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final FilmMpaDbStorage filmMpaDbStorage;
    private final FilmGenresDbStorage filmGenresDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMpaDbStorage filmMpaDbStorage, FilmGenresDbStorage filmGenresDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMpaDbStorage = filmMpaDbStorage;
        this.filmGenresDbStorage = filmGenresDbStorage;
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(filmMpaDbStorage.getMpa(resultSet.getInt("rating_id")))
                .build();
        if (filmGenresDbStorage.getGenresToFilm(resultSet.getInt("id")) != null) {
            film.setGenres(filmGenresDbStorage.getGenresToFilm(resultSet.getInt("id")));
        }
        return film;

    }

    @Override
    public Film create(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) || film.getName().isBlank() ||
                film.getDescription().length() > 201 || film.getDuration() < 0) {
            throw new ValidationException("Error");
        }
        String sqlQuery = "insert into films( name, description,release_date, duration, rating_id ) " +
                "values ( ?, ?,?,?,?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        if (film.getGenres() != null) {
            createGenresForFilm(film);
        }
        log.info("ggwp");
        log.info(getFilmByName(film.getName()).toString());
        return getFilmByName(film.getName());
    }

    private void createGenresForFilm(Film film) {
        String sqlQuery2 = "insert into film_genres( film_id, genre_id) " +
                "values ( ?, ?)";
        for (Genres genres : film.getGenres()) {
            jdbcTemplate.update(sqlQuery2,
                    getFilmByName(film.getName()).getId(),
                    genres.getId());
        }
    }

    @Override
    public Film put(Film film) throws ValidationException {
        if (getFilm(film.getId()) != null) {
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
            if (film.getGenres() != null) {
                updateGenresForFilm(film);
            }
            return getFilm(film.getId());
        } else {
            throw new ObjectNotFoundException("????????????????????????  ???? ????????????.");
        }
    }

    private void updateGenresForFilm(Film film) {
        if (filmGenresDbStorage.getGenresToFilm(film.getId()) != null) {
            String sqlQuery = "delete from film_genres where film_id = ?";
            jdbcTemplate.update(sqlQuery, film.getId());
        }

        String sqlQuery2 = "insert into film_genres( film_id, genre_id) " +
                "values ( ?, ?)";

        for (Genres genres : new LinkedHashSet<>(film.getGenres())) {
            jdbcTemplate.update(sqlQuery2,
                    getFilmByName(film.getName()).getId(),
                    genres.getId());
        }
    }

    @Override
    public Film delete(Film film) throws ValidationException {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return film;
    }

    public Film getFilm(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        // ???????????????????????? ?????????????????? ???????????????????? ??????????????
        if (filmRows.next()) {
            Film film = Film.builder().
                    id(filmRows.getInt("id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(filmRows.getDate("release_date").toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(filmMpaDbStorage.getMpa(filmRows.getInt("rating_id")))
                    .build();

            log.info("???????????? ??????????: {} {}", film.getId(), film.getName());
            if (filmGenresDbStorage.getGenresToFilm(filmRows.getInt("id")) != null) {
                film.setGenres(filmGenresDbStorage.getGenresToFilm(filmRows.getInt("id")));
            }
            return film;
        } else {
            log.info("?????????? ?? ?????????????????????????????? {} ???? ????????????.", id);
            throw new ObjectNotFoundException("??????????  ???? ????????????.");
        }
    }

    public Film getFilmByName(String name) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where name = ?", name);

        // ???????????????????????? ?????????????????? ???????????????????? ??????????????
        if (filmRows.next()) {
            Film film = Film.builder().
                    id(filmRows.getInt("id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(filmRows.getDate("release_date").toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(filmMpaDbStorage.getMpa(filmRows.getInt("rating_id")))
                    .build();

            log.info("???????????? ??????????: {} {}", film.getId(), film.getName());
            if (filmGenresDbStorage.getGenresToFilm(filmRows.getInt("id")) != null) {
                film.setGenres(filmGenresDbStorage.getGenresToFilm(filmRows.getInt("id")));
            }
            return film;
        } else {
            log.info("?????????? ?? ???????????? {} ???? ????????????.", name);
            throw new ObjectNotFoundException("??????????  ???? ????????????.");
        }
    }
}
