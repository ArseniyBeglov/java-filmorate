package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
@Component("filmGenresDbStorage")
public class FilmGenresDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void create(Integer id, Integer genreId) throws ValidationException {
        String sqlQuery = "insert into film_genres(film_id, genre_id) " +
                "values ( ?, ?)";
        jdbcTemplate.update(sqlQuery,
                id,genreId);

    }
    public void put(Integer id, Integer genreId) throws ValidationException {
        String sqlQuery = "update film_genres set " +
                " film_id=?, genre_id=?" +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery
                ,id,genreId,id);

    }

    public void delete(Integer id, Integer genreId) throws ValidationException {
        String sqlQuery = "delete from film_genres where genre_id = ?";
        jdbcTemplate.update(sqlQuery, genreId) ;
    }

    public Collection<Genres> findAll() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }



    private Genres mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Genres.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public Genres getGenre(Integer id)  {
        Collection<Genres> genres = findAll();
        if(genres.stream().filter(p->p.getId()==id).findFirst().isPresent()){
            return genres.stream().filter(p->p.getId()==id).findFirst().get();
        }else{
            throw new ObjectNotFoundException("такого жанра нет");
        }

    }
}
