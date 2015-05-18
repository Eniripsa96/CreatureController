package com.rit.sucy;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
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

        if (event.getDamager() instanceof Player) {
            return;
        }

        String configName = UnitManager.getConfigTitle(event.getDamager());
        if (configName == null) return;

        int damage = plugin.getConfig().getInt(configName + ".damage");
        if (damage < 0) return;
        event.setDamage(damage);
    }

    // Creeper explosions and Ghast fireballs
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onExplosion(EntityExplodeEvent event) {

        if (event.getEntity() == null) return;

        plugin.getLogger().info(event.getEntity().toString());
        boolean damageBlocks = plugin.getConfig().getBoolean("explosions.damage-blocks");
        Location location = event.getEntity().getLocation();
        event.setCancelled(true);

        if (event.getEntity() instanceof Creeper) {
            int creeperDamage = plugin.getConfig().getInt("creeper.damage");
            if (creeperDamage < 0) creeperDamage = 0;

            int multiplier = ((Creeper)event.getEntity()).isPowered() ? 2 : 1;
            event.getEntity().getWorld().createExplosion(
                    location.getX(), location.getY(), location.getZ(),
                    3.0f * creeperDamage * multiplier / 49, false, damageBlocks);
        }
        else if (event.getEntity() instanceof LargeFireball) {
            int ghastDamage = plugin.getConfig().getInt("ghast.damage");
            if (ghastDamage < 0) ghastDamage = 0;

            event.getEntity().getWorld().createExplosion(
                    location.getX(), location.getY(), location.getZ(),
                    1.0f * ghastDamage / 17, true, damageBlocks);
        }
        else {
            event.getEntity().getWorld().createExplosion(
                    location.getX(), location.getY(), location.getZ(),
                    4.0f, false, damageBlocks);
        }
    }
}
