import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    private File packFile;
    private int numberOfPlayers;

    private Card[] cards;
    private Deck[] decks;
    private Player[] players;


    public CardGame(File packFile, int numberOfPlayers) {
        this.packFile = packFile;
        this.numberOfPlayers = numberOfPlayers;
        this.cards = new Card[numberOfPlayers * 8];
    }


    public void readFile() throws IOException {

        ArrayList<Integer> cards = new ArrayList<>();
        FileReader instream = new FileReader(packFile);
        BufferedReader reader = new BufferedReader(instream);

        while (reader.ready()) {
            String str = reader.readLine();
            if (str != null) {
                try {
                    cards.add(Integer.parseInt(str));
                }
                catch (NumberFormatException e){
                    throw new NumberFormatException();
                }
            }
            else {
                reader.close();
                instream.close();
            }
        }


        if (cards.size() == (numberOfPlayers * 8)) {
            for (int i = (numberOfPlayers * 8)-1; i > -1; i--) {
                this.cards[i] = new Card(cards.get(i));
            }
        }
        else {
            throw new FileNotFoundException();
        }

    }



    public void dealCards(Player[] players, Deck[] decks) throws Exception {

        if (players.length == numberOfPlayers && decks.length == numberOfPlayers) {
            this.players = players;
            this.decks = decks;
        }

        for (int i = 0; i < (numberOfPlayers*4); i++) {
            if (!players[i%numberOfPlayers].addCard(cards[i])) {
                throw new Exception();
            }
        }

        for (int j = (numberOfPlayers*4); j < (numberOfPlayers*8); j++) {
            decks[j%numberOfPlayers].addCard(cards[j]);
        }


    }







    public static void main(String[] args) {

        CardGame gameMain = null;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Number of players: ");

        int numberOfPlayers = Integer.parseInt(scanner.nextLine());

        Deck[] decks = new Deck[numberOfPlayers];
        Player[] players = new Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            decks[i] = new Deck();
        }

        for (int j = 0; j < numberOfPlayers; j++) {
            int k = 0;
            if (j == numberOfPlayers-1) {
                k = 0;
            }
            else {
                k = j+1;
            }
            players[j] = new Player((j+1),decks[j],decks[k]);
        }

        String filePath = null;
        String msg = "Location of pack file: ";
        while (filePath == null) {
            System.out.print(msg);
            filePath = scanner.nextLine();

            File file = new File(filePath);

            if (!file.canRead()) {
                filePath = null;
                msg = "Couldn't find file. Enter a valid file location: ";
            }
            else {
                gameMain = new CardGame(file,numberOfPlayers);
                try {
                    gameMain.readFile();
                }
                catch (IOException e)
                {
                    msg = "File input error. Is there the right amount of numbers in the file? Enter a valid file location: ";
                    filePath = null;
                }
            }
        }
        try {
            gameMain.dealCards(players,decks);
        }
        catch (Exception e) {

        }


        for (int i = 0; i < numberOfPlayers; i++) {
            Thread thread = new Thread(players[i]);
            thread.start();
            try {
                thread.join();
            }
            catch (InterruptedException e) {

            }

        }


    }
}
