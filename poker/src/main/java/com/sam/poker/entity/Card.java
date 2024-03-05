package com.sam.poker.entity;

import lombok.Data;

@Data
public class Card {
    private Suit suit;
    private Rank rank;

    
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public int getValue()
    {
        return rank.getValue();
    }


    @Override
    public String toString()
    {
        return rank + " of " + suit;
    }
}
