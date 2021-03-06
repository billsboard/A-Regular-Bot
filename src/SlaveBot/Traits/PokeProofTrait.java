package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;
import SlaveBot.Weapon;

public class PokeProofTrait extends Trait {

    private static final long serialVersionUID = -4873204867707475504L;

    public PokeProofTrait(User u) {
        super(u);

        type = Types.ATTACK;
        name = "Pokeproof";
        uses = 40;
        desc = "+1.5x damage with stick\nImmunity to stick-based attacks";
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
        else if(attackType.equals("ATTACK")){
            if(item.name.toLowerCase().contains("stick")){
                return true;
            }
            else if(item.name.equalsIgnoreCase("baseball bat")){
                return true;
            }
        }
        else if(attackType.equals("DEFEND")){
            if(item.name.toLowerCase().contains("stick")){
                return true;
            }
            else if(item.name.equalsIgnoreCase("baseball bat")){
                return true;
            }
        }

        return false;
    }
}
