package com.trustmarco.bewerbungsplugin.worldanalyse;

import org.bukkit.Location;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

public class WorldAnalyse {

    /**
     * Diese Klasse ermöglicht es genauere Daten über Entitys in einem bestimmten Radius zu erfahren.
     */

    private Location location;
    private Integer radius;

    /**
     * @param location Ort, an dem sich der Spieler befindet.
     * @param radius Radius, in dem Entitys erfasst werden sollen.
     */

    public WorldAnalyse(final Location location, final Integer radius) {
        this.location = location;
        this.radius = radius;
    }

    /**
     * @return alle Entitys in einem bestimmten Radius
     */

    public List<Entity> getEntitys() {
        List<Entity> result_list = new ArrayList<Entity>();
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            result_list.add(entity);
        }
        return result_list;
    }

    /**
     * @return Alle Spieler in einem bestimmten Radius
     */

    public Integer getPlayers() {
        int result = 0;
        for (Entity entity : getEntitys()) {
            if (entity instanceof Player) {
                result++;
            }
        }
        return result;
    }

    /**
     * @return Alle Monster in einem bestimmten Radius
     */

    public Integer getMonstors() {
        int result = 0;
        for (Entity entity : getEntitys()) {
            if (entity instanceof Monster) {
                result++;
            }
        }
        return result;
    }

    /**
     * @return Alle Tiere in einem bestimmten Radius
     */

    public Integer getAnimals() {
        int result = 0;
        for (Entity entity : getEntitys()) {
            if (entity instanceof Animals) {
                result++;
            }
        }
        return result;
    }


}
