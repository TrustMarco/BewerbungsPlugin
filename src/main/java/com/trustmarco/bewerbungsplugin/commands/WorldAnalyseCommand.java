package com.trustmarco.bewerbungsplugin.commands;

import com.trustmarco.bewerbungsplugin.Main;
import com.trustmarco.bewerbungsplugin.mysql.MySQL;
import com.trustmarco.bewerbungsplugin.worldanalyse.WorldAnalyse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorldAnalyseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getInstance().getPrefix() + "§cDazu musst du dich Ingame befinden!");
            return true;
        }

        final Player player = (Player) sender;

        if (args.length == 1) {
            if (!(Main.getInstance().getCooldown().contains(player))) {
                try {
                    int radius = Integer.parseInt(args[0]);
                    WorldAnalyse worldAnalyse = new WorldAnalyse(player.getLocation(), radius);
                    final int players = worldAnalyse.getPlayers();
                    int monstors = worldAnalyse.getMonstors();
                    int animals = worldAnalyse.getAnimals();
                    int entitys = players + monstors + animals;
                    int players_percent = (players * 100) / entitys;
                    int monstors_percent = (monstors * 100) / entitys;
                    int animals_percent = (animals * 100) / entitys;

                    player.sendMessage(Main.getInstance().getPrefix() + "In deinem Umfeld befindet sich derzeit §e" + players + "(" + players_percent + "%) Spieler§7. Außerdem befinden sich in deinem Umfeld §e" + animals + "(" + animals_percent + "%)Tiere§7. Und wir haben in deinem Umfeld §e" + monstors + "(" + monstors_percent + "%) Monster §7gefunden");
                    Main.getInstance().getCooldown().add(player);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            if (Main.getInstance().getCooldown().contains(player)) {
                                Main.getInstance().getCooldown().remove(player);
                            }
                        }
                    }, 20*5L);
                } catch (NumberFormatException e) {
                    player.sendMessage(Main.getInstance().getPrefix() + "§cBitte verwende eine realistische Zahl!");
                }
            } else {
                player.sendMessage(Main.getInstance().getPrefix() + "§cDies kann nur alle 5 Sekunden ausgeführt werden.");
            }
        } else {
            player.sendMessage(Main.getInstance().getPrefix() + "Nutze §e/wa [Radius]");
        }

        return true;
    }
}
