package mtcg.Database;
import mtcg.Application.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.UUID;

public class Postgres {
    Connection conn;


    public Postgres() {
        conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mtcgdb", "mtcguser", "mtcg123");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
