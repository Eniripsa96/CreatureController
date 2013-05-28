package com.rit.sucy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerJoinEvent;

// Listens on various events to apply settings
public class CCListener implements Listener {

    // Plugin reference
    CreatureController plugin;

    // Constructor
    public CCListener(CreatureController plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // Sets player health on log-in
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerConnect(PlayerJoinEvent event) {
        int playerHealth = plugin.getConfig().getInt("player.health");
        if (playerHealth < 1) return;
        event.getPlayer().setMaxHealth(playerHealth);
    }

    // Checks if something can be spawned
    @EventHandler (priority = EventPriority.LOWEST)
    public void onEntitySpawnAttempt(CreatureSpawnEvent event) {
        String configName = UnitManager.getConfigTitle(event.getEntity());
        if (configName == null) return;

        boolean canSpawn = plugin.getConfig().getBoolean(configName + ".spawnable");
        if (!canSpawn) event.setCancelled(true);
    }

    // Sets health after spawning
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        String configName = UnitManager.getConfigTitle(event.getEntity());
        if (configName == null) return;

        int health = plugin.getConfig().getInt(configName + ".health");
        if (health < 1) return;
        event.getEntity().setMaxHealth(health);
        event.getEntity().setHealth(health);
    }

    // Checks if taming is allowed
    @EventHandler (priority = EventPriority.LOWEST)
    public void onTamed(EntityTameEvent event) {
        String configName = UnitManager.getConfigTitle(event.getEntity());
        if (configName == null) return;

        boolean canTame = plugin.getConfig().getBoolean(configName + ".spawnable");
        if (!canTame) event.setCancelled(true);
    }

    // Sets experience on death
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(EntityDeathEvent event) {
        String configName = UnitManager.getConfigTitle(event.getEntity());
        if (configName == null || event.getEntity() instanceof Player) return;

        int experience = plugin.getConfig().getInt(configName + ".experience");
        if (experience < 1) return;
        event.setDroppedExp(experience);
    }

    // Controls damage dealt
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        String configName = UnitManager.getConfigTitle(event.getDamager());
        if (configName == null || event.getDamager() instanceof Player) return;

        int damage = plugin.getConfig().getInt(configName + ".damage");
        if (damage < 0) return;
        event.setDamage(damage);
    }
}
