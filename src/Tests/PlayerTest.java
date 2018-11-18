import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        cardGame = new CardGame();
        player = new Player(1,deckLeft,deckRight,cardGame);
    }


    @Test
    public void testHaveTurn() {
        deckLeft.addCard(new Card(1));

        player.haveTurn();

        assertEquals(1,player.turnsHad);
        assertEquals(false,deckLeft.hasCards());
        assertEquals(true,player.getCards().isEmpty());
    }

    @Test
    public void testFalseHasWinningDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(Arrays.asList(new Card[] {new Card(3),new Card(1),new Card(2),new Card(1),new Card(1)}));
        player.setCards(cards);

        assertEquals(false, player.hasWinningDeck());
    }


    @Test
    public void testTrueHasWinningDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(2),new Card(1),new Card(1)}));
        player.setCards(cards);

        assertEquals(true, player.hasWinningDeck());
    }


    @Test
    public void testAddCardTrue() {
        Card card = new Card(1);

        assertEquals(true, player.addCard(card));
        assertEquals(false, player.getCards().isEmpty());
    }


    @Test
    public void testAddCardFalse() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(2),new Card(1)}));
        player.setCards(cards);

        Card card = new Card(1);

        assertEquals(false,player.addCard(card));
    }


    @Test
    public void testDrawCard() {
        //setting up the deck
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(2),new Card(1)}));
        deckLeft.setCardsInDeck(cards);

        player.drawCard();

        assertEquals(false,player.getCards().isEmpty());
    }


    @Test
    public void testDiscardCard() {
        ArrayList<Card> testedCards = new ArrayList<>(), expectedCards = new ArrayList<>();
        testedCards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(2),new Card(1),new Card(1)}));

        player.setCards(testedCards);
        player.discardCard();

        //we expect the 2 to be discarded
        expectedCards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(1),new Card(1)}));

        assertArrayEquals(expectedCards.toArray(), testedCards.toArray());
    }


    @Test
    public void testDiscardCardWithOnlyPreferredValues() {
        ArrayList<Card> testedCards = new ArrayList<>(), expectedCards = new ArrayList<>();
        testedCards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(1),new Card(1),new Card(1)}));

        player.setCards(testedCards);
        player.discardCard();

        //we expect the 2 to be discarded
        expectedCards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(1),new Card(1)}));

        assertArrayEquals(expectedCards.toArray(), testedCards.toArray());
    }


    @Test
    public void testWriteToFile() {
        Class playerClass = player.getClass();
        String text = "";
        try {
            Method writeToFile = playerClass.getDeclaredMethod("writeToFile");

            writeToFile.setAccessible(true);

            player.fileOutput.add("TESTING123");
            writeToFile.invoke(player);

            BufferedReader reader = new BufferedReader(new FileReader(new File("src/Assets/player1_output.txt")));
            text = reader.readLine();
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(true,text.equals("TESTING123"));
    }

    @Test
    public void testCheckDeckWithWinningHand() {
        ArrayList<Card> testedCards = new ArrayList<>();
        testedCards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(2),new Card(1),new Card(1),new Card(1)}));

        player.setCards(testedCards);
        player.checkDeck();

        assertEquals(true, cardGame.winner.equals(player));
        assertEquals(false,cardGame.gameRunning.get());

    }
}