package org.enxada.enxadaPlugin.db;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLConnection {
  private Connection conn = null;
  private final JavaPlugin plugin;

  public MySQLConnection(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  public Connection getConnection() throws SQLException {
    if (conn == null || conn.isClosed()) {
      try {
        String host = plugin.getConfig().getString("database.host");
        String database = plugin.getConfig().getString("database.name");
        String user = plugin.getConfig().getString("database.user");
        String password = plugin.getConfig().getString("database.password");
        String url = "jdbc:mysql://" + host + ":3306/" + database;

        conn = DriverManager.getConnection(url, user, password);
      }
      catch (SQLException e) {
        throw new SQLException(e.getMessage());
      }
    }

    return conn;
  }

  public void disconnect() throws SQLException {
    if (conn != null && !conn.isClosed()) {
      try {
        conn.close();
      } catch (SQLException e) {
        throw new SQLException(e.getMessage());
      }
    }
  }

}
