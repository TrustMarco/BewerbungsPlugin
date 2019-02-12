package com.trustmarco.bewerbungsplugin.listeners;

import com.trustmarco.bewerbungsplugin.Main;
import com.trustmarco.bewerbungsplugin.pointmanager.PointsManager;
import jdk.Exported;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PointsManager pointsManager = new PointsManager(player.getUniqueId());
        Main.getInstance().getExecutorService().submit(() -> {
            pointsManager.synchronize();
        });
    }
}
