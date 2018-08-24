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
import de.cerus.lobbycore.commands.GadgetsCommand;
import de.cerus.lobbycore.commands.LobbyCoreCommand;
import de.cerus.lobbycore.gadgets.FlyfeatherGadget;
import de.cerus.lobbycore.listeners.*;
import de.cerus.lobbycore.managers.CorePacketManager;
import de.cerus.lobbycore.managers.FileManager;
import de.cerus.lobbycore.managers.GadgetManager;
import de.cerus.lobbycore.managers.MessageManager;
import de.cerus.lobbycore.objects.CorePacket;
import de.cerus.lobbycore.objects.LobbyCorePlaceholderHook;
import de.cerus.lobbycore.utilities.Updater;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class LobbyCore extends JavaPlugin {

    private static LobbyCore instance;
    private FileManager fileManager;
    private Updater updater;
    private GadgetManager gadgetManager;
    private CorePacketManager corePacketManager;

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
        consoleCommandSender.sendMessage("§6[STARTUP] §rRegistering gadgets...");
        registerGadgets();
        consoleCommandSender.sendMessage("§6[STARTUP] §rLoading Packets...");
        loadPackets();

        getUpdater().checkForNewUpdate();

        consoleCommandSender.sendMessage("§6[STARTUP] §rStartup done. It took " + (System.currentTimeMillis() - time) + "ms to enable LobbyCore.");
        consoleCommandSender.sendMessage("------------------------------------------------");
        consoleCommandSender.sendMessage(" ");
    }

    @Override
    public void onDisable() {
        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
        consoleCommandSender.sendMessage(" ");
        consoleCommandSender.sendMessage("------------------------------------------------");
        consoleCommandSender.sendMessage("L  O  B  B  Y  C  O  R  E");
        consoleCommandSender.sendMessage("Version " + getDescription().getVersion() + ", Server Version " + Bukkit.getBukkitVersion().split("-")[0]);
        consoleCommandSender.sendMessage(" ");
        consoleCommandSender.sendMessage("§6[SHUTDOWN] Unregistering all packets...");
        unregisterPackets();
        consoleCommandSender.sendMessage("------------------------------------------------");
        consoleCommandSender.sendMessage(" ");
    }

    private void unregisterPackets() {
        for (CorePacket packet : new ArrayList<>(getCorePacketManager().getCorePackets())) {
            getCorePacketManager().unloadCorePacket(packet);
        }
    }

    private void loadPackets() {
        File packetDir = new File(System.getProperty("user.dir") + "/LobbyCorePackets");
        packetDir.mkdirs();

        if (packetDir.listFiles() == null) {
            System.out.println("null");
            return;
        }

        for (File file : packetDir.listFiles()) {
            if (file == null) return;
            if (file.getName().endsWith(".jar")) getCorePacketManager().loadCorePacket(file);
        }

        getCorePacketManager().fillPacketPagination();
    }

    public void setInstances() {
        instance = this;
        this.fileManager = new FileManager();
        this.updater = new Updater("NONE", this);
        this.gadgetManager = new GadgetManager();
        this.corePacketManager = new CorePacketManager();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) papiEnabled = true;
    }

    public void initiateInstances() {
        this.fileManager.init();
        MessageManager.init();

        if (isPapiEnabled()) new LobbyCorePlaceholderHook(getInstance()).hook();
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), getInstance());
        pluginManager.registerEvents(new PlayerQuitListener(), getInstance());
        pluginManager.registerEvents(new PlayerInteractListener(), getInstance());
        pluginManager.registerEvents(new InventoryClickListener(), getInstance());
        pluginManager.registerEvents(new BlockBreakBuildListener(), getInstance());
    }

    public void registerCommands() {
        //Useless: getCommand("test").setExecutor(new CommandTest());
        getCommand("lobbycore").setExecutor(new LobbyCoreCommand());
        getCommand("compass").setExecutor(new CompassCommand());
        getCommand("gadgets").setExecutor(new GadgetsCommand());
    }

    public void registerGadgets() {
        getGadgetManager().registerGadget(new FlyfeatherGadget());
        getGadgetManager().fillGadgetPagination();
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public Updater getUpdater() {
        return updater;
    }

    public GadgetManager getGadgetManager() {
        return gadgetManager;
    }

    public boolean isPapiEnabled() {
        return papiEnabled;
    }

    public CorePacketManager getCorePacketManager() {
        return corePacketManager;
    }
}
