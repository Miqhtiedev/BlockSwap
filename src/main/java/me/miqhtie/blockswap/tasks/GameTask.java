package me.miqhtie.blockswap.tasks;

import me.miqhtie.blockswap.BlockWhitelist;
import me.miqhtie.blockswap.Main;
import me.miqhtie.blockswap.PlayerStatus;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameTask extends BukkitRunnable {

    private ArrayList<Material> usedBlocks = new ArrayList<>();
    private int minPlayers = 1;

    public GameTask(boolean singleplayer) {
        me.miqhtie.blockswap.Main.instance.running = true;
        me.miqhtie.blockswap.Main.instance.gameTask = this;
        if(singleplayer) this.minPlayers = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            Main.instance.players.put(p, new PlayerStatus(getRandomBlock(), false));
            p.sendMessage(ChatColor.GOLD + "You have to find " + ChatColor.BOLD + getNormalizedBlockName(Main.instance.players.get(p).block));
        }

        me.miqhtie.blockswap.Main.LOGGER.info("Event is now starting!");
    }

    int countTicks = 0;
    int seconds = 0;

    int time = 60 * 5;

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            int min = (int) Math.floor((float) (time - seconds)  / 60);
            int sec = (time - seconds) - min * 60;
            String time = "";
            if(min < 10 && min > 0) time += "0" + min + ":";
            else if(min >= 10) time += min + ":";

            if (sec < 10) time += "0" + sec;
            else time += sec;
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "" + ChatColor.BOLD + String.format("%s%s%s", ChatColor.GREEN, ChatColor.BOLD, time)));
        }
        if(countTicks < 20) {
            countTicks++;
            return;
        }
        countTicks = 0;
        seconds++;

        if(seconds == time) {
            nextRound();
        }
    }

    private Material getRandomBlock() {
        if(usedBlocks.size() == BlockWhitelist.whitelistedItems.length) {
            usedBlocks.clear();
            me.miqhtie.blockswap.Main.LOGGER.info("All blocks have been used. Resetting block whitelist!");
        }
        while (true) {
            Material random = BlockWhitelist.whitelistedItems[new Random().nextInt(BlockWhitelist.whitelistedItems.length)];;
            if(!usedBlocks.contains(random)) {
                usedBlocks.add(random);
                return random;
            }
        }
    }

    // OAK_FENCE --> Oak Fence, OAK_PLANKS --> Oak Planks, etc
    private String getNormalizedBlockName(Material material) {
        String originalName = material.name();
        StringBuilder normalizedName = new StringBuilder();
        originalName = originalName.replace('_', ' ').toLowerCase();
        for (int i = 0; i < originalName.length(); i++) {
            char c = originalName.charAt(i);
            if(i == 0) normalizedName.append(Character.toUpperCase(c));
            else if(originalName.charAt(i - 1) == ' ') normalizedName.append(Character.toUpperCase(c));
            else normalizedName.append(c);
        }
        return normalizedName.toString();
    }

    public void nextRound() {
        seconds = 0;
        countTicks = 0;


        Main.instance.players.keySet().removeIf(player -> {
            if (!Main.instance.players.get(player).collected) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You Lose!");
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + player.getName() + " has been eliminated!");
                return true;
            }
            return false;
        });

        // Check if there is a winner
        if (Main.instance.players.size() <= minPlayers) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.teleport(p.getWorld().getSpawnLocation());
                p.setGameMode(Bukkit.getDefaultGameMode());
            }

            if (Main.instance.players.size() == 1) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&n[&f&nBLOCK-SWAP&c&n]&r &6Game over! &lGG!&r\n&6" + Main.instance.players.keySet().iterator().next().getName() + " has won!"));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&n[&f&nBLOCK-SWAP&c&n]&r &6Game over! &lGG!\n&4Everyone Lost!"));
            }
            Main.instance.running = false;
            Main.instance.gameTask = null;
            Main.instance.players.clear();
            cancel();
            return;
        }


        for (Player p : Main.instance.players.keySet()) {
            if (Main.instance.players.get(p).collected) {
                Main.instance.players.put(p, new PlayerStatus(getRandomBlock(), false));
                p.sendMessage(ChatColor.GOLD + "You have to find " + ChatColor.BOLD + getNormalizedBlockName(Main.instance.players.get(p).block));
            }
        }
    }

}
