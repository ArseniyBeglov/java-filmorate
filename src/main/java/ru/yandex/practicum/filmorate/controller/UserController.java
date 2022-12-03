package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
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
        log.debug("Текущее количество пользователей: {}", userService.getUserDbStorage().findAll().size());
        return userService.getUserDbStorage().findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return userService.getUserDbStorage().create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) throws ValidationException {

        return userService.getUserDbStorage().put(user);
    }

    @DeleteMapping
    public User delete(@Valid @RequestBody User user) throws ValidationException {
        return userService.getUserDbStorage().delete(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId)
            throws ValidationException {
        return userService.addFriend(id,friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        userService.delete(id,friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> allFriends(@PathVariable Integer id)  {
        return userService.getUserFriendsById(id);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id)  {
        return userService.getUserDbStorage().getUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> commonFriends(@PathVariable Integer id,@PathVariable Integer otherId) {
        return userService.getUserFriendsById(id).stream().filter(userService.getUserFriendsById(otherId)::contains).
                collect(Collectors.toList());
    }
}
