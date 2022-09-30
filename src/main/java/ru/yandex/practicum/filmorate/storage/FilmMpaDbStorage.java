package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component("filmMpaDbStorage")
public class FilmMpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmMpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public MPA getMpa(Integer id) {

        Collection<MPA> collection= findAll();
        if(collection.stream().filter(mpa -> mpa.getId()==id).findFirst().isPresent()){
            return collection.stream().filter(mpa -> mpa.getId()==id).findFirst().get();
        } else {
            throw new ObjectNotFoundException("такого mpa нет");
        }


    }

    public Collection<MPA> findAll() {
        String sqlQuery = "select * from rating_mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }



    private MPA mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
