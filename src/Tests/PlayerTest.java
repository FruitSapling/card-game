import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;
    Deck deckLeft,deckRight;
    CardGame cardGame;

    @Before
    public void setup() {
        cardGame = new CardGame();
    }

    @Test
    public void testFalseHasWinningDeck() {
        deckLeft = new Deck();
        deckRight = new Deck();

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
        deckLeft = new Deck();
        deckRight = new Deck();

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
    public void discardCard() {
        deckLeft = new Deck();
        deckRight = new Deck();

        for (int i = 0; i < 4; i++) {
            deckLeft.addCard(new Card(i));
            deckRight.addCard(new Card(4-i));
        }


        player = new Player(1,deckLeft,deckRight);

        player.drawCard();
        player.drawCard();
        player.drawCard();
        player.drawCard();

        player.discardCard();

        assertEquals(false, player.hasWinningDeck());
    }
}