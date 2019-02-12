package com.trustmarco.bewerbungsplugin.commands;

import com.trustmarco.bewerbungsplugin.Main;
import com.trustmarco.bewerbungsplugin.pointmanager.PointsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PuntkeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 1) {
            String name = args[0];
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            if (new PointsManager(uuid).exists() || Main.getInstance().getCache_points().containsKey(uuid)) {
                sender.sendMessage(Main.getInstance().getPrefix() + "Der Spieler §e" + name + "§7 besitzt §e" + new PointsManager(uuid).getPoints() + " Punkte§7.");
            } else {
                sender.sendMessage(Main.getInstance().getPrefix() + "§cDieser Spieler konnte nicht gefunden werden.");
            }
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(Main.getInstance().getPrefix() + "Du besitzt §e" + new PointsManager(player.getUniqueId()).getPoints() + " Punkte§7.");
            }
        }

        return true;
    }
}
