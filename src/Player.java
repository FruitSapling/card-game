import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Runnable{

    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<String> fileOutput = new ArrayList<>();
    private int playerNumber;
    private String name;
    private Deck leftDeck, rightDeck;
    private CardGame cardGame;
    private File outputFile;
    public volatile boolean running;

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck, CardGame cardGame) {
        this.playerNumber = playerNumber;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.cardGame = cardGame;
        this.name = "Player " + playerNumber;
        this.outputFile = new File("src/Assets/player" + playerNumber + "_output.txt");
    }

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck) {
        this.playerNumber = playerNumber;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.name = "Player " + playerNumber;
        this.outputFile = new File("src/Assets/player" + playerNumber + "_output.txt");
    }

    @Override
    public void run() {
        running = true;
        initialWriteToFile();

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

        finalWriteToFile();

    }

    public void checkDeck() {
        if (hasWinningDeck()) {
            fileOutput.add(name + " wins");
            cardGame.interruptPlayers();
        }
        else {
            writeHandToFile();
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
        Card card = leftDeck.removeCard(playerNumber);
        cards.add(card);
        fileOutput.add(name + " draws a " + card.getValue() + " from deck " + leftDeck.getDeckNumber());
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

        Card card = cards.get(randInt);
        rightDeck.addCard(card);

        fileOutput.add(name + " discards a " + card.getValue() + " to deck " + rightDeck.getDeckNumber());

        cards.remove(randInt);

    }

    private void initialWriteToFile () {
        String msg = name + " initial hand: ";
        for (Card card:cards) {
            msg += card.getValue();
        }

        fileOutput.add(msg);
    }


    private void finalWriteToFile() {
        String msg = name + " final hand: ";
        for (Card card:cards) {
            msg += card.getValue() + " ";
        }

        fileOutput.add(name + " exits");
        fileOutput.add(msg);

        writeToFile();
    }


    private void writeHandToFile() {
        String msg = name + " current hand is ";

        for (Card card:cards) {
            msg += card.getValue() + " ";
        }

        fileOutput.add(msg);
    }


    private void writeToFile() {
        try {
            PrintStream out = new PrintStream(outputFile);
            for (int i = 0; i < fileOutput.size(); i ++) {
                out.println(fileOutput.get(i));
            }
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
