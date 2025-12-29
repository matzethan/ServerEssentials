package de.matzethan.serverEssentials;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.matzethan.serverEssentials.commands.BedwarsCommand;
import de.matzethan.serverEssentials.commands.LobbyCommand;
import de.matzethan.serverEssentials.commands.SMPCommand;
import de.matzethan.serverEssentials.commands.UpdateCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable; // <-- Dieser Import hat gefehlt!

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class ServerEssentials extends JavaPlugin {

    public static final String PASTEL_GREEN = "\u001B[38;2;175;225;175m";
    public static final String PASTEL_BLUE = "\u001B[38;2;173;216;230m";
    public static final String RESET = "\u001B[0m";

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "bungeecord:main");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        var console = Bukkit.getConsoleSender();
        getLogger().info("");
        console.sendMessage(PASTEL_BLUE + "_____   _____" + RESET);
        console.sendMessage(PASTEL_BLUE + "|       |" + RESET);
        console.sendMessage(PASTEL_BLUE + "|____   |____" + RESET);
        console.sendMessage(PASTEL_BLUE + "    |   |" + RESET);
        console.sendMessage(PASTEL_BLUE + "____|   |____" + RESET);
        getLogger().info("Version: " + getDescription().getVersion()); // Dynamisch aus plugin.yml
        getLogger().info("Made by matzethan.");
        getLogger().info("");
        console.sendMessage(PASTEL_GREEN + "ServerEssentials has started successfully." + RESET);
        getLogger().info("");

        getCommand("lobby").setExecutor(new LobbyCommand(this));
        getLogger().info("The command /lobby has been activated.");

        getCommand("smp").setExecutor(new SMPCommand(this));
        getLogger().info("The command /smp has been activated.");

        getCommand("bedwars").setExecutor(new BedwarsCommand(this));
        getLogger().info("The command /bedwars has been activated.");

        getCommand("se-update").setExecutor(new UpdateCommand(this));
        getLogger().info("The command /se-update has been activated.");

        getLogger().info("");

    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");

        getLogger().info("All commands have been disabled.");
        getLogger().info("ServerEssentials has been stopped.");
    }
}