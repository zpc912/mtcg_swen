package mtcg.Database;
import mtcg.Application.*;
import mtcg.Enum.*;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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


    public boolean checkIfUserExists(String username) {
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


    public boolean registerUser(String username, String password) {
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


    public boolean loginUser(String username, String password) {
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
                else {
                    System.out.println("\nLogin failed!");
                    return false;
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


    public User fetchUserData(String username) {
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


    public void logoutUser(String username) {
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


    public void addUserToScoreboard(String username) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO scoreboard (score_id, username, wins, defeats, draws, coins_spent) VALUES (?,?,?,?,?,?)");

            UUID uuid = UUID.randomUUID();
            String id = ""+uuid;
            stmt.setString(1, id);
            stmt.setString(2, username);
            stmt.setInt(3, 0);
            stmt.setInt(4, 0);
            stmt.setInt(5, 0);
            stmt.setInt(6, 0);

            stmt.executeUpdate();
            stmt.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public Card[] getAllCards() {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cards");
            ResultSet rs = stmt.executeQuery();

            Card[] allCards = new Card[12];
            Card currCard;
            String name, typeStr, elementStr;
            int damage;
            cardType type;
            elementType element;
            int ctr = 0;

            while(rs.next()) {
                if(ctr == 12) {
                    break;
                }

                name = rs.getString(2);
                damage = rs.getInt(3);

                typeStr = rs.getString(4);
                if(typeStr == "MONSTER") {
                    type = cardType.MONSTER;
                }
                else {
                    type = cardType.SPELL;
                }

                elementStr = rs.getString(5);
                if(elementStr == "NORMAL") {
                    element = elementType.NORMAL;
                }
                else if(elementStr == "FIRE") {
                    element = elementType.FIRE;
                }
                else {
                    element = elementType.WATER;
                }

                currCard = new Card(name, damage, type, element);
                allCards[ctr] = currCard;

                ctr++;
            }

            return allCards;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean checkBalance(String username) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT coins FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            int balance = 0;
            if(rs.next()) {
                balance = rs.getInt("coins");

                if(balance >= 5) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public Card[] buyPackage(String username) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT coins FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            int currBalance = 0;
            int balanceAfterPurchase = 0;
            if(rs.next()) {
                currBalance = rs.getInt("coins");
            }
            balanceAfterPurchase = currBalance-5;
            stmt.close();


            stmt = conn.prepareStatement("UPDATE users SET coins = ? WHERE username = ?");
            stmt.setInt(1, balanceAfterPurchase);
            stmt.setString(2, username);
            stmt.executeUpdate();
            stmt.close();


            stmt = conn.prepareStatement("SELECT coins FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs2 = stmt.executeQuery();

            int newBalance = 0;
            if(rs2.next()) {
                newBalance = rs2.getInt("coins");
            }
            stmt.close();


            stmt = conn.prepareStatement("UPDATE scoreboard SET coins_spent = ? WHERE username = ?");
            int coinsSpent = 20-newBalance;
            stmt.setInt(1, coinsSpent);
            stmt.setString(2, username);
            stmt.executeUpdate();
            stmt.close();


            Pack newPack = new Pack();
            Card[] cardsOfPack = newPack.getPackageCards();

            return cardsOfPack;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public String getUserId(String username) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            String user_id = null;
            if(rs.next()) {
                user_id = rs.getString("user_id");
            }
            stmt.close();

            return user_id;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void insertNewCards(Card[] cardsOfPack, String username) {
        try {
            String user_id = getUserId(username);
            PreparedStatement stmt;

            for(int i=0; i<cardsOfPack.length; i++) {
                stmt = conn.prepareStatement("SELECT card_id FROM cards WHERE name = ?");
                stmt.setString(1, cardsOfPack[i].getName());
                ResultSet cardId = stmt.executeQuery();

                String card_id = null;
                if(cardId.next()) {
                    card_id = cardId.getString("card_id");
                }
                stmt.close();


                stmt = conn.prepareStatement("INSERT INTO user_cards (user_id, card_id, in_deck) VALUES (?,?,?)");
                stmt.setString(1, user_id);
                stmt.setString(2, card_id);
                stmt.setInt(3, 0);
                stmt.executeUpdate();
                stmt.close();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public Card getCardById(String card_id) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cards WHERE card_id = ?");
            stmt.setString(1, card_id);
            ResultSet rs = stmt.executeQuery();

            String name, typeStr, elementStr;
            int damage;
            cardType type;
            elementType element;

            if(rs.next()) {
                name = rs.getString("name");
                damage = rs.getInt("damage");

                typeStr = rs.getString("type");
                if(typeStr == "MONSTER") {
                    type = cardType.MONSTER;
                }
                else {
                    type = cardType.SPELL;
                }

                elementStr = rs.getString("element");
                if(elementStr == "NORMAL") {
                    element = elementType.NORMAL;
                }
                else if(elementStr == "FIRE") {
                    element = elementType.FIRE;
                }
                else {
                    element = elementType.WATER;
                }

                Card card = new Card(name, damage, type, element);
                return card;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public ArrayList<Card> initializeStack(String username) {
        try {
            String user_id = getUserId(username);

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_cards WHERE user_id = ?");
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();

            ArrayList<Card> stack = new ArrayList<>();
            Card currCard;
            String cardId = null;

            while(rs.next()) {
                cardId = rs.getString("card_id");
                currCard = getCardById(cardId);

                stack.add(currCard);
            }

            return stack;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Card[] initializeDeck(String username) {
        try {
            String user_id = getUserId(username);

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_cards WHERE user_id = ?");
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> cardIds = new ArrayList<>();
            int inDeck;
            String cardId;

            while(rs.next()) {
                inDeck = rs.getInt(3);

                if(inDeck == 1) {
                    cardId = rs.getString(2);
                    cardIds.add(cardId);
                }
            }

            stmt.close();


            if(cardIds.size() > 0) {
                String[] cardIdArr = new String[cardIds.size()];
                cardIdArr = cardIds.toArray(cardIdArr);
                Card card;
                Card[] deck = new Card[4];

                for(int i=0; i<cardIds.size(); i++) {
                    card = getCardById(cardIdArr[i]);
                    deck[i] = card;
                }

                return deck;
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
}
