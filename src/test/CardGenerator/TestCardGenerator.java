package CardGenerator;
import mtcg.Application.Card;
import mtcg.Application.CardGenerator;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestCardGenerator {
    private final CardGenerator cg = new CardGenerator();
    public TestCardGenerator() throws SQLException {}


    @Test
    void test_checkCardArrayLength_5() {
        Card[] cards = cg.shuffleCards();

        assertEquals(5, cards.length);
    }
}
