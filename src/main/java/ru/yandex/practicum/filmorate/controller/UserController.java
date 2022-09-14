package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if(user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }
        if(user.getId()==null){
            user.setId(1);
        }
        if(user.getName()==null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@Valid  @RequestBody User user) {
        if( user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }
        boolean flag=false;
        for(User use : users.values()){
            if(use.getId()==user.getId()){
                flag=true;
            }
        }
        if(flag){
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }


        return user;
    }
}
