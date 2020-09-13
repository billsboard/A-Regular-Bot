package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class WeakTrait extends Trait{

    private static final long serialVersionUID = -293892L;

    public WeakTrait(User u) {
        super(u);

        type = Types.ATTACK_UNBREAKABLE;
        name = "Weak";
        desc = "-25% Strength\n-25% Defense\n-50% Critical Hit Chance\n-20% Critical Damage";
    }

    @Override
    public void onEnable() {
        attachedUser.strengthMultiplier *= 0.75;
        attachedUser.defenseMultiplier *= 0.75;
        attachedUser.critModifier *= 0.5;
        attachedUser.critDamageModifier *= 0.5;
    }

    @Override
    public void onDisable() {
        attachedUser.strengthMultiplier /= 0.75;
        attachedUser.defenseMultiplier /= 0.75;
        attachedUser.critModifier /= 0.5;
        attachedUser.critDamageModifier /= 0.5;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        return false;
    }
}
