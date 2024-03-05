package com.sam.poker;

import java.util.List;

import com.sam.poker.entity.User;
import com.sam.poker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sam.poker.entity.Card;
import com.sam.poker.service.PokerService;

@SpringBootTest
class PokerApplicationTests {

	@Autowired
	private PokerService pokerService;

	@Autowired
	private UserService userService;


	@Test
	void contextLoads() {
	}

	@Test
	void shuffleCards()
	{
		pokerService.initializeTable();
		List<Card> cardList = pokerService.initializeCards();
		System.out.println("Shuffled card order" + cardList);
//
//		//Distribute cards
//
//		userService.addUser(new User(23L, "Sam", 500));
//		userService.addUser(new User(12L, "Momo", 500));
//
//		pokerService.distributeCards();
//		pokerService.processGame();

	}

}
