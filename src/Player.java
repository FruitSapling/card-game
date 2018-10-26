import java.util.ArrayList;
import java.util.Random;

public class Player {

    private ArrayList<Card> cards = new ArrayList<>();
    private int playerNumber;

    private Deck leftDeck, rightDeck;

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck) {
        this.playerNumber = playerNumber;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
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
