import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
    public void testHasCardsFalse() {

        assertEquals(false,deck.hasCards());

    }

    @Test
    public void testHasCardsTrue() {
        ArrayList<Card> arrayList = new ArrayList<>();
        arrayList.add(new Card(1));

        deck.setCardsInDeck(arrayList);
        assertEquals(true,deck.hasCards());

    }

    @Test
    public void testAddCard() {
        Card card1 = new Card(1);

        deck.addCard(card1);

        assertEquals(true,deck.hasCards());
    }

    @Test
    public void testWriteToFile() {
        Card[] cards = {new Card(2),new Card(2),new Card(2),new Card(2),new Card(2),new Card(2)};
        ArrayList<Card> arrCards = new ArrayList<>();
        arrCards.addAll(Arrays.asList(cards));

        deck.setCardsInDeck(arrCards);

        deck.writeToFile();

        String readin = "";

        try {
            BufferedReader in = new BufferedReader(new FileReader(new File("deck1_output.txt")));
            readin = in.readLine();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String expected = "Final cards left in deck: 2 2 2 2 2 2 ";
        System.out.println(readin);

        assertEquals(true, expected.equals(readin));

    }
}