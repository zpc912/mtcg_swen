package mtcg.Classes;
import mtcg.Enum.elementType;
import java.util.Arrays;
import java.util.Collections;

public class CardGenerator {
    private Card[] allCards;


    public CardGenerator() {
        // MONSTER CARDS:
        MonsterCard m_stoneteeth = new MonsterCard("Stoneteeth", 20, elementType.NORMAL);
        MonsterCard m_nightspawn = new MonsterCard("Nightspawn", 24, elementType.NORMAL);
        MonsterCard m_dustghoul = new MonsterCard("Dustghoul", 25, elementType.FIRE);
        MonsterCard m_hellface = new MonsterCard("Hellface", 23, elementType.FIRE);
        MonsterCard m_hauntsnake = new MonsterCard("Hauntsnake", 20, elementType.WATER);
        MonsterCard m_soulserpent = new MonsterCard("Soulserpent", 22, elementType.NORMAL);

        // SPELL CARDS:
        SpellCard s_demonicScream = new SpellCard("Demonic Scream", 18, elementType.NORMAL);
        SpellCard s_tunnelVision = new SpellCard("Tunnel Vision", 22, elementType.NORMAL);
        SpellCard s_fireFingers = new SpellCard("Fire Fingers", 24, elementType.FIRE);
        SpellCard s_phoenixBreath = new SpellCard("Phoenix Breath", 21, elementType.FIRE);
        SpellCard s_waterRush = new SpellCard("Water Rush", 19, elementType.WATER);
        SpellCard s_tsunamiPenance = new SpellCard("Tsunami Penance", 20, elementType.WATER);

        // Put all cards in an array:
        allCards = new Card[12];
        allCards[0] = m_stoneteeth;
        allCards[1] = m_nightspawn;
        allCards[2] = m_dustghoul;
        allCards[3] = m_hellface;
        allCards[4] = m_hauntsnake;
        allCards[5] = m_soulserpent;
        allCards[6] = s_demonicScream;
        allCards[7] = s_tunnelVision;
        allCards[8] = s_fireFingers;
        allCards[9] = s_phoenixBreath;
        allCards[10] = s_waterRush;
        allCards[11] = s_tsunamiPenance;
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
