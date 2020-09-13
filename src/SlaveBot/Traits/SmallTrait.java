package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class SmallTrait extends Trait {

    private static final long serialVersionUID = -8067868347791423268L;

    public SmallTrait(User u) {
        super(u);

        name = "Small";
        desc = "-10% Strength\n-25% Defense\n-33% Enemy Accuracy";
        uses = 15;
        type = Types.DEFENSE;
    }

    @Override
    public void onEnable() {
        attachedUser.strengthMultiplier *= 0.9;
        attachedUser.defenseMultiplier *= 0.75;
    }

    @Override
    public void onDisable() {
        attachedUser.strengthMultiplier /= 0.9;
        attachedUser.defenseMultiplier /= 0.75;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("DEFEND")){
            return true;
        }

        return false;
    }
}
