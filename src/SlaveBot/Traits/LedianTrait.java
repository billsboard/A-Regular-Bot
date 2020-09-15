package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class LedianTrait extends Trait {

    private static final long serialVersionUID = -280138012983L;

    public LedianTrait(User u) {
        super(u);

        name = "\"Lesbian Noire\"";
        desc = "-50% Strength\n-50% Defense\n0.5x Critical Hit Chance\n0.75x Critical Hit Damage\n0.9x Accuracy";
        type = Types.DEFEND_UNBREAKABLE;
        disableable = false;
    }

    @Override
    public void onEnable() {
        attachedUser.strengthMultiplier *= 0.5;
        attachedUser.defenseMultiplier *= 0.5;
        attachedUser.critModifier *= 0.5;
        attachedUser.critDamageModifier *= 0.75;
        attachedUser.accuracyModifier *= 0.9;
    }

    @Override
    public void onDisable() {
        attachedUser.strengthMultiplier /= 0.5;
        attachedUser.defenseMultiplier /= 0.5;
        attachedUser.critModifier /= 0.5;
        attachedUser.critDamageModifier /= 0.75;
        attachedUser.accuracyModifier /= 0.9;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        return false;
    }
}
