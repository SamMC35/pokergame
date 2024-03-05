package com.sam.poker.service;

import java.util.List;

import com.sam.poker.entity.User;

public interface UserService {

    public void addUser(User user);
    public List<User> retrieveUserList();
}