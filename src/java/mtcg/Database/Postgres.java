package mtcg.Database;
import mtcg.Application.*;
import mtcg.Enum.*;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.datatransfer.MimeTypeParseException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
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
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO scoreboard (score_id, username, elo, wins, defeats, draws, coins_spent) VALUES (?,?,?,?,?,?,?)");

            UUID uuid = UUID.randomUUID();
            String id = ""+uuid;
            stmt.setString(1, id);
            stmt.setString(2, username);
            stmt.setInt(3, 100);
            stmt.setInt(4, 0);
            stmt.setInt(5, 0);
            stmt.setInt(6, 0);
            stmt.setInt(7, 0);

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
            cardType typeMonster = cardType.MONSTER;
            elementType element;
            elementType elementNormal = elementType.NORMAL;
            elementType elementFire = elementType.FIRE;
            int ctr = 0;

            while(rs.next()) {
                if(ctr == 12) {
                    break;
                }

                name = rs.getString(2);
                damage = rs.getInt(3);

                typeStr = rs.getString(4);
                if(typeMonster.toString().equals(typeStr)) {
                    type = cardType.MONSTER;
                }
                else {
                    type = cardType.SPELL;
                }

                elementStr = rs.getString(5);
                if(elementNormal.toString().equals(elementStr)) {
                    element = elementType.NORMAL;
                }
                else if(elementFire.toString().equals(elementStr)) {
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
            cardType typeMonster = cardType.MONSTER;
            elementType element;
            elementType elementNORMAL = elementType.NORMAL;
            elementType elementFIRE = elementType.FIRE;

            if(rs.next()) {
                name = rs.getString("name");
                damage = rs.getInt("damage");

                typeStr = rs.getString("type");
                if(typeMonster.toString().equals(typeStr)) {
                    type = cardType.MONSTER;
                }
                else {
                    type = cardType.SPELL;
                }

                elementStr = rs.getString("element");
                if(elementNORMAL.toString().equals(elementStr)) {
                    element = elementType.NORMAL;
                }
                else if(elementFIRE.toString().equals(elementStr)) {
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


    public void removeOldDeck(String username) {
        try {
            String user_id = getUserId(username);

            PreparedStatement stmt = conn.prepareStatement("UPDATE user_cards SET in_deck = ? WHERE user_id = ?");
            stmt.setInt(1, 0);
            stmt.setString(2, user_id);
            stmt.executeUpdate();
            stmt.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public Card[] getSelectedCards(String[] selectedCards) {
        try {
            Card[] newDeckCards = new Card[4];
            String[] cardIds = new String[selectedCards.length];

            for(int i=0; i<selectedCards.length; i++) {
                PreparedStatement stmt = conn.prepareStatement("SELECT card_id FROM cards WHERE name = ?");
                stmt.setString(1, selectedCards[i]);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()) {
                    cardIds[i] = rs.getString("card_id");
                }

                stmt.close();
            }

            for(int i=0; i<newDeckCards.length; i++) {
                Card currCard = getCardById(cardIds[i]);
                newDeckCards[i] = currCard;
            }

            return newDeckCards;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void selectDeck(Card[] newDeck, String username) {
        try {
            String[] cardIds = new String[newDeck.length];
            String userId = getUserId(username);

            for(int i=0; i<newDeck.length; i++) {
                PreparedStatement stmt = conn.prepareStatement("SELECT card_id FROM cards WHERE name = ?");
                stmt.setString(1, newDeck[i].getName());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()) {
                    cardIds[i] = rs.getString("card_id");
                }

                stmt.close();
            }

            for(int i=0; i<newDeck.length; i++) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE user_cards SET in_deck = ? WHERE card_id = ? AND (user_id = ?)");
                stmt.setInt(1, 1);
                stmt.setString(2, cardIds[i]);
                stmt.setString(3, userId);
                stmt.executeUpdate();
                stmt.close();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getOpponents(String username) {
        try {
            String id = getUserId(username);

            PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM user_cards WHERE in_deck = ?");
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> userIds = new ArrayList<>();
            while(rs.next()) {
                if(!userIds.contains(rs.getString("user_id"))) {
                    userIds.add(rs.getString("user_id"));
                }
            }

            stmt.close();


            ArrayList<String> opponentList = new ArrayList<>();
            for(int i=0; i<userIds.size(); i++) {
                stmt = conn.prepareStatement("SELECT username FROM users WHERE user_id = ?");
                stmt.setString(1, userIds.get(i));
                ResultSet nameOfUser = stmt.executeQuery();

                if(nameOfUser.next()) {
                    opponentList.add(nameOfUser.getString("username"));
                }

                stmt.close();
            }

            return opponentList;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void transferCard(Card cardToTransfer, String usernameWinner, String usernameLoser) {
        try {
            String winnerId = getUserId(usernameWinner);
            String loserId = getUserId(usernameLoser);

            PreparedStatement stmt = conn.prepareStatement("SELECT card_id FROM cards WHERE name = ?");
            stmt.setString(1, cardToTransfer.getName());
            ResultSet rs = stmt.executeQuery();

            String card_id = null;
            if(rs.next()) {
                card_id = rs.getString("card_id");
            }

            stmt.close();


            stmt = conn.prepareStatement("UPDATE user_cards SET user_id = ?, in_deck = ? WHERE user_id = ? AND (card_id = ?) AND (in_deck = ?)");
            stmt.setString(1, winnerId);
            stmt.setInt(2, 0);
            stmt.setString(3, loserId);
            stmt.setString(4, card_id);
            stmt.setInt(5, 1);

            stmt.executeUpdate();
            stmt.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public void payBattlePenalty(String username) {
        try {
            String user_id = getUserId(username);

            PreparedStatement stmt = conn.prepareStatement("SELECT coins FROM users WHERE user_id = ?");
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();

            int currCoins = 0;
            if(rs.next()) {
                currCoins = rs.getInt("coins");
            }
            stmt.close();


            stmt = conn.prepareStatement("UPDATE users SET coins = ? WHERE user_id = ?");
            stmt.setInt(1, currCoins-5);
            stmt.setString(2, user_id);
            stmt.executeUpdate();
            stmt.close();


            stmt = conn.prepareStatement("SELECT coins_spent FROM scoreboard WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs2 = stmt.executeQuery();

            int currSpentCoins = 0;
            if(rs2.next()) {
                currSpentCoins = rs2.getInt("coins_spent");
            }
            stmt.close();


            stmt = conn.prepareStatement("UPDATE scoreboard SET coins_spent = ? WHERE username = ?");
            stmt.setInt(1, currSpentCoins+5);
            stmt.setString(2, username);
            stmt.executeUpdate();
            stmt.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getScoreboard() {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM scoreboard ORDER BY elo DESC");
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> scoreList = new ArrayList<>();
            String scoreData = null;
            while(rs.next()) {
                scoreData = rs.getString("username") + " | ELO: " + rs.getInt("elo") + " | WINS: " + rs.getInt("wins") + " | DRAWS:" + rs.getInt("draws") + " | DEFEATS: " + rs.getInt("defeats") + " | COINS SPENT: " + rs.getInt("coins_spent");
                scoreList.add(scoreData);
            }

            return scoreList;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void updateScoreboard(String winnerUsername, String loserUsername, boolean draw) {
        try {
            String winnerId = getUserId(winnerUsername);
            String loserId = getUserId(loserUsername);
            String[] userArr = {winnerId, loserId};
            PreparedStatement stmt;

            if(draw) {
                for(int i=0; i<userArr.length; i++) {
                    stmt = conn.prepareStatement("SELECT draws FROM scoreboard WHERE username = ?");
                    stmt.setString(1, userArr[i]);
                    ResultSet rs = stmt.executeQuery();

                    int draws = 0;
                    if(rs.next()) {
                        draws = rs.getInt("draws");
                    }
                    stmt.close();


                    stmt = conn.prepareStatement("UPDATE scoreboard SET draws = ? WHERE username = ?");
                    stmt.setInt(1, (draws+1));
                    stmt.setString(2, userArr[i]);
                    stmt.executeUpdate();
                    stmt.close();
                }
            }
            else {
                // Update scores of the winner
                stmt = conn.prepareStatement("SELECT elo FROM users WHERE user_id = ?");
                stmt.setString(1, winnerId);
                ResultSet rs = stmt.executeQuery();

                int eloWinner = 0;
                if(rs.next()) {
                    eloWinner = rs.getInt("elo");
                }
                stmt.close();


                stmt = conn.prepareStatement("UPDATE users SET elo = ? WHERE user_id = ?");
                stmt.setInt(1, (eloWinner+3));
                stmt.setString(2, winnerId);
                stmt.executeUpdate();
                stmt.close();


                stmt = conn.prepareStatement("SELECT wins FROM scoreboard WHERE username = ?");
                stmt.setString(1, winnerUsername);
                ResultSet rs2 = stmt.executeQuery();

                int winsWinner = 0;
                if(rs2.next()) {
                    winsWinner = rs2.getInt("wins");
                }
                stmt.close();

                stmt = conn.prepareStatement("UPDATE scoreboard SET elo = ?, wins = ? WHERE username = ?");
                stmt.setInt(1, (eloWinner+3));
                stmt.setInt(2, (winsWinner+1));
                stmt.setString(3, winnerUsername);
                stmt.executeUpdate();
                stmt.close();


                // Update scores of the loser
                stmt = conn.prepareStatement("SELECT elo FROM users WHERE user_id = ?");
                stmt.setString(1, loserId);
                ResultSet rs3 = stmt.executeQuery();

                int eloLoser = 0;
                if(rs3.next()) {
                    eloLoser = rs3.getInt("elo");
                }
                stmt.close();


                stmt = conn.prepareStatement("UPDATE users SET elo = ? WHERE user_id = ?");
                stmt.setInt(1, (eloLoser-5));
                stmt.setString(2, loserId);
                stmt.executeUpdate();
                stmt.close();


                stmt = conn.prepareStatement("SELECT defeats FROM scoreboard WHERE username = ?");
                stmt.setString(1, loserUsername);
                ResultSet rs4 = stmt.executeQuery();

                int defeatsLoser = 0;
                if(rs4.next()) {
                    defeatsLoser = rs4.getInt("defeats");
                }
                stmt.close();


                stmt = conn.prepareStatement("UPDATE scoreboard set elo = ?, defeats = ? WHERE username = ?");
                stmt.setInt(1, (eloLoser-5));
                stmt.setInt(2, (defeatsLoser+1));
                stmt.setString(3, loserUsername);
                stmt.executeUpdate();
                stmt.close();
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
