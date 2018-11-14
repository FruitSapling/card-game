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
        player = new Player(1,deckLeft,deckRight);
        cardGame = new CardGame();
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
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(2),new Card(1)}));
        deckLeft.setCardsInDeck(cards);

        player.drawCard();

        assertEquals(false,player.getCards().isEmpty());
        //assertEquals(cards.get(2), player.getCards().get(0));
    }


    @Test
    public void testDiscardCard() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(Arrays.asList(new Card[] {new Card(1),new Card(1),new Card(2),new Card(1),new Card(1)}));
        player.setCards(cards);

        player.discardCard();

        cards.remove(2);


        assertEquals(cards,player.getCards());
    }


    @Test
    public void testWriteToFile() throws Exception {
        Class playerClass = player.getClass();
        Method writeToFile = playerClass.getDeclaredMethod("writeToFile");

        writeToFile.setAccessible(true);

        player.fileOutput.add("TESTING123");
        writeToFile.invoke(player);

        BufferedReader reader = new BufferedReader(new FileReader(new File("src/Assets/player1_output.txt")));
        String text = reader.readLine();
        reader.close();

        assertEquals(true,text.equals("TESTING123"));
    }
}