package mtcg.Application;

public class Package {
    private Card[] packageCards;
    private int price;


    public Package() {
        setPackageCards();
        setPackagePrice();
    }


    public void setPackageCards() {
        CardGenerator cg = new CardGenerator();
        packageCards = cg.shuffleCards();
    }
    public Card[] getPackageCards() { return packageCards; }


    public void setPackagePrice() { this.price = 5; }
    public int getPackagePrice() { return price; }
}
