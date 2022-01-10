package mtcg;
import mtcg.Application.*;
import mtcg.Database.Postgres;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import mtcg.Enum.*;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        Gamelogic gamelogic = new Gamelogic();
        gamelogic.startScreen();

        User user = gamelogic.returnUser();
        Postgres db = new Postgres();
        db.logoutUser(user.getUsername());
    }
}
