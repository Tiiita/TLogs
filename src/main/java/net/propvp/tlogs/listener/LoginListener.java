package net.propvp.tlogs.listener;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.propvp.tlogs.TLogs;

/**
 * @author tiiita_
 * Created on Dezember 07, 2022 | 19:39:34
 * (●'◡'●)
 */
public class LoginListener implements Listener {

    private final TLogs plugin;

    public LoginListener(TLogs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission("tlogs.update")) {
            plugin.getProxy().getScheduler().runAsync(plugin, () -> {
                plugin.getUpdateChecker().getLatestVersion(version -> {
                    if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                        sendUpdateMessage(player);
                    } else if (plugin.getConfig().getBoolean("send-no-update-message")) {
                        player.sendMessage(new TextComponent(plugin.getPrefix() + " " + plugin.getConfig().color(plugin.getConfig().getString("no-update-message"))));
                    }
                });
            });
        }
    }

    private void sendUpdateMessage(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(plugin.getPrefix() + " §c§lFound a new Update!"));

        TextComponent clickableLink = new TextComponent(plugin.getPrefix() + " §7Download: §c§lClick here!");
        clickableLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/tlogs.106211/"));

        player.sendMessage(clickableLink);
    }
}
