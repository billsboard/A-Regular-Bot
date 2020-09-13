package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

import java.io.Serializable;

public abstract class Trait implements Serializable {

    private static final long serialVersionUID = 42L;

    public double attackMod, defMod, critMod, accMod, capMod, escMod;

    public String name, desc;
    public int uses;

    Types type;


    User attachedUser;

    Trait(User u){
        attachedUser = u;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract boolean checkEnable(Item item, String attackType);

    public void decrementDurability(){
        if(!isBreakable()) return;
        else{
            if(--uses <= 0){
                onDisable();
                //attachedUser.removeTrait(this);
            }
        }
    }

    public boolean isBreakable(){
        return !(type == Types.DEFEND_UNBREAKABLE || type == Types.ATTACK_UNBREAKABLE || type == Types.PERMANENT);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().isInstance(this)){
            if(((Trait) obj).name.equals(name)){
                return true;
            }
        }

        return false;
    }
}

enum Types{
    ATTACK,
    DEFENSE,
    ATTACK_SPECIALIZED,
    DEFENSE_SPECIALIZED,
    PERMANENT,
    ATTACK_UNBREAKABLE,
    DEFEND_UNBREAKABLE,
    CAPTURE,
    ESCAPE
}


