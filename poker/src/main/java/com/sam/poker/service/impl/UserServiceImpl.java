package com.sam.poker.service.impl;

import com.sam.poker.entity.User;
import com.sam.poker.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    private List<User> userList;

    @PostConstruct
    public void initialize()
    {
        userList = new ArrayList<>();
    }

    @Override
    public void addUser(User user) {
        userList.add(user);
    }

    @Override
    public List<User> retrieveUserList() {
        return userList;
    }
}
