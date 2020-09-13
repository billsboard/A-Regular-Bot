package SlaveBot;

class DamageObject {

    // Output Vars
    double damage, tDamage, sBypassDamage, sTrueDamage, sDamage;
    boolean miss, kill, shieldBreak, critical;

    User attack;
    User defend;

    Weapon item;

    DamageObject(User attacker, User defender, Weapon weapon){
        attack = attacker;
        defend = defender;
        item = weapon;
    }

    void calculate(){
        int rSeed = BotUtils.random.nextInt(101);

        double generalDamageModifier = 1;

        double effectiveAccuracy = attack.accuracyModifier * item.accuracy;
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

        double effectiveCritChance = attack.baseCrit * attack.critModifier;
        if(rSeed <= effectiveCritChance){
            critical = true;
            generalDamageModifier = 1.7;
        }

        tDamage = generalDamageModifier * item.getTDamageValue() * strengthFormula(attack.baseStrength * attack.strengthMultiplier);
        //System.out.println(item.getTDamageValue());

        double damageToShield = generalDamageModifier * item.getSDamageValue() * strengthFormula(attack.baseStrength * attack.strengthMultiplier) *
                defenseFormula(defend.baseDefense * defend.defenseMultiplier);
        double remainingShield = Math.max(0, defend.getShield() - damageToShield);


        double tempDamageVar = generalDamageModifier * item.getDamageValue() * strengthFormula(attack.baseStrength * attack.strengthMultiplier) *
                defenseFormula(defend.baseDefense * defend.defenseMultiplier);

        damageToShield += Math.min(Math.max(0, tempDamageVar - remainingShield), tempDamageVar);
        tempDamageVar = Math.max(0, tempDamageVar - remainingShield);
        if(damageToShield > defend.getShield() && defend.getShield() > 0){
            shieldBreak = true;
        }
        if(defend.getShield() <= 0) damageToShield = 0;

        damage = tempDamageVar;
        sDamage = damageToShield;

        if(defend.health < damage + tDamage) kill = true;







    }

    double strengthFormula(double x){
        return 0.2 * Math.log(x/2 + 1) + 1;
    }

    double defenseFormula(double x){
        return 1 - (x / (x + 150));
    }

}
