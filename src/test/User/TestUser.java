package User;
import mtcg.Application.Card;
import mtcg.Application.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestUser {
    private final User testUser = new User("testUser", "password123", 100, 20);


    @Test
    void test_checkDeck_true() {
        assertTrue(testUser.checkDeck());
    }


    @Test
    void test_checkStack_false() {
        assertFalse(testUser.checkStack());
    }


    @Test
    void test_getDeckCards_null() {
        Card[] deckCards = testUser.getDeckCards();

        for(int i=0; i<deckCards.length; i++) {
            assertNull(deckCards[i]);
        }
    }


    @Test
    void test_getStackCards_null() {
        Card[] stackCards = testUser.getStackCards();

        for(int i=0; i<stackCards.length; i++) {
            assertNull(stackCards[i]);
        }
    }
}
