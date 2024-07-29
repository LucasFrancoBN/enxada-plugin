package org.enxada.enxadaPlugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportListener implements Listener {
  private final JavaPlugin plugin;

  public TeleportListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    Location to = event.getTo();

    Bukkit.getScheduler().runTaskLater(
        plugin,
        () -> event.getPlayer().getWorld().spawnParticle(Particle.PORTAL, to, 100, 0.5, 0.5, 0.5, 0.1),
        1L);
  }
}
