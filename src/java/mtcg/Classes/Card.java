package mtcg.Classes;
import mtcg.Enum.elementType;

public abstract class Card {
    private String name;
    private int damage;
    private elementType element;


    /*public Card(String name, int damage, elementType element) {
        setName(name);
        setDamage(damage);
        setElement(element);
    }*/


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getDamage() {
        return damage;
    }


    public void setElement(elementType element) {
        this.element = element;
    }
    public elementType getElement() {
        return element;
    }


    public String getCardType() {
        String cardClass = getClass().toString();
        String cardType = cardClass.substring(19);

        return cardType;
    }


    public String getCardInfo() {
        return "Card Name: " + getName() + "\n" +
                "Card Type: " + getCardType() + "\n" +
                "Element: " + getElement() + "\n" +
                "Damage: " + getDamage() + "\n";
    }
}
