package Database;
import mtcg.Application.Card;
import mtcg.Database.Postgres;
import mtcg.Application.User;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestDatabase {
    private final Postgres db = new Postgres();


    @Test
    void test_checkIfUserExists_true() {
        assertFalse(db.checkIfUserExists("testUser"));
    }


    @Test
    @Disabled
    void test_registerUser_true() {
        User newUser = new User("newTestUser", "pw123", 100, 20);

        assertTrue(db.registerUser(newUser.getUsername(), newUser.getPassword()));
    }


    @Test
    void test_loginUser_true() {
        assertTrue(db.loginUser("testUser", "pw123"));
    }


    @Test
    void test_fetchUserData_NotNull() {
        User testUser = db.fetchUserData("testUser");

        assertNotNull(testUser);
    }


    @Test
    void test_getAllCards_NotNull() {
        Card[] allCards = db.getAllCards();

        assertNotNull(allCards);
    }


    @Test
    void test_checkBalance_true() {
        assertTrue(db.checkBalance("testUser"));
    }


    @Test
    @Disabled
    void test_buyPackage_5() {
        Card[] newPackage = db.buyPackage("testUser");

        assertEquals(5, newPackage.length);
    }


    @Test
    void test_getUserId_NotNull() {
        String userId = db.getUserId("testUser");

        assertNotNull(userId);
    }


    @Test
    void test_getCardById_NotNull() {
        Card testStoneteeth = db.getCardById("d23590f9-7360-44a4-877d-c053460d60d0");

        assertNotNull(testStoneteeth);
    }


    @Test
    void test_initializeDeck_NotNull() {
        Card[] testDeck = db.initializeDeck("testUser");

        assertNotNull(testDeck);
    }


    @Test
    void test_initializeStack_5() {
        ArrayList<Card> testStack = new ArrayList<>();
        testStack = db.initializeStack("testUser");

        assertEquals(5, testStack.size());
    }


    @Test
    void test_getOpponents_NotEquals0() {
        ArrayList<String> opponentList = db.getOpponents("testUser");

        assertNotEquals(0, opponentList.size());
    }


    @Test
    void test_getScoreboard_NotEquals0() {
        ArrayList<String> scoreboardList = db.getScoreboard();

        assertNotEquals(0, scoreboardList.size());
    }
}
