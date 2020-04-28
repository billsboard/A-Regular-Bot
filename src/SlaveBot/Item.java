package SlaveBot;

import java.io.Serializable;

public class Item implements Serializable, Comparable {

    public static final long serialVersionUID = 10L;

    String name;
    String description;

    int price;

    int[] damage = {0};

    boolean shield, utility, heal;

    String type = "";

    int minLevel = 0;

    public Item(String name, String description, int price){
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public Item(String name, String description, int price, int[] damage, String type, int minLevel){
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

    public Item(String name, String description, int price, int[] damage, String type){
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

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage[BotUtils.random.nextInt(damage.length)];
    }

    public String getDescription() {
        return description;
    }

    public void setDamage(int[] damage) {
        this.damage = damage;
    }

    public boolean isWeapon(){
        return !(shield || utility || heal);
    }


    public boolean isHeal(){
        return heal;
    }

    public boolean isShield(){
        return shield;
    }


    public boolean isUtility() {
        return utility;
    }

    public int compareTo(Object o){
        return this.getName().compareTo(((Item) o).getName());
    }
}
