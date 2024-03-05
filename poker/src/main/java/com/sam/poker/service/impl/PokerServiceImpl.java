package com.sam.poker.service.impl;


import java.util.*;
import java.util.stream.Collectors;

import com.sam.poker.entity.*;
import com.sam.poker.service.HandService;
import com.sam.poker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sam.poker.service.PokerService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PokerServiceImpl implements PokerService {

    private static final int COMMON_HAND_SIZE = 3;
    @Autowired
    private UserService userService;

    @Autowired
    private HandService handService;

    private List<Card> deck;

    private Table table;

    @Override
    public PokerHand calculateRank(List<Card> cardList) {

        return handService.calculateRank(cardList);
    }

    private void shuffleCards(List<Card> cardList) {

        for(int i = 0; i < 5; i++){
            List<Card> cardNum1 = cardList.subList(0, cardList.size()/2);
            List<Card> cardNum2 = cardList.subList(cardList.size()/2,  cardList.size());
            Collections.shuffle(cardNum1);
            Collections.shuffle(cardNum2);

            cardNum1.addAll(cardNum2);
            Collections.shuffle(cardNum1);
            cardList = cardNum1;
        }

    }

    @Override
    public List<Card> initializeCards() {
        
        deck = new ArrayList<>();

        for(Suit suit : Suit.values()) {
            for(Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }

        shuffleCards(deck);

        return deck;
    }


    @Override
    public void distributeCards() {

        int j = 0;

        //Distribute cards
        for(int i = 0; i < 2; i++) {
            for(User user : userService.retrieveUserList()) {
                user.getCardAtHand().add(deck.remove(j));
                j++;
            }
        }

        //Display all
        for(User user : userService.retrieveUserList())
        {
            log.info("User {}: Cards: {}", user.getName(), user.getCardAtHand());
        }

    }

    @Override
    public void processGame() {
        Map<User, PokerHand> results = new HashMap<>();

        List<Card> commonCards = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            commonCards.add(deck.remove(i));
        }

        for(User user : userService.retrieveUserList()) {
            takeBet(user, 10);
        }

        log.info("Common Cards: {}", commonCards);

        for(User user : userService.retrieveUserList())
        {
            List<List<Card>> finalList = generateCombinations(commonCards);

            PokerHand result = PokerHand.HIGH_CARD;


            for(List<Card> tempCard : finalList){
                List<Card> tempCardList = new ArrayList<>();
                tempCardList.addAll(tempCard);
                tempCardList.addAll(user.getCardAtHand());

                PokerHand pokerHand = calculateRank(tempCardList);
                result = pokerHand.getValue() > result.getValue() ? pokerHand : result;

                log.info("Hand: {}, Result: {}", tempCardList, pokerHand);
            }
            results.put(user, result);
        }

        log.info("Common Cards: {}", commonCards);
        findWinner(results);

        log.info(results.toString());

    }

    private void findWinner(Map<User, PokerHand> results) {

        if (!areAllResultsEqual(results.values())) {
            Optional<Map.Entry<User, PokerHand>> maxWinner = results.entrySet().stream().max(Map.Entry.comparingByValue());

            if(maxWinner.isPresent()){
                maxWinner.get().getKey().setWallet(maxWinner.get().getKey().getWallet() + table.getPot());
                log.info("The winner is {}, {}", maxWinner.get().getKey().getName(), maxWinner.get().toString());
            }
        }
        else {
            findMaxCard(new ArrayList<>(results.keySet()));
        }
    }

    private void findMaxCard(List<User> users) {
        Card maxRank = users.stream().flatMap(user -> user.getCardAtHand().stream()).max((obj1, obj2) -> Integer.compare(obj1.getRank().getValue(), obj2.getValue())).get();

        //Get other values

        log.info("Max Card: {}", maxRank);
    }

    private boolean areAllResultsEqual(Collection<PokerHand> values) {
        return values.stream().distinct().count() <= 1;
    }

    private void takeBet(User user, int money) {
        table.setPot(table.getPot() + money);
        user.setWallet(user.getWallet() - money);
    }

    @Override
    public void initializeTable() {
        table = new Table();

        table.setBettingStates(BettingStates.PREFLOP);
    }


    private List<List<Card>> generateCombinations(List<Card> cardList){

        List<List<Card>> finalList = new ArrayList<>();

        gatherAllCombos(cardList, new ArrayList<>(), finalList, 0);

        return finalList;
    }

    private void gatherAllCombos(List<Card> cardList, List<Card> dummyList, List<List<Card>> finalList, int start) {
        if(dummyList.size() == COMMON_HAND_SIZE){
            finalList.add(new ArrayList<>(dummyList));
            return;
        }

        for(int i = start; i < cardList.size(); i++)
        {
            dummyList.add(cardList.get(i));
            gatherAllCombos(cardList, dummyList, finalList, i+1);
            dummyList.remove(dummyList.size() - 1);
        }
    }



}
