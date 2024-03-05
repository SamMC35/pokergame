package com.sam.poker.service;

import com.sam.poker.entity.Card;
import com.sam.poker.entity.PokerHand;

import java.util.List;

public interface HandService {
    public PokerHand calculateRank(List<Card> cardList);
}
