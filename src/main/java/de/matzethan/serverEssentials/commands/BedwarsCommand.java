package de.matzethan.serverEssentials.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.matzethan.serverEssentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BedwarsCommand implements CommandExecutor {

    private final ServerEssentials plugin;
    public BedwarsCommand(ServerEssentials plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {



        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.YELLOW + "Warning: This Command is for players only!");
            return false;
        }

        Player player = (Player) commandSender;

        player.setInvulnerable(true);

        player.sendMessage(ChatColor.YELLOW + "You will be teleported to Bedwars...");
        player.sendMessage(ChatColor.YELLOW + "3");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            commandSender.sendMessage(ChatColor.YELLOW + "2");
        }, 20L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            commandSender.sendMessage(ChatColor.YELLOW + "1");
        }, 40L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            commandSender.sendMessage(ChatColor.YELLOW + "Teleporting ..");

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("bedwars");

            player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());

            player.setInvulnerable(false);
        }, 60L);

        return true;
    }
}

