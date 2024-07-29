package org.enxada.enxadaPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.enxada.enxadaPlugin.db.HomeQueries;
import org.enxada.enxadaPlugin.db.MySQLConnection;
import org.enxada.enxadaPlugin.dto.HomeDTO;
import org.enxada.enxadaPlugin.utils.ParseArgs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SetHome implements CommandExecutor {
  private MySQLConnection conn;
  private HomeQueries homeQueries;

  public SetHome(JavaPlugin plugin) {
    this.homeQueries = new HomeQueries(plugin);
    this.conn = new MySQLConnection(plugin);
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    if (!(commandSender instanceof Player)) {
      commandSender.sendMessage("Esse comando deve ser executado por um player!");
      return false;
    }


    try {
      Map<String, String> parsedArgs = ParseArgs.parse(strings);
      Location loc = ((Player) commandSender).getPlayer().getLocation();

      HomeDTO home = new HomeDTO(
          Integer.parseInt(parsedArgs.get("cooldown")),
          parsedArgs.get("apelido"),
          parsedArgs.get("particulas").equalsIgnoreCase("sim") ? 1 : 0,
          loc.getX(),
          loc.getY(),
          loc.getZ()
      );

      homeQueries.cadastrarHome(home, commandSender.getName());

    } catch (SQLException e) {
      Bukkit.getLogger().info("Erro ao executar comando sethome: " + e.getMessage());
    }



    return true;
  }

}
