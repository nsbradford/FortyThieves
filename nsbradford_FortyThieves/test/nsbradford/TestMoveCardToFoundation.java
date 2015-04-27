package nsbradford;

import static org.junit.Assert.*;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;
import org.junit.Test;

/**
 * Runs tests on MoveCardToFoundation, both doMove and undo.
 * 
 * @author Nicholas
 *
 */
public class TestMoveCardToFoundation {

	@Test
	public void testMoveCardToFoundation() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		Card topCard = new Card(1,1);
		
		MoveCardToFoundation cardToFoundation = 
				new MoveCardToFoundation(fortyThieves.column[1], topCard, fortyThieves.foundationPile[1]);
	
		// fortyThieves.column[1].get()
		assertTrue(cardToFoundation.valid(fortyThieves));
		
		cardToFoundation.doMove(fortyThieves);
		
		assertEquals(topCard, fortyThieves.foundationPile[1].peek());
		assertEquals(1, fortyThieves.foundationPile[1].count());
		
		cardToFoundation.undo(fortyThieves);
		assertEquals(topCard, fortyThieves.column[1].peek());
		assertEquals(5, fortyThieves.column[1].count());
		assertEquals(0, fortyThieves.foundationPile[1].count());
	}

}
