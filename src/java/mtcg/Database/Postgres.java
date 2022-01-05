package mtcg.Database;
import mtcg.Application.*;
import mtcg.Enum.*;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.transform.Result;

public class Postgres {
    Connection conn;
    private String salt;


    public Postgres() {
        conn = null;
        salt = "$2a$10$Q0NvEVKnzAty7Za0ftCB0e";

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mtcgdb", "mtcguser", "mtcg123");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean checkIfUserExists(String username) throws SQLException {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return false;
            }
            else {
                return true;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean registerUser(String username, String password) throws SQLException {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (user_id, username, password, elo, coins, logged) VALUES (?,?,?,?,?,?)");

            UUID uuid = UUID.randomUUID();
            String id = ""+uuid;
            stmt.setString(1, id);
            stmt.setString(2, username);
            String passwordHash = BCrypt.hashpw(password, salt);
            stmt.setString(3, passwordHash);
            stmt.setInt(4, 100);
            stmt.setInt(5, 20);
            stmt.setInt(6, 0);

            int rs = stmt.executeUpdate();
            stmt.close();

            if(rs == 1) {
                System.out.println("\nRegistration successful!");
                return true;
            }
            else {
                System.out.println("\nRegistration failed!");
                return false;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean loginUser(String username, String password) throws SQLException {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            String passwordHash = BCrypt.hashpw(password, salt);
            if(rs.next()) {
                String passwordDb = rs.getString("password");
                if(passwordHash.equals(passwordDb)) {
                    PreparedStatement stmt2 = conn.prepareStatement("UPDATE users SET logged = ? WHERE username = ?");
                    stmt2.setInt(1, 1);
                    stmt2.setString(2, username);
                    int rs2 = stmt2.executeUpdate();

                    if(rs2 == 1) {
                        System.out.println("\nLogin successful!");
                        return true;
                    }
                    else {
                        System.out.println("\nLogin failed!");
                        return false;
                    }
                }
            }
            else {
                System.out.println("\nLogin failed!");
                return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }


    public User fetchUserData(String username) throws SQLException {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                User user = new User(rs.getString("username"), rs.getString("password"), rs.getInt("elo"), rs.getInt("coins"));
                return user;
            }
            else {
                return null;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void logoutUser(String username) throws SQLException {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE users SET logged = ? WHERE username = ?");
            stmt.setInt(1, 0);
            stmt.setString(2, username);

            stmt.executeUpdate();
            stmt.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
