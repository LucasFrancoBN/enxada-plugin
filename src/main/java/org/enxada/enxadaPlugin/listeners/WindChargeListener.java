package org.enxada.enxadaPlugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WindChargeListener implements Listener {
  private final JavaPlugin plugin;

  public WindChargeListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerUseItem(PlayerInteractEvent event) {
    if(event.getHand() == EquipmentSlot.OFF_HAND) return;

    ItemStack item = event.getItem();

    if(item == null || item.getType() != Material.WIND_CHARGE) return;


    ItemMeta meta = item.getItemMeta();

    if(
        meta == null ||
        !meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "forca"), PersistentDataType.INTEGER) ||
        !meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "particulas"), PersistentDataType.STRING) ||
        !meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "velocidade"), PersistentDataType.DOUBLE)

    ) return;

    Bukkit.getLogger().info("WIND CHARGE COM ATRIBUTOS CORRETOS");

    int forca = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "forca"), PersistentDataType.INTEGER);
    boolean particulas = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "particulas"), PersistentDataType.STRING).equals("sim");
    double velocidade = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "velocidade"), PersistentDataType.DOUBLE);

    Bukkit.getLogger().info("VALORES CAPTURADOS");

    Player player = event.getPlayer();
    WindCharge windCharge = player.launchProjectile(WindCharge.class);
    windCharge.setMetadata("forca", new FixedMetadataValue(plugin, forca));
    windCharge.setMetadata("particulas", new FixedMetadataValue(plugin, particulas));
    windCharge.setVelocity(player.getLocation().getDirection().multiply(velocidade));
    Bukkit.getLogger().info("VALORES ALTERADOS");

    Bukkit.getLogger().info("Valores de particulas: " + windCharge.getMetadata("particulas").get(0).asString());

    if (item.getAmount() > 1) {
      item.setAmount(item.getAmount() - 1);
    } else {
      player.getInventory().remove(item);
    }
  }

  @EventHandler
  public void onProjectileLaunch(ProjectileLaunchEvent event) {
    try {
      Bukkit.getScheduler().runTask(plugin, () -> {
        Bukkit.getLogger().info("Preparando entidade");
        Entity projectile = event.getEntity();

        Bukkit.getLogger().info("testando condição");
        if (!projectile.hasMetadata("particulas")) {
          Bukkit.getLogger().info("Metadado 'particulas' não encontrado");
          return;
        }

        boolean particulas = projectile.getMetadata("particulas").get(0).asBoolean();
        if (!particulas) return;

        Bukkit.getLogger().info("Adicionando task");
        new BukkitRunnable() {
          @Override
          public void run() {
            if (projectile.isDead() || !projectile.isValid()) {
              this.cancel();
              return;
            }

            projectile.getWorld().spawnParticle(Particle.SMOKE, projectile.getLocation(), 5, 0.2, 0.2, 0.2, 0.01);
          }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(this.getClass()), 0, 1);
      });
    } catch (Exception e) {
      Bukkit.getLogger().info("Não foi possível lançar o evento de onProjectileLaunch.");
    }
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    try {
      Entity damager = event.getDamager();

      Bukkit.getLogger().info("Evento de dano disparado");

      int forca = damager.getMetadata("forca").get(0).asInt();

      Bukkit.getLogger().info("Força: " + forca);

      if(forca <= 0 || !(damager instanceof WindCharge)) return;

      Bukkit.getLogger().info("Validacao correta");
      event.setDamage(forca);
    } catch(Exception e) {
      Bukkit.getLogger().info("Não foi possível disparar o evento.");
    }
  }
}
