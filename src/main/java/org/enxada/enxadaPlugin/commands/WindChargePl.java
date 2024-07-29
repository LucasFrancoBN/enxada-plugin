package org.enxada.enxadaPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.enxada.enxadaPlugin.EnxadaPlugin;
import org.enxada.enxadaPlugin.utils.ParseArgs;

import java.util.Map;

public class WindChargePl implements CommandExecutor {
  private final EnxadaPlugin plugin;

  public WindChargePl(EnxadaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Map<String, String> params = ParseArgs.parse(args);

    if (params.isEmpty() || !params.containsKey("forca") || !params.containsKey("particulas") || !params.containsKey("velocidade")) {
      sender.sendMessage("Uso correto: /" + label + " --forca:<valor> --particulas:<sim/nao> --velocidade:<valor>");
      return false;
    }


    int forca;
    boolean particulas;
    double velocidade;

    try {
      forca = Integer.parseInt(params.get("forca"));
      particulas = params.get("particulas").equalsIgnoreCase("sim");
      velocidade = Double.parseDouble(params.get("velocidade"));
    } catch (NumberFormatException e) {
      sender.sendMessage("Parametros invalidos. Certifique-se de que 'forca' e 'velocidade' são números.");
      return false;
    }

    if(sender instanceof Player) {
      Player player = (Player) sender;

      ItemStack windchargeItem = new ItemStack(Material.WIND_CHARGE);
      ItemMeta meta = windchargeItem.getItemMeta();

      if(meta != null) {
        meta.setDisplayName("Wind Charge Customizado");
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "forca"), PersistentDataType.INTEGER, forca);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "particulas"), PersistentDataType.STRING, particulas ? "sim" : "nao");
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "velocidade"), PersistentDataType.DOUBLE, velocidade);
        windchargeItem.setItemMeta(meta);
      }

      player.getInventory().addItem(windchargeItem);
      player.sendMessage("Wind Charge Customizado adicionado ao seu inventário com sucesso.");

    } else {
      sender.sendMessage("Este comando só pode ser executado por um jogador.");
      return false;
    }

    return true;
  }
}