package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

public class NicoNicoNiiTrait extends Trait {

    private static final long serialVersionUID = -12903901280311L;


    public NicoNicoNiiTrait(User u) {
        super(u);

        name = "Nico Nico Nii";
        desc = "This trait has no effect. Disable this trait to remove it";
        type = Types.PERMANENT;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        attachedUser.removeTrait(this);
    }

    @Override
    public boolean checkEnable(Item item, String attackType) {
        return false;
    }
}
