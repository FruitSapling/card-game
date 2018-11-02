import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CardGameTest {

    CardGame cardGame;

    @Before
    public void setUp() {
        cardGame = new CardGame();
        cardGame.numberOfPlayers = 4;
        //give it a pack of cards, and all the other stuff you do in main
        cardGame.pack= new Card[] {
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4)
        };
    }

    @Test
    public void testGetDecks() {
        cardGame.decks = cardGame.getDecks();

        //ensure the method has filled 'decks' with 4 decks
        assertEquals(cardGame.decks.length, cardGame.numberOfPlayers);

    }

    @Test
    public void testGetPlayers() {
        cardGame.players = cardGame.getPlayers();

        //ensure the method has filled 'players' with 4 players
        assertEquals(cardGame.players.length, cardGame.numberOfPlayers);
    }

    @Test
    public void testDealCards() {
        Deck[] expectedDecks = new Deck[] {
                new Deck(new Card[] {new Card(1), new Card(1), new Card(1), new Card(1)}),
                new Deck(new Card[] {new Card(2), new Card(2), new Card(2), new Card(2)}),
                new Deck(new Card[] {new Card(3), new Card(3), new Card(3), new Card(3)}),
                new Deck(new Card[] {new Card(4), new Card(4), new Card(4), new Card(4)})
        };
    }
}