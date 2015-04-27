package nsbradford;

import static org.junit.Assert.*;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.launcher.Main;
import org.junit.Test;

/**
 * Runs tests on MoveColumnToColumn, both doMove and undo.
 * 
 * @author Nicholas
 *
 */
public class TestMoveColumnToColumn {

	@Test
	public void testMoveColumnToColumn() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		Card topCard = new Card(1,1); // pretend this was on top of the waste
		Column myColumn = new Column(); // a column of 1 card being moved
		myColumn.add(topCard);
		
		MoveColumnToColumn myMove = 
				new MoveColumnToColumn(fortyThieves.column[1], fortyThieves.column[2], myColumn, 1);
	
		assertEquals(4, fortyThieves.column[1].count());
		
		fortyThieves.column[2].add(new Card(2,1));
		assertTrue(myMove.valid(fortyThieves));
		
		myMove.doMove(fortyThieves);
		assertEquals(topCard, fortyThieves.column[2].peek());
		assertEquals(4, fortyThieves.column[1].count());
		assertEquals(6, fortyThieves.column[2].count());
		
		myMove.undo(fortyThieves);
		assertEquals(topCard, fortyThieves.column[1].peek());
		assertEquals(5, fortyThieves.column[1].count());
		assertEquals(5, fortyThieves.column[2].count());
		
		// second case
		MoveColumnToColumn myMove2 = 
				new MoveColumnToColumn(fortyThieves.column[1], fortyThieves.column[2], null, 1);
		
		myMove2.doMove(fortyThieves);
		assertEquals(topCard, fortyThieves.column[2].peek());
		assertEquals(4, fortyThieves.column[1].count());
		assertEquals(6, fortyThieves.column[2].count());
		
		myMove.undo(fortyThieves);
		assertEquals(topCard, fortyThieves.column[1].peek());
		assertEquals(5, fortyThieves.column[1].count());
		assertEquals(5, fortyThieves.column[2].count());
		
	}
}
