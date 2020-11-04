package SlaveBot;

import SlaveBot.Traits.Trait;
import SlaveBot.Traits.VigilanteTrait;
import SlaveBot.Traits.WantedTrait;

import java.util.ArrayList;
import java.util.Iterator;

class DamageObject {

    // Output Vars
    double damage, tDamage, sBypassDamage, sTrueDamage, sDamage;
    boolean miss, kill, shieldBreak, critical;
    double generalDamageModifier = 1;

    double dmgMod = 1, tDmgMod = 1, sDmgMod = 1, accMod = 1, critChanceMod = 1, critDmgMod = 1;

    StringBuilder messages = new StringBuilder();


    User attack;
    User defend;

    Weapon item;

    DamageObject(User attacker, User defender, Weapon weapon){
        attack = attacker;
        defend = defender;
        item = weapon;
    }

    void calculate(){

        calculateTraits();

        int rSeed = BotUtils.random.nextInt(101);


        double effectiveAccuracy = attack.accuracyModifier * item.accuracy * accMod;
        if(rSeed > effectiveAccuracy){ //Attack missed
            miss = true;
            kill = false;
            shieldBreak = false;
            critical = false;
            damage = 0;
            tDamage = 0;
            sBypassDamage = 0;
            sTrueDamage = 0;
            sDamage = 0;
            return;
        }

        rSeed = BotUtils.random.nextInt(101);

        double effectiveCritChance = attack.baseCrit * attack.critModifier * critChanceMod;
        if(rSeed <= effectiveCritChance){
            critical = true;
            generalDamageModifier *= 2.2 * attack.critDamageModifier * critDmgMod;
        }

        tDamage = generalDamageModifier * item.getTDamageValue() * strengthFormula(attack.baseStrength * attack.strengthMultiplier) * tDmgMod;
        //System.out.println(item.getTDamageValue());

        double damageToShield = generalDamageModifier * item.getSDamageValue() * strengthFormula(attack.baseStrength * attack.strengthMultiplier) *
                defenseFormula(defend.baseDefense * defend.defenseMultiplier) * sDmgMod;
        double remainingShield = Math.max(0, defend.getShield() - damageToShield);


        double tempDamageVar = generalDamageModifier * item.getDamageValue() * strengthFormula(attack.baseStrength * attack.strengthMultiplier) *
                defenseFormula(defend.baseDefense * defend.defenseMultiplier) * dmgMod;

        damageToShield += Math.max(Math.max(0, tempDamageVar - remainingShield), tempDamageVar);
        tempDamageVar = Math.max(0, tempDamageVar - remainingShield);
        if(damageToShield > defend.getShield() && defend.getShield() > 0){
            shieldBreak = true;
        }
        if(defend.getShield() <= 0) damageToShield = 0;

        damage = tempDamageVar;
        sDamage = damageToShield;

        if(defend.health < damage + tDamage) kill = true;

        calculateAfterTraits();
        damageTraits();




    }

    void damageTraits(){
        Iterator<Trait> attackIt = attack.buffs.iterator();
        while (attackIt.hasNext()){
            Trait t = attackIt.next();
            if(t.checkEnable(item, "ATTACK") && !t.isDisabled()){
                t.decrementDurability();
                if(t.uses <= 0 && t.isBreakable()){
                    try {
                        messages.append(attack.mentionString()).append("'s trait: **").append(t).append("** has broken!\n");
                        attackIt.remove();
                    }catch (Exception ignored){}
                }

            }

        }

        Iterator<Trait> defendIt = defend.buffs.iterator();
        while (defendIt.hasNext()){
            Trait t = defendIt.next();
            if(t.checkEnable(item, "DEFEND") && !t.isDisabled()){
                t.decrementDurability();
                if(t.uses <= 0 && t.isBreakable()){
                    try {
                        messages.append(defend.mentionString()).append("'s trait: **").append(t).append("** has broken!\n");
                        defendIt.remove();
                    }catch (Exception ignored){}
                }
            }

        }

    }

    void calculateTraits(){

        if(attack.buffs == null) attack.buffs = new ArrayList<>();
        if(defend.buffs == null) defend.buffs = new ArrayList<>();


        Iterator<Trait> attackIt = attack.buffs.iterator();
        while (attackIt.hasNext()){
            Trait t = attackIt.next();
            if(t.checkEnable(item, "ATTACK") && !t.isDisabled()){
                switch (t.name.toLowerCase()){
                    case "pokeproof":{
                        generalDamageModifier *= 1.5;
                        messages.append(attack.mentionString() + "'s Pokeproof buffed the damage!\n");
                        break;
                    }
                }

            }

        }



        Iterator<Trait> defendIt = defend.buffs.iterator();
        while (defendIt.hasNext()){
            Trait t = defendIt.next();
            if(t.checkEnable(item, "DEFEND") && !t.isDisabled()){
                switch (t.name.toLowerCase()){
                    case "pokeproof":{
                        generalDamageModifier *= 0;
                        messages.append(defend.mentionString() + "'s Pokeproof blocked the attack!\n");
                        break;
                    }
                    case "gas mask":{
                        generalDamageModifier *= 0;
                        messages.append(defend.mentionString() + "'s gas mask protected them!\n");
                        break;
                    }
                    case "small":{
                        accMod *= 0.67;
                        break;
                    }
                    case "agile":{
                        accMod *= 0.85;
                        break;
                    }
                }
            }

        }



    }

    void calculateAfterTraits(){
        Iterator<Trait> attackIt = attack.buffs.iterator();
        while (attackIt.hasNext()){
            Trait t = attackIt.next();
            if(t.checkEnable(item, "ATTACK_AFTER") && !t.isDisabled()){
                switch (t.name.toLowerCase()){
                    case "wanted":{
                        if(kill){
                            t.procedure1();
                        }
                    }
                    break;
                }

                t.decrementDurability();
                if(t.uses <= 0 && t.isBreakable()){
                    try {
                        messages.append(attack.mentionString()).append("'s trait: **").append(t).append("** has broken!\n");
                        attackIt.remove();
                    }catch (Exception ignored){}
                }
            }

        }

        Iterator<Trait> defendIt = defend.buffs.iterator();
        while (defendIt.hasNext()){
            Trait t = defendIt.next();
            if(t.checkEnable(item, "DEFEND_AFTER") && !t.isDisabled()){
                switch (t.name.toLowerCase()){
                    case "interesting protection":{
                        if(!kill){
                            damage = 0;
                            tDamage = 0;
                            sTrueDamage = 0;
                            sBypassDamage = 0;
                            sDamage = 0;
                            shieldBreak = false;
                            messages.append(defend.mentionString() + "'s Interesting Protection nullified the non-fatal attack!\n");
                        }
                        break;
                    }
                    case "wanted":{
                        if(kill){
                            messages.append(attack.mentionString() + " has claimed the bounty and become a celebrated hero!\n");
                            attack.applyTrait(new VigilanteTrait(attack));
                            t.procedureArgs1(attack);
                            t.onDisable();
                            defendIt.remove();
                            continue;
                        }
                        break;
                    }
                }

                t.decrementDurability();
                if(t.uses <= 0 && t.isBreakable()){
                    try {
                        messages.append(defend.mentionString()).append("'s trait: **").append(t).append("** has broken!\n");
                        defendIt.remove();
                    }catch (Exception ignored){}
                }
            }

        }
    }

    double strengthFormula(double x){
        return 0.25 * Math.sqrt(x);
    }

    double defenseFormula(double x){
        return 1 - (x / (x + 150));
    }

}
