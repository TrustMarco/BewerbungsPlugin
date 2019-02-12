package com.trustmarco.bewerbungsplugin.worldanalyse;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

public class WorldAnalyse {

    private Location location;
    private Integer radius;

    public WorldAnalyse(final Location location, final Integer radius) {
        this.location = location;
        this.radius = radius;
    }

    public List<Entity> getEntitys() {
        List<Entity> result_list = new ArrayList<Entity>();
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            result_list.add(entity);
        }
        return result_list;
    }

    public Integer getPlayers() {
        int result = 0;
        for (Entity entity : getEntitys()) {
            if (entity instanceof Player) {
                result++;
            }
        }
        return result;
    }

    public Integer getMonstors() {
        int result = 0;
        for (Entity entity : getEntitys()) {
            if (entity instanceof Monster) {
                result++;
            }
        }
        return result;
    }

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
