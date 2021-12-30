package mtcg.Classes;
import java.io.IOException;
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


    public void viewDeck() {
        if(deck.checkDeck()) {
            System.out.println("deck is empty");
        }
        else {
            System.out.println("deck is not empty");
        }
    }


    public void editDeck(char deckOption) {
        if(deckOption == 'a') {
            // TODO: select card to add to deck
        }
        else {
            // TODO: select card to remove from deck
        }
    }


    public void deleteDeck() {
        deck.cleanDeck();
    }
}
