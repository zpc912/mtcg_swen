package mtcg.Application;
import mtcg.Database.Postgres;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class CardGenerator {
    private Card[] allCards;


    public CardGenerator() throws SQLException {
        // MONSTER CARDS:
        /*Card stoneteeth = new Card("Stoneteeth", 20, cardType.MONSTER, elementType.NORMAL);
        Card nightspawn = new Card("Nightspawn", 24, cardType.MONSTER, elementType.NORMAL);
        Card dustghoul = new Card("Dustghoul", 25, cardType.MONSTER, elementType.FIRE);
        Card hellface = new Card("Hellface", 23, cardType.MONSTER, elementType.FIRE);
        Card hauntsnake = new Card("Hauntsnake", 20, cardType.MONSTER, elementType.WATER);
        Card soulserpent = new Card("Soulserpent", 22, cardType.MONSTER, elementType.WATER);

        // SPELL CARDS:
        Card demonicScream = new Card("Demonic Scream", 18, cardType.SPELL, elementType.NORMAL);
        Card tunnelVision = new Card("Tunnel Vision", 22, cardType.SPELL, elementType.NORMAL);
        Card fireFingers = new Card("Fire Fingers", 24, cardType.SPELL, elementType.FIRE);
        Card phoenixBreath = new Card("Phoenix Breath", 21, cardType.SPELL, elementType.FIRE);
        Card waterRush = new Card("Water Rush", 19, cardType.SPELL, elementType.WATER);
        Card surge = new Card("Surge", 20, cardType.SPELL, elementType.WATER);

        // Put all cards in an array:
        allCards = new Card[12];
        allCards[0] = stoneteeth;
        allCards[1] = nightspawn;
        allCards[2] = dustghoul;
        allCards[3] = hellface;
        allCards[4] = hauntsnake;
        allCards[5] = soulserpent;
        allCards[6] = demonicScream;
        allCards[7] = tunnelVision;
        allCards[8] = fireFingers;
        allCards[9] = phoenixBreath;
        allCards[10] = waterRush;
        allCards[11] = surge;*/


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
