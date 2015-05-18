package com.rit.sucy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CCNormalListener implements Listener
{
    private CreatureController plugin;

    public CCNormalListener(CreatureController plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player) {
            String item = ((Player)event.getDamager()).getInventory().getItemInHand().getType().name();
            int itemDamage;
            if (plugin.getConfig().contains("player.item-damage." + item)) {
                itemDamage = plugin.getConfig().getInt("player.item-damage." + item);
            }
            else {
                itemDamage = plugin.getConfig().getInt("player.item-damage.PUNCH");
            }
            event.setDamage(itemDamage);
        }
    }
}
