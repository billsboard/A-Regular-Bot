package SlaveBot;

import discord4j.core.object.entity.MessageChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

public class EvalJVM extends Thread{

    MessageChannel channel;
    String lang;
    String code;

    public void start(MessageChannel channel, String lang, String code){
        this.channel = channel;
        this.code = code;
        this.lang = lang;
        super.start();
    }

    public void run(){
        try{
            new TimerThread().start(channel, this);
            startSecondJVM();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startSecondJVM() throws Exception {
        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String path = System.getProperty("java.home")
                + separator + "bin" + separator + "java";
        ProcessBuilder processBuilder =
                new ProcessBuilder(path, "-cp",
                        classpath,
                        CreateEval.class.getName(), lang,  code);

        Process process = processBuilder.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader ebr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        process.waitFor();

        while((line=br.readLine())!=null) sb.append(line);

        String[] data = sb.toString().split("<<Verykiregex>>");

        String out = "";
        if(data[0] != null && !data[0].equals("null") && !data[0].isEmpty()) out += "Console output:\n```" + data[0] + "```\n";
        if(data.length > 1 && data[1] != null && !data[1].equals("null")) out += "Returned:\n```" + data[1] + "```";
        //System.out.println(out);

        if(out.isEmpty()){
            out = "```";
            String s = "";
            while((s = ebr.readLine()) != null){
                out += s;
            }
            out += "```";
            if(out.equals("``````")) out = "```Program exited with code 0```";
        }
        BotUtils.sendMessage(channel, out);
    }

    class TimerThread extends Thread{

        MessageChannel c;
        EvalJVM w;

        public void start(MessageChannel channel, EvalJVM wrapper){
            c = channel;
            w = wrapper;
            super.start();
        }

        @Override
        public void run() {
            try {
                w.join(TimeUnit.SECONDS.toMillis(45));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(w.isAlive()){
                w.interrupt();
                BotUtils.sendMessage(channel, "Execution time limit exceeded");
            }

        }
    }

}

