package SlaveBot;

public class Weapon extends Item{

    private static final long serialVersionUID = -7987687458297070753L;

    int[] damage;
    int[] tDamage;
    int[] sDamage;

    String description;

    double accuracy = 100;

    Weapon(String name, String description, int[] damage, int[] tDamage, int[] sDamage, double accuracy, int price){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.tDamage = tDamage;
        this.sDamage = sDamage;

        this.type = "weapon";
        this.price = price;
        this.accuracy = accuracy;
        this.minLevel = 0;
    }

    Weapon(String name, String description, int[] damage, int[] tDamage, int[] sDamage, double accuracy, int price, int minLevel){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.tDamage = tDamage;
        this.sDamage = sDamage;
        this.accuracy = accuracy;

        this.type = "weapon";
        this.price = price;
        this.minLevel = minLevel;
    }


    int getDamageValue(){
        if(damage == null) return 0;
        return damage[BotUtils.random.nextInt(damage.length)];
    }

    int getTDamageValue(){
        if(tDamage == null) return 0;
        return tDamage[BotUtils.random.nextInt(tDamage.length)];
    }

    int getSDamageValue(){
        if(sDamage == null) return 0;
        return sDamage[BotUtils.random.nextInt(sDamage.length)];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    int getPrice() {
        return super.getPrice();
    }

    public double getAccuracy() {
        return accuracy;
    }

    @Override
    public String getDescription() {
        return description;
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
