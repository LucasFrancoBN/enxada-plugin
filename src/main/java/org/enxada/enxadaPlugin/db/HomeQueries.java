package org.enxada.enxadaPlugin.db;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.enxada.enxadaPlugin.dto.ApelidoHomeDTO;
import org.enxada.enxadaPlugin.dto.HomeDTO;
import org.enxada.enxadaPlugin.dto.HomeEscolhidaDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomeQueries {
  private final MySQLConnection connection;

  public HomeQueries(JavaPlugin plugin) {
    connection = new MySQLConnection(plugin);
  }

  public List<ApelidoHomeDTO> listarHomesDoPlayer(String playerNickname) throws SQLException {
    List<ApelidoHomeDTO> listApelidoHomeDTO = new ArrayList<>();

    String query = "SELECT h.apelido_home FROM tb_player p " +
        "INNER JOIN tb_player_home ph ON p.id = ph.player_id " +
        "INNER JOIN tb_home h ON h.id = ph.home_id " +
        "WHERE p.nickname = ?;";

    try (Connection conn = connection.getConnection();) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, playerNickname);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          String apelidoHome = rs.getString("apelido_home");
          ApelidoHomeDTO home = new ApelidoHomeDTO(apelidoHome);
          listApelidoHomeDTO.add(home);
        }
      }

      return listApelidoHomeDTO;
    } catch (SQLException e) {
      Bukkit.getLogger().info(e.getMessage());
      throw e;
    }
  }

  public HomeEscolhidaDTO homeEscolhida(String apelidoHome, String playerNickname) throws SQLException {
    String query = "SELECT h.cooldown, h.particulas, h.x, h.y, h.z FROM tb_player p " +
        "INNER JOIN tb_player_home ph ON p.id = ph.player_id " +
        "INNER JOIN tb_home h ON h.id = ph.home_id " +
        "WHERE p.nickname = ? AND h.apelido_home = ?;";

    Bukkit.getLogger().info("Entrou");
    try (Connection conn = connection.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, playerNickname);
      stmt.setString(2, apelidoHome);


      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Integer cooldown = Integer.parseInt(rs.getString("cooldown"));
          Integer particulas = Integer.parseInt(rs.getString("particulas"));
          Double x = Double.parseDouble(rs.getString("x"));
          Double y = Double.parseDouble(rs.getString("y"));
          Double z = Double.parseDouble(rs.getString("z"));
          return new HomeEscolhidaDTO(cooldown, particulas, x, y, z);
        }
      }

    } catch (SQLException e) {
      Bukkit.getLogger().info(e.getMessage());
      Bukkit.getLogger().info("Caiu aqui :D");
      throw e;
    }
    return null;
  }

  public boolean cadastrarHome(HomeDTO homeDTO, String playnerNickname) throws SQLException {
    try {
      cadastrarTbHome(homeDTO);
      cadastrarTbPlayer(playnerNickname);

      Integer idHome = getHomeId(homeDTO.apelidoHome());
      Integer idPlayer = getPlayerId(playnerNickname);

      cadastrarTbPlayerHome(idHome, idPlayer);

      return true;

    } catch (SQLException e) {
      throw e;
    }
  }

  private void cadastrarTbHome(HomeDTO homeDTO) throws SQLException {
    String query = "INSERT INTO tb_home (cooldown, apelido_home, particulas, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = connection.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, homeDTO.cooldown().toString());
      stmt.setString(2, homeDTO.apelidoHome());
      stmt.setString(3, homeDTO.particulas().toString());
      stmt.setString(4, homeDTO.x().toString());
      stmt.setString(5, homeDTO.y().toString());
      stmt.setString(6, homeDTO.z().toString());
      stmt.executeUpdate();

    } catch (SQLException e) {
      throw e;
    }
  }

  private void cadastrarTbPlayer(String playerNickname) throws SQLException {
    String query = "INSERT INTO tb_player (nickname) VALUES (?)";
    try (Connection conn = connection.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, playerNickname);
      stmt.executeUpdate();

    } catch (SQLException e) {
      throw e;
    }
  }

  private void cadastrarTbPlayerHome(Integer idHome, Integer idPlayer) throws SQLException {
    String query = "INSERT INTO tb_player_home (player_id, home_id) VALUES (?, ?)";
    try (Connection conn = connection.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, idPlayer.toString());
      stmt.setString(2, idHome.toString());
      stmt.executeUpdate();

    } catch (SQLException e) {
      throw e;
    }
  }

  private Integer getPlayerId(String playerNickname) throws SQLException {
    String query = "SELECT id FROM tb_player WHERE nickname = ?";
    try (Connection conn = connection.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, playerNickname);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      throw e;
    }

    return null;
  }

  private Integer getHomeId(String apelidoHome) throws SQLException {
    String query = "SELECT id FROM tb_home WHERE apelido_home = ?";
    try (Connection conn = connection.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, apelidoHome);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      throw e;
    }

    return null;
  }
}
