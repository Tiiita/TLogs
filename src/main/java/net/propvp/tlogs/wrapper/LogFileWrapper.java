package net.propvp.tlogs.wrapper;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.propvp.tlogs.TLogs;
import sun.rmi.log.ReliableLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * @author tiiita_
 * Created on Dezember 07, 2022 | 16:33:48
 * (●'◡'●)
 */
public class LogFileWrapper {


    private File file;
    private final TLogs plugin;

    public LogFileWrapper(TLogs plugin, File path, String name) {
        this.plugin = plugin;

        try {
            file = new File(path, name);
            if (file.createNewFile()) {
                plugin.getLogger().log(Level.INFO, "Log file created successfully!");
            } else {
                plugin.getLogger().log(Level.INFO, "Log file already exist!");
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Cannot create log file. Please contact the plugin developer!");
            e.printStackTrace();
        }
    }

    public void saveLog(ProxiedPlayer player, String message) {

        String serverName = player.getServer().getInfo().getName();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateInMillis = new Date(System.currentTimeMillis());

        String log = plugin.getConfig().color(plugin.getConfig().getString("fileLog", "%server%",
                serverName, "%time%", dateFormat.format(dateInMillis), "%player%", player.getName(), "%message%", message));

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.newLine();
            bufferedWriter.append(log);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
