package com.songoda.ultimatestacker.utils.settings;

import com.songoda.ultimatestacker.UltimateStacker;
import com.songoda.ultimatestacker.entity.Check;
import com.songoda.ultimatestacker.entity.Split;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Setting {

    STACK_ITEMS("Main.Stack Items", true,
            "Should items be stacked?"),

    STACK_ENTITIES("Main.Stack Entities", true,
            "Should entities be stacked?"),

    STACK_SPAWNERS("Main.Stack Spawners", true,
            "Should spawners be stacked?"),

    STACK_SEARCH_TICK_SPEED("Main.Stack Search Tick Speed", 5,
            "The speed in which a new stacks will be created.",
            "It is advised to keep this number low."),

    DISABLED_WORLDS("Main.Disabled Worlds", Arrays.asList("World1", "World2", "World3"),
            "Worlds that stacking doesn't happen in."),

    MAX_STACK_ENTITIES("Entity.Max Stack Size", 15,
            "The max amount of entities in a single stack."),

    MIN_STACK_ENTITIES("Entity.Min Stack Amount", 5,
            "The minimum amount required before a stack can be formed."),

    ENTITY_HOLOGRAMS("Entity.Holograms Enabled", true,
            "Should holograms be displayed above stacked entities?"),

    HOLOGRAMS_ON_LOOK_ENTITY("Entity.Only Show Holograms On Look", false,
            "Only show nametags above an entities head when looking directly at them."),

    KILL_WHOLE_STACK_ON_DEATH("Entity.Kill Whole Stack On Death", false,
            "Should killing a stack of entities kill the whole stack or",
            "just one out of the stack?"),

    INSTANT_KILL("Entity.Instant Kill", Arrays.asList("FALL", "DROWNING", "LAVA", "VOID"),
            "Events that will trigger an entire stack to be killed.",
            "It should be noted that this is useless if the above setting is true.",
            "Any of the following can be added to the list:",
            "CONTACT, ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, PROJECTILE",
            "SUFFOCATION, FALL, FIRE, FIRE_TICK",
            "MELTING, LAVA, DROWNING, BLOCK_EXPLOSION",
            "ENTITY_EXPLOSION, VOID, LIGHTNING, SUICIDE",
            "STARVATION, POISON, MAGIC, WITHER",
            "FALLING_BLOCK, THORNS, DRAGON_BREATH, CUSTOM",
            "FLY_INTO_WALL, HOT_FLOOR, CRAMMING, DRYOUT"),

    STACK_CHECKS("Entity.Stack Checks", Arrays.asList(Check.values()).stream()
            .filter(Check::isEnabledByDefault).map(Check::name).collect(Collectors.toList()),
            "These are checks that are processed before an entity is stacked.",
            "You can add and remove from the list at will.",
            "The acceptable check options are:",
            "NERFED, AGE, TICK_AGE, PHANTOM_SIZE",
            "CAN_BREED, IS_TAMED, ANIMAL_OWNER, SKELETON_TYPE",
            "ZOMBIE_BABY, SLIME_SIZE,  PIG_SADDLE, SHEEP_SHEERED",
            "SHEEP_COLOR, WOLF_COLLAR_COLOR, OCELOT_TYPE, HORSE_COLOR",
            "HORSE_STYLE, HORSE_CARRYING_CHEST, HORSE_HAS_ARMOR",
            "HORSE_HAS_SADDLE, HORSE_JUMP, RABBIT_TYPE, VILLAGER_PROFESSION",
            "LLAMA_COLOR, LLAMA_STRENGTH, PARROT_TYPE, PUFFERFISH_STATE",
            "TROPICALFISH_PATTERN, TROPICALFISH_BODY_COLOR, TROPICALFISH_PATTERN_COLOR"),

    SPLIT_CHECKS("Entity.Split Checks", Arrays.asList(Split.values()).stream()
            .map(Split::name).collect(Collectors.toList()),
            "These are checks that when achieved will break separate a single entity",
            "from a stack."),

    NAME_FORMAT_ENTITY("Entity.Name Format", "&f{TYPE} &6{AMT}x",
            "The text displayed above an entities head where {TYPE} refers to",
            "The entities type and {AMT} is the amount currently stacked."),

    SEARCH_RADIUS("Entity.Search Radius", 5,
            "The distance entities must be to each other in order to stack."),

    MAX_STACK_ITEMS("Item.Max Stack Size", 512,
            "The max stack size for items.",
            "Currently this can only be set to a max of 120."),

    NAME_FORMAT_ITEM("Item.Name Format", "&f{TYPE} &6{AMT}x",
            "The text displayed above a dropped item."),

    ITEM_HOLOGRAMS("Item.Holograms Enabled", true,
            "Should holograms be displayed above stacked items?"),

    SPAWNER_HOLOGRAMS("Spawners.Holograms Enabled", true,
            "Should holograms be displayed above stacked spawners?"),

    MAX_STACK_SPAWNERS("Spawners.Max Stack Size", 5,
            "What should the max a spawner can stack to be?"),

    NAME_FORMAT_SPAWNER("Spawners.Name Format", "&f{TYPE} Spawner &6{AMT}x",
            "The text displayed above a stacked spawner where {TYPE} refers to",
            "The entities type and {AMT} is the amount currently stacked."),

    DATABASE_SUPPORT("Database.Activate Mysql Support", false,
            "Should MySQL be used for data storage?"),

    DATABASE_IP("Database.IP", "127.0.0.1",
            "MySQL IP"),

    DATABASE_PORT("Database.Port", 3306,
            "MySQL Port"),

    DATABASE_NAME("Database.Database Name", "UltimateStacker",
            "The database you are inserting data into."),

    DATABASE_PREFIX("Database.Prefix", "US-",
            "The prefix for tables inserted into the database."),

    DATABASE_USERNAME("Database.Username", "PUT_USERNAME_HERE",
            "MySQL Username"),

    DATABASE_PASSWORD("Database.Password", "PUT_PASSWORD_HERE",
            "MySQL Password"),

    LANGUGE_MODE("System.Language Mode", "en_US",
            "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    private String setting;
    private Object option;
    private String[] comments;

    Setting(String setting, Object option, String... comments) {
        this.setting = setting;
        this.option = option;
        this.comments = comments;
    }

    Setting(String setting, Object option) {
        this.setting = setting;
        this.option = option;
        this.comments = null;
    }

    public static Setting getSetting(String setting) {
        List<Setting> settings = Arrays.stream(values()).filter(setting1 -> setting1.setting.equals(setting)).collect(Collectors.toList());
        if (settings.isEmpty()) return null;
        return settings.get(0);
    }

    public String getSetting() {
        return setting;
    }

    public Object getOption() {
        return option;
    }

    public String[] getComments() {
        return comments;
    }

    public List<Integer> getIntegerList() {
        return UltimateStacker.getInstance().getConfig().getIntegerList(setting);
    }

    public List<String> getStringList() {
        return UltimateStacker.getInstance().getConfig().getStringList(setting);
    }

    public boolean getBoolean() {
        return UltimateStacker.getInstance().getConfig().getBoolean(setting);
    }

    public int getInt() {
        return UltimateStacker.getInstance().getConfig().getInt(setting);
    }

    public long getLong() {
        return UltimateStacker.getInstance().getConfig().getLong(setting);
    }

    public String getString() {
        return UltimateStacker.getInstance().getConfig().getString(setting);
    }

    public char getChar() {
        return UltimateStacker.getInstance().getConfig().getString(setting).charAt(0);
    }

    public double getDouble() {
        return UltimateStacker.getInstance().getConfig().getDouble(setting);
    }
}