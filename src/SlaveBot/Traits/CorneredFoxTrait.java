package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class CorneredFoxTrait extends Trait {

    private static final long serialVersionUID = -7013546014116229914L;

    /* This trait grants +35 base defense and +1.15x defense modifier;
     * It is an unbreakable trait */



    public CorneredFoxTrait(User u) {
        super(u);

        type = Types.DEFENSE;
        name = "Cornered Fox";
        desc = "+35 base defense\n+15% defense multiplier";
        uses = 30;
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
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("DEFEND")){
            return true;
        }

        return false;
    }

}
