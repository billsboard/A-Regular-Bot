package SlaveBot;

import jnr.ffi.annotations.In;

class LootBox {
    static Item[] LOOT = {
            Market.getItem("Stick"),
            Market.getItem("Stick"),
            Market.getItem("Pills"),
            Market.getItem("Pills"),
            Market.getItem("Pills"),
            Market.getItem("Potion"),
            Market.getItem("Potion"),
            Market.getItem("Potion"),
            Market.getItem("Splash potion"),
            Market.getItem("Splash potion"),
            Market.getItem("Splash potion"),
            Market.getItem("Splash potion"),
            Market.getItem("Potion"),
            Market.getItem("Splash potion"),
            Market.getItem("Splash potion"),
            Market.getItem("Slipper"),
            Market.getItem("Slipper"),
            Market.getItem("Slipper"),
            Market.getItem("Baseball Bat"),
            Market.getItem("Baseball Bat"),
            Market.getItem("Actual Sword"),
            Market.getItem("Actual Sword"),
            Market.getItem("German Rifle"),
            Market.getItem("German Rifle"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("Perfume"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("Perfume"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("Perfume"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("Perfume"),
            Market.getItem("Programmer's Blade"),
            Market.getItem("Perfume"),
            Market.getItem("Sans-culottes"),
            Market.getItem("Sans-culottes"),
            Market.getItem("Newspaper"),
            Market.getItem("Newspaper"),
            Market.getItem("Newspaper"),
            Market.getItem("Newspaper"),
            Market.getItem("Rag"),
            Market.getItem("Rag"),
            Market.getItem("Rag"),
            Market.getItem("Dynamite"),
            Market.getItem("Dynamite"),
            Market.getItem("Dynamite"),
            Market.getItem("Crowbar"),
            Market.getItem("Crowbar"),
            Market.getItem("Crowbar"),
            Market.getItem("Sans-culottes"),
            Market.getItem("Sans-culottes"),
            Market.getItem("Newspaper"),
            Market.getItem("Newspaper"),
            Market.getItem("Newspaper"),
            Market.getItem("Newspaper"),
            Market.getItem("Rag"),
            Market.getItem("Rag"),
            Market.getItem("Rag"),
            Market.getItem("Dynamite"),
            Market.getItem("Dynamite"),
            Market.getItem("Dynamite"),
            Market.getItem("Crowbar"),
            Market.getItem("Crowbar"),
            Market.getItem("Crowbar"),
            Market.getItem("Vault"),
            Market.getItem("Vault"),
            Market.getItem("Vault"),
            new Item("Old Bandage", "A simple old bandage", 200, new int[]{10,10,10,10,10,10,15,15,100},"heal"),
            new Item("Old Bandage", "A simple old bandage", 200, new int[]{10,10,10,10,10,10,15,15,100},"heal"),
            new Item("Full Heal", "Completely heals the user", 5000, new int[]{500000000},"heal"),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{500000000}, null, null, 100, 75000),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{500000000}, null, null, 100, 75000),
            new Item("Full Heal", "Completely heals the user", 5000, new int[]{500000000},"heal"),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{500000000}, null, null, 100, 75000),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,1000000}, new int[]{50,70,80}, null, 100, 1),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,1000000}, new int[]{50,70,80}, null, 100, 1),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,1000000}, new int[]{50,70,80}, null, 100, 1),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,1000000}, new int[]{50,70,80}, null, 100, 1),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,1000000}, new int[]{50,70,80}, null, 100, 1),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,1000000}, new int[]{50,70,80}, null, 100, 1),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,1000000}, new int[]{50,70,80}, null, 100, 1),
            new Weapon("Viola", "Deadly sound",
                    new int[]{100,145,150,200,1000000}, new int[]{50,70,80}, null, 100, 1),
            null, null, null, null, null, null, null, //null means cash
            new Item("Full Heal", "Completely heals the user", 5000, new int[]{500000000},"heal"),
            new Item("Administrator's Console", "Unchecked Power", 14000, new int[]{10000},"weapon"),
            new Item("Full Heal", "Completely heals the user", 5000, new int[]{500000000},"heal"),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{500000000}, null, null, 100, 75000),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{500000000}, null, null, 100, 75000),
            new Item("King's Shield", "Used by Aegislash", 12000, new int[]{700,1000,1600}, "shield"),
            new Item("King's Shield", "Used by Aegislash", 12000, new int[]{700,1000,1600}, "shield"),
            new Item("King's Shield", "Used by Aegislash", 12000, new int[]{700,1000,1600}, "shield"),
            new Item("King's Shield", "Used by Aegislash", 12000, new int[]{700,1000,1600}, "shield"),
            new Item("King's Shield", "Used by Aegislash", 12000, new int[]{700,1000,1600}, "shield"),
            new Item("King's Shield", "Used by Aegislash", 12000, new int[]{700,1000,1600}, "shield"),
            new Item("King's Shield", "Used by Aegislash", 12000, new int[]{700,1000,1600}, "shield"),
            new Item("Detect", "Marowak protected itself!", 32000, new int[]{1500,2500,5000}, "shield"),
            new Item("Detect", "Marowak protected itself!", 32000, new int[]{1500,2500,5000}, "shield"),
            new Item("Detect", "Marowak protected itself!", 32000, new int[]{1500,2500,5000}, "shield"),
            new Item("Nugget", "A chunk of gold", 40000, new int[]{}, "utility"),
            new Item("Nugget", "A chunk of gold", 40000, new int[]{}, "utility"),
            new Item("Nugget", "A chunk of gold", 40000, new int[]{}, "utility"),
            new Item("Nugget", "A chunk of gold", 40000, new int[]{}, "utility"),
            new Item("Nugget", "A chunk of gold", 40000, new int[]{}, "utility"),
            new Item("Nugget", "A chunk of gold", 40000, new int[]{}, "utility"),
            new Weapon("Command Prompt", "Not Powershell",
                    new int[]{100000, 100000, 100000, 500000000}, null, null, 100, 50000),
            new Weapon("Command Prompt", "Not Powershell",
                    new int[]{100000, 100000, 100000, 500000000}, null, null, 100, 50000),
            new Item("Trait Voucher", "Gives one free trait roll", 0, null, "utility", 0)
    };

    static Item[] GOOD_LOOT = {
            Market.getItem("air"),
            Market.getItem("spicy air"),
            Market.getItem("spicy stick"),
            Market.getItem("spicy stick"),
            new Item("Detect", "Marowak protected itself!", 32000, new int[]{1500,2500,5000}, "shield"),
            new Item("Detect", "Marowak protected itself!", 32000, new int[]{1500,2500,5000}, "shield"),
            new Item("Nugget", "A chunk of gold", 40000, new int[]{}, "utility"),
            new Item("Nugget", "A chunk of gold", 40000, new int[]{}, "utility"),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{500000000}, null, null, 100, 75000),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{500000000}, null, null, 100, 75000),
            new Weapon("Administrator's Console", "Unchecked Power",
                    new int[]{500000000}, null, null, 100, 75000),
            new Weapon("Command Prompt", "Not Powershell",
                    new int[]{100000,500000,500000000/2}, null, null, 100, 50000),
            new Weapon("Command Prompt", "Not Powershell",
                    new int[]{100000,500000,500000000/2}, null, null, 100, 50000),
            new Weapon("Command Prompt", "Not Powershell",
                    new int[]{100000,500000,500000000/2}, null, null, 100, 50000),
            new Weapon("Command Prompt", "Not Powershell",
                    new int[]{100000,500000,500000000/2}, null, null, 100, 50000)
    };

    static Item getItem(String item){
        for (Item i : LOOT) {
            if(i == null){
                //fall through
            }
            else if(i.getName().equalsIgnoreCase(item)) return i;
        }
        return null;
    }

    static Item getGItem(String item){
        for (Item i : GOOD_LOOT) {
            if(i == null){
                //fall through
            }
            else if(i.getName().equalsIgnoreCase(item)) return i;
        }
        return null;
    }


}
