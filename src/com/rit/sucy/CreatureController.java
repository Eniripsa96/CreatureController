package com.rit.sucy;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

// Main class
public class CreatureController extends JavaPlugin {

    // Available commands
    private static final String[] COMMANDS = new String[] {
            "getdamage", "getexp", "gethp", "getspawnable", "getunitlist",
            "setdamage", "setexp", "sethp", "setspawnable" };

    // Enables the plugin
    @Override
    public void onEnable(){

        // Config
        saveDefaultConfig();
        reloadConfig();

        // Commands
        CCCommandExecutor executor = new CCCommandExecutor(this);
        for (String command : COMMANDS) getCommand(command).setExecutor(executor);

        // Listners
        new CCListener(this);
    }

    // Disables the plugin
    @Override
    public void onDisable() {

        // Remove listeners
        HandlerList.unregisterAll(this);
        saveConfig();
    }

    // Does nothing when run as .jar
    public static void main(String[] args) {}
}
