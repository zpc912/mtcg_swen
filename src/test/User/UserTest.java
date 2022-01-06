package User;
import mtcg.Application.Deck;
import mtcg.Application.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private final User user = new User("testUsername", "testPassword", 100, 20);
}
