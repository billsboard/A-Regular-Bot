package SlaveBot;

import discord4j.core.object.entity.MessageChannel;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

public class EvalThread extends Thread{

    MessageChannel channel;

    ScriptEngine engine;

    String data;

    StringWriter writer = new StringWriter();




    @Override
    public void run() {
        RunnerThread runnerThread = new RunnerThread();
        runnerThread.start(channel, engine, data);
        try {
            runnerThread.join(TimeUnit.SECONDS.toMillis(30));
            if(runnerThread.isAlive()){
                runnerThread.interrupt();
                BotUtils.sendMessage(channel, "Code took too long to execute");
            }

            if(!writer.toString().equals("")){
                BotUtils.sendMessage(channel, "Console Output:```\n" + writer + "```");
            }

        } catch (InterruptedException e) {
        }


    }

    public void start(MessageChannel channel, String language, String data){
        this.channel = channel;


        engine = new ScriptEngineManager().getEngineByName(language);
        engine.getContext().setWriter(writer);

        this.data = data;

        super.start();

    }

    private class RunnerThread extends Thread{

        MessageChannel channel;
        ScriptEngine engine;

        String data;

        public void run(){
            try {
                Object result = engine.eval(data);
                if(result != null){
                    BotUtils.sendMessage(channel, "Returned:```\n" + String.valueOf(result) + "```");
                }
            } catch (Exception e) {
                BotUtils.sendMessage(channel, "Your code exited with a(n) " + e.getClass().getSimpleName());
                e.printStackTrace();
            }
        }

        public void start(MessageChannel channel, ScriptEngine engine, String data){
            this.channel = channel;
            this.engine = engine;
            this.data = data;

            super.start();

        }

    }
}
