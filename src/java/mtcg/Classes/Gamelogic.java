package mtcg.Classes;
import mtcg.Enum.elementType;

import java.io.IOException;
import java.util.Scanner;

public class Gamelogic {
     private User user;


    public void loginUser(String username, String password) throws IOException {
        System.out.println(username);
        System.out.println(password);

        // TODO: login user with credentials from database
    }


    public void registerUser(String username, String password) throws IOException {
        user = new User(username, password, 100, 20);
        // TODO: add user to database
        gameMenu();
    }


    public void gameMenu() {
        System.out.println("\nWhat would you like to do, " + user.getUsername() + "?");
        System.out.println("- Start a match (f)");
        System.out.println("- View & edit profile (p)");
        System.out.println("- View stats (s)");
        System.out.println("- Marketplace (m)");

        Scanner sc = new Scanner(System.in);
        char userInput;

        while(true) {
            System.out.print(": ");
            userInput = sc.nextLine().charAt(0);

            if(userInput != 'f' && userInput != 'p' && userInput != 's' && userInput != 'm') {
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
            // TODO: forward user to store
        }
    }


    public void editProfile() {
        char userInput;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n" + "Profile details:");
        System.out.println("   - Username: " + user.getUsername());
        System.out.println("   - ELO: " + user.getELO());
        System.out.println("   - Coins: " + user.getCoins());

        System.out.println("\nOther options:");
        System.out.println("Press 'd' to view your current deck");
        System.out.println("Press 'e' to edit your current deck");
        System.out.println("Press 'b' to go back");

        while(true) {
            System.out.print(": ");
            userInput = sc.nextLine().charAt(0);

            if(userInput != 'd' && userInput != 's' && userInput != 'b') {
                System.out.println("I couldn't understand you. Can you repeat that?");
            }
            else {
                break;
            }
        }

        if(userInput == 'd') {
            user.viewDeck();
        }
        else if(userInput == 'e') {
            System.out.println("Do you want to add (a) or remove (r) a card from your deck?");

            while(true) {
                System.out.println(": ");
                userInput = sc.nextLine().charAt(0);

                if(userInput != 'a' && userInput != 'r') {
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


    public void viewScore() {
        // TODO: load scores from database and display as scoreboard
    }
}
