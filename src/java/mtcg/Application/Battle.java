package mtcg.Application;
import mtcg.Database.Postgres;
import mtcg.Enum.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Battle {
    private User user1;
    private User user2;
    private Card[] deck1;
    private Card[] deck2;
    private ArrayList<String> battleLog;


    public Battle(User user1, User user2, Card[] deck1, Card[] deck2) {
        setUser1(user1);
        setUser2(user2);
        setDeck1(deck1);
        setDeck2(deck2);
        battleLog = new ArrayList<>();
        battleLog.add("\n\n--------------------------------------------------\n>> BATTLE LOG <<\n--------------------------------------------------\n");
    }


    public User getUser1() {
        return user1;
    }
    public void setUser1(User user1) {
        this.user1 = user1;
    }


    public User getUser2() {
        return user2;
    }
    public void setUser2(User user2) {
        this.user2 = user2;
    }


    public Card[] getDeck1() {
        return deck1;
    }
    public void setDeck1(Card[] deck1) {
        this.deck1 = deck1;
    }


    public Card[] getDeck2() {
        return deck2;
    }
    public void setDeck2(Card[] deck2) {
        this.deck2 = deck2;
    }


    public ArrayList<String> getBattleLog() {
        return battleLog;
    }


    public void fight() {
        int roundCtr = 1;
        int user1Pts = 0;
        int user2Pts = 0;
        int roundPts1, roundPts2;

        Card card1, card2;
        Random rand = new Random();
        int randomCard, fightResult, card1Dmg, card2Dmg, effectiveness;
        String newLog = "";

        System.out.println("\nLET THE BATTLE BEGIN!\n");
        while(true) {
            if(roundCtr == 101) {
                break;
            }

            roundPts1 = roundPts2 = 0;


            // Check if the decks are empty
            if(checkIfDeckEmpty(deck1)) {
                user2Pts += user1Pts;
                break;
            }

            if(checkIfDeckEmpty(deck2)) {
                user1Pts += user2Pts;
                break;
            }


            // Randomly choose a card out of each deck
            int sizeDeck1 = deck1.length-1;
            if(sizeDeck1 == 0) {
                randomCard = 0;
            }
            else if(sizeDeck1 < 0) {
                break;
            }
            else {
                randomCard = rand.nextInt(sizeDeck1);
            }
            card1 = deck1[randomCard];

            int sizeDeck2 = deck2.length-1;
            if(sizeDeck2 == 0) {
                randomCard = 0;
            }
            else if(sizeDeck2 < 0) {
                break;
            }
            else {
                randomCard = rand.nextInt(sizeDeck2);
            }
            card2 = deck2[randomCard];


            card1Dmg = card1.getDamage();
            card2Dmg = card2.getDamage();

            System.out.println("\n>> ROUND #" + roundCtr + " <<");
            System.out.println(user1.getUsername() + " --> " + card1.getName() + " (" + card1.getCardtype() + "/" + card1.getElement() + ") with " + card1.getDamage() + " damage!");
            System.out.println("- VS -");
            System.out.println(user2.getUsername() + " --> " + card2.getName() + " (" + card2.getCardtype() + "/" + card2.getElement() + ") with " + card2.getDamage() + " damage!");

            // BATTLE LOGIC
            if( (card1.getCardtype() == cardType.MONSTER) && (card2.getCardtype() == cardType.MONSTER) ) {
                fightResult = compareCards(card1Dmg, card2Dmg);
            }
            else {
                // Call checkEffectiveness() with the current cards and retrieve the effectiveness
                effectiveness = checkEffectiveness(card1, card2); // The returned int describes the effectiveness

                // No effect
                if(effectiveness == -3) {
                    fightResult = compareCards(card1Dmg, card2Dmg);
                }
                // Card of user2 is dominant
                else if(effectiveness == -2) {
                    card1Dmg = card1Dmg/2;
                    card2Dmg = card2Dmg*2;

                    fightResult = compareCards(card1Dmg, card2Dmg);
                }
                // Card of user1 is dominant
                else {
                    card1Dmg = card1Dmg*2;
                    card2Dmg = card2Dmg/2;

                    fightResult = compareCards(card1Dmg, card2Dmg);
                }
            }


            // Draw round
            if(fightResult == 0) {
                newLog = updateBattleLog(user1, user2, card1, card2, user1Pts, user2Pts, roundCtr);
            }
            // user1 won the round
            else if(fightResult == 1) {
                user1Pts++;
                roundPts1++;
                newLog = updateBattleLog(user1, user2, card1, card2, roundPts1, roundPts2, roundCtr);
            }
            // user2 won the round
            else {
                user2Pts++;
                roundPts2++;
                newLog = updateBattleLog(user1, user2, card1, card2, roundPts1, roundPts2, roundCtr);
            }

            battleLog.add(newLog);


            // Check which user won the current round and transfer the cards
            if(roundPts1 > roundPts2) {
                deck1 = transferCardToWinner(card2, deck1);
                deck2 = removeCardFromLoser(card2, deck2);
                cardTransferInDb(card2, user1.getUsername(), user2.getUsername());
            }
            else if(roundPts1 < roundPts2) {
                deck2 = transferCardToWinner(card1, deck2);
                deck1 = removeCardFromLoser(card1, deck1);
                cardTransferInDb(card1, user2.getUsername(), user1.getUsername());
            }


            System.out.println("--------------------------------------------------");
            System.out.println("||| Damage Calculation: " + card1Dmg + " vs. " + card2Dmg + " |||");
            if(roundPts1 > roundPts2) {
                System.out.println("||| " + user1.getUsername() + " won this round by using " + card1.getName() + " |||\n");
            }
            else if(roundPts1 < roundPts2) {
                System.out.println("||| " + user2.getUsername() + " won this round by using " + card2.getName() + " |||\n");
            }
            else {
                System.out.println("||| This round was a draw |||\n");
            }


            roundCtr++;
        }


        System.out.println("\nTHE BATTLE IS OVER!\n");

        String[] battleLogArr = new String[battleLog.size()];
        battleLogArr = battleLog.toArray(battleLogArr);

        for(int i=0; i<battleLogArr.length; i++) {
            System.out.println(battleLogArr[i]);
        }
        System.out.println("--------------------------------------------------");

        if(user1Pts == user2Pts) {
            System.out.println("\nUnfortunately, this match ended in a draw so we do not have a winner.\nNevertheless, it was an amazing fight!\n");
        }
        else if(user1Pts > user2Pts) {
            System.out.println("\n>> " + user1.getUsername() + " won this match! Congratulations for their victory and the acquired cards!\n");
        }
        else {
            System.out.println("\n>> " + user2.getUsername() + " won this match! Congratulations for their victory and the acquired cards!\n");
        }

        updateScoreboard(user1, user2, user1Pts, user2Pts);
    }


    public int compareCards(int card1Dmg, int card2Dmg) {
        if(card1Dmg > card2Dmg) {
            return 1;
        }
        else if(card1Dmg < card2Dmg) {
            return 2;
        }
        else {
            return 0;
        }
    }


    public int checkEffectiveness(Card card1, Card card2) {
        elementType e1 = card1.getElement();
        elementType e2 = card2.getElement();

        if(e1 == e2) {
            return -3;
        }
        else if( (e1 == elementType.WATER) && (e2 == elementType.FIRE) ) {
            return -1;
        }
        else if( (e1 == elementType.FIRE) && (e2 == elementType.NORMAL) ) {
            return -1;
        }
        else if( (e1 == elementType.NORMAL) && (e2 == elementType.WATER) ) {
            return -1;
        }
        else {
            return -2;
        }
    }


    public String updateBattleLog(User user1, User user2, Card card1, Card card2, int pointsOfUser1, int pointsOfUser2, int round) {
        Random rand = new Random();
        int r;
        User winner = null;
        User loser = null;
        Card cardW = null;
        Card cardL = null;
        String newLog = "- ROUND #"+round+" -";

        if(pointsOfUser1 > pointsOfUser2) {
            winner = user1;
            loser = user2;
            cardW = card1;
            cardL = card2;
        }
        else if(pointsOfUser1 < pointsOfUser2) {
            winner = user2;
            loser = user1;
            cardW = card2;
            cardL = card1;
        }

        if(pointsOfUser1 == pointsOfUser2) {
            r = rand.nextInt(3);
            switch(r) {
                case 0:
                    newLog += "\nOh, looks like no one won this time. What a boring round!\n";
                    break;

                case 1:
                    newLog += "\nNeither " + user1.getUsername() + " nor " + user2.getUsername() + " had a chance against their opponent's cards!\n";
                    break;

                case 2:
                    newLog += "\nCome on " + user1.getUsername() + " & " + user2.getUsername() + "! Bring some action into this battle!\n";
                    break;
            }
        }
        else {
            r = rand.nextInt(5);
            switch(r) {
                case 0:
                    newLog += "\n" + winner.getUsername() +  " just forced " + loser.getUsername() + " onto their knees by using " + cardW.getName() + "!\n";
                    break;

                case 1:
                    newLog += "\nOuch! Looks like " + loser.getUsername() + " just took a L from " + winner.getUsername() + " because of " + cardW.getName() + ".\n";
                    break;

                case 2:
                    newLog += "\n" + cardL.getName() + " used by " + loser.getUsername() + " had no chance against " + cardW.getName() + "!\n";
                    break;

                case 3:
                    newLog += "\nDid you see that? " + winner.getUsername() +  " brutally annihilated his opponent!\n";
                    break;

                case 4:
                    newLog += "\nWhoa! Crazy attack by " + winner.getUsername() + " using " + cardW.getName() + " - Good night " + loser.getUsername() + "!\n";
                    break;
            }
        }

        return newLog;
    }


    public boolean checkIfDeckEmpty(Card[] cardsOfUser) {
        int ctr = 0;

        for(int i=0; i<cardsOfUser.length; i++) {
            if(cardsOfUser[i] == null) {
                ctr++;
            }
        }

        if(ctr == cardsOfUser.length) {
            return true;
        }
        else {
            return false;
        }
    }


    public Card[] transferCardToWinner(Card cardOfLoser, Card[] deckOfWinner) {
        ArrayList<Card> deckOfWinnerList = new ArrayList<>(Arrays.asList(deckOfWinner));
        deckOfWinnerList.add(cardOfLoser);

        Card[] newDeckOfWinner = new Card[deckOfWinnerList.size()];
        newDeckOfWinner = deckOfWinnerList.toArray(newDeckOfWinner);

        return newDeckOfWinner;
    }


    public Card[] removeCardFromLoser(Card cardOfLoser, Card[] deckOfLoser) {
        ArrayList<Card> deckOfLoserList = new ArrayList<>(Arrays.asList(deckOfLoser));
        deckOfLoserList.remove(cardOfLoser);

        Card[] newDeckOfLoser = new Card[deckOfLoserList.size()];
        newDeckOfLoser = deckOfLoserList.toArray(newDeckOfLoser);

        return newDeckOfLoser;
    }


    public void cardTransferInDb(Card cardToTransfer, String usernameWinner, String usernameLoser) {
        Postgres db = new Postgres();
        db.transferCard(cardToTransfer, usernameWinner, usernameLoser);
    }


    public void updateScoreboard(User user1, User user2, int pointsOfUser1, int pointsOfUser2) {
        Postgres db = new Postgres();
        boolean draw = false;

        if(pointsOfUser1 > pointsOfUser2) {
            db.updateScoreboard(user1.getUsername(), user2.getUsername(), draw);
        }
        else if(pointsOfUser1 < pointsOfUser2) {
            db.updateScoreboard(user2.getUsername(), user1.getUsername(), draw);
        }
        else {
            draw = true;
            db.updateScoreboard(user1.getUsername(), user2.getUsername(), draw);
        }
    }
}
