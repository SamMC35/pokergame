package com.sam.poker.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.poker.entity.Card;
import com.sam.poker.entity.Rank;
import com.sam.poker.entity.Suit;
import com.sam.poker.service.PokerService;

@RestController
public class PokerController {

    @Autowired
    private PokerService pokerService;
    
    @GetMapping("/")
    public String showHomePage()
    {
        List<Card> cardList = new ArrayList<>();

        cardList.add(new Card(Suit.CLUBS, Rank.QUEEN));
        cardList.add(new Card(Suit.HEARTS, Rank.QUEEN));
        cardList.add(new Card(Suit.DIAMONDS, Rank.QUEEN));
        cardList.add(new Card(Suit.HEARTS, Rank.SEVEN));
        cardList.add(new Card(Suit.DIAMONDS, Rank.SEVEN));

        return pokerService.calculateRank(cardList).toString();
    }
}
