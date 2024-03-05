package com.sam.poker.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class User {    
    private Long id;
    private String name;
    private int wallet;
    private List<Card> cardAtHand;


    public User(Long id, String name, int wallet) {
        this.id = id;
        this.name = name;
        this.wallet = wallet;
        this.cardAtHand = new ArrayList<>();
    }
}