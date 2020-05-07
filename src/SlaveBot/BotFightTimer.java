package SlaveBot;

import discord4j.core.object.entity.MessageChannel;

class BotFightTimer extends Thread{
    long time = 0;
    MessageChannel c;

    BotFightTimer(long time, MessageChannel channel){
        this.time = time;
        c = channel;
    }

    @Override
    public void run(){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}

        if(BotUtils.currentFightTimer == this){
            BotUtils.endBotFight();
            BotUtils.sendMessage(c, "Time expired, fight has been automatically ended");
        }
    }
}
