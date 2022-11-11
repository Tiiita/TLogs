package net.propvp.tlogs.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.propvp.tlogs.TLogs;
import net.propvp.tlogs.handler.LogHandler;

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
        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (!player.hasPermission("tlogs.logs")) {
                player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("no-perms"))));
                return;
            }
            if (args.length == 0) {
                logHandler.toggleLogs(player);

            } else player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("command-usage"))));



        }
    }
}
