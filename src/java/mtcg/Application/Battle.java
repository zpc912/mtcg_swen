package mtcg.Application;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Battle {
    private User user1;
    private User user2;
    private Card[] deck1;
    private Card[] deck2;
    private int rounds;
    private ArrayList<String> battleLog;


    public Battle(User user1, User user2, Card[] deck1, Card[] deck2) {
        setUser1(user1);
        setUser2(user2);
        setDeck1(deck1);
        setDeck2(deck2);
        battleLog = new ArrayList<>();
    }


    public User getUser1() {
        return user1;
    }
    public void setUser1(User user1) {
        this.user1 = user1;
    }


    public User getUser2() {
        return user2;
    }
    public void setUser2(User user2) {
        this.user2 = user2;
    }


    public Card[] getDeck1() {
        return deck1;
    }
    public void setDeck1(Card[] deck1) {
        this.deck1 = deck1;
    }


    public Card[] getDeck2() {
        return deck2;
    }
    public void setDeck2(Card[] deck2) {
        this.deck2 = deck2;
    }


    public int getRounds() {
        return rounds;
    }
    public void setRounds(int rounds) {
        this.rounds = rounds;
    }


    public ArrayList<String> getBattleLog() {
        return battleLog;
    }


    public void updateBattleLog(String newLog) {
        // TODO: update battle log
    }


    public User fight() {
        System.out.println("fight");




        return null;
    }


    public int checkEffectiveness(Card card1, Card card2) {
        return -1;
    }


    public void fightMessages(User roundWinner, User roundLoser, Card card1, Card card2) {}


    public void updateScore(User battleWinner, User battleLoser) {}
}
