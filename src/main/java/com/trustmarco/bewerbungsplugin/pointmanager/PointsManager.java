package com.trustmarco.bewerbungsplugin.pointmanager;

import com.trustmarco.bewerbungsplugin.Main;
import com.trustmarco.bewerbungsplugin.mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PointsManager {

    private UUID uuid;
    private HashMap<UUID, Integer> points = Main.getInstance().getCache_points();

    public PointsManager(final UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getPoints() {
        if (this.points.containsKey(this.uuid)) return this.points.get(this.uuid);
        if (this.exists()) {
            try {
                PreparedStatement preparedStatement = MySQL.con.prepareStatement("SELECT * FROM points WHERE UUID = ?");
                preparedStatement.setString(1, this.uuid.toString());

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    this.points.put(this.uuid, Integer.parseInt(resultSet.getString("POINTS")));
                    return this.points.get(this.uuid);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Main.getInstance().getStandard_points();
    }

    public void setPoints(final Integer number) {
        this.points.put(this.uuid, number);
    }

    public void synchronize() {
        if (this.points.containsKey(this.uuid)) {
            Integer number = this.points.get(this.uuid);
            if (this.exists()) {
                try {
                    PreparedStatement preparedStatement = MySQL.con.prepareStatement("UPDATE points SET POINTS = ? WHERE UUID = ?");
                    preparedStatement.setString(1, String.valueOf(number));
                    preparedStatement.setString(2, this.uuid.toString());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    PreparedStatement preparedStatement = MySQL.con.prepareStatement("INSERT INTO points (UUID, POINTS) VALUES (?, ?)");
                    preparedStatement.setString(1, this.uuid.toString());
                    preparedStatement.setString(2, String.valueOf(number));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean exists() {
        try {
        PreparedStatement preparedStatement = MySQL.con.prepareStatement("SELECT * FROM points WHERE UUID = ?");
        preparedStatement.setString(1, this.uuid.toString());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return true;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
