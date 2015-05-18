package com.rit.sucy;

import com.sucy.skill.api.event.PhysicalDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CCSkillAPIListener implements Listener
{
    private CreatureController plugin;

    public CCSkillAPIListener(CreatureController plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(PhysicalDamageEvent event)
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
