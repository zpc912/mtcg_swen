package mtcg.Classes;
import mtcg.Enum.elementType;

public class SpellCard extends Card {
    private String name;
    private int damage;
    private elementType element;


    public SpellCard(String name, int damage, elementType element) {
        setName(name);
        setDamage(damage);
        setElement(element);
    }
}
