package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class SharpshooterTrait extends Trait {

    private static final long serialVersionUID = 1115660725742063529L;

    public SharpshooterTrait(User u) {
        super(u);

        type = Types.ATTACK;
        name = "Sharpshooter";
        desc = "+2x Accuracy\n+35% Critical Hit Chance\n+1.5x Critical Hit Damage";
        uses = 20;
    }

    @Override
    public void onEnable() {
        attachedUser.critDamageModifier *= 1.5;
        attachedUser.baseCrit += 35;
        attachedUser.accuracyModifier *= 2;
    }

    @Override
    public void onDisable() {
        attachedUser.critDamageModifier /= 1.5;
        attachedUser.baseCrit -= 35;
        attachedUser.accuracyModifier /= 2;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("ATTACK")){
            return true;
        }

        return false;
    }
}
