import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    private File packFile;
    private int numberOfPlayers;
    public Card[] cards;


    public CardGame(File packFile, int numberOfPlayers) {
        this.packFile = packFile;
        this.numberOfPlayers = numberOfPlayers;
        this.cards = new Card[numberOfPlayers * 8];
    }


    public void readFile() throws IOException {

        ArrayList<Integer> cards = new ArrayList<>();
        InputStream inStream = null;
        boolean reading = true;

        inStream = new FileInputStream(packFile);

        while (reading) {
            int cardValue = inStream.read();
            if (cardValue != -1) {
                cards.add(cardValue);
                System.out.println(cardValue);
            }
            else {
                reading = false;
            }
        }



        System.out.println(cards.size());


        if (cards.size() == (numberOfPlayers * 8)) {
            for (int i = (numberOfPlayers * 8)-1; i > -1; i--) {
                this.cards[i] = new Card(cards.get(i));
            }
        }
        else {
            throw new FileNotFoundException();
        }

    }







    public static void main(String[] args) {

        CardGame gameMain = null;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Number of players: ");

        int numberOfPlayers = Integer.parseInt(scanner.nextLine());

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

        System.out.println(numberOfPlayers);
        System.out.println(filePath);

        for (int i = 0; i < (numberOfPlayers * 8); i++) {
            System.out.println(gameMain.cards[i].getValue());
        }

    }
}
