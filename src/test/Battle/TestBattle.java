package Battle;
import mtcg.Application.Battle;
import mtcg.Application.Card;
import mtcg.Application.User;
import mtcg.Enum.cardType;
import mtcg.Enum.elementType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestBattle {
    private final User testUser1 = new User("testUser", "password123", 100, 20);
    private final User testUser2 = new User("testUser", "password123", 100, 20);
    private final Card[] testDeck1 = new Card[4];
    private final Card[] testDeck2 = new Card[4];
    private final Battle testBattle = new Battle(testUser1, testUser2, testDeck1, testDeck2);


    @Test
    void test_compareCards_0() {
        int dmg1 = 10;
        int dmg2 = 10;

        assertEquals(0, testBattle.compareCards(dmg1, dmg2));
    }


    @Test
    void test_compareCards_1() {
        int dmg1 = 10;
        int dmg2 = 5;

        assertEquals(1, testBattle.compareCards(dmg1, dmg2));
    }


    @Test
    void test_compareCards_2() {
        int dmg1 = 5;
        int dmg2 = 10;

        assertEquals(2, testBattle.compareCards(dmg1, dmg2));
    }


    @Test
    void test_checkEffectiveness_negative3() {
        Card card1 = new Card("card1", 10, cardType.SPELL, elementType.NORMAL);
        Card card2 = new Card("card2", 10, cardType.MONSTER, elementType.NORMAL);

        assertEquals(-3, testBattle.checkEffectiveness(card1, card2));
    }


    @Test
    void test_checkEffectiveness_negative1() {
        Card card1 = new Card("card1", 10, cardType.SPELL, elementType.WATER);
        Card card2 = new Card("card2", 10, cardType.MONSTER, elementType.FIRE);

        assertEquals(-1, testBattle.checkEffectiveness(card1, card2));
    }


    @Test
    void test_checkEffectiveness_negative2() {
        Card card1 = new Card("card1", 10, cardType.SPELL, elementType.WATER);
        Card card2 = new Card("card2", 10, cardType.MONSTER, elementType.NORMAL);

        assertEquals(-2, testBattle.checkEffectiveness(card1, card2));
    }


    @Test
    void test_checkIfDeckEmpty_true() {
        for(int i=0; i<testDeck1.length; i++) {
            assertNull(testDeck1[i]);
        }
    }
}
