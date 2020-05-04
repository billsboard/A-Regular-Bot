package SlaveBot;

import com.sun.management.OperatingSystemMXBean;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import jnr.ffi.annotations.In;
import reactor.core.publisher.Flux;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.annotation.Target;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.LongStream;

class EventProcessor {
    Flux<MessageCreateEvent> on;

    ArrayList<ListenerThread> gamesActive = new ArrayList<>();

    PrintStream logSteam = null;
    PrintStream commandLogStream = null;
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");


    EventProcessor(Flux<MessageCreateEvent> on) {
        this.on = on;

        File file = new File("log.txt");
        File commandLog = new File("commandLog.txt");

        try {
            logSteam = new PrintStream(file);
            commandLogStream = new PrintStream(commandLog);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PrintStream finalLogSteam = logSteam;
        on.subscribe(messageCreateEvent -> {
            try {
                onMessageReceived(messageCreateEvent.getMessage());
            }catch (Exception e){
                System.out.println(e.getClass().getSimpleName());
                e.printStackTrace(finalLogSteam);
            }
        });
    }

    private void onMessageReceived(Message message){

        if(!message.getContent().isPresent()) return;

        String body = message.getContent().get();
        MessageChannel channel = message.getChannel().block();
        Guild guild = message.getGuild().block();

        String[] lowerArgs = body.toLowerCase().split(" ");
        String[] rawArgs = body.split(" ");

        discord4j.core.object.entity.User sender = message.getAuthor().get();
        User internalSender = Tools.getUser(sender.getId().asLong());


        if(sender.isBot()) return;

        for (ListenerThread thread : gamesActive) {
            thread.processCommand(lowerArgs[0], lowerArgs, internalSender);
        }

        if(!body.contains(BotUtils.BOT_PREFIX)) return;

        for (int i = 0; i < lowerArgs.length; i++) {
            lowerArgs[i] = lowerArgs[i].replaceAll("’", "'");
        }

        commandLogStream.println("[" + sdf.format(new Date()) + "] " + internalSender.username + ": " + body);


        /* Person Commands */
        switch (lowerArgs[0].substring(1)){
            case "ping": case "status": case "botinfo":{
                Consumer<EmbedCreateSpec> embedCreateSpec = e -> {
                    e.setTitle("Bot information page");

                    e.addField("**Status**", ":green_circle: Online", true);
                    e.addField("**Ping**", Main.client.getResponseTime() + "ms", true);
                    e.addField("**Prefix**", "You already know", true);

                    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                            OperatingSystemMXBean.class);

                    String col1 = "",col2 = "",col3 = "";
                    col1 += "CPU Usage:\n"; col2 += osBean.getSystemCpuLoad() + "%\n"; col3 += "\u200b\n";
                    col1 += "Memory:\n"; col2 += "Free: `" + osBean.getFreePhysicalMemorySize() + "`B\n"; col3 += "Total: `" + osBean.getTotalPhysicalMemorySize() + "`B\n";
                    col1 += "Swap:\n"; col2 += "Free: `" + osBean.getFreeSwapSpaceSize() + "`B\n"; col3 += "Total: `" + osBean.getTotalSwapSpaceSize() + "`B\n";
                    col1 += "Network:\n"; col2 += "IP: `" + BotUtils.getPublicIP() + "`\n"; col3 += "Status: OK" + "\n";



                    e.addField("**System info**", col1, true);
                    e.addField("\u200b", col2, true);
                    e.addField("\u200b", col3, true);

                    discord4j.core.object.entity.User bill = Main.client.getUserById(Snowflake.of(506696814490288128L)).block();
                    e.addField("**Credits**", "Coded by " + bill.getMention() +
                            "\nCoded using Discord4j version 3.0.14\n", true);


                };
                BotUtils.sendEmbedSpec(channel, embedCreateSpec);
                break;
            }
            case "profile": case "p":{
                if(lowerArgs.length < 2){
                    Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                        embed.setTitle(sender.getUsername() + "'s profile");
                        embed.addField("Level:", "" + internalSender.getLevel(), true);
                        embed.addField("Progress:", String.format("%.2f", (float) internalSender.getXp()) + "/" + internalSender.getXPRequired(), true);
                        embed.addField("\u200b", String.format("%.2f", (float) (internalSender.getXp() / internalSender.getXPRequired()) * 100) + "%", true);
                        embed.addField("Balance", ":moneybag: " + internalSender.getMoney(), true);
                        embed.addField("Health", ":heart: " + internalSender.getHealth(), true);
                        embed.addField("Shield", ":shield: " + internalSender.getShield(), true);
                        embed.addField("Kills:", "" + internalSender.kills, true);
                        embed.addField("Deaths:", "" + internalSender.deaths, true);
                        embed.addField("K/D Ratio:", String.format("%.2f", internalSender.deaths > 0 ? ((float)internalSender.kills)/((float)internalSender.deaths) : 0.00f), true);
                        embed.addField("Reputation", ":scales: " + String.format("%.2f", (float) internalSender.getReputation()), false);
                        embed.addField("Slaves:", "**Alive**: " + internalSender.slaveList.size(), true);
                        embed.addField("\u200b", "**Escaped**: " + internalSender.escapedSlaves, true);
                        embed.addField("\u200b", "**Dead**: " + internalSender.deadSlaves, true);
                    };
                    BotUtils.sendEmbedSpec(channel,embedCreateSpec);
                }
                else{
                    User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                    if(target == null){
                        BotUtils.sendMessage(channel, "Target user not found!");
                        break;
                    }

                    Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                        embed.setTitle(target.username + "'s profile");
                        embed.addField("Level:", "" + target.getLevel(), true);
                        embed.addField("Progress:", String.format("%.2f", (float) target.getXp())+ "/" + target.getXPRequired(), true);
                        embed.addField("\u200b", String.format("%.2f", (float) (target.getXp() / target.getXPRequired()) * 100) + "%", true);
                        embed.addField("Balance", ":moneybag: " + target.getMoney(), true);
                        embed.addField("Health", ":heart: " + target.getHealth(), true);
                        embed.addField("Shield", ":shield: " + target.getShield(), true);
                        embed.addField("Kills:", "" + target.kills, true);
                        embed.addField("Deaths:", "" + target.deaths, true);
                        embed.addField("K/D Ratio:", String.format("%.2f", target.deaths > 0 ? ((float)target.kills)/((float)target.deaths) : 0.00f), true);
                        embed.addField("Reputation", ":scales: " + String.format("%.2f", (float) target.getReputation()), false);
                        embed.addField("Slaves:", "**Alive**: " + target.slaveList.size(), true);
                        embed.addField("\u200b", "**Escaped**: " + target.escapedSlaves, true);
                        embed.addField("\u200b", "**Dead**: " + target.deadSlaves, true);
                    };
                    BotUtils.sendEmbedSpec(channel,embedCreateSpec);
                }

                break;
            }
            case "werk":{
                BotUtils.sendMessage(channel, "No "+ sender.getMention() +", you must learn to spell!");
                break;
            }
            case "stats":{
                if(lowerArgs.length < 2){
                    Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                        embed.setTitle(sender.getUsername() + "'s stats");
                        embed.addField("Health", ":heart: " + internalSender.getHealth() + "/" + internalSender.getMaxHealth(), true);
                        embed.addField("Defense", ":shield: " + internalSender.defense, true);
                    };
                    BotUtils.sendEmbedSpec(channel,embedCreateSpec);
                }
                else {
                    User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                    if (target == null) {
                        BotUtils.sendMessage(channel, "Target user not found!");
                        break;
                    }

                    Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                        embed.setTitle(sender.getUsername() + "'s stats");
                        embed.addField("Health", ":heart: " + internalSender.getHealth() + "/" + internalSender.getMaxHealth(), true);
                        embed.addField("Defense", ":shield: " + internalSender.defense, true);
                    };
                    BotUtils.sendEmbedSpec(channel, embedCreateSpec);
                }
                break;
            }
            case "daily":{
                if(internalSender.canDaily()){
                    internalSender.addMoney(7500);
                    internalSender.gainXP(channel, 45);
                    BotUtils.sendMessage(channel, "You claimed your daily reward of 7500 Canadian Pesos and 45 XP");
                }
                else {
                    BotUtils.sendRatelimitMessage(channel, BotUtils.dailyTime - (new Date().getTime() - internalSender.lastDaily));
                }
                break;
            }
            case "weekly":{
                if(internalSender.canWeekly()){
                    internalSender.addMoney(90000);
                    internalSender.gainXP(channel, 200);
                    BotUtils.sendMessage(channel, "You claimed your daily reward of 90000 Canadian Pesos and 200 XP");
                }
                else {
                    BotUtils.sendRatelimitMessage(channel, BotUtils.weeklyTime - (new Date().getTime() - internalSender.lastWeekly));
                }
                break;
            }
            case "loot":{
                if(internalSender.canLoot()){
                    Item item = LootBox.LOOT[BotUtils.random.nextInt(LootBox.LOOT.length)];
                    if(item!=null){
                        internalSender.addItem(item);
                        BotUtils.sendMessage(channel, "You found a(n) " + item.getName() + " out of the crate!");
                    }
                    else {
                        int money = BotUtils.random.nextInt(10000);
                        internalSender.addMoney(money);
                        BotUtils.sendMessage(channel, "You found $" + money + " in the crate!");
                    }
                }
                else {
                    BotUtils.sendRatelimitMessage(channel, BotUtils.lootTime - (new Date().getTime() - internalSender.lastLoot));
                }
                break;
            }
            case "leaderboard":{
                Leveling.displayLeaderBoard(channel, internalSender);
                break;
            }
            case "deposit": case "dep":{
                if (lowerArgs.length < 2) {
                    BotUtils.sendMessage(channel, "Command usage: `deposit <amount>");
                    break;
                }

                if(lowerArgs[1].equalsIgnoreCase("all")) lowerArgs[1] = Integer.toString(internalSender.getMoney());

                if(!BotUtils.isNumeric(lowerArgs[1]) || Integer.parseInt(lowerArgs[1]) <= 0){
                    BotUtils.sendMessage(channel, "Argument must be a non-negative, positive integer that is greater than 0");
                    break;
                }

                int amount = Integer.parseInt(lowerArgs[1]);
                if(amount > internalSender.getMoney()){
                    BotUtils.sendMessage(channel, "You cannot deposit what you don't have!");
                    break;
                }

                int trueAmount = internalSender.getBank() + amount > internalSender.maxBankValue ? (internalSender.maxBankValue - internalSender.getBank()) : amount;
                if(trueAmount != amount){
                    internalSender.depositMoney(trueAmount);
                    BotUtils.sendMessage(channel, "Deposited $`" + trueAmount + "`. Your bank is now full. Additional money was returned to you");
                }
                else{
                    internalSender.depositMoney(trueAmount);
                    BotUtils.sendMessage(channel, "Deposited $`" + trueAmount + "` into the bank. Your bank balance is now $`" + internalSender.getBank() + "`");
                }
                break;

            }
            case "withdraw":{
                if (lowerArgs.length < 2) {
                    BotUtils.sendMessage(channel, "Command usage: `withdraw <amount>");
                    break;
                }

                if(lowerArgs[1].equalsIgnoreCase("all")) lowerArgs[1] = Integer.toString(internalSender.getBank());


                if(!BotUtils.isNumeric(lowerArgs[1]) || Integer.parseInt(lowerArgs[1]) <= 0){
                    BotUtils.sendMessage(channel, "Argument must be a non-negative, positive integer that is greater than 0");
                    break;
                }

                int amount = Integer.parseInt(lowerArgs[1]);
                if(amount > internalSender.getBank()){
                    BotUtils.sendMessage(channel, "You cannot withdraw what you don't have!");
                    break;
                }

                internalSender.withdrawMoney(amount);
                BotUtils.sendMessage(channel, "Withdrew $`" + amount + "` from the bank. Your bank balance is now `" + internalSender.getBank() + "`");
                break;
            }
            case "wd":{
                if (lowerArgs.length < 2) {
                    BotUtils.sendMessage(channel, "Command usage: `withdraw <amount>");
                    break;
                }

                if(lowerArgs[1].equalsIgnoreCase("all")) lowerArgs[1] = Integer.toString(internalSender.getBank());


                if(!BotUtils.isNumeric(lowerArgs[1]) || Integer.parseInt(lowerArgs[1]) <= 0){
                    BotUtils.sendMessage(channel, "Argument must be a non-negative, positive integer that is greater than 0");
                    break;
                }

                int amount = Integer.parseInt(lowerArgs[1]);
                if(amount > internalSender.getBank()){
                    BotUtils.sendMessage(channel, "You cannot withdraw what you don't have!");
                    break;
                }

                internalSender.withdrawMoney(amount);
                BotUtils.sendMessage(channel, "Withdrew $`" + amount + "` from the bank. Your bank balance is now `" + internalSender.getBank() + "`");
                break;
            }
            case "work":{
                if(internalSender.canWork()){
                    int money = BotUtils.random.nextInt((int) (internalSender.getReputation() + BotUtils.maxReputationCap) * 5 + 1) + 200;

                    internalSender.addMoney(money);
                    BotUtils.sendMessage(channel, "Your labour netted you a total of $" + money + ". Your reputation among the people increases.");
                    internalSender.changeReputation(BotUtils.random.nextDouble() * 40.0);
                    internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);
                }
                 else {
                    BotUtils.sendRatelimitMessage(channel, BotUtils.workTime - (new Date().getTime() - internalSender.lastWork));
                }
                break;
            }
            case "crime":{
                if(internalSender.canCrime()){
                    int money = BotUtils.random.nextInt(5000) + 500;
                    if(BotUtils.random.nextInt((int) BotUtils.maxReputationCap * 2) < internalSender.getReputation() + BotUtils.maxReputationCap){
                        internalSender.addMoney(money);
                        BotUtils.sendMessage(channel, "You stole $" + money + " from an upright citizen. Your reputation among the people decreases.");
                        internalSender.changeReputation(-BotUtils.random.nextDouble() * 50 * (money/1000.0));
                        internalSender.gainXP(channel, BotUtils.random.nextDouble() * 150 + 25);
                    }
                    else {
                        BotUtils.sendMessage(channel, "You were caught and fined $1500. The people now know how much of a scoundrel you are.");
                        internalSender.changeReputation(-BotUtils.random.nextDouble() * 40);
                        internalSender.removeMoney(1500);
                        internalSender.gainXP(channel, BotUtils.random.nextDouble() * 50 + 25);
                    }
                }
                else {
                    BotUtils.sendRatelimitMessage(channel, BotUtils.crimeTime - (new Date().getTime() - internalSender.lastCrime));
                }
                break;
            }
            case "money": case "bal": case "balance":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Your financial statement\nAccount: $`" + internalSender.getMoney() + " / Unlimited`\nBank: $`" +internalSender.getBank() + " / " + internalSender.maxBankValue + "`");
                    break;
                }

                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                if(target == null){
                    BotUtils.sendMessage(channel, "Target user not found!");
                    break;
                }

                BotUtils.sendMessage(channel, "" + target.username +"'s financial statement\nAccount: $`" + target.getMoney() + " / Unlimited`\nBank: $`" +target.getBank() + " / " + target.maxBankValue + "`");

                break;
            }
            case "give": {
                if (lowerArgs.length < 3) {
                    BotUtils.sendMessage(channel, "This command is to be used like `give <mentionedUser> <amount>");
                    break;
                }
                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                if (target == null) {
                    BotUtils.sendMessage(channel, "Target user not found!");
                    break;
                }

                if(BotUtils.isNumeric(lowerArgs[2]) && lowerArgs.length == 3){
                    int amount = 0;
                    try {
                        amount = Integer.parseInt(lowerArgs[2]);
                    } catch (NumberFormatException nfe) {
                        BotUtils.sendMessage(channel, "You didn't enter a number as your second argument!");
                        break;
                    }

                    if(amount < 1){
                        BotUtils.sendMessage(channel, "Quantity must be a non-negative, positive integer that is greater than 0");
                        break;
                    }
                    if (internalSender.getMoney() < amount) {
                        BotUtils.sendMessage(channel, "You can't give what you don't have");
                        break;
                    }

                    internalSender.removeMoney(amount);
                    target.addMoney(amount);
                    BotUtils.sendMessage(channel, "Transferred `$" + amount + "` over to " + target.username);
                }
                else if(BotUtils.isNumeric(lowerArgs[2])){
                    String item = "";
                    for (int i = 3; i < lowerArgs.length; i++) {
                        item += lowerArgs[i] + " ";
                    }
                    item = item.trim();

                    Item product = BotUtils.getItem(item);
                    int quantity = Integer.parseInt(lowerArgs[2]);

                    if(quantity < 1){
                        BotUtils.sendMessage(channel, "Quantity must be a non-negative, positive integer that is greater than 0");
                        break;
                    }

                    if(internalSender.getQuantity(product) < quantity){
                        BotUtils.sendMessage(channel, "You do not have `" + quantity + "`" + product.getName() + "(s) to give!");
                        break;
                    }

                    target.addItem(product, quantity);
                    internalSender.removeItem(product, quantity);
                    BotUtils.sendMessage(channel, "Transferred `" + quantity + "` " + product.getName() + "(s) over to " + target.username);

                }
                else{
                    String item = "";
                    for (int i = 2; i < lowerArgs.length; i++) {
                        item += lowerArgs[i] + " ";
                    }
                    item = item.trim();

                    Item product = BotUtils.getItem(item);

                    if(!internalSender.containsItem(product)){
                        BotUtils.sendMessage(channel, "You do not have any " + product.getName() + "(s) to give!");
                        break;
                    }

                    target.addItem(product);
                    internalSender.removeItem(product);
                    BotUtils.sendMessage(channel, "Transferred one " + product.getName() + " over to " + target.username);
                }

                break;
            }
            case "addmoney":{
                System.out.println(internalSender.id);
                if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission Denied");
                    break;
                }
                else if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "This command is to be used like `addmoney <amount>`");
                    break;
                }

                int amount;
                try{
                    amount = Integer.parseInt(lowerArgs[1]);
                }catch (NumberFormatException nfe){
                    BotUtils.sendMessage(channel, "You didn't enter a number as your second argument!");
                    break;
                }

                internalSender.addMoney(amount);
                BotUtils.sendMessage(channel, "Money added successfully!");
                break;
            }
            case "inventory": case "i": case "inv":{
                if(lowerArgs.length < 2){
                    internalSender.sendInventory(channel);
                    break;
                }

                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                if(target == null){
                    BotUtils.sendMessage(channel, "Target user not found!");
                    break;
                }

                target.sendInventory(channel);
                break;
            }
            case "slave":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Input a command!");
                    break;
                }

                if(lowerArgs[1].equalsIgnoreCase("list")){
                    BotUtils.sendMessage(channel, internalSender.getFormattedSlaveList());
                }
                else if(lowerArgs[1].equalsIgnoreCase("kill")){
                    
                }
                else if(lowerArgs[1].equalsIgnoreCase("work")){
                    if(!internalSender.canSlaveWork()){
                        BotUtils.sendRatelimitMessage(channel, BotUtils.slaveWorkTime - (new Date().getTime() - internalSender.lastSlaveWork));
                        break;
                    }
                    if(internalSender.slaveList.size() <= 0){
                        BotUtils.sendMessage(channel, "You do not have any slaves!");
                        break;
                    }
                    ArrayList<String> removeList = new ArrayList<>();
                    Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                        embed.setTitle("Slave report for " + sender.getUsername());

                        for (String s : internalSender.slaveList) {
                            if(BotUtils.random.nextInt(100) > 98){
                                removeList.add(s);
                                embed.addField(new String(s),  "Died of exhaustion", true);
                                internalSender.deadSlaves++;
                            }
                            else{
                                int money = BotUtils.random.nextInt(50);
                                internalSender.addMoney(money);
                                embed.addField(s, "$" + money , true);
                            }
                        }

                        for (String s : removeList) {
                            internalSender.removeSlave(s);
                        }
                    };
                    internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);
                    BotUtils.sendEmbedSpec(channel, embedCreateSpec);




                }

                break;
            }
            case "capture":{

                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Mention the user you want to target!");
                    break;
                }
                else if(internalSender.getMoney() < 700){
                    BotUtils.sendMessage(channel, "You don't have enough money to deal with the fine!");
                    break;
                }

                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                if(target == null){
                    BotUtils.sendMessage(channel, "Target user not found!");
                    break;
                }

                if(target.id == Main.client.getSelfId().get().asLong()){
                    BotUtils.sendMessage(channel, "The bot enacts it's vengeance on you and clears your data.");
                    Tools.users.remove(internalSender);
                    User temp = Tools.getUser(internalSender.id);
                    temp.canDaily();
                    temp.canWeekly();
                    break;
                }
                else if(target.id == internalSender.id){
                    BotUtils.sendMessage(channel, "You severely injured yourself tying to enslave yourself. Your slaves escaped in the confusion.");
                    internalSender.slaveList = new ArrayList<>();
                    internalSender.inventory.remove(internalSender.getItem(new Item("Slave", "A healthy, working slave", 200)));
                    break;
                }

                if(!internalSender.canSlave()){
                    BotUtils.sendRatelimitMessage(channel, BotUtils.slaveTime - (new Date().getTime() - internalSender.lastSlave));
                    break;
                }

                if(internalSender.idList.contains(target.id)){
                    BotUtils.sendMessage(channel, "You cannot capture more than one of each slave!");
                    break;
                }


                double captureChance = ((target.getHealth()/target.maxHealth)*100.0) + 10.0 > 90 ? 90 : ((target.getHealth()/target.maxHealth)*100) + 30;
                captureChance -= internalSender.slaveModifier;
                internalSender.slaveModifier = 0;

                if(BotUtils.random.nextInt(100) > captureChance){
                    BotUtils.sendMessage(channel, "You have captured " + target.username + " as your slave!");
                    internalSender.addSlave(target.username, target.id);
                    internalSender.gainXP(channel, BotUtils.random.nextDouble() * 500 + 25);
                }
                else{
                    BotUtils.sendMessage(channel, "You were caught and fined $700");
                    internalSender.removeMoney(700);
                    internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);
                }
                break;
            }
            case "escape":{
                if(!internalSender.canEscape()){
                    BotUtils.sendRatelimitMessage(channel, BotUtils.escapeTime - (new Date().getTime() - internalSender.lastEscape));
                    break;
                }
                if(BotUtils.random.nextInt(100) > 88 - internalSender.escapeModifier){
                    BotUtils.sendMessage(channel, "**Attention**! " + sender.getMention() + " has escaped captivity!");
                    for (User u : Tools.users) {
                        if(u.slaveList.contains(internalSender.username)){
                            u.escapedSlaves++;
                            u.removeSlave(internalSender.username);
                        }
                    }
                    internalSender.gainXP(channel, BotUtils.random.nextDouble() * 1500 + 300);
                }
                else{
                    int damage = BotUtils.random.nextInt(60) + 25;
                    internalSender.health = internalSender.health - damage <= 0 ? BotUtils.random.nextInt(4) + 1 : internalSender.health - damage;
                    BotUtils.sendMessage(channel, "You failed to escape and were severely beaten. You now have `" + String.format(
                            "%.2f", (float) internalSender.getHealth()) + "`hp left");
                    internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);

                }
                internalSender.escapeModifier = 0;
                break;
            }
            case "info":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `info <item>`");
                    break;
                }
                String item = "";
                for (int i = 1; i < lowerArgs.length; i++) {
                    item += lowerArgs[i] + " ";
                }
                item = item.trim();

                Item result = Market.getItem(item);
                if(result == null && LootBox.getItem(item) == null){
                    BotUtils.sendMessage(channel, "That item does not exist!");
                    break;
                }

                result = result == null ? LootBox.getItem(item) : result;

                Item finalResult = result;
                Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                    embed.setTitle("Information for " + finalResult.getName());
                    embed.addField("Category", finalResult.type != null ? BotUtils.capitalizeFirst(finalResult.type) : "Not specified", true);
                    embed.addField("Price", "$"+ finalResult.price, true);
                    embed.addField("Level requirement", ""+ finalResult.minLevel, true);
                    embed.addField("Description", finalResult.getDescription(), false);
                };

                BotUtils.sendEmbedSpec(channel,embedCreateSpec);
                break;
            }
            case "shop":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Sending shop to your private message!");
                    Market.sendMarket(sender.getPrivateChannel().block(), internalSender);
                }
                else{
                    String item = "";
                    for (int i = 1; i < lowerArgs.length; i++) {
                        item += lowerArgs[i] + " ";
                    }
                    item = BotUtils.capitalizeFirst(item.trim());

                    if(Market.categories.get(item) == null){
                        String out = "Category not found! Available categories are: ";
                        for (String s: Market.categories.keySet()) {
                            out += "\n\t-\t" + s;
                        }
                        BotUtils.sendMessage(channel, out);
                        break;
                    }

                    Market.sendMarket(channel, item, internalSender);
                }
                break;
            }
            case "buy":{
                if(lowerArgs.length < 3){
                    BotUtils.sendMessage(channel, "Command usage: `buy <amount> <item>`");
                    break;
                }

                if(!BotUtils.isNumeric(lowerArgs[1])){
                    BotUtils.sendMessage(channel, "Command usage: `buy <amount> <item>`");
                    break;
                }

                String item = "";
                for (int i = 2; i < lowerArgs.length; i++) {
                    item += lowerArgs[i] + " ";
                }
                item = item.trim();
                if(!Market.itemExists(item)){
                    BotUtils.sendMessage(channel, "The specified item does not exist!");
                    break;
                }

                Item result = Market.getItem(item);
                if(Integer.parseInt(lowerArgs[1]) > (Integer.MAX_VALUE / result.price) - 1){
                    BotUtils.sendMessage(channel, "Cannot buy more than `" + (Integer.MAX_VALUE / result.price - 1) + "` items due to memory limitations");
                    break;
                }
                if(Integer.parseInt(lowerArgs[1]) < 1){
                    BotUtils.sendMessage(channel, "Quantity must be a non-negative, positive integer that is greater than 0");
                    break;
                }
                if(internalSender.getMoney() < result.getPrice() * Integer.parseInt(lowerArgs[1])){
                    BotUtils.sendMessage(channel, ("You do not have the required funds\n```st\nAvailable: " + internalSender.getMoney() + "\nNeeded: " +
                            result.getPrice()*Integer.parseInt(lowerArgs[1]) + "```"));
                    break;
                }
                if(internalSender.getLevel() < result.minLevel){
                    BotUtils.sendMessage(channel, "Your level is too low for this item! Required level is `" + result.minLevel + "`");
                    break;
                }

                internalSender.addItem(result, Integer.parseInt(lowerArgs[1]));
                internalSender.removeMoney((result.price * Integer.parseInt(lowerArgs[1])));
                BotUtils.sendMessage(channel, "Item bought successfully! You now have `" + internalSender.getQuantity(result) + "` " + result.getName() + "(s)");
                break;

            }
            case "attack":{
                if(lowerArgs.length < 3){
                    BotUtils.sendMessage(channel, "Command usage: `attack <@user> <weapon>`");
                    break;
                }

                String item = "";
                for (int i = 2; i < lowerArgs.length; i++) {
                    item += lowerArgs[i] + " ";
                }
                item = item.trim();

                Item weapon = Market.getItem(item);

                if(weapon == null && LootBox.getItem(item) == null){
                    BotUtils.sendMessage(channel, "Item does not exist");
                    break;
                }

                weapon = weapon == null ? LootBox.getItem(item) : weapon;


                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                if(target == null){
                    BotUtils.sendMessage(channel, "Target user not found!");
                    break;
                }
                if(!internalSender.containsItem(weapon)){
                    BotUtils.sendMessage(channel, "You do not have any " + weapon.getName() + "(s) to use!");
                    break;
                }
                if(!internalSender.canAttack()){
                    BotUtils.sendRatelimitMessage(channel, BotUtils.attackTime - (new Date().getTime() - internalSender.lastAttack));
                    break;
                }

                if(target.id == Main.client.getSelfId().get().asLong() && BotUtils.botFightActive){
                    if(weapon.isUtility()) {
                        switch (weapon.getName()) {
                            case "Armor Piercing Bullet": {
                                int damage = weapon.getDamage();
                                if (target.getShield() <= 0) {
                                    BotUtils.sendMessage(channel, "Target had no shield to destroy");
                                } else {
                                    target.setShield(target.getShield() - damage < 0 ? 0 : target.getShield() - damage);
                                    String output = sender.getMention() + " hit <@" + target.id + ">'s shield for `" + damage + "` damage\n";
                                    output += target.getShield() <= 0 ? "Their shield has been destroyed" : "They have `" + target.getShield() + "` shield left";

                                    BotUtils.sendMessage(channel, output);


                                }
                                break;
                            }
                            default:{
                                BotUtils.sendMessage(channel, "Bot fight active, non-damaging items cannot be used against it");
                                break;
                            }
                        }
                    }
                    else if(!weapon.isWeapon()){
                        BotUtils.sendMessage(channel, weapon.getName() + " is not a weapon!");
                    }
                    else{
                        double dmg = weapon.getDamage();
                        if(dmg == 0){
                            BotUtils.sendMessage(channel, "Your attack missed!");
                            internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);
                        }
                        else{
                            //dmg = dmg * 1.8 > Integer.MAX_VALUE ? Integer.MAX_VALUE : dmg * 1.8;
                            target.damage(dmg);
                            dmg *= 1 - (target.defense / (target.defense + 150));
                            dmg = Double.parseDouble(String.format("%.2f", (float) dmg));
                            if(target.getHealth() <= 0){
                                BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() +" hit the bot for " + dmg + " damage!\nIt has been killed!"));
                                int tempInt = ((long) 3)* ((long) target.money) > Integer.MAX_VALUE ? Integer.MAX_VALUE : 3 * target.getMoney();
                                int moneyGained = BotUtils.random.nextInt(((tempInt) / 4)+1) + (target.money / 4);

                                BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " managed to loot $" + moneyGained + " from the bot\n"));

                                internalSender.addMoney(moneyGained);
                                internalSender.kills++;
                                target.deaths++;
                                target.money = 0;
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 1500 + 825);

                                BotUtils.botTier++;
                                if(BotUtils.botTier >= BotUtils.botTiers.length){
                                    BotUtils.sendMessage(channel, "The bot fight has concluded. Please wait for a new one to start or summon one with a programmer's tool");
                                    BotUtils.endBotFight();
                                }
                                else{
                                    BotUtils.sendMessage(channel, "Tier " + (BotUtils.botTier - 1) + " defeated! Next tier commencing...");
                                    BotUtils.setBotTier(BotUtils.botTier);
                                    User bot = Tools.getUser(Main.client.getSelfId().get().asLong());
                                    Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                                        embed.setTitle(bot.username + "'s profile");
                                        embed.addField("Level:", "" + bot.getLevel(), true);
                                        embed.addField("Progress:", String.format("%.2f", (float) bot.getXp()) + "/" + bot.getXPRequired(), true);
                                        embed.addField("\u200b", String.format("%.2f", (float) (bot.getXp() / bot.getXPRequired()) * 100) + "%", true);
                                        embed.addField("Balance", ":moneybag: " + bot.getMoney(), true);
                                        embed.addField("Health", ":heart: " + bot.getHealth(), true);
                                        embed.addField("Shield", ":shield: " + bot.getShield(), true);
                                        embed.addField("Kills:", "" + bot.kills, true);
                                        embed.addField("Deaths:", "" + bot.deaths, true);
                                        embed.addField("K/D Ratio:", String.format("%.2f", bot.deaths > 0 ? ((float)bot.kills)/((float)bot.deaths) : 0.00f), true);
                                        embed.addField("Reputation", ":scales: " + String.format("%.2f", (float) bot.getReputation()), false);
                                        embed.addField("Slaves:", "**Alive**: " + bot.slaveList.size(), true);
                                        embed.addField("\u200b", "**Escaped**: " + bot.escapedSlaves, true);
                                        embed.addField("\u200b", "**Dead**: " + bot.deadSlaves, true);
                                    };
                                    BotUtils.sendEmbedSpec(channel,embedCreateSpec);
                                }
                            }
                            else if(target.getShield() > 0){
                                BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " hit the bot for " + dmg + " damage!\nIt's shield took the blow!"));
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 500 + 25);
                            }
                            else{
                                BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " hit the bot for " + dmg + " damage!\nIt have `"
                                        + String.format("%.2f", (float) target.getHealth()) + "`hp remaining"));
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 500 + 25);
                            }
                        }
                        internalSender.removeItem(weapon);
                    }
                    break;
                }

                if(weapon.isUtility()){
                    switch (weapon.getName()){
                        case "Crowbar":{
                            if(!target.containsItem(Market.getItem("Vault"))){
                                BotUtils.sendMessage(channel, "You didn't find any vaults to attack! In rage, you broke your crowbar");
                                break;
                            }

                            if(BotUtils.random.nextInt(100) > 87){
                                int money = BotUtils.random.nextInt(10000);
                                internalSender.addMoney(money);
                                target.addMoney(10000 - money);
                                BotUtils.sendMessage(channel, "You broke open the vault, and managed to steal $" + money +
                                        "\nThe guards managed to secure the rest");
                                target.removeItem(Market.getItem("Vault"));
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 600 + 100);
                            }
                            else{
                                BotUtils.sendMessage(channel, "You were caught trying to attack the vault");
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);
                            }
                            break;
                        }
                        case "Dynamite":{
                            if(!target.containsItem(Market.getItem("Vault"))){
                                BotUtils.sendMessage(channel, "You threw your dynamite at the target, only to find that there were no vaults to attack");
                                break;
                            }

                            if(BotUtils.random.nextInt(100) > 95){
                                target.removeItem(Market.getItem("Vault"));
                                BotUtils.sendMessage(channel, "The explosion destyroyed the vault and all the contents inside. There was nothing left to steal");
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 1500 + 200);
                                break;
                            }
                            else if(BotUtils.random.nextInt(100) > 78){
                                int money = BotUtils.random.nextInt(10000);
                                internalSender.addMoney(money);
                                target.addMoney(10000 - money);
                                BotUtils.sendMessage(channel, "You broke open the vault, and managed to steal $" + money +
                                        "\nThe guards managed to secure the rest");
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 700 + 125);
                            }
                            else{
                                BotUtils.sendMessage(channel, "You were caught tying to attack the vault");
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);

                            }
                            break;
                        }
                        case "Underground Railroad":{
                            if(target.slaveList.size() <= 0){
                                BotUtils.sendMessage(channel, "You wasted your resources freeing slaves from a plantation where there were no slaves");
                                break;
                            }

                            int chance = 100 - internalSender.getQuantity(Market.getItem("ox"));

                            if(BotUtils.random.nextInt(102) > chance){
                                int slaves = BotUtils.random.nextInt(target.slaveList.size()) + 1;
                                BotUtils.sendMessage(channel, "You managed to free `" + slaves + "` slaves from captivity! Each slave pays you $`500` for a total of " +
                                        "$`" + slaves * 500 + "`");
                                internalSender.addMoney(slaves*500);
                                for (int i = 0; i < slaves; i++) {
                                    target.removeSlave(target.slaveList.get(0));
                                    target.escapedSlaves++;
                                }
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 1700 + 325);

                            }
                            else{
                                BotUtils.sendMessage(channel, "Your plan was foiled by Zeus, since you failed to sacrifice 100 oxen to the gods");
                                internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);
                            }

                            internalSender.removeItem(Market.getItem("ox") , internalSender.getQuantity(Market.getItem("ox")) > 100 ? 100 :
                                    internalSender.getQuantity(Market.getItem("ox")));
                            break;
                        }
                        case "Armor Piercing Bullet":{
                            int damage = weapon.getDamage();
                            if(target.getShield() <= 0){
                                BotUtils.sendMessage(channel, "Target had no shield to destroy");
                            }
                            else{
                                target.setShield(target.getShield() - damage < 0 ? 0 : target.getShield() - damage);
                                String output = sender.getMention() + " hit <@" + target.id + ">'s shield for `" + damage +"` damage\n";
                                output += target.getShield() <= 0 ? "Their shield has been destroyed" : "They have `" + target.getShield() + "` shield left";

                                BotUtils.sendMessage(channel, output);


                            }
                            break;
                        }
                        default:
                            return;
                    }

                    internalSender.removeItem(weapon);
                }
                else{
                    if(!weapon.isWeapon()){
                        BotUtils.sendMessage(channel, weapon.getName() + " is not a weapon!");
                        break;
                    }

                    double dmg = weapon.getDamage();
                    if(dmg == 0){
                        BotUtils.sendMessage(channel, "Your attack missed!");
                        internalSender.gainXP(channel, BotUtils.random.nextDouble() * 100 + 25);
                    }
                    else{
                        target.damage(dmg);
                        dmg *= 1 - (target.defense / (target.defense + 150));
                        dmg = Double.parseDouble(String.format("%.2f", (float) dmg));
                        if(target.getHealth() <= 0){
                            BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() +" hit " + Main.getUserByID(target.id).getMention() + " for " + dmg + " damage!\nThey have been killed!"));
                            int tempInt = ((long) 3)* ((long) target.money) > Integer.MAX_VALUE ? Integer.MAX_VALUE : 3 * target.getMoney();
                            int moneyGained = BotUtils.random.nextInt(((tempInt) / 4)+1) + (target.money / 4);
                            BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " managed to loot $" + moneyGained + " from the dead body!"));
                            target.setHealth(target.getMaxHealth());
                            target.removeMoney(moneyGained);
                            internalSender.addMoney(moneyGained);
                            internalSender.kills++;
                            target.deaths++;
                            if(target.id == 519326187491950593L){
                                BotUtils.sendMessage(channel, "The rest of the bot's money is encrypted and destroyed with a magnet");
                                target.money = 0;
                            }
                            internalSender.gainXP(channel, BotUtils.random.nextDouble() * 1500 + 425);
                        }
                        else if(target.getShield() > 0){
                            BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " hit " + Main.getUserByID(target.id).getMention() + " for " + dmg + " damage!\nTheir shield took the blow!"));
                            internalSender.gainXP(channel, BotUtils.random.nextDouble() * 500 + 25);
                        }
                        else{
                            BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " hit " + Main.getUserByID(target.id).getMention() + " for " + dmg + " damage!\nThey have `"
                                    + String.format("%.2f", (float) target.getHealth()) + "`hp remaining"));
                            internalSender.gainXP(channel, BotUtils.random.nextDouble() * 500 + 25);
                        }
                    }

                    internalSender.removeItem(weapon);
                }

                break;
            }
            case "heal":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `heal <item>");
                    break;
                }


                String item = "";
                for (int i = 1; i < lowerArgs.length; i++) {
                    item += lowerArgs[i] + " ";
                }
                item = item.trim();

                Item heal = Market.getItem(item);

                if(!internalSender.containsItem(heal)){
                    BotUtils.sendMessage(channel, "You do not any " + heal.getName() + "(s) to use!");
                    break;
                }
                if(!heal.isHeal()){
                    BotUtils.sendMessage(channel, heal.getName() + " is not a healing item!");
                    break;
                }

                if(!internalSender.canHeal()){
                    BotUtils.sendRatelimitMessage(channel, BotUtils.healTime - (new Date().getTime() - internalSender.lastHeal));
                    break;
                }


                int amount = heal.getDamage();
                if(amount == 0){
                    BotUtils.sendMessage(channel, "Your item did not manage to heal you (Try something more reliable?)");
                }
                else{
                    internalSender.heal(amount);
                    if(internalSender.getHealth() >= internalSender.getMaxHealth()){
                        BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() +" was healed to full health"));
                    }
                    else{
                        BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " was healed by " + amount + "hp!\nThey now have `"
                                + internalSender.getHealth() + "`hp"));
                    }
                }

                internalSender.removeItem(heal);

                break;
            }
            case "shield":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `shield <item>`");
                    break;
                }

                String item = "";
                for (int i = 1; i < lowerArgs.length; i++) {
                    item += lowerArgs[i] + " ";
                }
                item = item.trim();

                Item shield = BotUtils.getItem(item);

                if(!internalSender.containsItem(shield)){
                    BotUtils.sendMessage(channel, "You do not any " + shield.getName() + "(s) to use!");
                    break;
                }
                if(!shield.isShield()){
                    BotUtils.sendMessage(channel, shield.getName() + " is not a shield!");
                    break;
                }

                if(internalSender.getShield() > 0){
                    BotUtils.sendMessage(channel, "Cannot deploy shield while another shield is active!");
                    break;
                }


                int amount = shield.getDamage();
                if(amount == 0){
                    BotUtils.sendMessage(channel, "Your shield failed to deploy!");
                }
                else{
                    internalSender.setShield(amount);
                    BotUtils.sendMessage(channel, "You deployed a shield that provided you with `" + amount + "`hp worth of protection");
                }

                internalSender.removeItem(shield);
                break;
            }
            case "use":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `use <item>`. For attacks please use `attack` instead");
                    break;
                }

                if(BotUtils.isNumeric(lowerArgs[1])){
                    String item = "";
                    for (int i = 2; i < lowerArgs.length; i++) {
                        item += lowerArgs[i] + " ";
                    }
                    item = item.trim();
                    Item result = BotUtils.getItem(item);
                    if(result == null){
                        BotUtils.sendMessage(channel, "That item does not exist!");
                        break;
                    }
                    else if(!internalSender.containsItem(result)){
                        BotUtils.sendMessage(channel, "You do not have `" + Integer.parseInt(lowerArgs[1]) + "` " + result.getName() + "(s) to use!");
                        break;
                    }

                    switch (result.getName().toLowerCase()){
                        case "vault":
                            internalSender.addMoney(Integer.parseInt(lowerArgs[1]) * 10000);
                            internalSender.removeItem(result, Integer.parseInt(lowerArgs[1]));
                            BotUtils.sendMessage(channel, "You withdrew " + Integer.parseInt(lowerArgs[1]) * 10000 + " from " + Integer.parseInt(lowerArgs[1]) + " vaults");
                            break;
                        default:
                            BotUtils.sendMessage(channel, "Cannot use multiple of this particular item");
                            break;
                    }

                }
                else {

                    String item = "";
                    for (int i = 1; i < lowerArgs.length; i++) {
                        item += lowerArgs[i] + " ";
                    }
                    item = item.trim();

                    Item result = BotUtils.getItem(item);
                    if (result == null) {
                        BotUtils.sendMessage(channel, "That item does not exist!");
                        break;
                    } else if (!internalSender.containsItem(result)) {
                        BotUtils.sendMessage(channel, "You do not have any " + result.getName() + "(s) to use!");
                        break;
                    }

                    if (result.isHeal()) {
                        if (!internalSender.canHeal()) {
                            BotUtils.sendRatelimitMessage(channel, BotUtils.healTime - (new Date().getTime() - internalSender.lastHeal));
                            break;
                        }
                        int amount = result.getDamage();
                        if (amount == 0) {
                            BotUtils.sendMessage(channel, "Your item did not manage to heal you (Try something more reliable?)");
                        } else {
                            internalSender.heal(amount);
                            if (internalSender.getHealth() >= internalSender.getMaxHealth()) {
                                BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " was healed to full health"));
                            } else {
                                BotUtils.sendMessage(channel, (Main.getUserByID(internalSender.id).getMention() + " was healed by " + amount + "hp!\nThey now have `"
                                        + internalSender.getHealth() + "`hp"));
                            }
                        }
                    } else if (result.isShield()) {
                        if (internalSender.getShield() > 0) {
                            BotUtils.sendMessage(channel, "Cannot deploy shield while another shield is active!");
                            break;
                        }
                        int amount = result.getDamage();
                        if (amount == 0) {
                            BotUtils.sendMessage(channel, "Your shield failed to deploy!");
                        } else {
                            internalSender.setShield(amount);
                            BotUtils.sendMessage(channel, "You deployed a shield that provided you with `" + amount + "`hp worth of protection");
                        }
                    } else if (result.isUtility()) {
                        switch (result.getName().toLowerCase()) {
                            case "vault": {
                                internalSender.addMoney(10000);
                                BotUtils.sendMessage(channel, "You withdrew $10000 from your vault");
                                break;
                            }
                            case "net": {
                                internalSender.slaveModifier = 10;
                                BotUtils.sendMessage(channel, "You increased the capture rate of your next slave");
                                break;
                            }
                            case "chloroform": {
                                internalSender.slaveModifier = 25;
                                BotUtils.sendMessage(channel, "You sharply increased the capture rate of your next slave");
                                break;
                            }
                            case "smoke grenade": {
                                internalSender.escapeModifier = 8;
                                BotUtils.sendMessage(channel, "You increased the capture rate of your next escape");
                                break;
                            }
                            case "vietcong uniform": {
                                internalSender.escapeModifier = 15;
                                BotUtils.sendMessage(channel, "You sharply the capture rate of your next escape");
                                break;
                            }
                            case "programmer's tool": {
                                BotUtils.startBotFight();
                                User bot = Tools.getUser(Main.client.getSelfId().get().asLong());
                                Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                                    embed.setTitle(bot.username + "'s profile");
                                    embed.addField("Level:", "" + bot.getLevel(), true);
                                    embed.addField("Progress:", String.format("%.2f", (float) bot.getXp()) + "/" + bot.getXPRequired(), true);
                                    embed.addField("\u200b", String.format("%.2f", (float) (bot.getXp() / bot.getXPRequired()) * 100) + "%", true);
                                    embed.addField("Balance", ":moneybag: " + bot.getMoney(), true);
                                    embed.addField("Health", ":heart: " + bot.getHealth(), true);
                                    embed.addField("Shield", ":shield: " + bot.getShield(), true);
                                    embed.addField("Kills:", "" + bot.kills, true);
                                    embed.addField("Deaths:", "" + bot.deaths, true);
                                    embed.addField("K/D Ratio:", String.format("%.2f", bot.deaths > 0 ? ((float)bot.kills)/((float)bot.deaths) : 0.00f), true);
                                    embed.addField("Reputation", ":scales: " + String.format("%.2f", (float) bot.getReputation()), false);
                                    embed.addField("Slaves:", "**Alive**: " + bot.slaveList.size(), true);
                                    embed.addField("\u200b", "**Escaped**: " + bot.escapedSlaves, true);
                                    embed.addField("\u200b", "**Dead**: " + bot.deadSlaves, true);
                                };
                                BotUtils.sendMessage(channel, "Bot fight started, you have 15 minutes to defeat it");
                                new BotFightTimer(BotUtils.MILLIS_IN_MINUTE * 15, channel).start();
                                BotUtils.sendEmbedSpec(channel, embedCreateSpec);
                                break;
                            }
                            default:
                                BotUtils.sendMessage(channel, "Unable to use (try attack instead?)");
                                return;
                        }


                    } else {
                        BotUtils.sendMessage(channel, "Unable to use (try attack instead?)");
                        return;
                    }


                    internalSender.removeItem(result);
                }
                break;
            }
            case "clearinventory":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `clearinventory <player>`");
                    break;
                }
                else if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }

                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                target.inventory = new HashMap<>();
                BotUtils.sendMessage(channel, "Successfully cleared the inventory of " + Main.getUserByID(target.id).getMention());
                break;
            }
            case "clearuser":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `clearuser <player>`");
                    break;
                }
                else if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }

                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                Tools.users.remove(target);
                BotUtils.sendMessage(channel, "Successfully removed data from " + Main.getUserByID(target.id).getMention());
                break;
            }
            case "resetdaily":{
                if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }
                internalSender.lastDaily = 0;
                BotUtils.sendMessage(channel, "Daily reward countdown has been reset!");
                break;
            }
            case "additem":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `giveitem <item>`");
                    break;
                }
                else if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }

                String item = "";
                for (int i = 1; i < lowerArgs.length; i++) {
                    item += lowerArgs[i] + " ";
                }
                item = item.trim();

                internalSender.addItem(BotUtils.getItem(item));
                BotUtils.sendMessage(channel, "Added 1 " + BotUtils.getItem(item).getName() + " to your inventory");
                break;
            }
            case "giveitem":{
                if(lowerArgs.length < 3){
                    BotUtils.sendMessage(channel, "Command usage: `giveitem <user> <item>`");
                    break;
                }
                else if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }

                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                if(target == null){
                    BotUtils.sendMessage(channel, "Target user not found!");
                    break;
                }


                String item = "";
                for (int i = 2; i < lowerArgs.length; i++) {
                    item += lowerArgs[i] + " ";
                }
                item = item.trim();


                target.addItem(BotUtils.getItem(item));
                BotUtils.sendMessage(channel, "Added 1 " + BotUtils.getItem(item).getName() + " to their inventory");
                break;
            }
            case "addxp":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `addxp <amount>`");
                    break;
                }
                else if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }

                if(lowerArgs.length == 2){
                    internalSender.gainXP(channel, Integer.parseInt(lowerArgs[1]));
                    BotUtils.sendMessage(channel, "Added " + Integer.parseInt(lowerArgs[1]) + " exp to your acc");
                    break;
                }
                else{
                    User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                    if(target == null){
                        BotUtils.sendMessage(channel, "Target user not found!");
                        break;
                    }
                    target.gainXP(channel, Integer.parseInt(lowerArgs[2]));
                    BotUtils.sendMessage(channel, "Added " + Integer.parseInt(lowerArgs[2]) + " exp to their account");

                }
                break;

            }
            case "flip":{
                if(lowerArgs.length < 4){
                    BotUtils.sendMessage(channel, "Command Usage: `flip [user] [amount] [side]`");
                    break;
                }
                if(internalSender.challengeActive){
                    BotUtils.sendMessage(channel, "Cannot challenge while challenge is running. Wait for previous challenge to expire`");
                    break;
                }

                int money = Integer.parseInt(lowerArgs[2]);
                int pot = money;

                User target = BotUtils.getInternalUserFromMention(lowerArgs[1]);
                if(target == null){
                    BotUtils.sendMessage(channel, "Target user not found!");
                    break;
                }

                if(internalSender.getMoney() < money){
                    BotUtils.sendMessage(channel, "You can't gamble what you don't have");
                    break;
                }
                else if(target.getMoney() <= 0){
                    BotUtils.sendMessage(channel, "Your target has no money! No point in fighting");
                    break;
                }
                else if(target.getMoney() <= money){
                    BotUtils.sendMessage(channel, "Your target does not have enough money! The money has been set to their full balance");
                    money = target.getMoney();
                }

                internalSender.challengeActive = true;
                internalSender.removeMoney(money);
                BotUtils.sendMessage(channel, Main.getUserByID(target.id).getMention() + "! " + sender.getMention() + " wants to " +
                        "coin flip with you. You play `"+ (lowerArgs[3].startsWith("t") ? "heads" : "tails") + "` and bet $`" + money + "`. Use `acceptflip` to accept!");
                ListenerThread thread = new ListenerThread(target, channel, lowerArgs[3].startsWith("t") ? "h" : "t");
                gamesActive.add(thread);
                thread.setIntData(money);
                thread.setUserData(internalSender);
                thread.setWaitTimeMills(BotUtils.MILLIS_IN_MINUTE);
                thread.start();
                break;
            }
            case "sell":{
                if(lowerArgs.length < 2){
                    BotUtils.sendMessage(channel, "Command usage: `sell <quantity> <item>`");
                    break;
                }

                String item = "";
                for (int i = 2; i < lowerArgs.length; i++) {
                    item += lowerArgs[i] + " ";
                }
                item = item.trim();

                Item target = BotUtils.getItem(item);
                if(target == null){
                    BotUtils.sendMessage(channel, "Item does not exist!");
                    break;
                }
                else if(!internalSender.containsItem(target)){
                    BotUtils.sendMessage(channel, "You do not have any " + target.getName() + "(s) to sell!");
                    break;
                }
                else if(Integer.parseInt(lowerArgs[1]) > internalSender.inventory.get(internalSender.getItem(target))){
                    BotUtils.sendMessage(channel, "You do not have sufficient " + target.getName() + "(s) to sell!\n" +
                            "```st\nAvailable: " + internalSender.inventory.get(internalSender.getItem(target)) + "\nNeeded: " + Integer.parseInt(lowerArgs[1]) + "```");
                    break;
                }
                else if(Integer.parseInt(lowerArgs[1]) < 1){
                    BotUtils.sendMessage(channel, "Quantity must be a non-negative, positive integer that is greater than 0");
                    break;
                }


                int sellValue = target.getPrice() / 2;
                if(Integer.parseInt(lowerArgs[1]) > (Integer.MAX_VALUE / sellValue) - 1){
                    BotUtils.sendMessage(channel, "Cannot sell more than `" + (Integer.MAX_VALUE / sellValue - 1) + "` items due to memory limitations");
                    break;
                }

                internalSender.addMoney(sellValue * Integer.parseInt(lowerArgs[1]));
                internalSender.removeItem(target, Integer.parseInt(lowerArgs[1]));
                BotUtils.sendMessage(channel, "You sold `" + Integer.parseInt(lowerArgs[1]) + "` "
                        + target.getName() + "(s) for $`" + sellValue * Integer.parseInt(lowerArgs[1]) + "`");
                break;

            }
            case "eval":{

                if(!BotUtils.isAdmin(internalSender.id)){
                    BotUtils.sendMessage(channel, "Eval disabled");
                    break;
                }

                BotUtils.sendMessage(channel, "Evaluating... This could take a while");
                String data = "";
                for (int i = 2; i < lowerArgs.length; i++) {
                    data += rawArgs[i] + " ";
                }
                data = data.trim();
                try {
                    new EvalJVM().start(channel, lowerArgs[1], data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                if(internalSender.id == Main.client.getSelfId().get().asLong()){
                    break;
                }


                String data = "";
                for (int i = 2; i < lowerArgs.length; i++) {
                    data += rawArgs[i] + " ";
                }
                data = data.trim();

                System.out.println("Eval triggered by " + internalSender.username + ". Data:\n" + data);


                PrintStream finalLogStream = logSteam;
                try {
                    new EvalThread().start(channel, lowerArgs[1], data);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                    e.printStackTrace(finalLogStream);
                }*/
                break;
            }
            case "setinvincible":{
                if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }

                BotUtils.sendMessage(channel, internalSender.username + " has been set to invincible");
                break;
            }
            case "easteregg":{
                BotUtils.sendMessage(channel, ":egg:");
                break;
            }
            case "resetbot":{
                if(!LongStream.of(BotUtils.ADMINS).anyMatch(x -> x == internalSender.id)){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }

                User bot = Tools.getUser(Main.client.getSelfId().get().asLong());
                Tools.users.remove(bot);
                bot = Tools.getUser(bot.id);
                bot.maxHealth = Integer.MAX_VALUE;
                bot.setHealth(Integer.MAX_VALUE);
                bot.setShield(Integer.MAX_VALUE);
                bot.addMoney(400000);
                BotUtils.sendMessage(channel, "Bot reset!");
                break;
            }
            case "upgradebank": case "bankupgrade":{
                if(internalSender.getMoney() < BotUtils.bankUpgradeValues[internalSender.bankUpgrades][0]){
                    BotUtils.sendMessage(channel, "You do not have enough funds for tier " + (internalSender.bankUpgrades + 1) +
                            ":\n```st\nAvailable: " + internalSender.getMoney() + "\nNeeded: " + BotUtils.bankUpgradeValues[internalSender.bankUpgrades][0] + "```");
                }
                else{
                    internalSender.removeMoney(BotUtils.bankUpgradeValues[internalSender.bankUpgrades][0]);
                    internalSender.maxBankValue += BotUtils.bankUpgradeValues[internalSender.bankUpgrades][1];
                    internalSender.bankUpgrades++;
                    BotUtils.sendMessage(channel, "Bank upgraded! Your new max balance is $" + internalSender.maxBankValue);
                }
                break;
            }
            case "startbotfight":{
                if(!BotUtils.isAdmin(sender.getId().asLong())){
                    BotUtils.sendMessage(channel, "Error: Permission denied");
                    break;
                }

                BotUtils.startBotFight();
                User bot = Tools.getUser(Main.client.getSelfId().get().asLong());
                Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                    embed.setTitle(bot.username + "'s profile");
                    embed.addField("Level:", "" + bot.getLevel(), true);
                    embed.addField("Progress:", String.format("%.2f", (float) bot.getXp()) + "/" + bot.getXPRequired(), true);
                    embed.addField("\u200b", String.format("%.2f", (float) (bot.getXp() / bot.getXPRequired()) * 100) + "%", true);
                    embed.addField("Balance", ":moneybag: " + bot.getMoney(), true);
                    embed.addField("Health", ":heart: " + bot.getHealth(), true);
                    embed.addField("Shield", ":shield: " + bot.getShield(), true);
                    embed.addField("Kills:", "" + bot.kills, true);
                    embed.addField("Deaths:", "" + bot.deaths, true);
                    embed.addField("K/D Ratio:", String.format("%.2f", bot.deaths > 0 ? ((float)bot.kills)/((float)bot.deaths) : 0.00f), true);
                    embed.addField("Reputation", ":scales: " + String.format("%.2f", (float) bot.getReputation()), false);
                    embed.addField("Slaves:", "**Alive**: " + bot.slaveList.size(), true);
                    embed.addField("\u200b", "**Escaped**: " + bot.escapedSlaves, true);
                    embed.addField("\u200b", "**Dead**: " + bot.deadSlaves, true);
                };
                BotUtils.sendMessage(channel, "Bot fight manual override successful, you have 15 minutes to defeat it");
                new BotFightTimer(BotUtils.MILLIS_IN_MINUTE * 15, channel).start();
                BotUtils.sendEmbedSpec(channel, embedCreateSpec);
                break;
            }
            case "help":{
                Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
                    embed.setTitle("Help");
                    embed.addField("\u200b", "Current prefix is " + BotUtils.BOT_PREFIX, false);
                    String helpText = "`profile [user]` - Displays your profile or [user]'s profile if specified\n" +
                            "`money` - Displays your money\n" +
                            "`inventory` - Displays your inventory\n" +
                            "`loot` - Gives you a random item";
                    embed.addField("Profile Commands", helpText, false);

                    helpText = "`daily` - Claims your daily reward\n" +
                            "`weekly` - Claims your weekly reward\n" +
                            "`crime` - Quick cash command\n" +
                            "`work` - Quick cash command\n" +
                            "`give [user] [amount]` - Transfers [amount] over to [user]\n" +
                            "`give [user] [quantity] [item]` - Transfers [quantity] of [item] over to [user]\n" +
                            "`shop` - Displays the shop\n" +
                            "`buy [quantity] [item]` - Buy [quantity] of [item]\n" +
                            "`sell [quantity] [item]` - Sell [quantity] of [item] for `1/2` of market price";
                    embed.addField("Economy Commands", helpText, false);

                    helpText = "`attack [user] [weapon]` - Attacks [user] with weapons class item [weapon]\n" +
                            "`heal [item]` - Uses healing class item [item] to heal you\n" +
                            "`shield [item]` - Uses shield class item [item] to raise a shield\n" +
                            "`use [item]` - Same as doing `shield [item]` or `heal [item]`. Works with utilities.";
                    embed.addField("Combat Commands", helpText, false);

                    helpText = "`capture [user]` - Attempts to capture [user] as a slave\n" +
                            "`slave [command] [arguments]` - Activates slave subcommand, use `slave help` for more details";
                    embed.addField("Slave Commands", helpText, false);

                    helpText = "`ping` - Pings the bot\n" +
                            "`eval [language] [code]` - Attempts to run [code] as [language] code\n" +
                            "`help` - Displays this help text";
                    embed.addField("Other Commands", helpText, false);
                };

                BotUtils.sendMessage(channel, "Sending help message to your private channel!");
                BotUtils.sendEmbedSpec(sender.getPrivateChannel().block(), embedCreateSpec);
            }

        }
    }
}
