import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CardGameTest {

    CardGame cardGame;
    Class cardGameClass;

    @Before
    public void setUp() {
        cardGame = new CardGame();
        cardGame.numberOfPlayers = 4;
        //give it a pack of cards, and all the other stuff you do in main
        cardGame.pack = new Card[] {
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4),
                new Card(1),new Card(2),new Card(3),new Card(4)
        };

        cardGame.decks = new Deck[] {
                new Deck(1),
                new Deck(2),
                new Deck(3),
                new Deck(4)
        };

        cardGame.players = new Player[] {
                new Player(1,cardGame.decks[0],cardGame.decks[1],cardGame),
                new Player(1,cardGame.decks[1],cardGame.decks[2],cardGame),
                new Player(1,cardGame.decks[2],cardGame.decks[3],cardGame),
                new Player(1,cardGame.decks[3],cardGame.decks[0],cardGame),
        };

        cardGameClass = cardGame.getClass();

    }


    @Test
    public void testInterruptPlayers() throws Exception {
        Field running = cardGameClass.getDeclaredField("running");
        running.setAccessible(true);

        assertEquals(true, running.get(cardGame));

        //cardGame.tellPlayersToFinish();

        for (Player player:cardGame.players) {
            //assertEquals(false,player.running);
        }

        assertEquals(false, running.get(cardGame));
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
    public void testDealCards() throws Exception {
        Class deckClass = Deck.class;
        Field cards = deckClass.getDeclaredField("cardsInDeck");
        cards.setAccessible(true);

        ArrayList<Card> expectedCards = new ArrayList<>();
        expectedCards.addAll(Arrays.asList(new Card[]{new Card(1),new Card(1), new Card(1), new Card(1)}));

        cardGame.dealCards();

        assertEquals(cards.get(cardGame.decks[0]),expectedCards);

    }


}