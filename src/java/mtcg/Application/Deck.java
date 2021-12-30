package mtcg.Application;

public class Deck {
    private Card[] deckCards;


    public Deck() {
        deckCards = new Card[4];
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


    public void addCardToDeck(Card cardToAdd) {
        for(int i=0; i<deckCards.length; i++) {
            if(deckCards[i] == null) {
                deckCards[i] = cardToAdd;
                break;
            }
        }
    }


    public void removeCardFromDeck(Card cardToRemove) {
        // TODO: remove card from deck
    }


    public void cleanDeck() {
        for(int i=0; i<deckCards.length; i++) {
            deckCards[i] = null;
        }

        System.out.println("Deck successfully cleaned!");
    }
}
