package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.ZoneId;
import java.util.Optional;
@Component
public class UserDaoImpl implements UserDao{
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<User> findUserById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);

        // обрабатываем результат выполнения запроса
        if(userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));

            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}
