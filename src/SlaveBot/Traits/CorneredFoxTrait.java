package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class CorneredFoxTrait extends Trait {

    /* This trait grants +35 base defense and +1.15x defense modifier;
     * It is an unbreakable trait */



    public CorneredFoxTrait(User u) {
        super(u);

        type = Types.DEFEND_UNBREAKABLE;
        name = "Cornered Fox";
        desc = "+35 base defense\n+15% defense multiplier";
    }

    @Override
    public void onEnable() {
        attachedUser.baseDefense += 35;
        attachedUser.defenseMultiplier *= 1.15;
    }

    @Override
    public void onDisable() {
        attachedUser.baseDefense -= 35;
        attachedUser.defenseMultiplier /= 1.15;
    }

    @Override
    boolean checkEnable(Item item, String attackType) {
        return false;
    }

}
