package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user) throws ValidationException;
    User put( User user) throws ValidationException;
    Collection<User> findAll();
    User delete(User user) throws ValidationException;
}
