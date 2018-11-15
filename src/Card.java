public class Card {

    private int value;

    public Card(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /*
    * Written an override toString() method to make printing the card value easier in arrays.
    * */
    @Override
    public String toString() {
        return String.valueOf(this.getValue());
    }

}
