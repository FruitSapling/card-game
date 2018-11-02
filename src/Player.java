import java.util.ArrayList;
import java.util.Random;

public class Player implements Runnable{

    private ArrayList<Card> cards = new ArrayList<>();
    private int playerNumber;

    public Deck getLeftDeck() {
        return leftDeck;
    }

    public void setLeftDeck(Deck leftDeck) {
        this.leftDeck = leftDeck;
    }

    private Deck leftDeck;

    public Deck getRightDeck() {
        return rightDeck;
    }

    public void setRightDeck(Deck rightDeck) {
        this.rightDeck = rightDeck;
    }

    private Deck rightDeck;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck) {
        this.playerNumber = playerNumber;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        for (Card card:cards) {
            System.out.println(card.getValue());
        }
    }

    public boolean hasWinningDeck() {
        boolean won = true;
        int cardCollecting = 0;

        if (cards.size() == 4) {
            for (Card card:cards) {
                if (cardCollecting != 0) {
                    if (card.getValue() != cardCollecting) {
                        won = false;
                    }
                }
                else {
                    cardCollecting = card.getValue();
                }
            }
        }
        else {
            won = false;
        }


        return won;
    }

    public boolean addCard(Card card) {
        if (cards.size() < 4) {
            cards.add(card);
            return true;
        }
        else {
            return false;
        }
    }


    public void drawCard() {
        cards.add(leftDeck.removeCard(playerNumber));
    }


    public void discardCard() {
        ArrayList<Card> throwableCards = new ArrayList<>();

        for (Card card: cards) {
            if (card.getValue() != playerNumber) {
                throwableCards.add(card);
            }
        }

        Random random = new Random();
        int randInt = random.nextInt(throwableCards.size());

        rightDeck.addCard(cards.get(randInt));
        cards.remove(randInt);

    }


}
