package org.enxada.enxadaPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.enxada.enxadaPlugin.commands.Home;
import org.enxada.enxadaPlugin.commands.SetHome;
import org.enxada.enxadaPlugin.commands.WindChargePl;
import org.enxada.enxadaPlugin.db.MySQLConnection;
import org.enxada.enxadaPlugin.listeners.TeleportListener;
import org.enxada.enxadaPlugin.listeners.WindChargeListener;

import java.sql.SQLException;

public final class EnxadaPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    // Plugin startup logic
    Bukkit.getLogger().info("Enabling EnxadaPlugin");
    MySQLConnection connection;

    try {
      connection = new MySQLConnection(this);
      connection.getConnection();
      getLogger().info("Conexão com MySQL estabelecida com sucesso!");
    } catch (SQLException e) {
      getLogger().severe("Falha ao conectar ao MySQL: " + e.getMessage());
    }

    getCommand("windcharge").setExecutor(new WindChargePl(this));
    getCommand("sethome").setExecutor(new SetHome(this));
    getCommand("home").setExecutor(new Home(this));
    getServer().getPluginManager().registerEvents(new WindChargeListener(this), this);
    getServer().getPluginManager().registerEvents(new TeleportListener(this), this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    Bukkit.getLogger().info("Disabling EnxadaPlugin");

  }
}
