package com.trustmarco.bewerbungsplugin.pointmanager;

import com.trustmarco.bewerbungsplugin.Main;
import com.trustmarco.bewerbungsplugin.mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PointsManager {

    /**
     *
     * Punkte-Manager
     * Zuständig für die Verwaltung der einzelnen Punkte Konten, der jeweilgen Spieler (UUID´s)
     *
     */

    private UUID uuid;
    private HashMap<UUID, Integer> points = Main.getInstance().getCache_points();

    /**
     * Konstruktor Methode
     *
     * @param uuid UUID dies Spielers
     */

    public PointsManager(final UUID uuid) {
        this.uuid = uuid;
    }


    /**
     * Es wird geprüft ob sich die UUID, bzw. der Spieler bereits in einen Zwischenspeicher befindet.
     * Dannach wird abgefragt, ob die UUID in der Tabelle Registiert ist
     * Im Folgenden werden die Punkte aus der Tabelle abgerufen und in einen Zwischenspeicher gespeichert.
     * Diese werden schließlich zurückgegeben.
     *
     * @return Punkte eines Spielers (UUID)
     */

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

    /**
     * Die Punkte bzw. einer UUID werden in dem jeweiligen Zwischenspeicher abgespeichert.
     *
     * @param number Anzahl der Punkte, die gesetzt werden sollen
     */

    public void setPoints(final Integer number) {
        this.points.put(this.uuid, number);
    }

    /**
     * Zunächst wird geprüft, ob sich das Punktekonto des Spielers verändert hat.
     * Dannach wird in Frage gestellt, ob in der Tabelle bereits ein Eintrag bzg. dieser UUID vorhanden ist.
     * Folgend wird die Punkte-Anzahl aktualisiert.
     */

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

    /**
     * Die Tabelle wird durchsucht ob ein Eintrag bzg. der UUID zu finden ist.
     *
     * @return Ob ein Eintrag bzg. der UUID vorhanden ist.
     */

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
