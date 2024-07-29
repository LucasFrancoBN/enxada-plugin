package org.enxada.enxadaPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.enxada.enxadaPlugin.db.CooldownQueries;
import org.enxada.enxadaPlugin.db.HomeQueries;
import org.enxada.enxadaPlugin.db.MySQLConnection;
import org.enxada.enxadaPlugin.dto.ApelidoHomeDTO;
import org.enxada.enxadaPlugin.dto.HomeEscolhidaDTO;
import org.enxada.enxadaPlugin.utils.ParseArgs;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class Home implements CommandExecutor {
  private final JavaPlugin plugin;
  private final HomeQueries homeQueries;
  private final CooldownQueries cooldownQueries;

  public Home(JavaPlugin plugin) {
    this.homeQueries = new HomeQueries(plugin);
    this.cooldownQueries = new CooldownQueries(plugin);
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    if (!(commandSender instanceof Player)) {
      commandSender.sendMessage("Esse comando deve ser executado por um player!");
      return false;
    }

    Player player = (Player) commandSender;
    Map<String, String> parsedArgs = ParseArgs.parse(strings);

    if(parsedArgs.size() > 1) {
      commandSender.sendMessage("Esse comando dev ter apenas uma flag!");
      return false;
    }

    if(parsedArgs.containsKey("ver") && parsedArgs.get("ver").equals("sim")) {
      try {
        List<ApelidoHomeDTO> listaApelidoHome = homeQueries.listarHomesDoPlayer(player.getName());
        commandSender.sendMessage("Seus pontos de teleporte: ");
        listaApelidoHome.forEach(apelidoHomeDTO -> commandSender.sendMessage(apelidoHomeDTO.apelidoHome()));
        return true;
      } catch (SQLException e) {
        commandSender.sendMessage("Não foi possível encontrar seus pontos de teleporte D:");
        return false;
      }
    }

    if(parsedArgs.containsKey("tp") && !parsedArgs.get("tp").isBlank()) {
      try {
        HomeEscolhidaDTO homeEscolhida = homeQueries.homeEscolhida(parsedArgs.get("tp"), player.getName());
        Location loc = new Location(Bukkit.getWorld("world"), homeEscolhida.x(), homeEscolhida.y(), homeEscolhida.z());

        LocalDateTime ultimoTp = cooldownQueries.ultimoTp(player.getName());
        long diferencaSegundosEntreDatas = ChronoUnit.SECONDS.between(ultimoTp, LocalDateTime.now());

        if(diferencaSegundosEntreDatas < homeEscolhida.cooldown()) {
          commandSender.sendMessage("Você não pode se teletransportar por causa do seu cooldown. Espere mais um pouco.");
          return false;
        }

        cooldownQueries.atualizarCooldownPlayer(player.getName());
        player.teleport(loc);
        return true;
      } catch (SQLException e) {
        commandSender.sendMessage("Não foi possível se teletransportar ;-;");
        return false;
      }

    }

    return false;
  }
}
