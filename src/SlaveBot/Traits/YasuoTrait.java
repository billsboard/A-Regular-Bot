package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class YasuoTrait extends Trait{

    private static final long serialVersionUID = 1115660725742063529L;

    /* This trait grants 2x crit chance but also gives 0.8 crit damage */


    public YasuoTrait(User u) {
        super(u);

        type = Types.ATTACK_UNBREAKABLE;
        name = "Yasuo";
        desc = "+2.0x Critical Chance\n-20% Critical Damage";
        uses = 25;
    }

    @Override
    public void onEnable() {
        attachedUser.critModifier *= 2;
        attachedUser.critDamageModifier *= 0.8;
    }

    @Override
    public void onDisable() {
        attachedUser.critModifier /= 2;
        attachedUser.critDamageModifier /= 0.8;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("ATTACK")){
            return true;
        }

        return false;
    }
}
