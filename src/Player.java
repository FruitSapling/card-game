import java.util.ArrayList;
import java.util.Random;

public class Player implements Runnable{

    private ArrayList<Card> cards = new ArrayList<>();
    private int playerNumber;
    private Deck leftDeck, rightDeck;
    private CardGame cardGame;
    public volatile boolean running;

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck, CardGame cardGame) {
        this.playerNumber = playerNumber;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.cardGame = cardGame;
    }

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck) {
        this.playerNumber = playerNumber;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if (leftDeck.hasCards()) {
                System.out.println(this.playerNumber + "has cards!");
                if (leftDeck.deckLock.tryLock()) {
                    System.out.println(this.playerNumber + "got lock!" + leftDeck.deckLock.getHoldCount());
                    drawCard();
                    checkDeck();
                    discardCard();
                    leftDeck.deckLock.unlock();
                } else System.out.println(this.playerNumber + "NO lock!");
            }
        }
    }

    public synchronized void checkDeck() {
        if (hasWinningDeck()) {
            System.out.println(this.playerNumber + " has won");
            cardGame.interruptPlayers();
        }
        else {
            this.notifyAll();
        }
    }

    public boolean hasWinningDeck() {
        boolean won = false;
        int winningCardCount = 0;
        for (Card c: cards) {
            if (c.getValue() == this.playerNumber) winningCardCount+=1;
        }
        if (winningCardCount>=4) won=true;

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


    public Deck getLeftDeck() {
        return leftDeck;
    }

    public void setLeftDeck(Deck leftDeck) {
        this.leftDeck = leftDeck;
    }

    public Deck getRightDeck() {
        return rightDeck;
    }

    public void setRightDeck(Deck rightDeck) {
        this.rightDeck = rightDeck;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }


}
