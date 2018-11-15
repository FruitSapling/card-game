import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    public int numberOfPlayers;
    public Card[] pack;
    public Player[] players;
    public Deck[] decks;

    private boolean running = true;

    /*
    * A method to get the user to specify how many players are playing in the current game.
    * This method makes sure that the inputted value is an integer and if it isn't then it calls itself recursively.
    * Otherwise it returns the inputted numbers,
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

    /*
    * This method is called by the winning player thread.
    * It changes the running variable to false on every player object.
    * Then it adds the appropriate string to the output file of each player.
    * Finally, it calls each deck's 'writing to file' method.
    * */
    public void interruptPlayers(Player winner) {
        if (this.running) {

            this.running = false;

            for (Player p: players) {
                p.running = false;

                if (p != winner)
                    p.fileOutput.add(winner.name + " has informed " + p.name + " that " + winner.name + " has won");
                else
                    p.fileOutput.add(p.name + " wins");
            }

            System.out.println(winner.name + " wins");

            for (Deck deck:decks) {
                deck.writeToFile();
            }
        }
    }

    /*
    * This method gets the user to input a path to an appropriate file.
    * If the file path is valid then it reads the integer values from the pack file, checks they are valid then
    * it creates a card for each one.
    * It returns the arraylist of cards.
    * */
    private Card[] getPack() {
        String errorMessage = "Please enter a valid path name to the pack file.";

        System.out.print("File path of pack file: ");
        Scanner scanner = new Scanner(System.in);

        File packFile;

        try {
            //packFile = new File(scanner.nextLine());
            packFile = new File("C:/Users/bobby/Documents/University/Year 2/ECM2414 - Software Development/CA/card-game/src/Assets/packFile.txt");
            //packFile = new File("WILLEM'S PATH");
            System.out.println(packFile.getAbsoluteFile());

        } catch (Exception e) {
            System.out.println(errorMessage);
            return getPack();
        }

        Card[] cards = new Card[numberOfPlayers*8];

        try {
            FileReader instream = new FileReader(packFile);
            BufferedReader reader = new BufferedReader(instream);

            for (int i = 0; i < numberOfPlayers*8; i++) {
                String str = reader.readLine();
                //System.out.println(str);
                int cardValue = Integer.parseInt(str);

                //&& cardValue <= numberOfPlayers
                if (cardValue >  0) {
                    cards[i] = new Card(cardValue);
                } else {
                    throw new Exception();
                }
            }

            // if we have looped through 8*numberOfPlayers cards, and the
            // reader is still ready, then there are too many cards in
            // the file
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
    * This method creates the decks for the game.
    * */
    public Deck[] getDecks() {
        decks = new Deck[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            decks[i] = new Deck(i+1);
        }
        return decks;
    }

    /*
    * This method creates the player objects for the game.
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
    * This method creates and starts the thread for each player object.
    * */
    public void startGame() {

        for (int i = 0; i < numberOfPlayers; i++) {
            Thread thread = new Thread(players[i]);
            thread.start();
        }
    }


    public static void main(String[] args) {
        CardGame cardGame = new CardGame();
        cardGame.numberOfPlayers = cardGame.getNumPlayers();
        cardGame.pack = cardGame.getPack();
        cardGame.decks = cardGame.getDecks();
        cardGame.players = cardGame.getPlayers();
        cardGame.dealCards();

        cardGame.startGame();
    }
}
