import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CardGame {

    public int numberOfPlayers;
    public Card[] pack;
    public Player[] players;
    public Player winner;
    public Thread[] playerThreads;
    public Deck[] decks;

    public int turnsAllowed;
    public AtomicInteger finishedPlayers = new AtomicInteger(0);
    public AtomicBoolean gameRunning = new AtomicBoolean(true);

    /*
    * A method to get the user to specify how many players are playing in the current game.
    * This method makes sure that the inputted value is an integer and if it isn't then it calls itself recursively.
    * @return Returns the inputted number as an integer.
    * */
    public int getNumPlayers() {
        String errorMessage = "Please enter a valid number of players (1 to n, where n is a positive integer).";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Number of players: ");
        int numberOfPlayers;
        try {
            numberOfPlayers = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println(errorMessage);
            return getNumPlayers();
        }
        if (numberOfPlayers > 0) {
            return numberOfPlayers;
        } else {
            System.out.println(errorMessage);
            return getNumPlayers();
        }
    }


    //synchronized in case two threads attempt to simultaneously access finishedPlayers
    public synchronized void incrementFinishedPlayers() {
        finishedPlayers.incrementAndGet();
    }


    /*
    * This method gets the user to input a path to an appropriate file.
    * If the file path is valid then it reads the integer values from the pack file,
    * checks they are valid then it creates a card for each one.
    * @return Returns the array of cards.
    * */
    private Card[] getPack() {
        String errorMessage = "Please enter a valid path name to the pack file.";

        System.out.print("File path of pack file: ");
        Scanner scanner = new Scanner(System.in);

        File packFile;

        try {
            packFile = new File(scanner.nextLine());

        } catch (Exception e) {
            System.out.println(errorMessage);
            return getPack();
        }

        //Four cards for each player and each deck.
        Card[] cards = new Card[numberOfPlayers*8];

        try {
            FileReader instream = new FileReader(packFile);
            BufferedReader reader = new BufferedReader(instream);

            for (int i = 0; i < numberOfPlayers*8; i++) {
                String str = reader.readLine();
                int cardValue = Integer.parseInt(str);

                if (cardValue >  0) {
                    cards[i] = new Card(cardValue);
                } else {
                    throw new Exception();
                }
            }

            // if we have looped through 8*numberOfPlayers cards, and the
            // reader is still ready, then there are too many cards in the file.
            if (reader.readLine() != null) {
                throw new Exception();
            }

            reader.close();
            instream.close();

        } catch (Exception e) {
            System.out.println("There was an error while parsing your file. " +
                    "Please ensure it is a text file with 8n lines, each " +
                    "containing a positive integer between 0 and n.");
            return getPack();
        }

        return cards;
    }

    /*
    * This method creates the 'n' decks for the game.
    * @return Returns the array of 'n' decks.
    * */
    public Deck[] getDecks() {
        decks = new Deck[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            decks[i] = new Deck(i+1);
        }
        return decks;
    }

    /*
    * This method creates the 'n' player objects for the game.
    * @return Returns the array of 'n' players.
    * */
    public Player[] getPlayers() {
        players = new Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers-1; i++) {
            players[i] = new Player(i+1, decks[i], decks[i+1], this);
        }
        //last player instantiated here, to avoid indexOutOfBoundsError
        players[numberOfPlayers-1] = new Player(numberOfPlayers,
                decks[numberOfPlayers-1], decks[0], this);

        return players;
    }

    /*
    * This method gives every player 4 cards,
    * then it deals every deck 4 cards.
    * */
    public void dealCards() {
        try {
            for (int i = 0; i < (numberOfPlayers * 4); i++) {
                if (!players[i % numberOfPlayers].addCard(pack[i])) {
                    throw new Exception();
                }
            }

            for (int j = (numberOfPlayers * 4); j < (numberOfPlayers * 8); j++) {
                decks[j % numberOfPlayers].addCard(pack[j]);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * This method calls other methods which are needed to set the game up
     */

    public void setupGame() {
        numberOfPlayers = getNumPlayers();
        pack = getPack();
        decks = getDecks();
        players = getPlayers();
        dealCards();
    }


    /*
    * This method creates and starts the thread for each player object.
    * It then does nothing until a winner is declared, at which point
    * it allows the other players to catch up so they have all had the same number of turns.
    * Finally, once all players have finally finished it writes the deck output files.
    * */
    public void playGame() {

        this.setupGame();

        playerThreads = new Thread[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            Thread thread = new Thread(players[i]);
            playerThreads[i] = thread;
            thread.start();
        }

        while (gameRunning.get() || finishedPlayers.get() != numberOfPlayers) {

        }

        //Find player with most turns had to enable the others to catch up
        turnsAllowed = 0;

        for (int i = 0; i < numberOfPlayers; i++) {
            if (players[i].turnsHad > turnsAllowed)
                turnsAllowed = players[i].turnsHad;
        }

        finishedPlayers.set(0);

        //Notify all the players to continue catching up.
        for (Player p:players) {
            synchronized (p){
                p.notify();
            }
        }

        //Wait till the players are finished again
        while (finishedPlayers.get() != numberOfPlayers) {

        }

        for (Deck deck:decks) {
            deck.writeToFile();
        }

    }


    public static void main(String[] args) {
        CardGame cardGame = new CardGame();
        cardGame.playGame();
    }
}
