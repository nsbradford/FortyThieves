package nsbradford;

import static org.junit.Assert.*;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

import org.junit.Test;

public class TestMoveDealCardToWaste {

	@Test
	public void testMoveDealCardToWaste() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		Card topCard = fortyThieves.deck.peek();
		MoveDealCardToWaste dcm = new MoveDealCardToWaste(fortyThieves.deck, fortyThieves.wastePile);
	
		assertTrue(dcm.valid(fortyThieves));
		
		dcm.doMove(fortyThieves);
		assertEquals(topCard, fortyThieves.wastePile.peek());
		assertEquals(63, fortyThieves.deck.count());
		
		dcm.undo(fortyThieves);
		assertEquals(64, fortyThieves.deck.count());

	}

}
