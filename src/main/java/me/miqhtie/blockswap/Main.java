package me.miqhtie.blockswap;

import me.miqhtie.blockswap.commands.EndCommand;
import me.miqhtie.blockswap.commands.StartCommand;
import me.miqhtie.blockswap.events.MoveEvent;
import me.miqhtie.blockswap.events.PlayerJoinEvent;
import me.miqhtie.blockswap.tasks.GameTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static Logger LOGGER = Bukkit.getLogger();
    public static Main instance;

    public boolean running = false;
    public GameTask gameTask = null;
    public HashMap<Player, PlayerStatus> players = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        LOGGER.info(getName() + " has been enabled!");

        getCommand("start").setExecutor(new StartCommand());
        getCommand("end").setExecutor(new EndCommand());

        Bukkit.getPluginManager().registerEvents(new MoveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), this);
    }
}
