package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userService.getInMemoryUserStorage().findAll().size());
        return userService.getInMemoryUserStorage().findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return userService.getInMemoryUserStorage().create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) throws ValidationException {

        return userService.getInMemoryUserStorage().put(user);
    }

    @DeleteMapping
    public User delete(@Valid @RequestBody User user)  {
        return userService.getInMemoryUserStorage().delete(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId)  {
        return userService.addFriend(id,friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId)  {
        return userService.delete(id,friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> allFriends(@PathVariable Integer id)  {
        return userService.findAll(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id)  {
        return userService.getInMemoryUserStorage().usersContainUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> commonFriends(@PathVariable Integer id,@PathVariable Integer otherId) {
        return userService.findAll(id).stream().filter(userService.findAll(otherId)::contains).
                collect(Collectors.toList());
    }
}
