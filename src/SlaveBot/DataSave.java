package SlaveBot;

import java.io.*;
import java.rmi.server.ExportException;
import java.util.ArrayList;

class DataSave {
    static void saveData(){
        if(!new File("botData.ser").exists()){
            try {
                new File("botData.ser").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("botData.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(Tools.users);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!new File("leaderboard.ser").exists()){
            try {
                new File("leaderboard.ser").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fout = null;
        try {
            fout = new FileOutputStream("leaderboard.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(Leveling.leaderBoard);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadData(){
        try{
            FileInputStream fileIn = new FileInputStream("botData.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Tools.users = (ArrayList) in.readObject();
            in.close();
            fileIn.close();

            File file = new File("leaderboard.ser");
            if(!file.exists()) Leveling.createLeaderboard();
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);
            Leveling.leaderBoard = (User[]) in.readObject();
            in.close();
            fileIn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
