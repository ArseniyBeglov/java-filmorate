package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
public class UserService  implements UserStorage {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addFriend(Integer id, Integer friendId)  {

        User userMain=inMemoryUserStorage.usersContainUser(id);
        User userFriend=inMemoryUserStorage.usersContainUser(friendId);
        if(userMain.getFriendIds()==null){
            userMain.setFriendIds(new HashSet<>());
            userMain.getFriendIds().add(friendId);
        } else{
            userMain.getFriendIds().add(friendId);
        }
        if(userFriend.getFriendIds()==null){
            userFriend.setFriendIds(new HashSet<>());
            userFriend.getFriendIds().add(id);
        } else{
            userFriend.getFriendIds().add(id);
        }

        return userMain;
    }

    public Collection<User> findAll(Integer id) {
        User userMain=inMemoryUserStorage.usersContainUser(id);
        if(userMain.getFriendIds()==null){
            userMain.setFriendIds(new HashSet<>());
        }
        Set<Integer> list=userMain.getFriendIds();
        return inMemoryUserStorage.findAll().stream().filter(p-> list.contains(p.getId())).collect(Collectors.toList());
    }

    public User delete(Integer id, Integer friendId) {
        User userMain=inMemoryUserStorage.usersContainUser(id);
        User userFriend=inMemoryUserStorage.usersContainUser(friendId);
        if(userMain.getFriendIds()==null){
            userMain.setFriendIds(new HashSet<>());
            userMain.getFriendIds().remove(friendId);
        } else{
            userMain.getFriendIds().remove(friendId);
        }
        if(userFriend.getFriendIds()==null){
            userFriend.setFriendIds(new HashSet<>());
            userFriend.getFriendIds().remove(id);
        } else{
            userFriend.getFriendIds().remove(id);
        }

        return userMain;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public User delete(User user)  {
        return null;
    }
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User put(User user)  {
        return null;
    }
}
