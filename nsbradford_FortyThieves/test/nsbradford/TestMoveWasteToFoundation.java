package nsbradford;

import static org.junit.Assert.*;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

import org.junit.Test;

public class TestMoveWasteToFoundation {

	@Test
	public void test() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		Card topCard = new Card(1,1); // pretend this was on top of the waste
		
		MoveWasteToFoundation cardToFoundation = 
				new MoveWasteToFoundation(fortyThieves.wastePile, topCard, fortyThieves.foundationPile[1]);
	
		// fortyThieves.column[1].get()
		assertTrue(cardToFoundation.valid(fortyThieves));
		
		cardToFoundation.doMove(fortyThieves);
		assertEquals(topCard, fortyThieves.foundationPile[1].peek());
		assertEquals(1, fortyThieves.foundationPile[1].count());
		
		cardToFoundation.undo(fortyThieves);
		assertEquals(topCard, fortyThieves.wastePile.peek());
		assertEquals(1, fortyThieves.wastePile.count());
		assertEquals(0, fortyThieves.foundationPile[1].count());
	}

}
