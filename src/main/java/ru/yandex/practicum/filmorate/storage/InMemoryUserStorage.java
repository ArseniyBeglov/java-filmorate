package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Integer, User> users = new HashMap<>();
    private int idCount=0;
    private int makeNewId(){
        return ++idCount;
    }
    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User delete(User user) {
        if(users.containsKey(user.getId())){
            users.remove(user.getId());
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public User create( User user) throws ValidationException {
        if(user.getBirthday().after(Date.from(Instant.now()))) {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }
        if(user.getId()==null){
            user.setId(makeNewId());
        }
        if(!StringUtils.hasText(user.getName())){
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User put( User user) throws ValidationException {
        if( user.getBirthday().after(Date.from(Instant.now()))) {
            throw new ValidationException("Пользователь не соответсвует критериям.");
        }
        if(users.containsKey(user.getId())){
            users.put(user.getId(), user);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public User getUser(Integer id)  {
        if(users.containsKey(id)){
            return  users.get(id);
        } else{
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public Collection<User> getUserFriends(User user){
        //return users.values().stream().filter(p-> user.getFriendIds().contains(p.getId())).collect(Collectors.toList());
        return users.values().stream().collect(Collectors.toList());
    }

    public User removeFriend(Integer id, Integer friendId)  {
        User userMain=getUser(id);
        User userFriend=getUser(friendId);
//        if(userMain.getFriendIds()==null){
//            userMain.setFriendIds(new HashSet<>());
//            userMain.getFriendIds().remove(friendId);
//        } else{
//            userMain.getFriendIds().remove(friendId);
//        }
//        if(userFriend.getFriendIds()==null){
//            userFriend.setFriendIds(new HashSet<>());
//            userFriend.getFriendIds().remove(id);
//        } else{
//            userFriend.getFriendIds().remove(id);
//        }

        return userMain;
    }
}
