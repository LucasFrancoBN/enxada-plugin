package org.enxada.enxadaPlugin.db;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CooldownQueries {
  private final MySQLConnection mySQLConnection;
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public CooldownQueries(JavaPlugin plugin) {
    this.mySQLConnection = new MySQLConnection(plugin);
  }

  public LocalDateTime ultimoTp(String player) throws SQLException {
    String query = "SELECT ultima_data FROM tb_cooldown " +
        "WHERE player = ?;";
    try (Connection conn = mySQLConnection.getConnection()) {
       PreparedStatement stmt = conn.prepareStatement(query);
       stmt.setString(1, player);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          LocalDateTime ultimaData = LocalDateTime.parse(rs.getString("ultima_data"), formatter);

          return ultimaData;
        }
      }

    } catch (SQLException e) {
      Bukkit.getLogger().info(e.getMessage());
      throw e;
    }

    return null;
  }

  public void atualizarCooldownPlayer(String player) throws SQLException {
    String horaData = LocalDateTime.now().format(formatter);
    String query = "UPDATE tb_cooldown SET ultima_data = ? WHERE player = ?;";
    try (Connection conn = mySQLConnection.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, horaData);
      stmt.setString(2, player);
      stmt.executeUpdate();
    } catch (SQLException e) {
      Bukkit.getLogger().info(e.getMessage());
      throw e;
    }
  }
}
