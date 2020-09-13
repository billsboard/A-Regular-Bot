package SlaveBot;

import java.util.TimerTask;

public class SaveTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("Saving data to disk...");
        DataSave.saveData();
    }
}
