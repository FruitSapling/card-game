import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Deck {

    public ReentrantLock deckLock;
    private int deckNumber;
    private ArrayList<Card> cardsInDeck = new ArrayList<>();

    public Deck(int deckNumber) {
        this.deckNumber = deckNumber;
        this.deckLock = new ReentrantLock();
    }

    public Deck(Card[] cards) {
        this.cardsInDeck = new ArrayList<Card> (Arrays.asList(cards));
        System.out.println(this.cardsInDeck);
    }

    public void addCard(Card card) {
        this.cardsInDeck.add(card);
    }

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

    public ArrayList<Card> getCardsInDeck() {
        return cardsInDeck;
    }

    public void setCardsInDeck(ArrayList<Card> cardsInDeck) {
        this.cardsInDeck = cardsInDeck;
    }

    public int getDeckNumber() { return deckNumber; }

}
