package mtcg.Application;
import mtcg.Enum.elementType;
import mtcg.Enum.cardType;

public class Card {
    private String name;
    private int damage;
    private cardType type;
    private elementType element;


    public Card(String name, int damage, cardType type, elementType element) {
        setName(name);
        setDamage(damage);
        setCardtype(type);
        setElement(element);
    }


    public void setName(String name) { this.name = name; }
    public String getName() { return name; }


    public void setDamage(int damage) { this.damage = damage; }
    public int getDamage() { return damage; }


    public void setCardtype(cardType type) { this.type = type; }
    public cardType getCardtype() { return type; }


    public void setElement(elementType element) { this.element = element; }
    public elementType getElement() { return element; }


    public String getCardInfo() {
        return "Card Name: " + getName() + "\n" +
                "Damage: " + getDamage() + "\n" +
                "Card Type: " + getCardtype() + "\n" +
                "Element: " + getElement() + "\n";
    }
}
