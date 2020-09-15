package SlaveBot.Traits;

import SlaveBot.Item;
import SlaveBot.User;

import java.io.Serializable;

public abstract class Trait implements Serializable {

    private static final long serialVersionUID = 42L;

    public double attackMod, defMod, critMod, accMod, capMod, escMod;

    public String name, desc;
    public int uses;

    public int repairCount = 0;

    boolean disabled = false;
    boolean disableable = true;

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

    public void disable(){
        if(isDisableable()) {
            onDisable();
            disabled = true;
        }
    }

    public void enable(){
        onEnable();
        disabled = false;
    }

    public boolean isDisabled(){
        return disabled;
    }

    public boolean isDisableable(){return disableable;}

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

    public boolean isAfter(){
        return type == Types.DEFEND_AFTER || type == Types.ATTACK_AFTER;
    }

    public void procedure1(){}

    public void procedure2(){}

    public void procedure3(){}

    public void procedure4(){}

    public void procedure5(){}

    public void procedureArgs1(Object o){}

    public void procedureArgs2(Object o){}




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
    ESCAPE,
    DEFEND_AFTER,
    ATTACK_AFTER,
    ATTACK_DEFENSE;
}


