package com.rit.sucy;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

// Performs the actions for commands
public class CCCommandExecutor  implements CommandExecutor {

    // Error message when a config path isn't found
    private static final String ERROR_MESSAGE = ChatColor.RED + "Invalid unit name";

    // Plugin reference
    private CreatureController plugin;

    // Constructor
    public CCCommandExecutor(CreatureController plugin) {
        this.plugin = plugin;
    }

    // Organizes the commands into their own methods
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String commandName = cmd.getName();
        boolean sentByPlayer = sender instanceof Player;

        // Get commands (args[0] = unit name)
        if (args.length == 1) {

            // Get the damage of a unit
            if (commandName.equalsIgnoreCase("getdamage")) {
                get(args[0], "damage", sender, sentByPlayer);
            }

            // Get the experience of a unit
            else if (commandName.equalsIgnoreCase("getexp")) {
                get(args[0], "experience", sender, sentByPlayer);
            }

            // Get the health of a unit
            else if (commandName.equalsIgnoreCase("gethp")) {
                get(args[0], "health", sender, sentByPlayer);
            }

            // Get whether or not a unit can be spawned
            else if (commandName.equalsIgnoreCase("getspawnable")) {
                get(args[0], "spawnable", sender, sentByPlayer);
            }

            else return false;

            return true;
        }

        // Set commands (args[0] = unit name, args[1] = value for property)
        else if (args.length == 2) {

            String configPath = args[0] + ".";

            // Set the damage of a unit
            if (commandName.equalsIgnoreCase("setdamage")) {

                // Validate the damage
                int damage;
                try {
                    damage = Integer.parseInt(args[1]);
                }
                catch (Exception e) {
                    return false;
                }

                if (damage < 0) {
                    if (sentByPlayer) ((Player)sender).sendMessage("Damage cannot be less than 0");
                    else plugin.getLogger().info("[ERROR] Damage cannot be less than 0");
                    return true;
                }

                // Set it
                if (set(configPath + "damage", sender, sentByPlayer, damage)) {
                    sender.sendMessage("The damage has been set");
                }
            }

            // Set the exp of a unit
            else if (commandName.equalsIgnoreCase("setexp")) {

                // Validate the exp
                int exp;
                try {
                    exp = Integer.parseInt(args[1]);
                }
                catch (Exception e) {
                    return false;
                }

                if (exp < 0) {
                    if (sentByPlayer) sender.sendMessage("Experience cannot be less than 0");
                    else plugin.getLogger().info("[ERROR] Experience cannot be less than 0");
                    return true;
                }

                // Set it
                if (set(configPath + "experience", sender, sentByPlayer, exp)) {
                    sender.sendMessage("The experience has been set");
                }
            }

            // Set the health of a unit
            else if (commandName.equalsIgnoreCase("sethp")) {

                // Validate the health
                int hp;
                try {
                    hp = Integer.parseInt(args[1]);
                }
                catch (Exception e) {
                    return false;
                }

                if (hp < 1) {
                    if (sentByPlayer) ((Player)sender).sendMessage("Health cannot be less than 1");
                    else plugin.getLogger().info("[ERROR] Health cannot be less than 1");
                    return true;
                }

                // Set it
                boolean valid = set(configPath + "health", sender, sentByPlayer, hp);

                if (args[0].equalsIgnoreCase("player")) {
                    for (Player player : plugin.getServer().getOnlinePlayers())
                        player.setMaxHealth(hp);
                    sender.sendMessage("The health has been set and all players have been updated");
                }
                else if (valid) {
                    for (World world : plugin.getServer().getWorlds()) {
                        for (Object entity : world.getEntitiesByClass(UnitManager.getClass(args[0]))) {
                            ((LivingEntity)entity).setMaxHealth(hp);
                            ((LivingEntity)entity).setHealth(hp);
                        }
                    }
                    sender.sendMessage("The health has been set and all mobs have been updated");
                }
            }

            // Set if a unit can be spawned
            else if (commandName.equalsIgnoreCase("setspawnable")) {

                // Validate the value
                boolean spawnable;
                try {
                    spawnable = Boolean.parseBoolean(args[1]);
                }
                catch (Exception e) {
                    return false;
                }

                // Set it
                if (set(configPath + "spawnable", sender, sentByPlayer, spawnable)) {
                    if (!spawnable) {
                        for (World world : plugin.getServer().getWorlds()) {
                            for (Object entity : world.getEntitiesByClass(UnitManager.getClass(args[0]))) {
                                ((LivingEntity)entity).setHealth(0);
                            }
                        }
                        sender.sendMessage("The spawnable property has been set and all previous spawns have been removed");
                    }
                    else sender.sendMessage("The spawnable property has been set");
                }
            }

            else return false;

            return true;
        }

        return false;
    }

    // Sends a message to a command sender containing the property requested
    // Sends an error message to the command sender if the path doesn't exist
    private void get(String unit, String property, CommandSender sender, boolean sentByPlayer) {

        unit = unit.toLowerCase();
        property = property.toLowerCase();
        String configPath = unit + "." + property;

        // Validate the config path
        if (plugin.getConfig().contains(configPath)) {
            String message = "The " + property + " of a " + unit + " is: " + plugin.getConfig().get(configPath);
            if (sentByPlayer) sender.sendMessage(message);
            else plugin.getLogger().info(message);
        }

        // Send error message
        else {
            if (sentByPlayer) sender.sendMessage(ERROR_MESSAGE);
            else plugin.getLogger().info("[ERROR] " + ERROR_MESSAGE);
        }
    }

    // Attempts to set a value in the config
    // Sends an error message to the command sender if the path doesn't exist
    private boolean set(String configPath, CommandSender sender, boolean sentByPlayer, Object value) {

        configPath = configPath.toLowerCase();

        // Validate the config path
        if (plugin.getConfig().contains(configPath)) {
            plugin.getConfig().set(configPath, value);
            return true;
        }

        // Give an error message if the unit name was not found
        else {
            if (sentByPlayer) sender.sendMessage(ERROR_MESSAGE);
            else plugin.getLogger().info("[ERROR] " + ERROR_MESSAGE);
            return false;
        }
    }
}
