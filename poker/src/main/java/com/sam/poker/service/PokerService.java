package com.sam.poker.service;

import java.util.List;

import com.sam.poker.entity.Card;
import com.sam.poker.entity.PokerHand;

public interface PokerService {
    
    public PokerHand calculateRank(List<Card> cardList);
    
    public List<Card> initializeCards();

    public void distributeCards();

    public void processGame();

    public void initializeTable();
}
