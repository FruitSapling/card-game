import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeckTest {

    Deck deck;

    @Before
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testRemoveCardWithoutPerferedValue() {
        Card card1 = new Card(3);
        Card card2 = new Card(2);
        Card card3 = new Card(3);

        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        assertEquals(card1,deck.removeCard(1));
    }

    @Test
    public void testRemoveCardWithPreferedValue() {
        Card card1 = new Card(1);
        Card card2 = new Card(2);
        Card card3 = new Card(3);

        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        assertEquals(card3,deck.removeCard(3));
    }

    @Test
    public void testAddCard() {
        Card card1 = new Card(1);

        deck.addCard(card1);

        assertEquals(card1,deck.removeCard(1));
    }
}