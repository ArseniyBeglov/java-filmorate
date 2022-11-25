package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMpaDbStorage filmMpaDbStorage;
    private final FilmGenresDbStorage filmGenresDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;


    @Test
    public void testFindUserById() throws ValidationException {
        Collection<Genres> list = new ArrayList<>();
        list.add(new Genres(1, "name"));
        MPA mpa = new MPA(1, "mpa");
        Optional<Film> film = Optional.ofNullable(filmDbStorage.create(new Film(0, "name", "descrip",
                LocalDate.of(1999, 1, 1), 120, new MPA(1, "name"), list)));
        assertThat(film)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );

        System.out.println(film);

    }

}
