package com.sam.poker.service.impl;

import com.sam.poker.entity.Card;
import com.sam.poker.entity.PokerHand;
import com.sam.poker.entity.Rank;
import com.sam.poker.service.HandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HandServiceImpl implements HandService {
    @Override
    public PokerHand calculateRank(List<Card> cardList) {
        log.info("Checking for hand: {}", cardList);

        try{

            if(cardList.size() < 5) {
                throw new Exception("Less than five cards");
            }

            cardList.sort(Comparator.comparingInt(Card::getValue));

            //check royal flush
            if(checkFlush(cardList)) {
                if(checkStraight(cardList)) {
                    if(cardList.get(0).getRank() == Rank.TEN && cardList.get(4).getRank() == Rank.ACE) {
                        return PokerHand.ROYAL_FLUSH;
                    }
                }
            }

            //check straight flush
            if(checkFlush(cardList) && checkStraight(cardList)) {
                return PokerHand.STRAIGHT_FLUSH;
            }

            //check four of a kind
            if(checkFourOfAKind(cardList)) {
                return PokerHand.FOUR_OF_A_KIND;
            }

            //check full house

            if(checkFullHouse(cardList)) {
                return PokerHand.FULL_HOUSE;
            }

            //check flush
            if(checkStraight(cardList)) {
                return PokerHand.STRAIGHT;
            }

            if(checkFlush(cardList)) {
                return PokerHand.FLUSH;
            }

            //check three of a kind

            if(checkThreeOfAKind(cardList)) {
                return PokerHand.THREE_OF_A_KIND;
            }

            //check pairs
            int pair = checkPair(cardList);

            if(pair == 2) {
                return PokerHand.TWO_PAIR;
            }else if(pair == 1){
                return PokerHand.PAIR;
            }else{
                return PokerHand.HIGH_CARD;
            }
        }
        catch(Exception e) {
            log.error("Error in calculateRank: ", e);
        }

        return null;
    }

    private boolean checkFourOfAKind(List<Card> cardList) {

        log.info("checking four of a kind");

        Map<Card, Integer> cardCount = new HashMap<>();

        for(Card card : cardList){
            cardCount.put(card, cardCount.getOrDefault(card, 0) + 1);
        }

        for(Map.Entry<Card, Integer> e : cardCount.entrySet()){
            if(e.getValue() >= 4){
                return true;
            }
        }

        return false;
    }

    private boolean checkFlush(List<Card> cardList) {
        if(areAllEqual(cardList.get(0).getSuit(), cardList.get(1).getSuit(),cardList.get(2).getSuit(),cardList.get(3).getSuit(),cardList.get(4).getSuit())){
            return true;
        }
        return false;
    }

    private boolean checkStraight(List<Card> cardList) {
        for(int i = 0; i < cardList.size()-1; i++)
        {
            if(cardList.get(i).getValue() + 1 == cardList.get(i+1).getValue())
            {
                continue;
            }else{
                return false;
            }
        }
        return true;
    }

    private boolean checkFullHouse(List<Card> cardList) {
        log.info("Checking for full house");

        Map<Integer, Integer> counts = new HashMap<>();

        for(Card num : cardList)
        {
            counts.put(num.getValue(), counts.getOrDefault(num.getValue(), 0) + 1);
        }

//        Collections.sort(cardList, (a, b) -> {
//            int aVal = a.getValue();
//            int bVal = b.getValue();
//            int countComparison = counts.get(aVal).compareTo(counts.get(bVal));
//            return countComparison != 0 ? countComparison : aVal-bVal;
//        });

        boolean threeOfAKindExists = false, pairExists = false;

        for(Map.Entry<Integer,Integer> e : counts.entrySet()) {
            if(e.getValue() == 2) {
                pairExists = true;
            }
            else if(e.getValue() == 3) {
                threeOfAKindExists = true;
            }
        }
        log.info(cardList.toString());
        return threeOfAKindExists && pairExists;
    }

    private boolean checkThreeOfAKind(List<Card> cardList) {
        log.info("Checking for three of a kind");

        for(int i = 2; i < cardList.size(); i++)
        {
            if(areAllEqual(cardList.get(i).getValue(), cardList.get(i-1).getValue(), cardList.get(i-2).getValue()))
            {
                return true;
            }
        }
        return false;
    }

    private int checkPair(List<Card> cardList) {

        log.info("Checking for pairs");

        int pair = 0;

        for(int i = 1; i < cardList.size(); i++)
        {
            if(cardList.get(i).getValue() == cardList.get(i-1).getValue())
            {
                pair++;
            }
            if(pair == 2)
                break;
        }
        return pair;
    }

    public static boolean areAllEqual(Object... objects) {
        // Check if all objects are null
        if (objects == null) {
            return false;
        }

        // If less than 2 objects, return false
        if (objects.length < 2) {
            return false;
        }

        // Check if all objects are equal
        Object firstObject = objects[0];
        for (Object obj : objects) {
            if (!obj.equals(firstObject)) {
                return false;
            }
        }

        return true;
    }
}
