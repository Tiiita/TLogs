package net.propvp.tlogs;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.propvp.tlogs.command.LogsCommand;
import net.propvp.tlogs.handler.LogHandler;
import net.propvp.tlogs.wrapper.ConfigWrapper;
import net.propvp.tlogs.wrapper.LogFileWrapper;

import java.io.*;
import java.nio.file.Files;

public final class TLogs extends Plugin {

    private LogHandler logHandler;


    private ConfigWrapper config;
    private LogFileWrapper logFile;

    @Override
    public void onEnable() {
        // Plugin startup logic

        try {
            loadConfig("config.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        config = new ConfigWrapper("config.yml", getDataFolder(), this);
        logHandler = new LogHandler(this);

        logFile = new LogFileWrapper(this, getDataFolder(), "tlogs.log");


        registerListener();
        registerCommands();


    }

    private void registerListener() {
        PluginManager pm = getProxy().getPluginManager();

        //register all listener here:
        pm.registerListener(this, logHandler);
    }

    private void registerCommands() {
        PluginManager pm = getProxy().getPluginManager();

        //register all commands here:
        pm.registerCommand(this, new LogsCommand("logs", logHandler, this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigWrapper getConfig() {
        return config;
    }


    private void loadConfig(String name) throws IOException {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(this.getDataFolder(), name);

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream(name)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public LogFileWrapper getLogFile() {
        return logFile;
    }
}
