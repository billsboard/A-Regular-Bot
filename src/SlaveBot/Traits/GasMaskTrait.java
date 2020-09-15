package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class GasMaskTrait extends Trait {

    private static final long serialVersionUID = 1230213912130L;


    public GasMaskTrait(User u) {
        super(u);

        this.name = "Gas Mask";
        this.desc = "Immunity to air-based attacks";
        this.uses = 25;
        this.type = Types.DEFENSE_SPECIALIZED;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        if(isDisabled()) return false;
        else if(attackType.equals("DEFEND")){
            if(item.name.toLowerCase().contains("air") || item.name.toLowerCase().equals("perfume")){
                return true;
            }
        }

        return false;
    }
}
