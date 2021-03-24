package me.miqhtie.blockswap.commands;

import me.miqhtie.blockswap.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EndCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!Main.instance.running) {
            sender.sendMessage(ChatColor.RED + "Game is not running! Do " + ChatColor.UNDERLINE + "/start" + ChatColor.RESET + "" + ChatColor.RED + " to start it!");
            return true;
        }

        Main.instance.running = false;
        Main.instance.gameTask.cancel();
        Main.instance.gameTask = null;
        sender.sendMessage(ChatColor.GREEN + "Stopped game!");
        return false;
    }
}
