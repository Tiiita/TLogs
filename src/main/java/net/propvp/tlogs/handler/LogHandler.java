package net.propvp.tlogs.handler;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.propvp.tlogs.TLogs;
import net.propvp.tlogs.wrapper.ConfigWrapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/**
 * @author tiiita_
 * Created on November 10, 2022 | 20:38:22
 * (●'◡'●)
 */
public class LogHandler implements Listener {

    TLogs plugin;
    private final HashSet<ProxiedPlayer> playersThatEnabledLogs = new HashSet<>();
    private boolean logsForConfig;

    public LogHandler(TLogs plugin) {
        this.plugin = plugin;

        logsForConfig = true;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();

            if (logsForConfig) {
                saveLogsToFile(player, event.getMessage());
            }

            sendLog(player, event.getMessage());
        }
    }


    public void enableLogsForPlayer(ProxiedPlayer player) {

        playersThatEnabledLogs.add(player);
        player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("enabled-logs-for-player"))));
    }

    public void disableLogsForPlayer(ProxiedPlayer player) {

        playersThatEnabledLogs.remove(player);
        player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("disabled-logs-for-player"))));
    }

    public boolean isViewingLogs(ProxiedPlayer player) {
        return playersThatEnabledLogs.contains(player);
    }

    public void toggleLogsForPlayer(ProxiedPlayer player) {
        if (isViewingLogs(player)) {
            disableLogsForPlayer(player);
        } else {
            enableLogsForPlayer(player);
        }
    }

    public void enableLogsForFile(ProxiedPlayer player) {
        logsForConfig = true;
        player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("enabled-logs-for-config"))));
    }

    public void disableLogsForFile(ProxiedPlayer player) {
        logsForConfig = false;
        player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("disabled-logs-for-config"))));
    }

    public void toggleLogsForFile(ProxiedPlayer player) {
        if (logsForConfig) {
            disableLogsForFile(player);
        } else {
            enableLogsForFile(player);
        }
    }


    private void sendLog(ProxiedPlayer writer, String message) {
        String serverName = writer.getServer().getInfo().getName();
        DateFormat obj = new SimpleDateFormat("HH:mm:ss");
        Date res = new Date(System.currentTimeMillis());
        String log = plugin.getConfig().color(plugin.getConfig().getString("log", "%server%",
                serverName, "%time%", obj.format(res), "%player%", writer.getName(), "%message%", message));


        for (ProxiedPlayer onlinePlayer : playersThatEnabledLogs) {
            onlinePlayer.sendMessage(new TextComponent(log));
        }
    }


    private void saveLogsToFile(ProxiedPlayer player, String message) {
        plugin.getLogFile().saveLog(player, message);
    }
}
