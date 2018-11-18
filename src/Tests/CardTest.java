import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {
    Card card;

    @Before
    public void setup() {
        card = new Card(4);
    }

    @Test
    public void testGetValue() {

        assertEquals(4,card.getValue());

    }


    @Test
    public void testToString() {

        assertEquals("4",card.toString());

    }


    @Test
    public void testEqualsTrue() {

        Card card2 = new Card(4);
        assertEquals(true, card.equals(card2));

    }

    @Test
    public void testEqualsFalse() {

        Card card2 = new Card(3);
        assertEquals(false, card.equals(card2));

    }
}