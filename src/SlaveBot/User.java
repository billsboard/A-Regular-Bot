package SlaveBot;

import discord4j.core.object.entity.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public class User implements Serializable {

    private static final long serialVersionUID = 42L;

    int money;
    long id;
    String username;

    int level = 0;
    double xp = 0.0;

    long lastDaily, lastEscape, lastCrime, lastSlave, lastAttack, lastHeal, lastWork, lastWeekly, lastSlaveWork, lastLoot;

    int escapedSlaves = 0, deadSlaves = 0;

    double health = 100, maxHealth = 100;

    int slaveModifier = 0, escapeModifier = 0;
    String tempStringVar;

    int bankMoney = 0;

    double defense = 0;

    int shieldRemaining = 0;

    int kills = 0, deaths = 0;
    boolean challengeActive = false;

    double reputation = 0.00;

    HashMap<Item, Integer> inventory = new HashMap<>();
    ArrayList<String> slaveList = new ArrayList<>();
    ArrayList<Long> idList = new ArrayList<>();


    public User(long discordID){
        id = discordID;
        username = Main.getUserByID(id).getUsername();
        money = 0;
    }

    public boolean canWeekly(){
        Date date1 = new Date(lastWeekly);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.weeklyTime){
            Calendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.setTimeZone(TimeZone.getTimeZone("PST"));
            lastWeekly =  c.getTime().getTime();
            return true;
        }
        return false;
    }

    public boolean canDaily(){
        Date date1 = new Date(lastDaily);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.dailyTime){
            Calendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.setTimeZone(TimeZone.getTimeZone("PST"));
            lastDaily = c.getTime().getTime();
            return true;
        }
        return false;
    }

    public boolean canCrime(){
        Date date1 = new Date(lastCrime);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.crimeTime){
            lastCrime = date2.getTime();
            return true;
        }
        return false;
    }

    public boolean canLoot(){
        Date date1 = new Date(lastLoot);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.lootTime){
            lastLoot = date2.getTime();
            return true;
        }
        return false;
    }

    public boolean canSlaveWork(){
        Date date1 = new Date(lastSlaveWork);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.slaveWorkTime){
            lastSlaveWork = date2.getTime();
            return true;
        }
        return false;
    }

    public boolean canWork(){
        Date date1 = new Date(lastWork);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.workTime){
            lastWork = date2.getTime();
            return true;
        }
        return false;
    }

    public boolean canSlave(){
        Date date1 = new Date(lastSlave);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.slaveTime){
            lastSlave = date2.getTime();
            return true;
        }
        return false;
    }

    public boolean canEscape(){
        Date date1 = new Date(lastEscape);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.escapeTime){
            lastEscape = date2.getTime();
            return true;
        }
        return false;
    }

    public boolean canAttack(){
        Date date1 = new Date(lastAttack);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.attackTime){
            lastAttack = date2.getTime();
            return true;
        }
        return false;
    }

    public boolean canHeal(){
        Date date1 = new Date(lastHeal);
        Date date2 = new Date();
        if(Math.abs(date1.getTime() - date2.getTime()) >= BotUtils.healTime){
            lastHeal = date2.getTime();
            return true;
        }
        return false;
    }

    public int getQuantity(Item item){
        if(!containsItem(item)) return 0;
        if(inventory.get(getItem(item)) <= 0){
            inventory.remove(getItem(item));
        }

        return inventory.get(getItem(item));
    }

    public boolean containsItem(Item item){
        for (Item i : inventory.keySet()) {
            if(i == null){}
            else if(i.getName().equals(item.getName()) && inventory.get(i) >= 1){
                return true;
            }
        }

        return false;
    }

    public Item getItem(Item item){
        if(!containsItem(item)){return null;}
        for (Item i : inventory.keySet()) {
            if(i.getName().equals(item.getName())){
                return i;
            }
        }


        return item;
    }

    public void addItem(Item item){
        if(!containsItem(item)){
            inventory.put(item, 1);
        }
        else{
            inventory.put(getItem(item), inventory.get(getItem(item)) + 1);
        }
    }

    public void addItem(Item item, int amount){
        if(!containsItem(item)){
            inventory.put(item, amount);
        }
        else{
            inventory.put(getItem(item), inventory.get(getItem(item)) + amount);
        }
    }


    public void removeItem(Item item){
        if(!containsItem(item)){return;}
        else if(inventory.get(getItem(item)) - 1 <= 0){
            inventory.remove(getItem(item));
        }
        else{
            inventory.put(getItem(item), inventory.get(getItem(item)) - 1);
        }
    }

    public void removeItem(Item item, int amount){
        if(!containsItem(item)){return;}
        else if(inventory.get(getItem(item)) - amount <= 0){
            inventory.remove(getItem(item));
        }
        else{
            inventory.put(getItem(item), inventory.get(getItem(item)) - amount);
        }
    }


    public int getMoney(){
        return money;
    }

    public void addMoney(int amount){
        if((long) money + (long) amount > Integer.MAX_VALUE){money = Integer.MAX_VALUE;}
        else if(amount <= 0){return;}
        else{
            money += amount;
        }
    }

    public void removeMoney(int amount){
        if(money - amount < 0){money = 0;}
        else if(amount <= 0){return;}
        else {
            money -= amount;
        }
    }

    public void addSlave(String slaveName, long id){
        slaveList.add(slaveName);
        idList.add(id);
        addItem(new Item("Slave", "A healthy, working slave", 200));
    }

    public void removeSlave(String slaveName){
        if(!slaveList.contains(slaveName)){return;}

        int index = slaveList.indexOf(slaveName);
        slaveList.remove(slaveName);
        idList.remove(index);
        removeItem(new Item("Slave", "A healthy, working slave", 200));

        //verifySlaves();
    }

    public void verifySlaves(){
        int slavesByName, slavesByInventory;
        slavesByName = slaveList.size();
        slavesByInventory = inventory.get(getItem(new Item("Slave", "A healthy, working slave", 200)));

        if(slavesByInventory > slavesByName){
            int diff = slavesByInventory - slavesByName;
            for (int i = 0; i < diff; i++) {
                slaveList.add("Slave-" + Math.abs(BotUtils.random.nextInt()));
            }
        }
        else if(slavesByName > slavesByInventory){
            int diff = slavesByName - slavesByInventory;
            for (int i = 0; i < diff; i++) {
                slaveList.remove(0);
            }
        }
    }

    public String getFormattedSlaveList(){
        String output = "Displaying captured slaves for " + username + ":\n";
        for (int i = 0; i < slaveList.size(); i++) {
            output += slaveList.get(i) + "\n";
        }

        return output.trim();
    }

    public void sendInventory(MessageChannel channel){
        Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
            embed.setTitle(username + "'s inventory");
            String s = "";
            ArrayList<Item> keys = new ArrayList<>(inventory.keySet());
            Collections.sort(keys);
            for (Item i : keys) {
                int quantity = inventory.get(i);
                if(quantity <= 0) inventory.remove(i);
                else{
                    s += quantity + " " + i.getName() + "\n";
                }
            }
            embed.addField("\u200b", s, false);
        };

        BotUtils.sendEmbedSpec(channel, embedCreateSpec);
    }

    public double getHealth(){
        health = Double.parseDouble(String.format("%.2f", (float) health));
        return Double.parseDouble(String.format("%.2f", (float) health));
    }

    public void damage(double health){
        health *= (1 - (defense / (defense + 150)));
        if(shieldRemaining > health) {shieldRemaining -= health;}
        else{
            health -= shieldRemaining;
            shieldRemaining = 0;
            if(this.health - health <= 0){
                this.health = 0;
            }
            else{
                this.health -= health;
            }
        }
    }

    public void heal(int health){
        if(this.health + health > maxHealth || this.health + health < 0){
            this.health = maxHealth;
        }
        else{
            this.health += health;
        }
    }

    public void setHealth(double health){
        this.health = health;
    }

    public double getMaxHealth(){
        return maxHealth;
    }

    public void setShield(int shield){
        this.shieldRemaining = shield;
    }

    public int getShield(){
        return shieldRemaining;
    }

    public double getReputation(){
        return reputation;
    }

    public void changeReputation(double change){
        if(reputation + change > BotUtils.maxReputationCap) reputation = BotUtils.maxReputationCap;
        else if(reputation + change <= -BotUtils.maxReputationCap) reputation = -BotUtils.maxReputationCap;
        else reputation += change;
    }

    public double getXPRequired(){
        return Double.parseDouble(String.format("%.2f", (float) 25* Math.pow(1.5, level)));
    }

    public void gainXP(MessageChannel channel, double xp){
        this.xp += xp;
        while (this.xp > getXPRequired()){
            this.xp -= getXPRequired();
            level++;
            defense = level*5;
            maxHealth = 100 + level*3;
            Leveling.addLeaderboard(this);
            BotUtils.sendMessage(channel, "Congratulations " + Main.getUserByID(id).getMention() + ", you have advanced to level " + level);
        }
    }

    public double getXp(){
        return xp;
    }

    public int getLevel(){
        return level;
    }




}
