import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;
    Deck deckLeft,deckRight;
    CardGame cardGame;

    @Before
    public void setup() {
        deckLeft = new Deck(1);
        deckRight = new Deck(2);
        player = new Player(1,deckLeft,deckRight);
        cardGame = new CardGame();
    }

    @Test
    public void testFalseHasWinningDeck() {
        deckLeft = new Deck(1);
        deckRight = new Deck(2);

        for (int i = 0; i < 4; i++) {
            deckLeft.addCard(new Card(i+1));
            deckRight.addCard(new Card(4-i));
        }


        player = new Player(1,deckLeft,deckRight);

        player.drawCard();
        player.drawCard();
        player.drawCard();
        player.drawCard();

        assertEquals(false,player.hasWinningDeck());
    }


    @Test
    public void testTrueHasWinningDeck() {
        deckLeft = new Deck(1);
        deckRight = new Deck(2);

        for (int i = 0; i < 4; i++) {
            deckLeft.addCard(new Card(1));
            deckRight.addCard(new Card(4-i));
        }


        player = new Player(1,deckLeft,deckRight);

        player.drawCard();
        player.drawCard();
        player.drawCard();
        player.drawCard();

        assertEquals(true,player.hasWinningDeck());
    }


    @Test
    public void testDiscardCard() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(2),new Card(1),new Card(1)}));
        player.setCards(cards);

        player.discardCard();

        ArrayList<Card> newCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            newCards.add(new Card(1));
        }


        assertEquals(newCards,player.getCards());
    }
}