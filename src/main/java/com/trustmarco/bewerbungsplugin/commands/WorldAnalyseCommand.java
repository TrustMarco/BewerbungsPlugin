package com.trustmarco.bewerbungsplugin.commands;

import com.trustmarco.bewerbungsplugin.Main;
import com.trustmarco.bewerbungsplugin.worldanalyse.WorldAnalyse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldAnalyseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getInstance().getPrefix() + "§cDazu musst du dich Ingame befinden!");
            return true;
        }

        final Player player = (Player) sender;

        if (args.length == 1) {
            if (this.canRun(player)) {
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
                    Main.getInstance().getCooldown().put(player, (System.currentTimeMillis()+5000));
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

    /**
     * Prüft, ob der Cooldown abgelaufen ist
     *
     * @param player Spieler
     * @return Boolean
     */

    private boolean canRun(final Player player) {
        if (Main.getInstance().getCooldown().containsKey(player)) {
            Long millis = Main.getInstance().getCooldown().get(player);
            if (millis >= System.currentTimeMillis()) {
                return false;
            }
        }
        return true;
    }
}
