package com.trustmarco.bewerbungsplugin.listeners;

import com.trustmarco.bewerbungsplugin.pointmanager.PointsManager;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Cow) {
            if (e.getEntity().getKiller() instanceof Player) {
                Player player = e.getEntity().getKiller();
                new PointsManager(player.getUniqueId()).setPoints(new PointsManager(player.getUniqueId()).getPoints()+5);
            }
        }
    }

}
