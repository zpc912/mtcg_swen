package mtcg.Main;
import mtcg.Classes.*;
import mtcg.Classes.Package;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Gamelogic gamelogic = new Gamelogic();
        Scanner sc = new Scanner(System.in);
        char userInput;
        String username, password;


        /*CardGenerator cg = new CardGenerator();
        Card[] shc;
        shc = cg.shuffleCards();
        for(int i=0; i<shc.length; i++) {
            System.out.println(shc[i].getName());
        }*/

        /*Package pack = new Package();
        Card[] packCards = pack.getPackageCards();
        System.out.println(packCards[1].getCardInfo());*/

        User user = new User("username123", "pw123", 100, 20);
        //user.viewDeck();
        //user.deleteDeck();
        user.editDeck('a');


        System.out.println();

        System.out.println("Hello traveler! Do we know each other?");
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
            System.out.println("Hm, could you help me remember?");

            System.out.print("Username: ");
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();

            gamelogic.loginUser(username, password);
        }
        else if(userInput == 'n') {
            System.out.println("\nThen tell me, what is your name traveler?");

            System.out.print("Username: ");
            username = sc.nextLine();
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

            System.out.println("\n" + username + "... it is an honour to meet you!");
            gamelogic.registerUser(username, password);
        }
    }
}
