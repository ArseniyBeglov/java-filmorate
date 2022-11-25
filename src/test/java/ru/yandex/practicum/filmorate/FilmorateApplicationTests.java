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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {



	@Test
	public void testFindUserById() throws ValidationException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
		FilmMpaDbStorage filmMpaDbStorage=new FilmMpaDbStorage(jdbcTemplate);
		FilmGenresDbStorage filmGenresDbStorage = new FilmGenresDbStorage(jdbcTemplate,filmMpaDbStorage);
		UserService userService= new UserService(userDbStorage, new UserFriendDbStorage(jdbcTemplate));
		FilmService filmService = new FilmService(userService, new FilmDbStorage(jdbcTemplate,filmMpaDbStorage,filmGenresDbStorage),
				filmGenresDbStorage, new FilmLikeDbStorage(jdbcTemplate, filmMpaDbStorage, userDbStorage),
				filmMpaDbStorage);
		MPA mpa= new MPA(3,"R");
		List<Genres> list = new ArrayList<>();
		list.add(new Genres(1,"R"));
		list.add(new Genres(2,"NC"));
//		Film film = new Film(1, "new film","descp", LocalDate.of(1999,4,20),120,
//				mpa);
//		System.out.println(film);

	}

}
