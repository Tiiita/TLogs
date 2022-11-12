package net.propvp.tlogs.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;
import net.propvp.tlogs.TLogs;
import net.propvp.tlogs.handler.LogHandler;

import java.util.List;

/**
 * @author tiiita_
 * Created on November 10, 2022 | 20:37:36
 * (●'◡'●)
 */
public class LogsCommand extends Command {
    private final LogHandler logHandler;
    private final TLogs plugin;

    public LogsCommand(String name, LogHandler logHandler, TLogs plugin) {
        super(name);

        this.logHandler = logHandler;
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (!player.hasPermission("tlogs.toggle")) {
            player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("no-perms"))));
            return;
        }

        if (!(args.length == 1)) {
            sendCommandUsage(player);
            return;
        }

        if (args[0].equalsIgnoreCase("toggle")) {
            logHandler.toggleLogsForPlayer(player);

        } else if (args[0].equalsIgnoreCase("toggleconfig")) {
            logHandler.toggleLogsForConfig(player);

        } else sendCommandUsage(player);
    }

    private void sendCommandUsage(ProxiedPlayer player) {

        for (String currentMessage : plugin.getConfig().getStringList("command-usage")) {
            player.sendMessage(new TextComponent(plugin.getConfig().color(currentMessage)));
        }
    }
}
