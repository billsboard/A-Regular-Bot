package SlaveBot;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CreateEval {
    public static void main(String args[]){
        ScriptEngine sE = new ScriptEngineManager().getEngineByName(args[0]);

        try {

            String returned = String.valueOf(sE.eval(args[1]));
            System.out.println("<<Verykiregex>>");
            if(returned != null){
                System.out.println(returned);
            }

        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
