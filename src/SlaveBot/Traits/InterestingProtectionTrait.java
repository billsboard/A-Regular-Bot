package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class InterestingProtectionTrait extends Trait {

    private static final long serialVersionUID = -3469561842740068123L;

    public InterestingProtectionTrait(User u) {
        super(u);

        name = "Interesting Protection";
        desc = "If an attack would not kill, attack does zero damage instead";
        uses = 10;
        type = Types.DEFEND_AFTER;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(attackType.equals("DEFEND_AFTER")) return true;

        return false;
    }
}
