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
                new Weapon("Stick", "A generic stick from a normal tree",
                        new int[]{1,2,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1000},
                        null, null, 100, 45),

                new Weapon("Baseball Bat", "A long, wooden stick",
                        new int[]{5,5,5,6,6,6,8,9,9,14,14,16,19,22}, null, null,
                        97, 100 ),

                new Weapon("Slipper", "Taken from an asian parent",
                        new int[]{1,1,1,1,2,2,2,3,5,9,13,15,35}, null, null,
                        100, 150),

                new Weapon("Wooden Sword", "Two planks and a stick",
                        new int[]{20,22}, null, null,
                        95, 200, 2),

                new Weapon("Actual Sword", "Lightly used",
                        new int[]{17,20,22,25,29,32,47,50}, null, null,
                        92, 450, 2),

                new Weapon("French Rifle", "Never fired, dropped once",
                        new int[]{100}, null, null,
                        25, 500, 2),

                new Weapon("German Rifle", "Better dead than red",
                        new int[]{25,25,50,50,50,75,75,100,200}, null, null,
                        87, 500, 3),

                new Weapon("Perfume", "Strangely effective",
                        new int[]{10,20,22,30}, new int[]{10,15,17}, null,
                        80, 1050, 4),

                new Weapon("Programmer's Blade", "`rm -rf /`",
                        new int[]{100,100,114,125,125,136,139,149,150,159,175,193,199,200,250,12000,Integer.MAX_VALUE}, null, null,
                        100, 5000, 5),

                new Weapon("Air", "saBOTage's favourite weapon",
                        new int[]{1,100,123,253,200,190,320,195,200,384,420,320,295,600,15000,Integer.MAX_VALUE}, null, null,
                        100, 7600, 12),

                new Weapon("Spicy Air", "Seasoning, perhaps?",
                        new int[]{1000,10000,10000,Integer.MAX_VALUE}, null, null,
                        43, 12500, 15),

                new Weapon("Spicy Stick", "Spicy Stick vs Sheer Cold",
                        new int[]{Integer.MAX_VALUE}, null, null,
                        30, 17500, 15),

                new Weapon("Missile", "So much power, just like Kim-Jong Un",
                        null, new int[]{20,21,20}, null,
                        90, 10000, 18),

                new Weapon("Lyndon's Inventory", "Contains NSFW content",
                        new int[]{325,402,431,425,425,452,340,472,475,501,503,321,352,Integer.MAX_VALUE,730}, null, null,
                        100, 8000, 20),

                new Weapon("Thot Slayer", "Two diamonds, two sticks",
                        new int[]{0,350,303,405,857,921,392,349,912,413,413,413,531,446,652,20000,Integer.MAX_VALUE}, null, null,
                        100, 9020, 22),

                new Weapon("Armor Piercing Bullet", "Good against shields",
                        null, new int[]{25, 22,21,10,10,50,40                                                     }, null,
                        90, 6600, 20),


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
                new Item("Vault", "Keeps $10000 from being stolen",12500, new int[]{0}, "utility"),
                new Item("Crowbar", "Useful for penetrating vaults",1000, new int[]{0}, "utility"),
                new Item("Dynamite", "Useful for penetrating vaults",3000, new int[]{0}, "utility",5),
                new Item("Smoke Grenade", "Increases evasion stat",2000, new int[]{0}, "utility",5),
                new Item("Net", "Increases slave capture chance",1000, new int[]{0}, "utility",5),
                new Item("Chloroform", "Sharply increases slave capture chance",5000, new int[]{0}, "utility",10),
                new Item("Vietcong Uniform", "Increases escape rate",2500, new int[]{0}, "utility",10),
                new Item("Underground Railroad", "Assists in freeing of slaves", 3000, new int[]{0}, "utility",10),
                new Item("Ox", "Good for sacrificing to Zeus", 299, new int[]{0}, "utility", 10),
                new Item("Programmer's tool", "`sudo make me a sandwich`",50000, new int[]{0}, "utility",19),
                new Item("Trait Remover", "Removes one trait", 40000, new int[]{0}, "utility")
        };
        categories.put("Utilities", utilities);

        Item[] traitstuff = {
                new Item("Trait Remover", "Removes one trait", 40000, new int[]{0}, "utility"),
                new Item("Trait Repair", "Repairs one trait", 20000, new int[]{0}, "utility"),
                new Item("Trait Voucher", "Gives one free trait roll", 45000, null, "utility", 0)
        };
        categories.put("Traits", traitstuff);



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
