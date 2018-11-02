import java.util.ArrayList;
import java.util.Arrays;
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

    public Deck(Card[] cards) {
        this.cardsInDeck = new ArrayList<Card> (Arrays.asList(cards));
        System.out.println(this.cardsInDeck);
    }

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

//    @Override
//    public String toString() {
//        String s = "Deck: ";
//        for (Card c: cardsInDeck) {
//            s += c.toString() + ",";
//        }
//        return s;
//    }

}
