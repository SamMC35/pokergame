package com.sam.poker.entity;

import lombok.Data;

import java.util.List;

@Data
public class Table {
    private int pot;
    private BettingStates bettingStates;
    private List<Card> cardsOnTable;
}
