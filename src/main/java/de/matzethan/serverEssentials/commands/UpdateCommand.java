package de.matzethan.serverEssentials.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.matzethan.serverEssentials.ServerEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateCommand implements CommandExecutor {

    // 1. Wir deklarieren die Variable, damit wir sie überall in dieser Klasse nutzen können
    private final ServerEssentials plugin;

    public UpdateCommand(ServerEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        commandSender.sendMessage(ChatColor.YELLOW + "Suche nach Updates...");

        // 2. Wir geben 'plugin' (unsere Hauptklasse) an den Scheduler weiter
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            checkForUpdatesManual(commandSender);
        });

        return true; // Sagt Bukkit, dass der Befehl erfolgreich ausgeführt wurde
    }

    // 3. Diese Methode steht AUSSERHALB der geschweiften Klammern von onCommand
    private void checkForUpdatesManual(CommandSender commandSender) {
        String user = "matzethan";
        String repo = "ServerEssentials";

        try {
            URL url = new URL("https://api.github.com/repos/" + user + "/" + repo + "/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Paper-Plugin-UpdateChecker");

            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                String latestVersion = json.get("tag_name").getAsString();
                String currentVersion = plugin.getDescription().getVersion();

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    commandSender.sendMessage(ChatColor.GREEN + "Ein Update ist verfügbar: " + ChatColor.WHITE + latestVersion);
                    commandSender.sendMessage(ChatColor.GREEN + "Download: " + ChatColor.AQUA + "https://github.com/" + user + "/" + repo + "/releases");
                } else {
                    commandSender.sendMessage(ChatColor.GREEN + "ServerEssentials ist aktuell! (" + currentVersion + ")");
                }
            }
        } catch (Exception e) {
            commandSender.sendMessage(ChatColor.RED + "Fehler beim Update-Check: " + e.getMessage());
        }
    }
}