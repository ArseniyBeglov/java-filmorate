package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
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
    public Optional<MPA> getMpa(Integer id)  {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from rating_mpa where id = ?", id);

        // обрабатываем результат выполнения запроса
        if(filmRows.next()) {
            MPA mpa= new MPA(
                    filmRows.getInt("id"),
                    filmRows.getString("name "));
            return Optional.of(mpa);
        } else {
            return Optional.empty();
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
