package net.propvp.tlogs.handler;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.propvp.tlogs.TLogs;
import net.propvp.tlogs.wrapper.ConfigWrapper;

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
                saveLogsToConfig(plugin.getLogsConfig(), player, event.getMessage());
            }

            sendLogs(player, event.getMessage());
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

    public void enableLogsForConfig(ProxiedPlayer player) {
        logsForConfig = true;
        player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("enabled-logs-for-config"))));
    }

    public void disableLogsForConfig(ProxiedPlayer player) {
        logsForConfig = false;
        player.sendMessage(new TextComponent(plugin.getConfig().color(plugin.getConfig().getString("disabled-logs-for-config"))));
    }

    public void toggleLogsForConfig(ProxiedPlayer player) {
        if (logsForConfig) {
            disableLogsForConfig(player);
        } else {
            enableLogsForConfig(player);
        }
    }


    private void sendLogs(ProxiedPlayer writer, String message) {
        String serverName = writer.getServer().getInfo().getName();
        DateFormat obj = new SimpleDateFormat("HH:mm:ss");
        Date res = new Date(System.currentTimeMillis());

        for (ProxiedPlayer onlinePlayer : playersThatEnabledLogs) {

            onlinePlayer.sendMessage(new TextComponent(
                    plugin.getConfig().color(plugin.getConfig().getString("log", "%server%",
                            serverName, "%time%", obj.format(res), "%player%", writer.getName(), "%message%", message))));
        }
    }


    private void saveLogsToConfig(ConfigWrapper config, ProxiedPlayer player, String message) {


        String serverName = player.getServer().getInfo().getName();
        DateFormat obj = new SimpleDateFormat("dd/MM/yyyy HH|mm|ss");
        Date res = new Date(System.currentTimeMillis());

        String configLog = plugin.getConfig().color(plugin.getConfig().getString("configLog", "%server%",
                serverName, "%time%", obj.format(res), "%player%", player.getName(), "%message%", message));


        config.reload();
        config.setString(obj.format(res), configLog);
        config.save();
    }
}
