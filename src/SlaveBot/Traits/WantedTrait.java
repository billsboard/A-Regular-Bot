package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class WantedTrait extends Trait {

    private static final long serialVersionUID = -625814039934946097L;


    int kills = 0;
    String description = "+50 Base Strength\n2x Strength\n2x Crit Damage\nKilling players stacks more bounty on you.\n_On death_:\nBounty is given to player that kill you\n" +
            "This trait is removed from you\nCurrent kills: ";

    public WantedTrait(User u) {
        super(u);

        name = "Wanted";
        desc = description + kills;
        type = Types.PERMANENT;
        disableable = false;
    }

    @Override
    public void onEnable() {
        attachedUser.baseStrength += 50;
        attachedUser.strengthMultiplier *= 2;
        attachedUser.critDamageModifier *= 2;
    }

    @Override
    public void procedure1() {
        //Increase kill bounty;
        kills++;
        desc = description + kills;
    }

    @Override
    public void procedureArgs1(Object o) {
        //Death
        if(o instanceof User){
            User u = (User) o;
            u.addMoney(kills * 2000);
            try {
                attachedUser.removeTrait(this);
            }catch (Exception ignored){}
        }
    }

    @Override
    public void onDisable() {
        attachedUser.baseStrength -= 50;
        attachedUser.strengthMultiplier /= 2;
        attachedUser.critDamageModifier /= 2;
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("ATTACK_AFTER")){
            return true;
        }
        if(attackType.equals("DEFEND_AFTER")){
            return true;
        }

        return false;
    }
}
