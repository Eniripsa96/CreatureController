package com.rit.sucy;

import org.bukkit.entity.*;

// Handles keeping track of unit names
public class UnitManager {

    // Size values for a slime
    private static final int BIG_SIZE = 4;
    private static final int SMALL_SIZE = 2;
    private static final int TINY_SIZE = 1;

    // The list of all supported units
    private static final String[] UNIT_LIST = new String[] {
            "bat", "blaze", "cavespider", "chicken", "cow", "creeper", "enderdragon",
            "enderman", "ghast", "irongolem", "magmacubebig", "magmacubesmall", "magmacubetiny",
            "mooshroom", "ocelottamed", "ocelotwild", "pig", "sheep", "silverfish",
            "skeleton", "slimebig", "slimesmall", "slimetiny", "snowgolem", "spider",
            "squid", "villager", "witch", "wither", "witherskeleton", "wolftamed",
            "wolfwild", "zombie", "zombiepigman" };

    // The list of all corresponding classes
    private static final Class[] CLASS_LIST = new Class[] {
            Bat.class, Blaze.class, CaveSpider.class, Chicken.class, Cow.class, Creeper.class, EnderDragon.class,
            Enderman.class, Ghast.class, IronGolem.class, MagmaCube.class, MagmaCube.class, MagmaCube.class,
            MushroomCow.class, Ocelot.class, Ocelot.class, Pig.class, Sheep.class, Silverfish.class,
            Skeleton.class, Slime.class, Slime.class, Slime.class, Snowman.class, Spider.class,
            Squid.class, Villager.class, Witch.class, Wither.class, Skeleton.class, Wolf.class,
            Wolf.class, Zombie.class, PigZombie.class };

    // Displays the list of units for a player
    public static void displayList(Player player) {
        player.sendMessage(getDisplayMessage());
    }

    // Displays the list of units for the server
    public static void displayList(CreatureController plugin) {
        plugin.getLogger().info(getDisplayMessage());
    }

    // Gets the message containing the list of units
    private static String getDisplayMessage() {
        String message = "Valid units: ";
        for (String unit : UNIT_LIST) message += unit + ", ";
        return message.substring(0, message.length() - 2);
    }

    // Gets the title of the entity that is used in the config file
    public static String getConfigTitle(Entity entity) {
        if (entity instanceof Arrow) return "skeleton";
        if (entity instanceof Bat) return "bat";
        if (entity instanceof Blaze) return "blaze";
        if (entity instanceof CaveSpider) return "cavespider";
        if (entity instanceof Chicken) return "chicken";
        if (entity instanceof Cow) {
            if (entity instanceof MushroomCow) return "mooshroom";
            else return "cow";
        }
        if (entity instanceof Creeper) return "creeper";
        if (entity instanceof EnderDragon) return "enderdragon";
        if (entity instanceof Enderman) return "enderman";
        if (entity instanceof Ghast) return "ghast";
        if (entity instanceof IronGolem) return "irongolem";
        if (entity instanceof LargeFireball) return "ghast";
        if (entity instanceof MagmaCube) return "magmacube" + getSlimeSize((Slime)entity);
        if (entity instanceof Ocelot) return "ocelot" + getTamed((Ocelot)entity);
        if (entity instanceof Pig) return "pig";
        if (entity instanceof Sheep) return "sheep";
        if (entity instanceof Silverfish) return "silverfish";
        if (entity instanceof Skeleton) {
            if (((Skeleton)entity).getSkeletonType() == Skeleton.SkeletonType.WITHER) return "witherskeleton";
            else return "skeleton";
        }
        if (entity instanceof Slime) return "slime" + getSlimeSize((Slime)entity);
        if (entity instanceof SmallFireball) return "blaze";
        if (entity instanceof Snowman) return "snowgolem";
        if (entity instanceof Spider) return "spider";
        if (entity instanceof Squid) return "squid";
        if (entity instanceof Villager) return "villager";
        if (entity instanceof Witch) return "witch";
        if (entity instanceof Wither) return "wither";
        if (entity instanceof Wolf) return "wolf" + getTamed((Wolf)entity);
        if (entity instanceof Zombie) {
            if (entity instanceof PigZombie) return "zombiepigman";
            else return "zombie";
        }

        return null;
    }

    public static Class getClass(String name) {
        for (int i = 0; i < UNIT_LIST.length; i++) {
            if (name.equalsIgnoreCase(UNIT_LIST[i]))
                return CLASS_LIST[i];
        }

        return null;
    }

    // Gets the size string of a slime
    private static String getSlimeSize(Slime slime) {
        switch (slime.getSize()) {
            case BIG_SIZE:
                return "big";
            case SMALL_SIZE:
                return "small";
            case TINY_SIZE:
                return "tiny";
            default:
                return null;
        }
    }

    // Gets the tamed string of a wolf or ocelot
    private static String getTamed(Tameable tameable) {
        if (tameable.isTamed()) return "tamed";
        else return "wild";
    }
}
