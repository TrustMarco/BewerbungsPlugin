package com.trustmarco.bewerbungsplugin;

import com.trustmarco.bewerbungsplugin.commands.PuntkeCommand;
import com.trustmarco.bewerbungsplugin.commands.WorldAnalyseCommand;
import com.trustmarco.bewerbungsplugin.listeners.DeathListener;
import com.trustmarco.bewerbungsplugin.listeners.QuitListener;
import com.trustmarco.bewerbungsplugin.mysql.MySQL;
import com.trustmarco.bewerbungsplugin.pointmanager.PointsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import javax.print.attribute.HashAttributeSet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bewerbungs-Plugin für Spieleoase.net
 *
 * @author TrustMarco
 * @version 1.0
 *
 */

public class Main extends JavaPlugin {


    /**
     *
     * Hauptklasse
     *
     */


    public static Main instance;
    private MySQL mySQL;
    /**
     * Thread, der speziell für MySQL Anwendungen genutzt wird.
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private HashMap<UUID, Integer> cache_points = new HashMap<UUID, Integer>();
    private HashMap<Player, Long> cooldown = new HashMap<>();
    private HashMap<UUID, PointsManager> point_manager = new HashMap<>();
    private String prefix;
    private Integer standard_points;


    @Override
    public void onEnable() {
        this.init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    /**
     * Instanz der Hauptklasse wird festgelegt.
     * Der Prefix wird ebenfalls festgelegt.
     * Eine File wird erstellt.
     * Verbindung zu MySQL wird aufgebaut.
     * Sämtliche Commands und Events werden registiert.
     * Die Standart-Punkte werden festgelegt.
     */

    private void init() {
        this.setInstance(this);
        this.setPrefix("§eBewerbung §8> §7");
        this.createConfigFile();
        if (!this.connectToMySQL()) {
            System.out.println("Bitte trage zuvor gültige MySQL-Daten ein. (plugins/BewerbungsPlugin/config.yml)");
            return;
        }
        this.registerCommands();
        this.registerListeners();
        File file = new File("plugins//BewerbungsPlugin//config.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        this.standard_points = yamlConfiguration.getInt("standard_points");
    }

    /**
     * Commands werden registiert
     */

    private void registerCommands() {
        this.getInstance().getCommand("worldanalyse").setExecutor(new WorldAnalyseCommand());
        this.getInstance().getCommand("punkte").setExecutor(new PuntkeCommand());
    }

    /**
     * Events werden registiert
     */

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }

    /**
     * Instanz der Hauptklasse wird gesetzt.
     *
     * @param instance Hauptklasse
     */

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    /**
     * @return Prefix
     */

    public String getPrefix() {
        return prefix;
    }

    /**
     * Prefix wird gesetzt.
     *
     * @param prefix Prefix
     */

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Eine File wird erstellt und mit Einträgen bestückt.
     */

    private void createConfigFile() {
        File file = new File("plugins//BewerbungsPlugin//config.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        yamlConfiguration.options().copyDefaults(true);
        yamlConfiguration.addDefault("mysql.host", "127.0.1");
        yamlConfiguration.addDefault("mysql.password", "password");
        yamlConfiguration.addDefault("mysql.database", "database");
        yamlConfiguration.addDefault("mysql.user", "spieleoasenet");
        yamlConfiguration.addDefault("standard_points", 5);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getStandard_points() {
        return standard_points;
    }

    /**
     * Verbindungsaufbau zu einer MySQL-Tabelle.
     * Die Anmelde-Daten werden direkt aus einer File abgerufen
     */

    private boolean connectToMySQL() {
        File file = new File("plugins//BewerbungsPlugin//config.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        if (file.exists()) {
            String password = yamlConfiguration.getString("mysql.password");

            if (!(password.equalsIgnoreCase("password"))) {
                String host = yamlConfiguration.getString("mysql.host");
                String user = yamlConfiguration.getString("mysql.user");
                String database = yamlConfiguration.getString("mysql.database");

                this.mySQL = new MySQL(host, database, user, password);
                this.mySQL.createTable();
                return true;
            }
        }
        return false;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public HashMap<UUID, Integer> getCache_points() {
        return cache_points;
    }

    public HashMap<Player, Long> getCooldown() {
        return cooldown;
    }

    /**
     * Wenn die Instanz bereits im Zwischenspeicher ist, wird die aus dem Zwischenspeicher
     * zurückgegeben, wenn nicht wird einen neue erstellt.
     *
     * @param uuid UUID des Spielers
     * @return Instanz der Punkte-Manager Klasse
     */

    public PointsManager getPointsManager(final UUID uuid) {
        if (this.point_manager.containsKey(uuid)) return this.point_manager.get(uuid);
        this.point_manager.put(uuid, new PointsManager(uuid));
        return this.point_manager.get(uuid);
    }
}
