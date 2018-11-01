import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    public static int getNumPlayers() {
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

    private static Card[] getPack(int numberOfPlayers) {
        String errorMessage = "Please enter a valid path name to the pack file.";

        System.out.print("File path of pack file: ");
        Scanner scanner = new Scanner(System.in);


        File packFile;

        try {
            packFile = new File(scanner.nextLine());
        } catch (Exception e) {
            System.out.println(errorMessage);
            return getPack(numberOfPlayers);
        }

        Card[] cards = new Card[numberOfPlayers];

        int cardIndex = 0;

        try {
            FileReader instream = new FileReader(packFile);
            BufferedReader reader = new BufferedReader(instream);

            while (reader.ready()) {
                String str = reader.readLine();
                if (str != null) {
                    cards[cardIndex] = new Card(Integer.parseInt(str));
                    cardIndex++;
                }
                else {
                    reader.close();
                    instream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was an error while parsing your file. " +
                    "Please ensure it is a text file with 8n lines, each " +
                    "containing a positive integer between 0 and n.");
            return getPack(numberOfPlayers);
        }

        return cards;
    }

    public static void main(String[] args) {
        int numberOfPlayers = getNumPlayers();
        Card[] pack = getPack(numberOfPlayers);
    }
}
