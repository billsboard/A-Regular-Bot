package SlaveBot;

import discord4j.core.object.entity.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Consumer;

public class Leveling implements Serializable {
    private static final long serialVersionUID = 100L;

    static User[] leaderBoard = new User[7];

    public static User getUserAtIndex(int index){
        return leaderBoard[index];
    }

    private static int getIndexOfUser(User user){
        for (int i = 0; i < leaderBoard.length; i++) {
            if(user.id == leaderBoard[i].id){
                return i;
            }
        }
        return -1;
    }

    private static boolean isOnLeaderboard(User user){
        for (User u : leaderBoard) {
            if(u == null){}
            else if(u.id == user.id) return true;
        }
        return false;
    }

    static void addLeaderboard(User user){
        if(user == null) return;
        if(isOnLeaderboard(user)){
            int index = getIndexOfUser(user);
            leaderBoard[index] = user;
            int newIndex = 0;
            for (int i = 0; i < leaderBoard.length; i++) {
                if(isHigher(user, leaderBoard[i])){
                    newIndex = i;
                    break;
                }
                else if(user.id == leaderBoard[i].id){
                    return;
                }
            }
            if(newIndex < index){
                for (int i = index; i > newIndex; i--) {
                    leaderBoard[i] = leaderBoard[i - 1];
                }
                leaderBoard[newIndex] = user;
            }
            else if(newIndex > index){
                for (int i = newIndex; i > index; i--) {
                    leaderBoard[i] = leaderBoard[i - 1];
                }
                leaderBoard[newIndex] = user;
            }
        }
        else{
            int index = leaderBoard.length;
            for (int i = 0; i < leaderBoard.length; i++) {
                if(isHigher(user, leaderBoard[i])){
                    index = i;
                    break;
                }
            }

            for (int i = leaderBoard.length - 1; i > index; i--) {
                leaderBoard[i] = leaderBoard[i - 1];
            }
            if(index != leaderBoard.length)
                leaderBoard[index] = user;
        }


    }

    private static boolean isHigher(User a, User b){
        if (b == null) return true;
        if(a.getLevel() > b.getLevel()) return true;
        else if(a.getLevel() == b.getLevel() && a.getXp() > b.getXp()) return true;
        //else if(a.getLevel() == b.getLevel() && a.getXp() == b.getXp() && a.getMoney() > b.getMoney()) return true;
        return false;
    }

    static void createLeaderboard(){
        leaderBoard = new User[leaderBoard.length];
        for (User u : Tools.users) {
            addLeaderboard(u);
        }


        for (User u: leaderBoard
             ) {
            if(u != null)
            System.out.println(u.username);
        }
    }

    static void displayLeaderBoard(MessageChannel channel, User user){
        addLeaderboard(user);
        Consumer<EmbedCreateSpec> embedCreateSpec = embed -> {
            embed.setTitle("Leveling leaderboard");
            int count = 1;
            for (User u : leaderBoard) {
                if(u == null){
                    embed.addField(count + ") No user", "Level: " + Integer.MIN_VALUE, true);
                    embed.addField("\u200b", "Completion: 0.00%", true);
                    embed.addField("\u200b", "\u200b", true);
                }
                else{
                    embed.addField(count + ") " +u.username, "Level: " + u.getLevel(), true);
                    embed.addField("\u200b", "Completion: " + String.format("%.2f", (float) u.getXp() / u.getXPRequired() * 100.0) + "%", true);
                    embed.addField("\u200b", "\u200b", true);
                }
                count++;
            }
        };

        BotUtils.sendEmbedSpec(channel, embedCreateSpec);
    }

    public static void displayLeaderBoard(MessageChannel channel, User user, String board){
        if(board.equalsIgnoreCase("leveling")){
            displayLeaderBoard(channel, user);
        }
    }
}
