package SlaveBot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.object.util.Snowflake;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {

    /* Create a file in the root directory of the bot called 'token.txt' containing your token */

    static DiscordClient client;
    static EventProcessor eventProcessor;

    public static void main(String[] args) throws FileNotFoundException {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (SlaveBot.User u : Tools.users) {
                u.challengeActive = false;
            }
            DataSave.saveData();
            System.out.println("Application Terminating ...");
        }));

        Scanner scan = new Scanner(new FileReader("token.txt"));
        DiscordClientBuilder builder = DiscordClientBuilder.create(scan.nextLine());
        scan.close();

        builder.setInitialPresence(Presence.online(Activity.watching(" for commands | $help")));
        client = builder.build();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
                });



        eventProcessor = new EventProcessor(client.getEventDispatcher().on(MessageCreateEvent.class));

        DataSave.loadData();
        Market.init();

        Timer timer = new Timer();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("PST"));
        Date time = calendar.getTime();
        System.out.println(time.getTime());

        timer.scheduleAtFixedRate(new DailyTask(), time, BotUtils.MILLIS_IN_HOUR * 24);

        //new DailyTask().run();
        //Leveling.createLeaderboard();

        Timer runTimer = new Timer();
        runTimer.scheduleAtFixedRate(new SaveTask(), 0, 7 * 60 * 1000);

        TempRunner.run();

        client.updatePresence(Presence.online(Activity.listening("commands | " + BotUtils.BOT_PREFIX + "help"))).block();

        client.login().block();

        client.updatePresence(Presence.online(Activity.listening("commands | " + BotUtils.BOT_PREFIX + "help"))).block();






    }

    static class DailyTask extends TimerTask{
        public void run() {
            SlaveBot.User bot = Tools.getUser(519326187491950593L);
            Tools.users.remove(bot);
            bot = Tools.getUser(519326187491950593L);
            bot.money = 400000;
            bot.health = Integer.MAX_VALUE;
            bot.maxHealth = Integer.MAX_VALUE;
            bot.setShield(Integer.MAX_VALUE);
            System.out.println("Bot reset!");
        }
    }

    public static User getUserByID(long id){
        return client.getUserById(Snowflake.of(id)).block();
    }

}