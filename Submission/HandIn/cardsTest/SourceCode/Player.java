import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player implements Runnable{

    private ArrayList<Card> cards = new ArrayList<>();
    public ArrayList<String> fileOutput = new ArrayList<>();
    public int playerNumber;
    public String name;
    private Deck leftDeck, rightDeck;
    private CardGame cardGame;
    private File outputFile;
    public int turnsHad, turnsAllowed;

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck, CardGame cardGame) {
        this.playerNumber = playerNumber;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.cardGame = cardGame;
        this.name = "Player " + playerNumber;
        this.outputFile = new File("player" + playerNumber + "_output.txt");
    }

    public Player(int playerNumber, Deck leftDeck, Deck rightDeck) {
        this.playerNumber = playerNumber;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.name = "Player " + playerNumber;
        this.outputFile = new File("player" + playerNumber + "_output.txt");
    }

    @Override
    public void run() {
        turnsHad = 0;

        initialWriteToFile();

        //Check if initial hand is a winning hand.
        checkDeck();

        //while the game is still running, keep having turns.
        while (cardGame.gameRunning.get()) {
            haveTurn();
        }

        cardGame.incrementFinishedPlayers();

        //Wait until the cardGame notifies it to continue.
        synchronized (this) {
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Catch up to the player who had the most turns.
        turnsAllowed = cardGame.turnsAllowed;

        while (turnsHad < turnsAllowed) {
            haveTurn();
        }
        if (this != cardGame.winner)
            fileOutput.add(cardGame.winner.name + " has informed " + name + " that " + cardGame.winner.name + " has won");
        else {
            System.out.println(name + " won");
        }

        finalWriteToFile();

        cardGame.incrementFinishedPlayers();

    }


    /*
    * This method is a 'turn' for a player, it makes the player:
    *   - Draw a card
    *   - Discard a card
    *   - Check for a winning hand
    * However this only occurs if the deck to the left of the player contains any cards
    * and if the lock on that deck has been released by all other players.
    * */
    public void haveTurn() {
        if (leftDeck.hasCards()) {
            if (leftDeck.deckLock.tryLock()) {
                drawCard();
                discardCard();
                checkDeck();
                turnsHad += 1;
                leftDeck.deckLock.unlock();
            }
        }
    }

    /*
    * A method to check the current player's hand, if a winning deck is held
    * the method sets the CardGame's 'gameRunning' variable to false and the 'winner' to the current player.
    * However, if the player doesn't hold a winning hand it writes the current hand to the fileOutput.
    * */
    public void checkDeck() {
        //if the game is running, and they have won, start ending game.
        if (cardGame.gameRunning.get() && hasWinningDeck()) {
            cardGame.winner = this;
            cardGame.gameRunning.set(false);
            fileOutput.add(name + " wins");
        } else {
            writeHandToFile();
        }
    }

    /*
    * This method counts the number of cards that match the player number.
    * If the player holds more than 4 cards of the same value as the player number then
    * @return Returns boolean depending on if the current hand is a 'winning hand'.
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
    * @return Returns boolean depending on if adding was successful.
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
    * The removed card can't be one that the player prefers.
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
    * Method to add to the fileOutput the initial starting strings.
    * */
    public void initialWriteToFile () {
        String msg = name + " initial hand: ";
        for (Card card:cards) {
            msg += card.getValue() + " ";
        }

        fileOutput.add(msg);
    }


    /*
     * Method to add to the fileOutput the final ending strings.
     * */
    public void finalWriteToFile() {
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
    public void writeHandToFile() {
        String msg = name + " current hand is ";

        for (Card card:cards) {
            msg += card.getValue() + " ";
        }

        fileOutput.add(msg);
    }


    /*
     * Method to write every part in the fileOutput ArrayList to a line in the output text file.
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
