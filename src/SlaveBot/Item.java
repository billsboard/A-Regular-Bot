package SlaveBot;

import java.io.Serializable;

public class Item implements Serializable, Comparable {

    public static final long serialVersionUID = 10L;

    private String name;
    private String description;

    int price;

    private int[] damage = {0};

    private boolean shield, utility, heal;

    String type = "";


    int minLevel = 0;

    Item(String name, String description, int price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    Item(){}


    Item(String name, String description, int price, int[] damage, String type, int minLevel){
        this.name = name;
        this.description = description;
        this.price = price;
        this.damage = damage;
        this.type = type;
        this.minLevel = minLevel;
        switch (type){
            case "weapon":
                break;
            case "heal":
                heal = true;
                break;
            case "utility":
                utility = true;
                break;
            case "shield":
                shield = true;
                break;
            default:
                break;
        }
    }

    Item(String name, String description, int price, int[] damage, String type){
        this.name = name;
        this.description = description;
        this.price = price;
        this.damage = damage;
        this.type = type;
        switch (type){
            case "weapon":
                break;
            case "heal":
                heal = true;
                break;
            case "utility":
                utility = true;
                break;
            case "shield":
                shield = true;
                break;
            default:
                break;
        }
    }

    int getPrice() {
        return price;
    }

    String getName() {
        return name;
    }

    int getDamage() {
        return damage[BotUtils.random.nextInt(damage.length)];
    }

    String getDescription() {
        return description;
    }

    public void setDamage(int[] damage) {
        this.damage = damage;
    }

    boolean isWeapon(){
        return !(shield || utility || heal);
    }


    boolean isHeal(){
        return heal;
    }

    boolean isShield(){
        return shield;
    }


    boolean isUtility() {
        return utility;
    }

    public int compareTo(Object o){
        return this.getName().compareTo(((Item) o).getName());
    }
}
