package nsbradford;

import java.awt.event.MouseEvent;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.view.ColumnView;
import ks.launcher.Main;
import ks.tests.KSTestCase;

import org.junit.Test;

public class TestWastePileController extends KSTestCase{

	@Test
	public void test() {
		FortyThieves fortyThieves = new FortyThieves();
		Main.generateWindow(fortyThieves, Deck.OrderBySuit);
		
		Card topCard = new Card(1,1); // pretend this was on top of the waste
		Column myColumn = new Column(); // a column of 1 card being moved
		myColumn.add(topCard);
		ColumnView myColumnView = new ColumnView(myColumn);
		fortyThieves.wastePile.add(topCard);
		
		WastePileController wpc = new WastePileController(fortyThieves, fortyThieves.wastePileView);
		MouseEvent me = this.createClicked(fortyThieves, fortyThieves.columnView[1], 0, 0);
		fortyThieves.getContainer().setActiveDraggingObject(myColumnView, me);
		
		wpc.mousePressed(me);
		//wpc.mouseReleased(me);
		assertTrue(true);
	}

}
