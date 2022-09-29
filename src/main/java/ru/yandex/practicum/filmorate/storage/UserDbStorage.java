package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.dao.UserDaoImpl;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage{
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private int idCount=0;
    private int makeNewId(){
        return ++idCount;
    }
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) throws ValidationException {
        String sqlQuery = "insert into users(id, email, login,name,birthday) " +
                "values (?, ?, ?,?,?)";
        if(user.getBirthday().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }
        if(user.getId()==null){
            user.setId(makeNewId());
        }
        if(!StringUtils.hasText(user.getName())){
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(sqlQuery,
                user.getId(),user.getEmail(),user.getLogin(),user.getName(),user.getBirthday());
        return user;
    }

    @Override
    public User put(User user) throws ValidationException {
        String sqlQuery = "update users set " +
                " email = ?, login = ?,name = ?,birthday=? " +
                "where id = ?";

        jdbcTemplate.update(sqlQuery
                ,user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User delete(User user) throws ValidationException {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, user.getId()) ;
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday"))
                .build();
    }

    public Optional<User> getUser(Integer id)  {
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
