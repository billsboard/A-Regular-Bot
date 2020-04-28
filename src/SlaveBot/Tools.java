package SlaveBot;

import discord4j.core.object.util.Snowflake;

import java.util.ArrayList;

public class Tools {
    static ArrayList<User> users = new ArrayList<>();

    public static User getUser(long DiscordID){
        for (User u: users) {
            if(u.id == DiscordID){
                discord4j.core.object.entity.User user = Main.client.getUserById(Snowflake.of(DiscordID)).block();
                if(!user.getUsername().equals(u.username)){
                    u.username = user.getUsername();
                }

                return u;
            }
        }

        if(Main.getUserByID(DiscordID) == null){return null;}
        User newUser = new User(DiscordID);
        users.add(newUser);
        return newUser;
    }
}
