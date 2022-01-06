package mtcg.Application;
import mtcg.Database.Postgres;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Gamelogic {
     private User user;


    public void userLogin(String username, String password) throws IOException, SQLException {
        Postgres db = new Postgres();
        boolean res = db.loginUser(username, password);

        if(res) {
            user = db.fetchUserData(username);

            ArrayList<Card> tmpStack = db.initializeStack(user.getUsername());
            user.addCardsToStack(tmpStack);

            Card[] deck = db.initializeDeck(user.getUsername());
            if(deck != null) {
                user.addCardsToDeck(deck);
                db.selectDeck(deck);
            }

            gameMenu();
        }
        else {
            startScreen();
        }
    }


    public void userRegistration(String username, String password) throws IOException, SQLException {
        Postgres db = new Postgres();
        boolean res = db.registerUser(username, password);
        db.addUserToScoreboard(username);

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
                System.out.println("\nPlease select one of the options above.");
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
            marketplace();
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
                System.out.println("\nI couldn't understand you. Can you repeat that?");
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
            System.out.println("\nDo you want to clean (c) or re-select (r) your deck?");

            while(true) {
                System.out.print(": ");
                userInput = sc.nextLine().charAt(0);

                if(userInput != 'c' && userInput != 'r') {
                    System.out.println("\nI can't figure out what you mean.");
                }
                else {
                    break;
                }
            }

            if(userInput == 'c') {
                Postgres db = new Postgres();
                user.deleteDeck();
                db.removeOldDeck(user.getUsername());

                System.out.println("\nDeck successfully cleaned!");
            }
            else {
                if(!user.checkStack()) {
                    System.out.println("\nLooks like your stack is empty!");
                    System.out.println("You can acquire cards by buying a package in the marketplace.");
                    editProfile();
                }

                String[] selection = new String[4];
                String cardNum;
                int ctr = 0;
                System.out.println("\nSelect new cards from your stack:\n");

                Card[] stackCards = user.getStackCards();
                String[] stackCardsNames = new String[stackCards.length];
                for(int i=0; i<stackCards.length; i++) {
                    System.out.println(">> CARD #" + (i+1) + " (PRESS \"" + (i+1) + "\" FOR THIS CARD) <<");
                    System.out.println(stackCards[i].getCardInfo());
                    stackCardsNames[i] = stackCards[i].getName();
                }

                while(true) {
                    if(ctr == 4) {
                        break;
                    }

                    System.out.print(": ");
                    cardNum = sc.nextLine();

                    if(!cardNum.matches("\\d+")) {
                        System.out.println("\nPlease type in a number!");
                        continue;
                    }
                    else if(Arrays.asList(selection).contains(cardNum)) {
                        System.out.println("\nYou cannot choose the same card twice!");
                        continue;
                    }
                    else {
                        selection[ctr] = cardNum;
                        int tmp = Integer.parseInt(cardNum);
                        stackCardsNames[ctr] = stackCards[tmp-1].getName();
                    }

                    ctr++;
                }

                Postgres db = new Postgres();
                Card[] newDeck = db.getSelectedCards(stackCardsNames);

                user.deleteDeck();
                db.removeOldDeck(user.getUsername());
                user.deckReselection(newDeck);
                db.selectDeck(newDeck);

                System.out.println("\nGood choices you've made!");
            }
        }
        else if(userInput == 'b') {
            gameMenu();
        }

        editProfile();
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
                System.out.println("\nUnfortunately, I could not understand you, traveler!");
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


    public void marketplace() throws SQLException {
        Scanner sc = new Scanner(System.in);
        char userInput;

        System.out.println("\nWelcome to the marketplace, traveler!");
        System.out.println("What would you like to do?");
        System.out.println("- Buy a package (p)");
        System.out.println("- Trade cards with another user (t)");
        System.out.println("- Go back (b)");

        while(true) {
            System.out.print(": ");
            userInput = sc.nextLine().charAt(0);

            if(userInput != 'p' && userInput != 't' && userInput != 'b') {
                System.out.println("\nPlease select one of the options above!");
            }
            else {
                break;
            }
        }

        if(userInput == 'p') {
            System.out.println("\nA new package costs 5 coins");
            System.out.println("Do you want to buy one? (y/n)");

            while(true) {
                System.out.print(": ");
                userInput = sc.nextLine().charAt(0);

                if(userInput != 'y' && userInput != 'n') {
                    System.out.println("Invalid input!");
                }
                else {
                    break;
                }
            }

            if(userInput == 'y') {
                Postgres db = new Postgres();
                boolean allowPurchase = db.checkBalance(user.getUsername());

                if(allowPurchase) {
                    Card[] packCards = db.buyPackage(user.getUsername());

                    if(packCards != null) {
                        db.insertNewCards(packCards, user.getUsername());
                        ArrayList<Card> tmpStack = db.initializeStack(user.getUsername());
                        user.addCardsToStack(tmpStack);

                        System.out.println("\nOpening package . . .");
                        System.out.println("This package contains:\n");

                        for(int i=0; i<packCards.length; i++) {
                            System.out.println(packCards[i].getCardInfo() + "\n");
                        }
                    }
                    else {
                        //System.out.println("ERROR WITH CARD PURCHASE");
                    }
                }
                else {
                    System.out.println("Unfortunately, you do not have enough coins to buy a package!");
                    System.out.println("If you want new cards maybe consider trading with someone else ;)");
                    marketplace();
                }
            }
            else {
                marketplace();
            }
        }
        else if(userInput == 't') {
            // TODO: trade cards with other users
            System.out.println("TRADING NOT AVAILABLE YET");
            gameMenu();
        }
        else if(userInput == 'b') {
            gameMenu();
        }
    }


    public void closeGame(String username) {
        System.out.println("\nGoodbye!");

        Postgres db = new Postgres();
        db.logoutUser(username);

        System.exit(0);
    }
}
