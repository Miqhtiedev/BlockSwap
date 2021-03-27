package me.miqhtie.blockswap.events;

import me.miqhtie.blockswap.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {
    @EventHandler
    public void joinEvent(org.bukkit.event.player.PlayerJoinEvent event) {
        if (Main.instance.running) {
            if (!Main.instance.players.containsKey(event.getPlayer())) {
                event.getPlayer().sendMessage(ChatColor.GOLD + "Sorry! There is a game already going on. Please wait till this one ends to play!");
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
