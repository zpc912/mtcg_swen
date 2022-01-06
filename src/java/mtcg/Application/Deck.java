package mtcg.Application;

public class Deck {
    private Card[] deckCards;


    public Deck() {
        deckCards = new Card[4];
    }


    public Card[] getDeckCards() {
        return deckCards;
    }


    public boolean checkDeck() {
        boolean deckIsEmpty = true;

        // Iterate through deck and check if any value is unequal to null:
        for(int i=0; i<deckCards.length; i++) {
            if(deckCards[i] != null) {
                // If yes then deck is not empty:
                deckIsEmpty = false;
            }
        }

        return deckIsEmpty;
    }


    public void printDeck() {
        if(checkDeck()) {
            System.out.println("\nYour deck is empty!");
        }
        else {
            System.out.println("\nYour deck:\n");

            for(int i=0; i<deckCards.length; i++) {
                System.out.println(deckCards[i].getCardInfo() + "\n");
            }
        }
    }


    public Deck initializeDeck(Card[] cards) {
        Deck deck = new Deck();

        for(int i=0; i<cards.length; i++) {
            deck.addCardToDeck(cards[i]);
        }

        return deck;
    }


    public void addCardToDeck(Card cardToAdd) {
        for(int i=0; i<deckCards.length; i++) {
            if(deckCards[i] == null) {
                deckCards[i] = cardToAdd;
                break;
            }
        }
    }


    public void cleanDeck() {
        for(int i=0; i<deckCards.length; i++) {
            deckCards[i] = null;
        }
    }
}
