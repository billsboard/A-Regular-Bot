package SlaveBot;

import SlaveBot.Traits.*;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.ArrayList;
import java.util.function.Consumer;

class Tools {
    static ArrayList<User> users = new ArrayList<>();

    static User getUser(long DiscordID){
        for (User u: users) {
            if(u.id == DiscordID){
                discord4j.core.object.entity.User user = Main.client.getUserById(Snowflake.of(DiscordID)).block();
                if(!user.getUsername().equals(u.username)){
                    u.username = user.getUsername();
                }

                return u;
            }
        }

        if(Main.getUserByID(DiscordID) == null){return null;}
        User newUser = new User(DiscordID);
        users.add(newUser);
        return newUser;
    }

    static User recreateUser(long DiscordID){
        users.remove(getUser(DiscordID));
        return getUser(DiscordID);
    }

    static DamageObject doDamage(User attack, User defend, Weapon item){
        DamageObject dmg = new DamageObject(attack, defend, item);
        dmg.calculate();

        if(attack.buffs == null) attack.buffs = new ArrayList<>();
        if(defend.buffs == null) defend.buffs = new ArrayList<>();

        /*for (Trait t : attack.buffs) {
            if(t.type.equals("ATTACK")){
                t.uses -= 1;
                if(t.uses <= 0){
                    attack.removeBuff(t);
                }
            }
        }

        for (Trait t : defend.buffs) {
            if(t.type.equals("DEFEND")){
                t.uses -= 1;
                if(t.uses <= 0){
                    defend.removeBuff(t);
                }
            }
        }*/

        defend.health -= dmg.damage;
        defend.health -= dmg.tDamage;
        defend.shieldRemaining -= dmg.sDamage;

        return dmg;
    }

    static DamageObject damageCalculation(MessageChannel c, User attack, User defend, Weapon item){
        DamageObject dmg = doDamage(attack, defend, item);

        StringBuilder noteData = new StringBuilder();
        noteData.append("\u200b");

        if(!dmg.messages.toString().isEmpty()) noteData.append(dmg.messages);

        if(dmg.critical) noteData.append("A critical hit!\n");
        if(dmg.shieldBreak) noteData.append(defend.mentionString() + "'s shield was destroyed!\n");
        if(dmg.miss) noteData.append("The attack missed!\n");
        if(dmg.kill) noteData.append("**The target was killed!**");

        Consumer<EmbedCreateSpec> messageEmbed = e -> {
            e.setDescription(attack.mentionString() + " attacked " + defend.mentionString() + " with a(n) " + item.name);
            e.addField("Player Damage", String.format("%.2f", dmg.damage), true)
                    .addField("Shield Damage", String.format("%.2f", dmg.sDamage), true)
                    .addField("True Damage", String.format("%.2f", dmg.tDamage), true);

            e.addField("Attack log", noteData.toString(), false);
        };
        BotUtils.sendEmbedSpec(c, messageEmbed);

        if(dmg.kill){
            int moneyStolen = (int) (((double) (BotUtils.random.nextInt(70) + 20)) / 100.0 * defend.getMoney());
            defend.removeMoney(moneyStolen);
            attack.addMoney(moneyStolen);

            Consumer<EmbedCreateSpec> lootMsg = e -> {
                e.setDescription(attack.mentionString() + " looted $" + moneyStolen + " from " + defend.mentionString() + "'s dead body");
            };
            BotUtils.sendEmbedSpec(c, lootMsg);

            attack.kills++;
            defend.deaths++;

            defend.setHealth(defend.getMaxHealth());
            defend.setShield(0);

            attack.gainXP(c, 1500 + BotUtils.random.nextInt(250));

        }
        if(dmg.shieldBreak){
            defend.setShield(0);
        }

        attack.gainXP(c, 500 + BotUtils.random.nextInt(150));

        return dmg;
    }

    static Trait traitTable(int r, User u){
        if(r == 0){
            return new CorneredFoxTrait(u);
        }
        else if(r == 1){
            return new YasuoTrait(u);
        }
        else if(r == 2){
            return new PokeProofTrait(u);
        }
        else if(r == 3){
            return new WeakTrait(u);
        }
        else if(r == 4){
            return new SharpshooterTrait(u);
        }
        else if(r == 5){
            return new GasMaskTrait(u);
        }
        else if(r == 6){
            return new InterestingProtectionTrait(u);
        }

        return null;
    }

    static Trait rollTrait(User u){
        int r = BotUtils.random.nextInt(7);

        return traitTable(r, u);
    }

}
