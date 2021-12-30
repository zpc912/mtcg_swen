package mtcg.Application;
import mtcg.Enum.elementType;

public class MonsterCard extends Card {
    private String name;
    private int damage;
    private elementType element;


    public MonsterCard(String name, int damage, elementType element) {
        setName(name);
        setDamage(damage);
        setElement(element);
    }
}
