package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserFriendDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Getter
public class UserService   {

    @Qualifier("userDbStorage")
    private final UserDbStorage userDbStorage;
    @Qualifier("userFriendDbStorage")
    private final UserFriendDbStorage userFriendDbStorage;

    public UserService(UserDbStorage userDbStorage, UserFriendDbStorage userFriendDbStorage) {
        this.userDbStorage = userDbStorage;
        this.userFriendDbStorage = userFriendDbStorage;
    }

    public User addFriend(Integer id, Integer friendId) throws ValidationException {
        if(userDbStorage.getUser(friendId).isPresent() && userDbStorage.getUser(friendId).isPresent() ){
            return userFriendDbStorage.create(id,friendId);
        } else {
            throw new ValidationException("неверный запрос");
        }
    }

    public Collection<User> getUserFriendsById(Integer id) {
        return userFriendDbStorage.getUserFriends(id);
    }

    public void delete(Integer id, Integer friendId) throws ValidationException {
        userFriendDbStorage.delete(id, friendId);
    }

}
