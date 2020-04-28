package SlaveBot;

import discord4j.core.object.entity.MessageChannel;

import java.util.Date;

public class ListenerThread extends Thread{
    User user;
    MessageChannel channel;

    String dataString1;
    int dataInt1;
    User dataUser1;

    long waitTimeMills;

    boolean gameOver = false;

    public ListenerThread(User user, MessageChannel channel, String data){
        this.user = user;
        this.channel = channel;
        this.dataString1 = data;
    }

    public User getUser() {
        return user;
    }

    public void setIntData(int data){
        dataInt1 = data;
    }

    public void setUserData(User data){
        dataUser1 = data;
    }

    @Override
    public void run() {
        Date start = new Date();
        while (new Date().getTime() - start.getTime() < waitTimeMills){}
        if(!gameOver){
            dataUser1.addMoney(dataInt1);
            dataUser1.challengeActive = false;
            BotUtils.sendMessage(channel, "Request expired!");
            Main.eventProcessor.gamesActive.remove(this);
        }
    }

    public void setWaitTimeMills(long time){
        waitTimeMills = time;
    }

    public void processCommand(String command, String[] lowerArgs, User sender){
        if(sender.id != user.id) return;
        switch (command){
            case "acceptflip":{
                if(sender.money < dataInt1){
                    BotUtils.sendMessage(channel, "You do not have sufficient money anymore");
                    break;
                }

                BotUtils.sendMessage(channel, "Flip accepted! **" + user.username + "** is " + (dataString1.equalsIgnoreCase("h") ? "heads" : "tails"));
                String result = BotUtils.random.nextInt(100) >= 50 ? "h" : "t";

                if(dataString1.equalsIgnoreCase(result)){
                    BotUtils.sendMessage(channel, "The coin landed on **" + (result.equalsIgnoreCase("h") ? "heads**" : "tails**") + "\n" +
                            user.username + " won!\n$`" + dataInt1 + "` was transferred to " + user.username);
                    user.addMoney(dataInt1);
                }
                else{
                    BotUtils.sendMessage(channel, "The coin landed on **" + (result.equalsIgnoreCase("h") ? "heads**" : "tails**") + "\n" +
                            dataUser1.username + " won!\n$`" + dataInt1 + "` was transferred to " + dataUser1.username);
                    dataUser1.addMoney(dataInt1 * 2);
                    user.removeMoney(dataInt1);

                }
                dataUser1.challengeActive = false;
                gameOver = true;
                Main.eventProcessor.gamesActive.remove(this);
                break;
            }
        }
    }
}
