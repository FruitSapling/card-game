import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class DeckTest {

    Deck deck;

    @Before
    public void setUp() {
        deck = new Deck(1);
    }

    @Test
    public void testRemoveCardWithoutPreferredValue() {
        Card[] cards = {new Card(1),new Card(2),new Card(2),new Card(2)};
        ArrayList<Card> arrCards = new ArrayList<>();
        arrCards.addAll(Arrays.asList(cards));

        deck.setCardsInDeck(arrCards);
        assertEquals(cards[0],deck.removeCard(3));
    }

    @Test
    public void testRemoveCardWithPreferredValue() {
        Card[] cards = {new Card(2),new Card(2),new Card(1),new Card(2)};
        ArrayList<Card> arrCards = new ArrayList<>();

        arrCards.addAll(Arrays.asList(cards));

        deck.setCardsInDeck(arrCards);
        assertEquals(cards[2],deck.removeCard(1));
    }

    @Test
    public void testHasCardsEmpty() {
        assertEquals(false,deck.hasCards());
    }


    @Test
    public void testAddCard() {
        Card card1 = new Card(1);

        deck.addCard(card1);

        assertEquals(true,deck.hasCards());
    }
}