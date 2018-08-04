/*
 *  Copyright (c) 2018 Cerus
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cerus
 *
 */

package de.cerus.lobbycore;

import de.cerus.lobbycore.commands.CompassCommand;
import de.cerus.lobbycore.commands.LobbyCoreCommand;
import de.cerus.lobbycore.listeners.CommandTest;
import de.cerus.lobbycore.listeners.PlayerInteractListener;
import de.cerus.lobbycore.listeners.PlayerJoinListener;
import de.cerus.lobbycore.managers.FileManager;
import de.cerus.lobbycore.objects.LobbyCorePlaceholderHook;
import de.cerus.lobbycore.utilities.Updater;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyCore extends JavaPlugin {

    private static LobbyCore instance;
    private FileManager fileManager;
    private Updater updater;

    private boolean papiEnabled = false;

    public static LobbyCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
        consoleCommandSender.sendMessage(" ");
        consoleCommandSender.sendMessage("------------------------------------------------");
        consoleCommandSender.sendMessage("L  O  B  B  Y  C  O  R  E");
        consoleCommandSender.sendMessage("Version " + getDescription().getVersion() + ", Server Version " + Bukkit.getBukkitVersion().split("-")[0]);
        consoleCommandSender.sendMessage(" ");

        consoleCommandSender.sendMessage("§6[STARTUP] §rSetting all instances...");
        setInstances();
        if (isPapiEnabled())
            consoleCommandSender.sendMessage("§5[PAPI] §rThe PlaceholderAPI was successfully hooked into LobbyCore.");
        consoleCommandSender.sendMessage("§6[STARTUP] §rInitiating all instances...");
        initiateInstances();
        consoleCommandSender.sendMessage("§6[STARTUP] §rRegistering listeners...");
        registerListeners();
        consoleCommandSender.sendMessage("§6[STARTUP] §rRegistering commands...");
        registerCommands();

        getUpdater().checkForNewUpdate();

        consoleCommandSender.sendMessage("§6[STARTUP] §rStartup done. It took " + (System.currentTimeMillis() - time) + "ms to enable LobbyCore.");
        consoleCommandSender.sendMessage("------------------------------------------------");
        consoleCommandSender.sendMessage(" ");
    }

    public void setInstances() {
        instance = this;
        this.fileManager = new FileManager();
        this.updater = new Updater("NONE", this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) papiEnabled = true;
    }

    public void initiateInstances() {
        this.fileManager.init();

        if (isPapiEnabled()) new LobbyCorePlaceholderHook(getInstance()).hook();
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), getInstance());
        pluginManager.registerEvents(new PlayerInteractListener(), getInstance());
    }

    public void registerCommands() {
        getCommand("test").setExecutor(new CommandTest());
        getCommand("lobbycore").setExecutor(new LobbyCoreCommand());
        getCommand("compass").setExecutor(new CompassCommand());
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public Updater getUpdater() {
        return updater;
    }

    public boolean isPapiEnabled() {
        return papiEnabled;
    }
}
