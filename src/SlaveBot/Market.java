package SlaveBot;

import discord4j.core.object.entity.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.function.Consumer;

public class Market implements Serializable {
    static HashMap<String, Item[]> categories = new HashMap<>();

    public static void init(){




        Item[] weapons = {
                new Item("Stick", "A generic stick from a normal tree", 50, new int[]{0,0,1,2,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1000}, "weapon"),
                new Item("Baseball Bat", "Weapon of choice for stereotypical gangs", 100, new int[]{0,5,5,5,6,6,6,8,9,9,14,14,16,19,22}, "weapon"),
                new Item("Slipper", "Taken from an asian parent", 150, new int[]{0,0,0,1,1,1,1,2,2,2,3,5,9,13,15,35}, "weapon"),
                new Item("Wooden Sword", "Two planks and a stick", 200, new int[]{0,20,20,20,20,20,20,20}, "weapon"),
                new Item("Actual Sword", "Lightly used", 450, new int[]{0,0,0,15,15,15,16,16,20,20,23,23,34}, "weapon", 3),
                new Item("French Rifle", "Never fired, dropped once", 500, new int[]{0,0,0,0,0,0,0,0,0,0,0,100}, "weapon",5),
                new Item("German Rifle", "Better dead than red", 650, new int[]{0,0,10,10,25,25,25,25,25,50,50,75,100}, "weapon",7),
                new Item("Perfume", "Strangely effective", 1000, new int[]{0,0,0,0,0,50,75,100,100}, "weapon",10),
                new Item("Programmer's Blade", "`rm -rf /`", 5000, new int[]{100,100,114,125,125,136,139,149,150,159,175,193,199,200,250,12000,Integer.MAX_VALUE}, "weapon", 15),
                new Item("Air", "saBOTage's favourite weapon", 7500, new int[]{1,100,123,253,200,190,320,195,200,384,420,320,295,600,15000,Integer.MAX_VALUE}, "weapon", 17),
                new Item("Spicy Stick", "Basically a horn drill", 17500, new int[]{0,0,0,0,0,0,0,0,1000,0,0,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE}, "weapon", 18),
                new Item("Spicy Air", "Seasoning, perhaps?", 12500, new int[]{0,200,250,340,472,475,501,10000,15000,12000,Integer.MAX_VALUE,730}, "weapon", 20),
                new Item("Lyndon's Inventory", "Contains NSFW content", 8000, new int[]{325,302,231,325,325,352,340,472,475,501,503,321,352,Integer.MAX_VALUE,730}, "weapon", 21),
                new Item("Missile", "Like the name suggests", 2000, new int[]{0}, "weapon", 22),
                new Item("Thot Slayer", "Repels Marie Antoinette", 9001, new int[]{0,150,203,205,857,921,192,349,912,213,413,213,231,246,252}, "weapon", 24)
        };
        categories.put("Weapons", weapons);

        Item[] health = {
                new Item("Rag", "Works in a pinch", 10, new int[]{5,6,7,8,9,10}, "heal"),
                new Item("Bandage", "Generic medical device", 20, new int[]{7,7,7,7,8,8,8,15},"heal"),
                new Item("Antibiotic", "How do pills stop bullet wounds?", 100, new int[]{2,5,10,10,15,15,15,12,11},"heal"),
                new Item("Wound sealant", "Glorified glue", 150, new int[]{10,10,10,10,11,11,11,11,12,12,12,13,15,18,20,21},"heal"),
                new Item("Potion", "Good healing, but unreliable", 200, new int[]{0,50,65},"heal",3),
                new Item("Splash potion (Tier III)", "Even better, even more unreliable", 400, new int[]{0,0,0,75,100},"heal",5),
                new Item("Pills", "Drugs to take your mind off the pain", 600, new int[]{30,30,30,31,36,38,47,50},"heal",10),
                new Item("MedKit", "Certified medical treatment kit", 1500, new int[]{100},"heal",15)

        };
        categories.put("Medical Items", health);

        Item[] shield = {
                new Item("Newspaper", "Effective against weak, low velocity projectiles", 50, new int[]{1,1,1,1,1,2,2,2,3,4,5}, "shield"),
                new Item("Leather Shield", "At least its better than paper", 100, new int[]{2,2,2,2,2,2,3,3,7,8,9,10,13,12,13,17,18}, "shield"),
                new Item("Iron Sheet", "Strong sheet metal", 200, new int[]{1,4,4,4,4,5,10,12,12,11,13,13,18}, "shield"),
                new Item("Chainmail Armor", "Hard to obtain in Minecraft", 499, new int[]{3,3,3,3,5,7,9,11,13,12,14,15,20,22,25}, "shield"),
                new Item("Shield", "Steel and Iron plating, with a strong wooden base", 600, new int[]{10,16,11,23,29,51,53,55,78}, "shield",3),
                new Item("Sans-culottes", "Vive la Révolution", 1000, new int[]{30,35,60,76,76,68,80,100}, "shield",5),
                new Item("Programmer's Guard", "`int i = 0;`", 1500, new int[]{100,120,150,134,110}, "shield",10),
                new Item("Substitute", "Foe put in a substitute", 2500, new int[]{120,220,250,234,510,310}, "shield",15),
                new Item("Firewall", "Cut ethernet cable to activate firewall", 3141, new int[]{200,220,250,334,410,321,493,675,372}, "shield",17)
        };
        categories.put("Shields", shield);

        Item[] utilities = {
                new Item("Armor Piercing Bullet", "Good against shields",2150, new int[]{150,200,200,232,182,182,443,263,243,201,201,201,201,201,302,500,736,Integer.MAX_VALUE}, "utility", 10),
                new Item("Vault", "Keeps $10000 from being stolen",12500, new int[]{0}, "utility"),
                new Item("Crowbar", "Useful for penetrating vaults",1000, new int[]{0}, "utility"),
                new Item("Dynamite", "Useful for penetrating vaults",3000, new int[]{0}, "utility",5),
                new Item("Smoke Grenade", "Increases evasion stat",2000, new int[]{0}, "utility",5),
                new Item("Net", "Increases slave capture chance",1000, new int[]{0}, "utility",5),
                new Item("Chloroform", "Sharply increases slave capture chance",5000, new int[]{0}, "utility",10),
                new Item("Vietcong Uniform", "Increases escape rate",2500, new int[]{0}, "utility",10),
                new Item("Underground Railroad", "Assists in freeing of slaves", 3000, new int[]{0}, "utility",10),
                new Item("Ox", "Good for sacrificing to Zeus", 299, new int[]{0}, "utility", 10),
                new Item("Programmer's tool", "`sudo make me a sandwich`",50000, new int[]{0}, "utility",19)
        };
        categories.put("Utilities", utilities);



    }

    private static Consumer<EmbedCreateSpec> createEmbedMarket(String category, User user){
        return embed -> {
            embed.setTitle("Available " + category);

            Item[] items = categories.get(category);
            for (Item i : items) {
                if(i.minLevel <= user.getLevel()) {
                    embed.addField(i.getName() + " -- $" + i.getPrice(), i.getDescription(), false);
                }

            }
        };
    }

    static void sendMarket(MessageChannel channel, User user){
        for (String s : categories.keySet()) {
            BotUtils.sendEmbedSpec(channel, createEmbedMarket(s, user));
        }
    }

    static void sendMarket(MessageChannel channel, String category, User user){
        BotUtils.sendEmbedSpec(channel, createEmbedMarket(category, user));
    }

    static boolean itemExists(String item){
        for (String s : categories.keySet()) {
            for (Item i : categories.get(s)) {
                if(i.getName().equalsIgnoreCase(item)) return true;
            }
        }

        return false;
    }

    static Item getItem(String item){
        for (String s : categories.keySet()) {
            for (Item i : categories.get(s)) {
                if(i.getName().equalsIgnoreCase(item)) return i;
            }
        }

        return null;
    }
}
