package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class AgileTrait extends Trait {
    AgileTrait(User u) {
        super(u);

        name = "Agile";
        desc = "-15% Enemy Accuracy\n+10 Strength\n+10 Defense";
        uses = 25;
        type = Types.ATTACK_DEFENSE;
    }

    @Override
    public void onEnable() {
        attachedUser.baseStrength += 10;
        attachedUser.baseDefense += 10;
    }

    @Override
    public void onDisable() {
        attachedUser.baseStrength -= 10;
        attachedUser.baseDefense -= 10;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("ATTACK") || attackType.equals("DEFEND")){
            return true;
        }

        return false;
    }
}
