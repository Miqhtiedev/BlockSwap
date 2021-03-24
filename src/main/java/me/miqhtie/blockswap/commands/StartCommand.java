package me.miqhtie.blockswap.commands;

import me.miqhtie.blockswap.tasks.GameTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(me.miqhtie.blockswap.Main.instance.running) {
            sender.sendMessage(ChatColor.RED + "Game already running! Do " + ChatColor.UNDERLINE + "/end" + ChatColor.RESET + "" + ChatColor.RED + " to stop it!");
            return true;
        }
        GameTask gameTask;
        if(args.length == 0 || !args[0].equalsIgnoreCase("true")) {
           System.out.println("not singleplayer");
           gameTask = new GameTask(false);
        } else {
            gameTask = new GameTask(true);
            System.out.println("singleplayer");
        }


        gameTask.runTaskTimer(me.miqhtie.blockswap.Main.instance, 0, 1);
        sender.sendMessage(ChatColor.GREEN + "Started game!");
        return false;
    }
}
