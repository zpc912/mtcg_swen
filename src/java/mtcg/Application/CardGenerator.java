package mtcg.Application;
import mtcg.Database.Postgres;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class CardGenerator {
    private Card[] allCards;


    public CardGenerator() throws SQLException {
        Postgres db = new Postgres();
        allCards = db.getAllCards();
    }


    public Card[] shuffleCards() {
        Card[] cardsToShuffle; // Array-copy of all cards
        Card[] shuffledCards = new Card[5]; // Array with the shuffled cards

        cardsToShuffle = allCards;
        Collections.shuffle(Arrays.asList(cardsToShuffle)); // Using built-in method for shuffling the cards

        for(int i=0; i<shuffledCards.length; i++) {
            shuffledCards[i] = cardsToShuffle[i];
        }

        return shuffledCards;
    }
}
