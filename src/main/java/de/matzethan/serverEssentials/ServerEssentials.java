package de.matzethan.serverEssentials;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.matzethan.serverEssentials.commands.UpdateCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ServerEssentials extends JavaPlugin {

    public static final String PASTEL_GREEN = "\u001B[38;2;175;225;175m";
    public static final String PASTEL_BLUE = "\u001B[38;2;173;216;230m";
    public static final String RESET = "\u001B[0m";

    @Override
    public void onEnable() {
        // Config laden/erstellen 📂
        saveDefaultConfig();

        // Plugin-Channels registrieren
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // Schönes Logo in der Konsole 🎨
        var console = Bukkit.getConsoleSender();
        console.sendMessage("");
        console.sendMessage(PASTEL_BLUE + "_____   _____" + RESET);
        console.sendMessage(PASTEL_BLUE + "|       |" + RESET);
        console.sendMessage(PASTEL_BLUE + "|____   |____" + RESET);
        console.sendMessage(PASTEL_BLUE + "    |   |" + RESET);
        console.sendMessage(PASTEL_BLUE + "____|   |____" + RESET);
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("Made by matzethan.");
        console.sendMessage("");
        console.sendMessage(PASTEL_GREEN + "ServerEssentials has started successfully." + RESET);

        // Standard-Update Command registrieren
        Objects.requireNonNull(getCommand("se-update")).setExecutor(new UpdateCommand(this));

        // Dynamische Commands aus der Config laden 🚀
        registerDynamicCommands();

        console.sendMessage("");
    }

    public void registerDynamicCommands() {
        ConfigurationSection section = getConfig().getConfigurationSection("teleport-commands");
        if (section == null) return;

        try {
            java.lang.reflect.Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            org.bukkit.command.CommandMap commandMap = (org.bukkit.command.CommandMap) commandMapField.get(Bukkit.getServer());

            for (String key : section.getKeys(false)) {
                String cmdName = section.getString(key + ".command-name");
                if (cmdName == null) continue;

                org.bukkit.command.defaults.BukkitCommand dynamicCommand = new org.bukkit.command.defaults.BukkitCommand(cmdName) {
                    @Override
                    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                        return handleTeleportCommand(sender, key);
                    }
                };

                commandMap.register("serveressentials", dynamicCommand);
                getLogger().info("The command /" + cmdName + " has been activated.");
            }
        } catch (Exception e) {
            getLogger().severe("Fehler beim Registrieren der dynamischen Befehle: " + e.getMessage());
        }
    }

    public boolean handleTeleportCommand(CommandSender sender, String configKey) {
        if (!(sender instanceof Player)) {
            String warning = getConfig().getString("teleport-commands." + configKey + ".messages.warning-player-only", "&cOnly for players!");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', warning));
            return true;
        }

        Player player = (Player) sender;
        String path = "teleport-commands." + configKey + ".";

        String target = getConfig().getString(path + "target-server");
        int delay = getConfig().getInt(path + "countdown-seconds");
        boolean protect = getConfig().getBoolean(path + "cancel-damage-during-teleport");
        String msgTeleport = getConfig().getString(path + "messages.teleporting");
        String msgDone = getConfig().getString(path + "messages.done");

        if (protect) player.setInvulnerable(true);
        if (msgTeleport != null) player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgTeleport));

        // Kleiner Countdown-Logik-Check
        for (int i = 0; i <= delay; i++) {
            int timeLeft = delay - i;
            if (timeLeft > 0) {
                Bukkit.getScheduler().runTaskLater(this, () -> player.sendMessage(ChatColor.YELLOW + String.valueOf(timeLeft)), i * 20L);
            } else {
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    if (msgDone != null) player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgDone));

                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(target != null ? target : "lobby");
                    player.sendPluginMessage(this, "BungeeCord", out.toByteArray());

                    if (protect) player.setInvulnerable(false);
                }, i * 20L);
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        getLogger().info("ServerEssentials has been stopped.");
    }
}