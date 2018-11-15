import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Deck {

    public ReentrantLock deckLock;
    private int deckNumber;
    private ArrayList<Card> cardsInDeck = new ArrayList<>();
    private File outputFile;

    public Deck(int deckNumber) {
        this.deckNumber = deckNumber;
        this.deckLock = new ReentrantLock();
        this.outputFile = new File("src/Assets/deck" + deckNumber + "_output.txt");
    }

    public Deck(int deckNumber, Card[] cards) {
        this.cardsInDeck = new ArrayList<>(Arrays.asList(cards));
        this.deckNumber = deckNumber;
        this.outputFile = new File("src/Assets/deck" + deckNumber + "_output.txt");
        System.out.println(this.cardsInDeck);
    }


    public void addCard(Card card) {
        this.cardsInDeck.add(card);
    }


    /*
    * A method to remove a card from the current deck.
    * It either returns the first card that has a value that matches the player's preferred value,
    * or it returns the first card in the deck. Before then removing the returned card from the current deck.
    *
    * PARAMS:
    *   - preferredValue - The player's preferred value which is the same as the player number to look for.
    * RETURNS:
    *   - Card - A card from the current deck.
    * */
    public Card removeCard(int preferredValue) {

        for(Card card : cardsInDeck) {
            if (card.getValue() == preferredValue) {
                cardsInDeck.remove(card);
                return card;
            }
        }

        Card removedCard = cardsInDeck.get(0);
        cardsInDeck.remove(0);
        return removedCard;
    }


    public boolean hasCards() {
        if (!this.cardsInDeck.isEmpty()) return true;
        else return false;
    }

    /*
    * A method to write a string to the outputFile who's path is specified in the constructor.
    * */
    public void writeToFile() {
        String msg = "Final cards left in deck: ";
        try {
            PrintStream out = new PrintStream(outputFile);
            for (int i = 0; i < cardsInDeck.size(); i ++) {
                msg += cardsInDeck.get(i) + " ";
            }
            out.println(msg);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Card> getCardsInDeck() {
        return cardsInDeck;
    }

    public void setCardsInDeck(ArrayList<Card> cardsInDeck) {
        this.cardsInDeck = cardsInDeck;
    }

    public int getDeckNumber() { return deckNumber; }

}
