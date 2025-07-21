package me.braydon.sync.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.braydon.sync.PlayerSyncEngine;
import me.braydon.sync.common.StyleUtils;
import me.braydon.sync.common.TimeUtils;
import me.braydon.sync.server.MinecraftServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @author Braydon
 */
@AllArgsConstructor
@CommandAlias("pse")
public final class PlayerSyncEngineCommand extends BaseCommand {
    @NonNull private final PlayerSyncEngine plugin;

    @HelpCommand
    public void help(@NonNull CommandSender sender){
        sendPluginVersion(sender);
    }

    @Subcommand("stats")
    @CommandPermission("playersyncengine.command.stats")
    public void onStats(@NonNull CommandSender sender) {
        sender.sendMessage(StyleUtils.style("<gray>Getting stats..."));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            MinecraftServer thisServer = plugin.getServerManager().getThisServer();
            long redisPing = plugin.getRedis().getPing();

            // Send the stats
            sendPluginVersion(sender);
            sender.sendMessage(StyleUtils.style("<dark_gray>- %s: <white>%s".formatted(
                    StyleUtils.secondary("Server Version"), Bukkit.getVersion()
            )));

            // Redis
            sender.sendMessage(StyleUtils.style("<dark_gray>- " + StyleUtils.primary("Realtime")));
            sender.sendMessage(StyleUtils.style("<dark_gray>    %s: <white>%s".formatted(
                    StyleUtils.secondary("Type"), plugin.getRedis().getName()
            )));
            sender.sendMessage(StyleUtils.style("<dark_gray>    %s: <white>%sms".formatted(
                    StyleUtils.secondary("Ping"), redisPing
            )));

            // Instance
            sender.sendMessage(StyleUtils.style("<dark_gray>- " + StyleUtils.primary("Instance")));
            sender.sendMessage(StyleUtils.style("<dark_gray>    %s: <white>%s".formatted(
                    StyleUtils.secondary("Unique ID"), thisServer.getUniqueId()
            )));
            sender.sendMessage(StyleUtils.style("<dark_gray>    %s: <white>%s".formatted(
                    StyleUtils.secondary("Region"), thisServer.getRegion().getName()
            )));
            sender.sendMessage(StyleUtils.style("<dark_gray>    %s: <white>%s".formatted(
                    StyleUtils.secondary("Players"), Bukkit.getOnlinePlayers().size()
            )));
            sender.sendMessage(StyleUtils.style("<dark_gray>    %s: <white>%s".formatted(
                    StyleUtils.secondary("Uptime"), TimeUtils.format(System.currentTimeMillis() - thisServer.getFirstSeen())
            )));
        });
    }

    private void sendPluginVersion(@NonNull CommandSender sender) {
        String me = "<hover:show_text:\"<gray>View my GitHub (:\"><click:open_url:https://github.com/Rainnny7><dark_red>Braydon (Rainnny)</dark_red></click></hover>";
        sender.sendMessage(StyleUtils.style("<gray>This server is running <gold>PlayerSyncEngine <white>v%s <gray>by %s.".formatted(
                plugin.getPluginMeta().getVersion(), me
        )));
    }
}