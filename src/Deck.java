import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Deck {

    private ArrayList<Card> cardsInDeck = new ArrayList<>();

    public Deck() {}


    public void addCard(Card card) {
        this.cardsInDeck.add(card);
    }


    public Card removeCard(int preferedValue) {

        for(Card card : cardsInDeck) {
            if (card.getValue() == preferedValue) {
                return card;
            }
        }

        return cardsInDeck.get(0);
    }

}
