package de.matzethan.serverEssentials.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.matzethan.serverEssentials.ServerEssentials;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        player.sendActionBar(Component.text("You will be teleported to Bedwars...", NamedTextColor.YELLOW));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendActionBar(Component.text("3", NamedTextColor.YELLOW));
        }, 20L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendActionBar(Component.text("2", NamedTextColor.YELLOW));
        }, 40L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendActionBar(Component.text("1", NamedTextColor.YELLOW));
        }, 60L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendActionBar(Component.text("Teleporting ...", NamedTextColor.GREEN));

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("bedwars");

            player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());

            player.setInvulnerable(false);
        }, 80L);

        return true;
    }
}

