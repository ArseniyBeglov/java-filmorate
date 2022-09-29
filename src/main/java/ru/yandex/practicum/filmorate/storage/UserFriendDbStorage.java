package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Collection;

@Component("userFriendDbStorage")
public class UserFriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserFriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public User create(Integer id, Integer friendId) throws ValidationException {
        String sqlQuery = "insert into user_friends(user_id, friend_id) " +
                "values ( ?, ?)";
        jdbcTemplate.update(sqlQuery,
                id,friendId);
        return getUser(id);
    }


    public void put(Integer id, Integer friendId) throws ValidationException {
        String sqlQuery = "update user_friends set " +
                " user_id=?, friend_id =?" +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery
                ,id,friendId,id);
    }


    public Collection<User> getUserFriends(Integer id) {
        String sqlQuery = "select friend_id " +
                "from user_friends where user_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser,id);
    }
    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return getUser(resultSet.getInt("friend_id"));
    }

    public void delete(Integer id, Integer friendId) throws ValidationException {
        String sqlQuery = "delete from user_friends where friend_id = ?";
        jdbcTemplate.update(sqlQuery, friendId) ;
    }

    private User getUser(Integer id)  {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        // обрабатываем результат выполнения запроса
        if(userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday"));
            return user;
        }
        return null;
    }
}
