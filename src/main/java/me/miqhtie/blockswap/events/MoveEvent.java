package me.miqhtie.blockswap.events;

import me.miqhtie.blockswap.Main;
import me.miqhtie.blockswap.PlayerStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {
    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        if(!Main.instance.running || !Main.instance.players.containsKey(event.getPlayer())) return;
        if(Main.instance.players.get(event.getPlayer()).collected) return;

        Location playerLocation = event.getPlayer().getLocation();
        if(event.getPlayer().getWorld().getBlockAt(playerLocation).getBlockData().getMaterial() == Main.instance.players.get(event.getPlayer()).block || event.getPlayer().getWorld().getBlockAt(playerLocation.getBlockX(), playerLocation.getBlockY() -1, playerLocation.getBlockZ()).getBlockData().getMaterial() == Main.instance.players.get(event.getPlayer()).block) {
            Main.instance.players.put(event.getPlayer(), new PlayerStatus(Main.instance.players.get(event.getPlayer()).block, true));
            event.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You completed this round!");
            Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + event.getPlayer().getName() + " found what they needed!");

            boolean containsFalse = false;
            for (Player p : Main.instance.players.keySet()) {
                if(!Main.instance.players.get(p).collected) containsFalse = true;
            }
            if(!containsFalse) Main.instance.gameTask.nextRound();
        };
    }
}
