package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("filmGenresDbStorage")
public class FilmGenresDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMpaDbStorage filmMpaDbStorage;
    public FilmGenresDbStorage(JdbcTemplate jdbcTemplate, FilmMpaDbStorage filmMpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMpaDbStorage = filmMpaDbStorage;
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

    public Collection<Genres> getGenresToFilm(Integer filmId){
        String sqlQuery = "select * from film_genres";
        List<FilmGenres> list= jdbcTemplate.query(sqlQuery, this::mapRowToGenres);
        Collection<Genres> genres= new ArrayList<>();
        for(FilmGenres filmGenres: list){
            if(filmGenres.getFilmId()==filmId){
                genres.add(getGenre(filmGenres.getGenreId()));
            }
        }
        return genres;
    }
    private FilmGenres mapRowToGenres(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmGenres.builder()
                .filmId(resultSet.getInt("film_id"))
                .genreId(resultSet.getInt("genre_id"))
                .build();
    }

    public Collection<Genres> findAll() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    private Genres mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
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
