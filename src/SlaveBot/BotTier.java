package SlaveBot;

class BotTier {
    int moneyMax;
    int moneyMin;

    int health;
    int def;
    int shield;


    BotTier(int moneyMin, int moneyMax, int health, int def, int shield){
        this.moneyMin = moneyMin;
        this.moneyMax = moneyMax;
        this.health = health;
        this.def = def;
        this.shield = shield;
    }
}
