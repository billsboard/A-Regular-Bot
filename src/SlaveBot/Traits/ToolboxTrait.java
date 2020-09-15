package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class ToolboxTrait extends Trait {

    private static final long serialVersionUID = 12390812908302L;


    public ToolboxTrait(User u) {
        super(u);

        name = "Toolbox";
        desc = "3x Strength\n+200 Base Strength\n+100% Base Crit Chance\n2x Crit Damage\n_On Equip:_\nGives +20 durability to all active breakable traits";
        uses = 1;
        type = Types.ATTACK;
        disableable = false;
        repairCount = 3;
    }

    @Override
    public void onEnable() {
        attachedUser.strengthMultiplier *= 3;
        attachedUser.baseStrength += 200;
        attachedUser.baseCrit += 100;
        attachedUser.critDamageModifier *= 2;
        for (Trait t : attachedUser.buffs) {
            if(t.isBreakable() && t != this){
                t.uses += 20;
            }
        }
    }

    @Override
    public void onDisable() {
        attachedUser.strengthMultiplier /= 3;
        attachedUser.baseStrength -= 200;
        attachedUser.baseCrit -= 100;
        attachedUser.critDamageModifier /= 2;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("ATTACK")){
            return true;
        }

        return false;
    }
}
