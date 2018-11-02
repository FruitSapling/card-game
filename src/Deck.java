import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Deck {

    public ArrayList<Card> getCardsInDeck() {
        return cardsInDeck;
    }

    public void setCardsInDeck(ArrayList<Card> cardsInDeck) {
        this.cardsInDeck = cardsInDeck;
    }

    private ArrayList<Card> cardsInDeck = new ArrayList<>();

    public Deck() {}

    public void addCard(Card card) {
        this.cardsInDeck.add(card);
    }


    public Card removeCard(int preferedValue) {

        for(Card card : cardsInDeck) {
            if (card.getValue() == preferedValue) {
                cardsInDeck.remove(card);
                return card;
            }
        }

        Card removedCard = cardsInDeck.get(0);
        cardsInDeck.remove(0);
        return removedCard;
    }

}
