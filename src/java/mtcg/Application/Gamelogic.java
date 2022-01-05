package mtcg.Application;
import mtcg.Database.Postgres;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class Gamelogic {
     private User user;


    public void userLogin(String username, String password) throws IOException, SQLException {
        Postgres db = new Postgres();
        boolean res = db.loginUser(username, password);

        if(res) {
            user = db.fetchUserData(username);
            gameMenu();
        }
        else {
            startScreen();
        }
    }


    public void userRegistration(String username, String password) throws IOException, SQLException {
        Postgres db = new Postgres();
        boolean res = db.registerUser(username, password);

        if(res) {
            user = new User(username, password, 100, 20);
            gameMenu();
        }
        else {
            startScreen();
        }
    }


    public User returnUser() {
        if(user != null) {
            return user;
        }
        else {
            return null;
        }
    }


    public void gameMenu() throws SQLException {
        System.out.println("\nWhat would you like to do, " + user.getUsername() + "?");
        System.out.println("- Start a match (f)");
        System.out.println("- View & edit profile (p)");
        System.out.println("- View stats (s)");
        System.out.println("- Marketplace (m)");
        System.out.println("- Logout and Exit (q)");

        Scanner sc = new Scanner(System.in);
        char userInput;

        while(true) {
            System.out.print(": ");
            userInput = sc.nextLine().charAt(0);

            if(userInput != 'f' && userInput != 'p' && userInput != 's' && userInput != 'm' && userInput != 'q') {
                System.out.println("Please select one of the options above.");
            }
            else {
                break;
            }
        }

        if(userInput == 'f') {
            // TODO: forward user to the battle menu
        }
        else if(userInput == 'p') {
            editProfile();
        }
        else if(userInput == 's') {
            viewScore();
        }
        else if(userInput == 'm') {
            // TODO: go to store
        }
        else if(userInput == 'q') {
            closeGame(user.getUsername());
        }
    }


    public void editProfile() throws SQLException {
        char userInput;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n" + "Profile details:");
        System.out.println("   - Username: " + user.getUsername());
        System.out.println("   - ELO: " + user.getELO());
        System.out.println("   - Coins: " + user.getCoins());

        System.out.println("\nOther options:");
        System.out.println("Press 's' to view your stack");
        System.out.println("Press 'd' to view your current deck");
        System.out.println("Press 'e' to edit your current deck");
        System.out.println("Press 'b' to go back");

        while(true) {
            System.out.print(": ");
            userInput = sc.nextLine().charAt(0);

            if(userInput != 's' && userInput != 'd' && userInput != 'e' && userInput != 'b') {
                System.out.println("I couldn't understand you. Can you repeat that?");
            }
            else {
                break;
            }
        }

        if(userInput == 's') {
            user.viewStack();
        }
        else if(userInput == 'd') {
            user.viewDeck();
        }
        else if(userInput == 'e') {
            System.out.println("Do you want to clean (c), add (a) or remove (r) cards from your deck?");

            while(true) {
                System.out.println(": ");
                userInput = sc.nextLine().charAt(0);

                if(userInput != 'c' && userInput != 'a' && userInput != 'r') {
                    System.out.println("I can't figure out what you mean.");
                }
                else {
                    break;
                }
            }

            user.editDeck(userInput);
        }
        else if(userInput == 'b') {
            gameMenu();
        }
    }


    public void startScreen() throws SQLException, IOException {
        Scanner sc = new Scanner(System.in);
        char userInput;
        String username, password;

        System.out.println("\nHello traveler! Do we know each other?");
        System.out.println("- Yes (y)");
        System.out.println("- No (n)");

        while(true) {
            System.out.print(": ");
            userInput = sc.nextLine().charAt(0);

            if(userInput != 'y' && userInput != 'n') {
                System.out.println("Unfortunately, I could not understand you, traveler!");
            }
            else {
                break;
            }
        }

        if(userInput == 'y') {
            System.out.println("\nHm, could you help me remember?");

            System.out.print("Username: ");
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();

            userLogin(username, password);
        }
        else if(userInput == 'n') {
            System.out.println("\nThen tell me, what is your name traveler?");

            while(true) {
                System.out.print("Username: ");
                username = sc.nextLine();

                Postgres db = new Postgres();
                boolean res = db.checkIfUserExists(username);

                if(!res) {
                    System.out.println("This username is already taken!");
                    continue;
                }

                if(username.length() > 12) {
                    System.out.println("Your username is too long (max. 12 characters)!");
                    continue;
                }

                break;
            }

            System.out.print("Password: ");
            password = sc.nextLine();
            System.out.print("Repeat password: ");
            String passwordTmp = sc.nextLine();

            while(!Objects.equals(password, passwordTmp)) {
                System.out.println("\n" + username + ", I think your secret phrases do not match!");

                System.out.print("Password: ");
                password = sc.nextLine();

                System.out.print("Repeat password: ");
                passwordTmp = sc.nextLine();
            }

            userRegistration(username, password);
        }
    }


    public void viewScore() {
        // TODO: load scores from database and display as scoreboard
    }


    public void store() {
        // TODO: store etc.
    }


    public void closeGame(String username) throws SQLException {
        System.out.println("\nGoodbye!");

        Postgres db = new Postgres();
        db.logoutUser(username);
    }
}
