package mtcg.Application;
import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private int ELO;
    private int coins;
    private ArrayList<Card> stack;
    private Deck deck;


    public User(String username, String password, int ELO, int coins) {
        setUsername(username);
        setPassword(password);
        setELO(ELO);
        setCoins(coins);

        setStack();
        setDeck();
    }


    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }


    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }


    public void setELO(int ELO) {
        this.ELO = ELO;
    }
    public int getELO() {
        return ELO;
    }


    public void setCoins(int coins) {
        this.coins = coins;
    }
    public int getCoins() {
        return coins;
    }


    public void setStack() {
        this.stack = new ArrayList<>();
    }


    public void setDeck() {
        this.deck = new Deck();
    }
    public Deck getDeck() { return deck; }


    public Card[] getDeckCards() {
        Card[] deckCards = deck.getDeckCards();

        return deckCards;
    }


    public Card[] getStackCards() {
        Card[] stackCards = this.stack.toArray(new Card[this.stack.size()]);

        return stackCards;
    }


    public void viewDeck() {
        deck.printDeck();
    }


    public void deckReselection(Card[] newCardsForDeck) {
        addCardsToDeck(newCardsForDeck);
    }


    public void deleteDeck() {
        deck.cleanDeck();
    }


    public void addCardsToStack(ArrayList<Card> cards) {
        this.stack = (ArrayList<Card>)cards.clone();
    }


    public void addCardsToDeck(Card[] deckCards) {
        this.deck = deck.initializeDeck(deckCards);
    }


    public boolean checkStack() {
        Card[] cardsFromStack = this.stack.toArray(new Card[this.stack.size()]);

        if(cardsFromStack.length == 0) {
            return false;
        }
        else {
            return true;
        }
    }


    public boolean checkDeck() {
        return deck.checkDeck();
    }


    public boolean viewStack() {
        Card[] cardsFromStack = this.stack.toArray(new Card[this.stack.size()]);

        if(cardsFromStack.length == 0) {
            System.out.println("\nLooks like your stack is empty!");
            System.out.println("You can acquire cards by buying a package in the marketplace.");

            return false;
        }

        System.out.println("\nYour stack:\n");
        for(int i=0; i<cardsFromStack.length; i++) {
            System.out.println(cardsFromStack[i].getCardInfo() + "\n");
        }

        return true;
    }
}
