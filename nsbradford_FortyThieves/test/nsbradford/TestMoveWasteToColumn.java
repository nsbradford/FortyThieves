package nsbradford;

import static org.junit.Assert.*;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

import org.junit.Test;

public class TestMoveWasteToColumn {

	@Test
	public void testMoveWasteToColumn() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		Card topCard = new Card(1,1); // pretend this was on top of the waste
		
		MoveWasteToColumn cardToFoundation = 
				new MoveWasteToColumn(fortyThieves.wastePile, topCard, fortyThieves.column[1]);
	
		assertEquals(4, fortyThieves.column[1].count());
		
		fortyThieves.column[1].add(new Card(2,1));
		assertTrue(cardToFoundation.valid(fortyThieves));
		
		cardToFoundation.doMove(fortyThieves);
		assertEquals(topCard, fortyThieves.column[1].peek());
		assertEquals(6, fortyThieves.column[1].count());
		
		cardToFoundation.undo(fortyThieves);
		assertEquals(topCard, fortyThieves.wastePile.peek());
		assertEquals(1, fortyThieves.wastePile.count());
		assertEquals(5, fortyThieves.column[1].count());
	}

}
