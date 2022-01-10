package mtcg.Application;
import mtcg.Database.Postgres;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.Random;
import java.lang.InterruptedException;

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
                db.selectDeck(deck, user.getUsername());
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
            fightPreparation();
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
                if(!user.checkStack()) {
                    System.out.println("\nYour deck is already empty but so is your stack!");
                    System.out.println("You can acquire cards by buying a package in the marketplace.");
                    editProfile();
                }

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
                db.selectDeck(newDeck, user.getUsername());

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


    public void viewScore() throws SQLException {
        Postgres db = new Postgres();
        ArrayList<String> scoreboard = db.getScoreboard();
        String[] scoreboardArr = new String[scoreboard.size()];
        scoreboardArr = scoreboard.toArray(scoreboardArr);

        System.out.println("\n>> SCOREBOARD <<\n--------------------------------------------------");
        for(int i=0; i<scoreboardArr.length; i++) {
            System.out.println("#" + (i+1) + " | " + scoreboardArr[i]);
            System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        }

        gameMenu();
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
                        try {
                            Thread.sleep(2000);
                        }
                        catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
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
            System.out.println("\nTRADING NOT AVAILABLE YET");
            marketplace();
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


    public void fightPreparation() throws SQLException {
        if(!user.checkStack()) {
            System.out.println("\nLooks like you do not have any cards yet.");
            System.out.println("If you want to prove yourself in fighting you must acquire cards first.");
            System.out.println("You can do that by buying a package in the store.");

            gameMenu();
        }
        else if(user.checkStack() && user.checkDeck()) {
            System.out.println("\nIf you want to fight you must first select some cards for your deck!");
            System.out.println("You can do that in your profile settings.");

            gameMenu();
        }

        System.out.println();
        for(int i=0; i<3; i++) {
            System.out.println("Searching opponent . . .");
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();

        Postgres db = new Postgres();
        ArrayList<String> opponents = db.getOpponents(user.getUsername());
        opponents.remove(user.getUsername());

        int opponentCnt = opponents.size();
        Random rand = new Random();
        int randomOpponent = rand.nextInt(opponentCnt);
        String opponentName = opponents.get(randomOpponent);
        User opponent = db.fetchUserData(opponentName);
        Card[] opponentDeck = db.initializeDeck(opponentName);


        System.out.println(">> OPPONENT FOUND <<\n");

        System.out.println(">> YOU ARE BATTLING AGAINST <<");
        System.out.println(opponent.getUsername() + " (ELO: " + opponent.getELO() + ")\n");

        System.out.println(">> OPPONENT DECK <<");
        for(int i=0; i<opponentDeck.length; i++) {
            System.out.println(opponentDeck[i].getCardInfo());
        }

        char userInput;
        Scanner sc = new Scanner(System.in);

        System.out.println("\nPress 'y' to confirm or 'n' to decline the fight.");
        System.out.println("ATTENTION: If you decline this fight you will have to pay a penalty of 5 coins - choose wisely!");
        while(true) {
            System.out.print(": ");
            userInput = sc.nextLine().charAt(0);

            if(userInput != 'y' && userInput != 'n') {
                System.out.println("\nInvalid option! Try again.");
            }
            else {
                break;
            }
        }

        if(userInput == 'y') {
            startFight(opponent, opponentDeck);
        }
        else {
            boolean validBalance = db.checkBalance(user.getUsername());

            if(!validBalance) {
                System.out.println("You do not have enough coins to pay the penalty - you need to fight your opponent!");
                startFight(opponent, opponentDeck);
            }

            System.out.println("\nPaying penalty . . .");
            try {
                Thread.sleep(2000);
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            db.payBattlePenalty(user.getUsername());
            System.out.println(">> PENALTY PAID <<");

            battleOverScreen();
        }
    }


    public void startFight(User opponent, Card[] opponentDeck) throws SQLException {
        System.out.println();
        for(int i=5; i>=1; i--) {
            System.out.println("Battle starts in " + i + " seconds . . .");
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        Postgres db = new Postgres();
        Battle battle = new Battle(user, opponent, user.getDeckCards(), opponentDeck);
        battle.fight();

        user.deleteDeck();
        ArrayList<Card> tmpStack = db.initializeStack(user.getUsername());
        user.addCardsToStack(tmpStack);
        db.removeOldDeck(user.getUsername());
        db.removeOldDeck(opponent.getUsername());

        battleOverScreen();
    }


    public void battleOverScreen() throws SQLException {
        char userInput;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n\nPress 'b' to go back to the main menu or 'q' to exit the game . . .");
        while(true) {
            System.out.print(": ");
            userInput = sc.nextLine().charAt(0);

            if(userInput != 'b' && userInput != 'q') {
                System.out.println("\nInvalid option! Try again.");
            }
            else {
                break;
            }
        }

        if(userInput == 'b') {
            gameMenu();
        }
        else {
            closeGame(user.getUsername());
        }
    }
}
