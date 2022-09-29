package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class UserFriends {
    private int id;
    private int userId;
    private int userFriendId;
}
