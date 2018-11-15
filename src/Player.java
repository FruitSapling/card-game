import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Runnable{

    private ArrayList<Card> cards = new ArrayList<>();
    public ArrayList<String> fileOutput = new ArrayList<>();
    private int playerNumber;
    public String name;
    private Deck leftDeck, rightDeck;
    private CardGame cardGame;
    private File outputFile;
    public int turnsHad;
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
        turnsHad = 0;
        initialWriteToFile();
        //checkDeck();

        while (running) {
            if (leftDeck.hasCards()) {
                //System.out.println(this.playerNumber + "has cards!");
                if (leftDeck.deckLock.tryLock()) {
                    //System.out.println(this.playerNumber + "got lock!" + leftDeck.deckLock.getHoldCount());
                    drawCard();
                    checkDeck();
                    discardCard();
                    turnsHad += 1;
                    leftDeck.deckLock.unlock();
                } //else System.out.println(this.playerNumber + "NO lock!");
            }
        }

        finalWriteToFile();

    }


    /*
    * A method to check the current player's hand, if a winning deck is held
    * the method calls a method from the CardGame class.
    * However, if the player doesn't hold a winning hand it writes the current hand to the fileOutput.
    * */
    public void checkDeck() {
        if (hasWinningDeck()) {
            cardGame.interruptPlayers(this);
        }
        else {
            writeHandToFile();
        }
    }

    /*
    * This method counts the number of cards that match the player number.
    * If the player holds more than 4 cards of the same value as the player number then
    * it returns true otherwise it returns false.
    * */
    public boolean hasWinningDeck() {
        boolean won = false;
        int winningCardCount = 0;
        for (Card c: cards) {
            if (c.getValue() == this.playerNumber) winningCardCount+=1;
        }
        if (winningCardCount>=4) won=true;

        return won;
    }

    /*
    * Used when dealing cards to add a card to the players hand.
    * */
    public boolean addCard(Card card) {
        if (cards.size() < 4) {
            cards.add(card);
            return true;
        }
        else {
            return false;
        }
    }

    /*
    * This method takes a card from the deck to the player's left.
    * If possible it takes a card which has the same value as the playerNumber,
    * Otherwise it takes the first card in the deck and adds it to the current deck held.
    * */
    public void drawCard() {
        Card card = leftDeck.removeCard(playerNumber);
        cards.add(card);
        fileOutput.add(name + " draws a " + card.getValue() + " from deck " + leftDeck.getDeckNumber());
    }

    /*
    * This method removes a card from the player's current hand.
    * The removed card can't be one that the player perfers.
    * Of the cards that the player doesn't prefer it picks one at random to discard to the
    * deck on the player's right side.
    * */
    public void discardCard() {
        ArrayList<Card> throwableCards = new ArrayList<>();

        for (Card card: cards) {
            if (card.getValue() != playerNumber) {
                throwableCards.add(card);
            }
        }

        Random random = new Random();
        Card card = cards.get(0);

        if (throwableCards.size() > 0) {
            int randInt = random.nextInt(throwableCards.size());
            card = throwableCards.get(randInt);
        }

        rightDeck.addCard(card);

        fileOutput.add(name + " discards a " + card.getValue() + " to deck " + rightDeck.getDeckNumber());

        cards.remove(card);

    }

    /*
    * Method to add to the fileOutput the game starting strings.
    * */
    private void initialWriteToFile () {
        String msg = name + " initial hand: ";
        for (Card card:cards) {
            msg += card.getValue() + " ";
        }

        fileOutput.add(msg);
    }


    /*
     * Method to add to the fileOutput the game ending strings.
     * */
    private void finalWriteToFile() {
        String msg = name + " final hand: ";
        for (Card card:cards) {
            msg += card.getValue() + " ";
        }

        fileOutput.add(name + " exits");
        fileOutput.add(msg);

        writeToFile();
    }


    /*
     * Method to add to the fileOutput the player's current hand.
     * */
    private void writeHandToFile() {
        String msg = name + " current hand is ";

        for (Card card:cards) {
            msg += card.getValue() + " ";
        }

        fileOutput.add(msg);
    }


    /*
     * Method to write every part in the fileOutput arraylist to a line in the output textfile.
     * */
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
