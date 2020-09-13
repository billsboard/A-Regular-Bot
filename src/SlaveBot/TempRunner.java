package SlaveBot;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

public class TempRunner {
    public static void run(){
        //Leveling.createLeaderboard();
        //new Main.DailyTask().run();


        /*
        ScriptEngineManager mgr = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = mgr.getEngineFactories();
        for (ScriptEngineFactory factory : factories)
        {
            System.out.println("ScriptEngineFactory Info");
            String engName = factory.getEngineName();
            String engVersion = factory.getEngineVersion();
            String langName = factory.getLanguageName();
            String langVersion = factory.getLanguageVersion();
            System.out.printf("\tScript Engine: %s (%s)\n", engName, engVersion);
            List<String> engNames = factory.getNames();
            for (String name : engNames)
            {
                System.out.printf("\tEngine Alias: %s\n", name);
            }
            System.out.printf("\tLanguage: %s (%s)\n", langName, langVersion);
        }*/

        for (User u : Tools.users) {
            u.critModifier = 1;
            u.accuracyModifier = 1;
            u.captureModifier = 1;
            u.escapeModifier = 1;
            u.strengthMultiplier = 1;
            u.defenseMultiplier = 1;

            u.baseDefense = u.level * 2 + 10;
            u.baseStrength = u.level * 2 + 10;
            u.baseAccuracy = 20;
            u.baseCapture = 10;
            u.baseCrit = 10;
            u.baseEscape = 2;
        }

    }

}
