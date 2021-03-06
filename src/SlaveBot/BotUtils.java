package SlaveBot;

import SlaveBot.Traits.CorneredFoxTrait;
import SlaveBot.Traits.Trait;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.LongStream;

class BotUtils {

    // Constants for use throughout the bot
    static String BOT_PREFIX = "$";
    static String DISCORD_BOTLIST_PREFIX = "~";
    static long MILLIS_IN_MINUTE = 1000 * 60;
    static long MILLIS_IN_HOUR = 1000 * 60 * 60;

    static long slaveTime = BotUtils.MILLIS_IN_MINUTE;
    static long dailyTime = BotUtils.MILLIS_IN_HOUR * 24;
    static long escapeTime = BotUtils.MILLIS_IN_MINUTE / 2;
    static long attackTime = BotUtils.MILLIS_IN_MINUTE / 3;
    static long healTime = BotUtils.MILLIS_IN_MINUTE * 2/ 3;
    static long workTime = BotUtils.MILLIS_IN_MINUTE * 2;
    static long crimeTime = BotUtils.MILLIS_IN_MINUTE * 6;
    static long weeklyTime = BotUtils.MILLIS_IN_HOUR * 24 * 7;
    static long slaveWorkTime = BotUtils.MILLIS_IN_MINUTE / 3;
    static long lootTime = (BotUtils.MILLIS_IN_MINUTE * 10);
    static long dropTime = (BotUtils.MILLIS_IN_HOUR);
    static long traitToggleTime = BotUtils.MILLIS_IN_MINUTE * 20;


    static boolean botFightActive = false;
    static int botTier = 0;
    static BotTier[] botTiers = {
            null,
            new BotTier(20000, 25000, 100, 0, 100),
            new BotTier(25000, 45000, 300, 50, 150),
            new BotTier(50000, 150000, 400, 75, 200),
            new BotTier(100000, 250000, 500, 100, 200),
            new BotTier(180000, 380000, 500, 100, 350),
            new BotTier(400000, 760000, Integer.MAX_VALUE, 0, Integer.MAX_VALUE),
            new BotTier(500000, 1000000, Integer.MAX_VALUE, 150, Integer.MAX_VALUE)
    };

    static double maxReputationCap = 100.0;

    static int maxBankValue = 650000;

    static int[][] bankUpgradeValues = {{450000,350000},{900000,400000},{1150000,600000},{1550000, 850000}, {1950000, 1000000}};

    static Item[] botUsableWeapons = {
            Market.getItem("Actual sword"),
            Market.getItem("Wooden Sword"),
            Market.getItem("Stick"),
            Market.getItem("Perfume"),
            Market.getItem("Perfume"),
            Market.getItem("German Rifle"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("Air"),
            Market.getItem("Spicy Air"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("German rifle"),
            Market.getItem("Air"),
            Market.getItem("Lyndon's inventory"),
            Market.getItem("Lyndon's inventory"),
            Market.getItem("Spicy air"),
            Market.getItem("Air"),
            Market.getItem("Air"),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,Integer.MAX_VALUE}, null, null, 100, 1),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,Integer.MAX_VALUE}, null, null, 100, 1),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{Integer.MAX_VALUE}, null, null, 100, 75000),
    };

    static long[] ADMINS = {506696814490288128L};

    static BotFightTimer currentFightTimer;
    static long fightChannelID;

    static Random random = new Random();

    static void sendMessage(MessageChannel channel, String message){
        channel.createMessage(message).block();
    }

    static User getInternalUserFromMention(String mention){
        return Tools.getUser(Long.parseLong(mention.replaceAll("[^\\d.]", "")));
    }

    static void sendRatelimitMessage(MessageChannel channel, long millisRemaining){
        channel.createMessage(":clock10: Please wait " + convertSecondsToHMmSs(millisRemaining/1000) + " longer").block();
    }

    static void sendEmbedSpec(MessageChannel channel, Consumer<EmbedCreateSpec> spec){

        channel.createMessage(embed -> embed.setEmbed(spec)).block();
    }

    static Item getItem(String name){

        Item item = Market.getItem(name);
        if(item == null) item = LootBox.getItem(name);

        return item;
    }

    static void startBotFight(MessageChannel channel){
        currentFightTimer = new BotFightTimer(TimeUnit.MINUTES.toMillis(15), channel);
        fightChannelID = channel.getId().asLong();
        currentFightTimer.start();
        botTier = 1;
        setBotTier(botTier);
        botFightActive = true;
    }

    static void endBotFight(){
       User bot = Tools.recreateUser(Main.client.getSelfId().get().asLong());
       currentFightTimer.interrupt();
       botTier = 0;
       botFightActive = false;
    }

    static void setBotTier(int tier){
        if(tier > botTiers.length){return;}

        User bot = Tools.getUser(Main.client.getSelfId().get().asLong());
        Tools.users.remove(bot);
        bot = Tools.getUser(bot.id);
        bot.maxHealth = botTiers[tier].health;
        bot.level = tier + 100;
        bot.setHealth(bot.maxHealth);
        bot.baseDefense = botTiers[tier].def;
        bot.setShield(botTiers[tier].shield);
        bot.addMoney(random.nextInt(botTiers[tier].moneyMax - botTiers[tier].moneyMin) + botTiers[tier].moneyMin);
    }

    private static String convertSecondsToHMmSs(long seconds) {

        long s = seconds % 60;

        long m = (seconds / 60) % 60;

        long h = (seconds / (60 * 60)) % 24;

        long d = (seconds / (60 * 60 * 24));



        if(d > 0){
            return String.format("%d days, %02d:%02d:%02d", d,h,m,s);
        }
        else{
            return String.format("%d:%02d:%02d", h,m,s);
        }
    }

    static String capitalizeFirst(String s){
        String[] words = s.split(" ");
        String out = "";
        for (String s1 : words){
            if(s1.length() > 0){
                s1 = s1.substring(0,1).toUpperCase() + s1.substring(1);
            }

            out += s1 + " ";
        }
        out = out.trim();

        return out;
    }

    static boolean isNumeric(String s){
        try{
            Integer.parseInt(s);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    static boolean isAdmin(long DiscordID){
        return LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == DiscordID);
    }

    static String getPublicIP(){
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            String ip = in.readLine();
            return ip;
        } catch (Exception e){}
        return "0.0.0.0";
    }
}