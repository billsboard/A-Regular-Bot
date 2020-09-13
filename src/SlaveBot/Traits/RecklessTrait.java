package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class RecklessTrait extends Trait{

    private static final long serialVersionUID = -2190381290830128L;

    public RecklessTrait(User u) {
        super(u);

        name = "Reckless";
        desc = "+20 Base Strength\n+   % Strength\n-50% Defense\n+1.1x Crit Chance\n1.5x Crit Damage\n0.8x Accuracy";
        type = Types.ATTACK;
        uses = 15;
    }

    @Override
    public void onEnable() {
        attachedUser.baseStrength += 20;
        attachedUser.strengthMultiplier *= 1.2;
        attachedUser.defenseMultiplier *= 0.5;
        attachedUser.critModifier *= 1.1;
        attachedUser.critDamageModifier *= 1.5;
        attachedUser.accuracyModifier *= 0.8;
    }

    @Override
    public void onDisable() {
        attachedUser.baseStrength -= 20;
        attachedUser.strengthMultiplier /= 1.2;
        attachedUser.defenseMultiplier /= 0.5;
        attachedUser.critModifier /= 1.1;
        attachedUser.critDamageModifier /= 1.5;
        attachedUser.accuracyModifier /= 0.8;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("ATTACK")){
            return true;
        }

        return false;
    }
}
