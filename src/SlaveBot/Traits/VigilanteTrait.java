package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class VigilanteTrait extends Trait {

    private static final long serialVersionUID = 217893897492L;


    public VigilanteTrait(User u) {
        super(u);

        name = "Vigilante";
        desc = "+40 defense\n1.2x Accuracy\n_On Equip_:\n+200 reputation\n+$9900\n+5000 hp shield";
        disableable = true;
        type = Types.ATTACK_DEFENSE;
        uses = 20;


        attachedUser.addMoney(9900);
        attachedUser.changeReputation(200);
        attachedUser.setShield(5000);
    }

    @Override
    public void onEnable() {
        attachedUser.baseDefense += 40;
        attachedUser.accuracyModifier *= 1.2;
    }

    @Override
    public void onDisable() {
        attachedUser.baseDefense -= 40;
        attachedUser.accuracyModifier /= 1.2;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("ATTACK") || attackType.equals("DEFEND")){
            return true;
        }

        return false;
    }
}
