package mtcg.Application;

import java.sql.SQLException;

public class Pack {
    private Card[] packageCards;
    private int price;


    public Pack() throws SQLException {
        setPackageCards();
        setPackagePrice();
    }


    public void setPackageCards() throws SQLException {
        CardGenerator cg = new CardGenerator();
        packageCards = cg.shuffleCards();
    }
    public Card[] getPackageCards() { return packageCards; }


    public void setPackagePrice() { this.price = 5; }
    public int getPackagePrice() { return price; }
}
